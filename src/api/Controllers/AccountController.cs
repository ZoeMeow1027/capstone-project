using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/account")]
    public class AccountController : UserSessionControllerBase
    {
        public AccountController(
            IUserSessionService userSessionService
            ) : base(userSessionService)
        {
        }

        [HttpGet("my")]
        public ActionResult MyInfo()
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                var token = Request.Cookies["token"];
                if (token == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                User? user = GetUserByToken(token);
                if (user == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                result.Data = user;
                result.StatusCode = 200;
                result.Message = "Successful";
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 401;
                result.Message = uaEx.Message;
            }
            catch (Exception ex)
            {
                result.StatusCode = 500;
                result.Message = ex.Message;
            }

            return StatusCode(result.StatusCode, result.ToDynamicObject());
        }

        [HttpPost("logout")]
        public ActionResult Logout()
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                string? token = Request.Cookies["token"];
                if (token == null)
                    throw new UnauthorizedAccessException("Session has expired.");
                if (GetUserByToken(token) == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                DeleteSession(token);

                result.StatusCode = 200;
                result.Message = "Logged out!";
                result.Data = null;
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 401;
                result.Message = uaEx.Message;
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
