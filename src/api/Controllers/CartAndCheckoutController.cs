using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Services;
using System.Dynamic;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/cart")]
    public class CartAndCheckoutController : UserSessionControllerBase
    {
        private readonly IUserCartService _cartService;
        private readonly IBillService _billService;
        private readonly IUserAddressService _userAddressService;
        private readonly IProductService _productService;

        public CartAndCheckoutController(
            IUserSessionService userSessionService,
            IUserCartService cartService,
            IBillService billService,
            IUserAddressService userAddressService,
            IProductService productService
            ) : base(userSessionService)
        {
            _cartService = cartService;
            _billService = billService;
            _userAddressService = userAddressService;
            _productService = productService;
        }

        [HttpGet]
        public ActionResult Get()
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                string? token = Request.Cookies["token"];
                CheckPermission(Request.Cookies["token"]);

                User? user = GetUserByToken(token);
                result.Data = _cartService.GetAllItems(user.ID);
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
            ReturnResultTemplate result = new ReturnResultTemplate();

            try
            {
                string? token = Request.Cookies["token"];
                User? user = GetUserByToken(token);
                if (user == null)
                {
                    throw new UnauthorizedAccessException("Not logged in!");
                }

                RequestDTO? requestDTO = args.ToObject<RequestDTO>();
                if (requestDTO == null)
                    throw new Exception("Error while converting parameters!");

                if (requestDTO.Action == null)
                {
                    throw new BadHttpRequestException("Missing parameters!");
                }

                switch (requestDTO.Action.ToLower())
                {
                    case "add":
                        if (requestDTO.Data == null)
                        {
                            throw new BadHttpRequestException("Missing parameters!");
                        }
                        Utils.CheckRequiredArguments(
                            requestDTO.Data,
                            new List<string>() { "productid", "count" }
                            );

                        // TODO: Check if product has limit exceed.
                        _cartService.AddItem(
                            user,
                            int.Parse(requestDTO.Data["productid"].ToString()),
                            int.Parse(requestDTO.Data["count"].ToString())
                            );
                        break;
                    case "update":
                        if (requestDTO.Data == null)
                        {
                            throw new BadHttpRequestException("Missing parameters!");
                        }
                        Utils.CheckRequiredArguments(
                            requestDTO.Data,
                            new List<string>() { "id", "count" }
                            );

                        // TODO: Check if product has limit exceed.
                        _cartService.UpdateItem(
                            user,
                            int.Parse(requestDTO.Data["id"].ToString()),
                            int.Parse(requestDTO.Data["count"].ToString())
                            );
                        break;
                    case "remove":
                        if (requestDTO.Data == null)
                        {
                            throw new BadHttpRequestException("Missing parameters!");
                        }
                        Utils.CheckRequiredArguments(
                            requestDTO.Data,
                            new List<string>() { "id" }
                            );

                        _cartService.RemoveItem(
                            user,
                            int.Parse(requestDTO.Data["id"].ToString())
                            );
                        break;
                    case "removeall":
                        _cartService.ClearCart(user);
                        break;
                    case "checkout":
                        if (requestDTO.Data == null)
                        {
                            throw new BadHttpRequestException("Missing parameters!");
                        }
                        Utils.CheckRequiredArguments(
                            requestDTO.Data,
                            new List<string>() { "addressid" }
                            );

                        int userAddressId = requestDTO.Data["addressid"].Value<int>();
                        UserAddress? userAddress = _userAddressService.GetUserAddressById(userAddressId, user.ID);
                        if (userAddress == null)
                        {
                            throw new BadHttpRequestException(string.Format("UserAddress with ID {0} is not exist or you don't have permission for this address.", userAddressId));
                        }
                        List<UserCart> userCart = _cartService.GetAllItems(user.ID);
                        if (userCart.Count == 0)
                        {
                            throw new BadHttpRequestException("You don't have any items in your cart!");
                        }
                        string? userMessage = null;
                        if (requestDTO.Data["message"] != null)
                        {
                            userMessage = requestDTO.Data["message"].Value<string>();
                        }

                        // TODO: Check if product has limit exceed.
                        int orderid = AddOrder(user.ID, userCart, userAddress, userMessage);
                        dynamic data = new ExpandoObject();
                        data.orderid = orderid;
                        result.Data = data;

                        // Clear user cart after successful billing.
                        _cartService.ClearCart(user.ID);

                        // Subtract product after successful billing.
                        foreach (var userCartItem in userCart)
                        {
                            var productTemp = _productService.GetProductById(userCartItem.ProductID);
                            productTemp.InventoryCount -= userCartItem.Count;
                            _productService.UpdateProduct(productTemp);
                        }
                        break;
                    default:
                        throw new BadHttpRequestException("Unknown 'action' value!");
                }

                result.StatusCode = 200;
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

        private int AddOrder(int userId, List<UserCart> userCartList, UserAddress userAddress, string? userMessage)
        {
            double shippingPrice = 3;
            var billDetails = new List<BillDetails>();
            foreach (var item in userCartList)
            {
                billDetails.Add(new BillDetails()
                {
                    ProductID = item.ProductID,
                    Count = item.Count
                });
            }
            var billSummary = new BillSummary()
            {
                UserID = userId,
                Recipient = userAddress.Name,
                RecipientAddress = userAddress.Address,
                RecipientCountryCode = userAddress.CountryCode,
                RecipientPhone = userAddress.Phone,
                DateCreated = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(),
                BillDetails = billDetails,
                ShippingPrice = shippingPrice,
                TotalPrice = userCartList.Sum(p => p.Product.Price * p.Count) + shippingPrice,
                Status = Model.Enums.DeliverStatus.WaitingForPurchase,
                StatusAddress = "",
                StatusAdditional = "Waiting for purchase",
                PaymentMethod = Model.Enums.PaymentMethod.Unselected,
                PaymentCompleted = false,
                UserMessage = userMessage
            };
            _billService.AddBill(billSummary);
            return billSummary.ID;
        }
    }
}
