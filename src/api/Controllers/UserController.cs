using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/users")]
    public class UserController : UserSessionControllerBase
    {
        private readonly IUserService userService;
        private readonly IUserAddressService userAddressService;

        public UserController(
            IUserService userService,
            IUserAddressService userAddressService,
            IUserSessionService userSessionService
            ) : base(userSessionService)
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
                try
                {
                    CheckPermission(
                        Request.Cookies["token"],
                        new List<UserType>() { UserType.Administrator }
                        );

                    switch (type.ToLower())
                    {
                        case "user":
                            result.Data = nameQuery != null
                                ? userService.FindAllUsersByUsername(nameQuery, includeDisabled)
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
                            throw new BadHttpRequestException("Invalid \"type\" value!");
                    }
                }
                catch (UnauthorizedAccessException uaEx)
                {
                    result.StatusCode = 401;
                    result.Message = uaEx.Message;
                }
                catch (BadHttpRequestException bhqEx)
                {
                    result.StatusCode = 400;
                    result.Message = bhqEx.Message;
                }
                catch (Exception ex)
                {
                    result.StatusCode = 500;
                    result.Message = ex.Message;
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
                CheckPermission(
                    Request.Cookies["token"],
                    new List<UserType>() { UserType.Administrator }
                    );

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
                                UpdateUser(data);
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
                                UpdateUserAddress(data);
                                break;
                            case "delete":
                                DeleteUserAddress(data);
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
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 401;
                result.Message = uaEx.Message;
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
            List<string> reqArgList = new List<string>() { "username", "password", "name" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // TODO: User [Add] - Check if username is valid or not exist.
            // TODO: User [Add] - Check if password is valid.

            // User [Add] - Check if email and phone are valid.
            if ((string?)data["phone"] != null)
            {
                if (!Utils.DataValidate.IsValidPhone((string?)data["phone"]))
                {
                    throw new ArgumentException("Failed while validating 'phone' parameter!", "phone");
                }
            }
            if ((string?)data["email"] != null)
            {
                if (!Utils.DataValidate.IsValidEmail((string?)data["email"]))
                {
                    throw new ArgumentException("Failed while validating 'email' parameter!", "email");
                }
            }

            // User [Add] - Begin adding
            userService.AddUser(new User()
            {
                Username = (string)data["username"],
                // TODO: User [Add] - Encrypt password here!
                Password = (string)data["password"],
                Name = (string)data["name"],
                Email = (string?)data["email"],
                Phone = (string?)data["phone"]
            });
        }

        private void UpdateUser(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // TODO: User [Update] - Check if username is valid or not exist.
            // TODO: User [Update] - Check if password is valid.
            // User [Update] - Check if email and phone are valid!
            if ((string?)data["phone"] != null)
            {
                if (!Utils.DataValidate.IsValidPhone((string?)data["phone"]))
                {
                    throw new ArgumentException("Failed while validating 'phone' parameter!", "phone");
                }
            }
            if ((string?)data["email"] != null)
            {
                if (!Utils.DataValidate.IsValidEmail((string?)data["email"]))
                {
                    throw new ArgumentException("Failed while validating 'email' parameter!", "email");
                }
            }

            // User [Update] - Check if exist
            var dataTemp = userService.GetUserById((int)data["id"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("User ID isn't exist!");
            }

            dataTemp.Username = (string?)data["username"] ?? dataTemp.Username;
            // TODO: User [Update] - Encrypt password here!
            dataTemp.Password = (string?)data["password"] ?? dataTemp.Password;
            dataTemp.Name = (string?)data["name"] ?? dataTemp.Name;
            dataTemp.Email = (string?)data["email"] ?? dataTemp.Email;
            dataTemp.Phone = (string?)data["phone"] ?? dataTemp.Phone;
            dataTemp.IsEnabled = (bool?)data["isenabled"] ?? dataTemp.IsEnabled;
            dataTemp.DisabledReason = (string?)data["disabledreason"] ?? dataTemp.DisabledReason;
            dataTemp.UserType = (UserType?)data["usertype"] ?? dataTemp.UserType;
            dataTemp.DateModified = DateTime.UtcNow;
            // User [Update] - Begin updating
            userService.UpdateUser(dataTemp);
        }

        private void ToggleUserEnabled(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "userid", "enabled" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // User [Toggle User] - Check if user exist
            var dataTemp = userService.GetUserById((int)data["userid"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("User ID isn't exist!");
            }

            // User [Toggle User] - Begin toggling
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
            List<string> reqArgList = new List<string>() { "userid", "usertype" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // User [Change User Type] - Check if "usertype" value is valid
            if ((int)data["type"] < 0 || (int)data["usertype"] > 2)
            {
                throw new ArgumentException("Invalid \"usertype\" value!", "usertype");
            }

            // User [Change User Type] - Check if user is exist
            var dataTemp = userService.GetUserById((int)data["userid"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("User ID isn't exist!");
            }

            // User [Change User Type] - Begin changing
            userService.ChangeUserType(dataTemp, (UserType)data["usertype"]);
        }

        private void ChangePassword(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "userid", "newpassword" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // User [Change Password] - Check if user exist
            var dataTemp = userService.GetUserById((int)data["userid"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("User ID isn't exist!");
            }

            // TODO: User [Change Password] - Encrypt password here! Make sure equals to Add user.
            dataTemp.Password = data["newpassword"];

            // User [Change Password] - Begin changing
            userService.UpdateUser(dataTemp);
        }
        #endregion

        #region User Address area
        private void AddUserAddress(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "userid", "name", "address", "phone" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // TODO: User Address [Add] - Check if user is exist!
            // User Address [Add] - CHeck if phone are valid!
            if ((string?)data["phone"] != null)
            {
                if (!Utils.DataValidate.IsValidPhone((string?)data["phone"]))
                {
                    throw new ArgumentException("Failed while validating 'phone' parameter!", "phone");
                }
            }

            // User Address [Add] - Begin adding
            userAddressService.AddUserAddress(new UserAddress()
            {
                UserID = (int)data["userid"],
                Name = (string)data["name"],
                Address = (string)data["address"],
                Phone = (string)data["phone"]
            });
        }

        private void UpdateUserAddress(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // User Address [Update] - Check if address exist
            var dataTemp = userAddressService.GetUserAddressById((int)data["id"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("UserAddress with ID is not exist!", "id");
            }

            // User Address [Update] - Check if user exist
            if (userService.GetUserById((int)data["userid"]) == null)
            {
                throw new ArgumentException("User with ID is not exist!", "userid");
            }

            // User Address [Update] - Check if phone is valid.
            if ((string?)data["phone"] != null)
            {
                if (!Utils.DataValidate.IsValidPhone((string?)data["phone"]))
                {
                    throw new ArgumentException("Failed while validating 'phone' parameter!", "phone");
                }
            }

            // User Address [Update] - Begin updating
            dataTemp.UserID = (int?)data["userid"] ?? dataTemp.UserID;
            dataTemp.Name = (string?)data["name"] ?? dataTemp.Name;
            dataTemp.Address = (string?)data["address"] ?? dataTemp.Address;
            dataTemp.Phone = (string?)data["phone"] ?? dataTemp.Phone;
            userAddressService.UpdateUserAddress(dataTemp);

        }

        private void DeleteUserAddress(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // User Address [Delete] - Begin deleting
            userAddressService.DeleteUserAddressById((int)data["id"]);
        }
        #endregion
    }
}
