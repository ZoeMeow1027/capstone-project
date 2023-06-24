using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Services;
using System.Dynamic;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("auth")]
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
                    throw new BadHttpRequestException("Missing parameters or invalid login!");

                var data = userService.FindAllUsersByUsername(loginItem.Username, true).ToList();
                User? user = null;
                foreach (User userItem in data)
                {
                    if (userItem.Username.ToLower() == loginItem.Username.ToLower())
                    {
                        user = userItem;
                        break;
                    }
                }

                if (user == null)
                    throw new BadHttpRequestException("Username or password is incorrect!");

                string pHash = Utils.EncryptSHA256(string.Format("{0}{1}", loginItem.Password, user.PasswordHash));
                if (user.Password != pHash)
                    throw new UnauthorizedAccessException("Username or password is incorrect!");

                if (!user.IsEnabled)
                    throw new UnauthorizedAccessException("Your account has been disabled! Please go to contact support for more information.");

                string token = userSessionService.CreateAndStoreAccountToken(user.ID, 365);
                dynamic dataTemp = new ExpandoObject();
                dataTemp.username = user.Username;
                dataTemp.usertype = (int)user.UserType;
                dataTemp.token = token;

                result.Data = dataTemp;
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

        [HttpPost("register")]
        public ActionResult Register([FromBody] RegisterDTO registerDTO)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                if (userService.FindAllUsersByUsername(registerDTO.Username, true).Count > 0)
                    throw new BadHttpRequestException(string.Format("Username \'{0}\'is exist!", registerDTO.Username));

                User user = new User();
                user.Username = registerDTO.Username;
                string pHash = Utils.RandomString(12);
                user.PasswordHash = pHash;
                user.Password = Utils.EncryptSHA256(string.Format("{0}{1}", registerDTO.Password, pHash));
                user.Name = registerDTO.Name;
                user.Phone = registerDTO.Phone;
                user.Email = registerDTO.Email;
                userService.AddUser(user);

                var data = userService.FindAllUsersByUsername(registerDTO.Username, false).ToList();
                string token = userSessionService.CreateAndStoreAccountToken(data[0].ID, 365);
                dynamic dataTemp = new ExpandoObject();
                dataTemp.username = data[0].Username;
                dataTemp.token = token;

                result.StatusCode = 200;
                result.Message = "Successful";
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
