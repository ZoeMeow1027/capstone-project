using Azure.Core;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Model.Enums;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("bills")]
    public class BillController : UserSessionControllerBase
    {
        private readonly IBillService _billService;
        private readonly IProductService _productService;

        public BillController(
            IBillService billService,
            IProductService productService,
            IUserSessionService userSessionService
            ) : base(userSessionService)
        {
            _billService = billService;
            _productService = productService;
        }

        [HttpGet]
        public ActionResult Get(int? id = null, bool? activeonly = false)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                CheckPermission(
                    Request.Cookies["token"],
                    new List<UserType>() { UserType.Administrator, UserType.Staff }
                );

                List<DeliverStatus> deliverStatusExclude = new List<DeliverStatus>()
                {
                    DeliverStatus.Failed,
                    DeliverStatus.Cancelled,
                    DeliverStatus.Completed
                };

                result.Data = id == null
                    ? _billService.GetBillSummaries().Where(p => (activeonly == null) ? true : ((activeonly == false) ? true : !deliverStatusExclude.Contains(p.Status)))
                    : _billService.GetBillSummaryById(id.Value);

                result.Message = "Successful";
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 401;
                result.Message = uaEx.Message;
            }
            catch (BadHttpRequestException bhrEx)
            {
                result.StatusCode = 400;
                result.Message = bhrEx.Message;
            }
            catch (Exception ex)
            {
                result.StatusCode = 500;
                result.Message = ex.Message;
            }

            return StatusCode(result.StatusCode, result.ToDynamicObject());
        }

        [HttpPost]
        public ActionResult Post(JToken args)
        {
            ReturnResultTemplate result = new ReturnResultTemplate
            {
                StatusCode = 200,
                Message = "Successful!"
            };

            try
            {
                string? token = Request.Cookies["token"];
                CheckPermission(token, new List<UserType>() { UserType.Administrator, UserType.Staff });
                User? user = GetUserByToken(token);
                if (user == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                RequestDTO? requestDTO = args.ToObject<RequestDTO>();
                if (requestDTO == null)
                    throw new Exception("Error while converting parameters!");

                if (requestDTO.Action == null || requestDTO.Data == null)
                {
                    throw new BadHttpRequestException("Missing parameters!");
                }

                switch (requestDTO.Action.ToLower())
                {
                    case "cancel":
                        if (requestDTO.Data == null)
                        {
                            throw new BadHttpRequestException("Missing data!");
                        }
                        OrderCancel(requestDTO.Data);
                        break;
                    case "updatestatus":
                        if (requestDTO.Data == null)
                        {
                            throw new BadHttpRequestException("Missing data!");
                        }
                        OrderUpdate(requestDTO.Data);
                        break;
                    default:
                        throw new BadHttpRequestException("Unknown \"action\" value!");
                }
            }
            catch (ArgumentException argEx)
            {
                result.StatusCode = 400;
                result.Message = argEx.Message;
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 403;
                result.Message = uaEx.Message;
            }
            catch (BadHttpRequestException bhrEx)
            {
                result.StatusCode = 400;
                result.Message = bhrEx.Message;
            }
            catch (Exception ex)
            {
                result.StatusCode = 500;
                result.Message = ex.Message;
            }

            return StatusCode(result.StatusCode, result.ToDynamicObject());
        }

        #region Order history
        private void OrderCancel(JToken args)
        {
            Utils.CheckRequiredArguments(args, new List<string>() { "orderid" });

            BillSummary? billSummary = _billService.GetBillSummaryById(args["orderid"].Value<int>());
            if (billSummary == null)
            {
                throw new ArgumentException(string.Format("Order is not exist!"));
            }

            billSummary.Status = DeliverStatus.Cancelled;
            billSummary.StatusAdditional = "An admin or staff has cancelled this order.";

            _billService.UpdateBill(billSummary);

            // TODO: Add back to product
            foreach (var item in billSummary.BillDetails)
            {
                var productData = _productService.GetProductById(item.ID);
                productData.InventoryCount += item.Count;
                _productService.UpdateProduct(productData);
            }
        }

        private void OrderUpdate(JToken args)
        {
            Utils.CheckRequiredArguments(args, new List<string>() { "orderid", "status", "statusaddress", "statusadditional" });

            BillSummary? billSummary = _billService.GetBillSummaryById(args["orderid"].Value<int>());
            if (billSummary == null)
            {
                throw new ArgumentException(string.Format("Order is not exist!"));
            }

            var excludeStatus = new List<DeliverStatus>()
            {
                DeliverStatus.Failed,
                DeliverStatus.Cancelled,
                DeliverStatus.Completed
            };
            if (excludeStatus.Contains(billSummary.Status))
            {
                throw new ArgumentException("This delivery is already completed. This method is not allowed for this order anymore.");
            }

            var status = args["status"].Value<int>();
            if (!Enum.IsDefined(typeof(DeliverStatus), status))
            {
                throw new ArgumentException(string.Format("No status with ID {0}!", status));
            }
            if (billSummary.Status == DeliverStatus.Delivering)
            {
                if ((DeliverStatus)status == DeliverStatus.Cancelled)
                {
                    throw new ArgumentException("Status is not allowed when it was delivering.");
                }
            }
            billSummary.Status = (DeliverStatus)status;
            billSummary.StatusAddress = args["statusaddress"].Value<string>();
            billSummary.StatusAdditional = args["statusadditional"].Value<string>();
            if (billSummary.Status == DeliverStatus.Completed)
            {
                billSummary.DateCompleted = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
            }
            _billService.UpdateBill(billSummary);

            if (billSummary.Status == DeliverStatus.Failed || billSummary.Status == DeliverStatus.Cancelled)
            {
                foreach (var item in billSummary.BillDetails)
                {
                    var productData = _productService.GetProductById(item.ID);
                    productData.InventoryCount += item.Count;
                    _productService.UpdateProduct(productData);
                }
            }
        }
        #endregion
    }
}
