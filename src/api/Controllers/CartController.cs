using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/cart")]
    public class CartController : UserSessionControllerBase
    {
        private readonly ICartService _cartService;

        public CartController(
            IUserSessionService userSessionService,
            ICartService cartService
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
    }
}
