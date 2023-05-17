using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Services;
using System.Dynamic;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/auth")]
    public class AuthController : UserSessionControllerBase
    {
        private IUserService userService;
        private IUserSessionService userSessionService;

        public AuthController(
            IUserService userService,
            IUserSessionService userSessionService
            ): base(userSessionService)
        {
            this.userService = userService;
            this.userSessionService = userSessionService;
        }

        [HttpPost("login")]
        public ActionResult Login([FromBody] LoginDTO loginItem)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                if (!loginItem.IsValidatedLogin())
                    throw new BadHttpRequestException("Invalid data for login.");

                var data = userService.FindAllUsersByUsername(loginItem.Username, false).ToList();
                User? user = null;
                foreach (User userItem in data)
                {
                    if (userItem.Username.ToLower() == loginItem.Username.ToLower())
                    {
                        user = userItem;
                        break;
                    }
                }
                if (user != null)
                {
                    if (user.Password == loginItem.Password)
                    {
                        string token = userSessionService.CreateAndStoreAccountToken(user.ID, 365);
                        dynamic dataTemp = new ExpandoObject();
                        dataTemp.username = user.Username;
                        dataTemp.usertype = (int)user.UserType;
                        dataTemp.token = token;

                        result.Data = dataTemp;
                        result.StatusCode = 200;
                        result.Message = "";
                    }
                    else
                    {
                        throw new UnauthorizedAccessException("Password mismatch!");
                    }
                }
                else
                {
                    throw new BadHttpRequestException("Username isn't exist!");
                }
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 401;
                result.Message = uaEx.Message;
                result.Data = null;
            }
            catch (BadHttpRequestException bhrEx)
            {
                result.StatusCode = 400;
                result.Message = bhrEx.Message;
                result.Data = null;
            }
            catch (Exception ex)
            {
                result.StatusCode = 500;
                result.Message = ex.Message;
                result.Data = null;
            }

            return StatusCode(result.StatusCode, result.ToDynamicObject());
        }

        [HttpPost("register")]
        public ActionResult Register([FromBody] RegisterDTO registerDTO)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                if (userService.FindAllUsersByUsername(registerDTO.Username, true).Count > 0)
                    throw new BadHttpRequestException("Username is exist!");

                User user = new User();
                user.Username = registerDTO.Username;
                user.Password = registerDTO.Password;
                user.Name = registerDTO.Name;
                user.Phone = registerDTO.Phone;
                userService.AddUser(user);

                var data = userService.FindAllUsersByUsername(registerDTO.Username, false).ToList();
                string token = userSessionService.CreateAndStoreAccountToken(data[0].ID, 365);
                dynamic dataTemp = new ExpandoObject();
                dataTemp.username = data[0].Username;
                dataTemp.token = token;

                result.StatusCode = 200;
                result.Message = string.Empty;
                result.Data = dataTemp;
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
