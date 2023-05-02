using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
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

        [HttpPost]
        public ActionResult Post(JToken args)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                string? action = (string?)args["action"];
                string? type = (string?)args["type"];
                dynamic? data = args["data"];

                if (action == null || type == null || data == null)
                {
                    throw new ArgumentException("Invalid requests!");
                }

#pragma warning disable CS8602 // Dereference of a possibly null reference.
                switch (type.ToLower())
                {
                    case "user":
                        switch (action.ToLower())
                        {
                            case "add":
                                AddUser(data);
                                break;
                            case "update":
                                // TODO: User - Update
                                break;
                            case "delete":
                                // TODO: User - Delete
                                break;
                            case "enable":
                            case "disable":
                                ToggleUserEnabled(data);
                                break;
                            case "changetype":
                                ChangeUserType(data);
                                break;
                            case "changepassword":
                                ChangePassword(data);
                                break;
                            default:
                                throw new ArgumentException("Invalid \"action\" value!", "action");
                        }
                        break;
                    case "useraddress":
                        switch (action.ToLower())
                        {
                            case "add":
                                AddUserAddress(data);
                                break;
                            case "update":
                                // TODO: User Address - Update
                                break;
                            case "delete":
                                // TODO: User Address - Delete
                                break;
                            default:
                                throw new ArgumentException("Invalid \"action\" value!", "action");
                        }
                        break;
                    default:
                        throw new ArgumentException("Invalid \"type\" value!", "type");
                }
#pragma warning restore CS8602 // Dereference of a possibly null reference.
            }
            catch (ArgumentNullException argNullEx)
            {
                result.StatusCode = 400;
                result.Message = argNullEx.Message;
            }
            catch (ArgumentException argEx)
            {
                result.StatusCode = 400;
                result.Message = argEx.Message;
            }
            catch (Exception ex)
            {
                result.StatusCode = 500;
                result.Message = ex.Message;
            }

            return StatusCode(result.StatusCode, result.ToDynamicObject());
        }

        #region User area
        private void AddUser(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "username", "password", "name" };

            foreach(string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            // TODO: Add User - Check if user is exist!
            // TODO: Add User - Check if username is valid!
            // TODO: Add User - Check if password is valid!
            // TODO: Add User - CHeck if email and phone are valid!
            userService.AddUser(new Model.User()
            {
                Username = (string)data[reqParamList[0]],
                // TODO: Add User - Encrypt password here!
                Password = (string)data[reqParamList[1]],
                Name = (string)data[reqParamList[2]],
                Email = (string?)data["email"],
                Phone = (string?)data["phone"]
            });
        }

        private void ToggleUserEnabled(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "userid", "enabled" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            // Check if user is exist.
            var dataTemp = userService.GetUserById((int)data["userid"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("User ID isn't exist!");
            }

            switch ((bool)data["enabled"])
            {
                case true:
                    userService.EnableUser(dataTemp);
                    break;
                case false:
                    userService.DisableUser(dataTemp);
                    break;
            }
        }

        private void ChangeUserType(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "userid", "usertype" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            // Check if "usertype" value is valid!
            if ((int)data["type"] < 0 || (int)data["usertype"] > 2)
            {
                throw new ArgumentException("Invalid \"usertype\" value!", "usertype");
            }

            // Check if user is exist!
            var dataTemp = userService.GetUserById((int)data["userid"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("User ID isn't exist!");
            }

            userService.ChangeUserType(dataTemp, (UserType)data["usertype"]);
        }

        private void ChangePassword(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "userid", "newpassword" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            var dataTemp = userService.GetUserById((int)data["userid"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("User ID isn't exist!");
            }

            // TODO: Change User Password - Encrypt password here! Make sure equals to Add user.
            dataTemp.Password = data["newpassword"];
            userService.UpdateUser(dataTemp);
        }
        #endregion

        #region User Address area
        private void AddUserAddress(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "userid", "name", "address", "phone" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            // TODO: Add User Address - Check if user id is valid!
            // TODO: Add User Address - CHeck if phone are valid!
            userAddressService.AddUserAddress(new UserAddress()
            {
                UserID = (int)data[reqParamList[0]],
                Name = (string)data[reqParamList[1]],
                Address = (string)data[reqParamList[2]],
                Phone = (string)data.Get(reqParamList[3])
            });
        }
        #endregion
    }
}
