using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/users")]
    public class UserController : ControllerBase
    {
        private readonly IUserService userService;
        private readonly IUserAddressService userAddressService;

        public UserController(
            IUserService userService,
            IUserAddressService userAddressService
            ): base()
        {
            this.userService = userService;
            this.userAddressService = userAddressService;
        }

        [HttpGet]
        public ActionResult Get(string? type = null, int? id = null, string? nameQuery = null, bool includeDisabled = false)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            if (type == null)
            {
                result.StatusCode = 400;
                result.Message = string.Format("Missing \"type\" parameter!", "type");
            }
            else
            {
                switch (type.ToLower())
                {
                    case "user":
                        result.Data = nameQuery != null
                            ? userService.FindAllUsersByUsernameAndName(nameQuery, includeDisabled)
                            : id == null
                                ? userService.GetAllUsers(includeDisabled)
                                : userService.GetUserById(id.Value);
                        break;
                    case "useraddress":
                        result.Data = nameQuery != null
                            ? userAddressService.FindAllUserAddressesByAddress(nameQuery)
                            : id == null
                                ? userAddressService.GetAllUserAddresses()
                                : userAddressService.GetUserAddressById(id.Value);
                        break;
                    default:
                        result.StatusCode = 400;
                        result.Message = "Invalid \"type\" value!";
                        break;
                }
            }

            return StatusCode(result.StatusCode, result.ToDynamicObject());
        }
    }
}
