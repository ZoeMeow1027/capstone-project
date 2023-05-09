using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Services;
using System.Dynamic;
using System.Net.Mime;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/auth")]
    public class AuthController : ControllerBase
    {
        private IUserService userService;
        private IUserSessionService userSessionService;

        public AuthController(
            IUserService userService,
            IUserSessionService userSessionService
            )
        {
            this.userService = userService;
            this.userSessionService = userSessionService;
        }

        [HttpPost("login")]
        public ActionResult Login([FromBody] LoginDTO loginItem)
        {
            try
            {
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
                        dynamic result = new ExpandoObject();
                        result.username = user.Username;
                        result.token = token;
                        return StatusCode(200, Content(JsonConvert.SerializeObject(result), "application/json"));
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
                return Unauthorized(uaEx);
            }
            catch (BadHttpRequestException bhrEx)
            {
                return BadRequest(bhrEx);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex);
            }
        }

        [HttpPost("register")]
        public ActionResult Register([FromBody] RegisterDTO registerDTO)
        {
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
                dynamic result = new ExpandoObject();
                result.username = data[0].Username;
                result.token = token;
                return StatusCode(200, Content(JsonConvert.SerializeObject(result), "application/json"));
            }
            catch (BadHttpRequestException bhrEx)
            {
                return BadRequest(bhrEx);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex);
            }
        }

        [HttpPost("logout")]
        public ActionResult Logout()
        {
            return Ok();
        }
    }
}
