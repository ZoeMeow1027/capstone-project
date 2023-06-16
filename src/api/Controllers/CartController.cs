using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/cart")]
    public class CartController : UserSessionControllerBase
    {
        private readonly IUserCartService _cartService;

        public CartController(
            IUserSessionService userSessionService,
            IUserCartService cartService
            ) : base(userSessionService)
        {
            _cartService = cartService;
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

                RequestDTO? productDTO = args.ToObject<RequestDTO>();
                if (productDTO == null)
                    throw new Exception("Error while converting parameters!");

                if (productDTO.Action == null)
                {
                    throw new BadHttpRequestException("Missing parameters!");
                }

                switch (productDTO.Action.ToLower())
                {
                    case "add":
                        if (productDTO.Data == null)
                        {
                            throw new BadHttpRequestException("Missing parameters!");
                        }
                        Utils.CheckRequiredArguments(
                            productDTO.Data,
                            new List<string>() { "productid", "count" }
                            );

                        _cartService.AddItem(
                            user,
                            int.Parse(productDTO.Data["productid"].ToString()),
                            int.Parse(productDTO.Data["count"].ToString())
                            );
                        break;
                    case "update":
                        if (productDTO.Data == null)
                        {
                            throw new BadHttpRequestException("Missing parameters!");
                        }
                        Utils.CheckRequiredArguments(
                            productDTO.Data,
                            new List<string>() { "id", "count" }
                            );

                        _cartService.UpdateItem(
                            user,
                            int.Parse(productDTO.Data["id"].ToString()),
                            int.Parse(productDTO.Data["count"].ToString())
                            );
                        break;
                    case "remove":
                        if (productDTO.Data == null)
                        {
                            throw new BadHttpRequestException("Missing parameters!");
                        }
                        Utils.CheckRequiredArguments(
                            productDTO.Data,
                            new List<string>() { "id" }
                            );

                        _cartService.RemoveItem(
                            user,
                            int.Parse(productDTO.Data["id"].ToString())
                            );
                        break;
                    case "removeall":
                        _cartService.ClearCart(user);
                        break;
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
    }
}
