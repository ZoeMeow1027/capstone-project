using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Model.Enums;
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
        public ActionResult Get(string? type = null, int? id = null, string? query = null, bool includedisabled = false)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                if (type == null)
                    throw new BadHttpRequestException("Missing \"type\" parameter!");

                CheckPermission(
                        Request.Cookies["token"],
                        new List<UserType>() { UserType.Administrator }
                        );

                switch (type.ToLower())
                {
                    case "user":
                        result.Data = query != null
                            ? userService.FindAllUsersByUsername(query, includedisabled)
                            : id == null
                                ? userService.GetAllUsers(includedisabled)
                                : userService.GetUserById(id.Value);
                        break;
                    case "useraddress":
                        result.Data = query != null
                            ? userAddressService.FindAllUserAddressesByAddress(query)
                            : id == null
                                ? userAddressService.GetUserAddresses()
                                : userAddressService.GetUserAddressById(id.Value);
                        break;
                    default:
                        throw new BadHttpRequestException("Invalid \"type\" value!");
                }
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 403;
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
            ReturnResultTemplate result = new ReturnResultTemplate
            {
                StatusCode = 200
            };

            try
            {
                RequestDTO? userDTO = args.ToObject<RequestDTO>();
                if (userDTO == null)
                    throw new Exception("Error while converting parameters!");

                CheckPermission(
                    Request.Cookies["token"],
                    new List<UserType>() { UserType.Administrator }
                    );

                if (userDTO.Type == null || userDTO.Action == null || userDTO.Data == null)
                {
                    throw new BadHttpRequestException("Missing parameters!");
                }

                // This line for avoid null reference issue from VS.

                switch (userDTO.Type.ToLower())
                {
                    case "user":
                        switch (userDTO.Action.ToLower())
                        {
                            case "add":
                                AddUser(userDTO.Data.ToObject<UserDataDTO>());
                                break;
                            case "update":
                                UpdateUser(userDTO.Data.ToObject<UserDataDTO>());
                                break;
                            case "enable":
                                ToggleUserEnabled(userDTO.Data.ToObject<UserDataDTO>(), true);
                                break;
                            case "disable":
                                ToggleUserEnabled(userDTO.Data.ToObject<UserDataDTO>(), false);
                                break;
                            case "changeusertype":
                                ChangeUserType(userDTO.Data.ToObject<UserDataDTO>());
                                break;
                            case "changepassword":
                                ChangePassword(userDTO.Data.ToObject<UserDataDTO>());
                                break;
                            default:
                                throw new BadHttpRequestException("Invalid \"action\" value!");
                        }
                        break;
                    case "useraddress":
                        switch (userDTO.Action.ToLower())
                        {
                            case "add":
                                AddUserAddress(userDTO.Data.ToObject<UserAddressDataDTO>());
                                break;
                            case "update":
                                UpdateUserAddress(userDTO.Data.ToObject<UserAddressDataDTO>());
                                break;
                            case "delete":
                                DeleteUserAddress(userDTO.Data.ToObject<UserAddressDataDTO>());
                                break;
                            default:
                                throw new BadHttpRequestException("Invalid \"action\" value!");
                        }
                        break;
                    default:
                        throw new BadHttpRequestException("Invalid \"type\" value!");
                }
            }
            catch (ArgumentException argEx)
            {
                result.StatusCode = 400;
                result.Message = argEx.Message;
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 403;
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

        #region User area
        private void AddUser(UserDataDTO userDataDTO)
        {
            List<string> reqArgList = new List<string>() { "username", "password", "name" };
            Utils.CheckRequiredArguments(userDataDTO, reqArgList);

            // TODO: User [Add] - Check if username is valid or not exist.
            // TODO: User [Add] - Check if password is valid.

            // User [Update] - Check if exist
            if (userDataDTO.Phone != null)
            {
                if (!Utils.DataValidate.IsValidPhone(userDataDTO.Phone))
                {
                    throw new BadHttpRequestException("Failed while validating 'phone' parameter!");
                }
            }
            if (userDataDTO.Email != null)
            {
                if (!Utils.DataValidate.IsValidEmail(userDataDTO.Email))
                {
                    throw new BadHttpRequestException("Failed while validating 'email' parameter!");
                }
            }
            if (userDataDTO.UserType != null)
            {
                if (!(new List<int>() { 0, 1, 2 }.Contains(userDataDTO.UserType.Value)))
                {
                    throw new BadHttpRequestException("Invalid 'usertype' value!");
                }
            }
            if (userDataDTO.Password.Length < 6)
            {
                throw new ArgumentException("Password must be greater than 5 characters!");
            }

            // User [Add] - Begin adding
            string pHash = Utils.RandomString(12);
            userService.AddUser(new User()
            {
                Username = userDataDTO.Username,
                // User [Add] - Encrypt password here!
                PasswordHash = pHash,
                Password = Utils.EncryptSHA256(string.Format("{0}{1}", userDataDTO.Password, pHash)),
                Name = userDataDTO.Name,
                Email = userDataDTO.Email,
                Phone = userDataDTO.Phone,
                UserType = (UserType)userDataDTO.UserType.Value
            });
        }

        private void UpdateUser(UserDataDTO userDataDTO)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(userDataDTO, reqArgList);

            // TODO: User [Update] - Check if username is valid or not exist.

            // User [Update] - Check if email and phone are valid!
            if (userDataDTO.Phone != null)
            {
                if (!Utils.DataValidate.IsValidPhone(userDataDTO.Phone))
                {
                    throw new BadHttpRequestException("Failed while validating 'phone' parameter!");
                }
            }
            if (userDataDTO.Email != null)
            {
                if (!Utils.DataValidate.IsValidEmail(userDataDTO.Email))
                {
                    throw new BadHttpRequestException("Failed while validating 'email' parameter!");
                }
            }
            if (userDataDTO.UserType != null)
            {
                if (!(new List<int>() { 0, 1, 2 }.Contains(userDataDTO.UserType.Value)))
                {
                    throw new BadHttpRequestException("Invalid 'usertype' value!");
                }
            }

            // User [Update] - Check if exist
            var dataTemp = userService.GetUserById(userDataDTO.ID.Value);
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("User ID {0} is not exist!", userDataDTO.ID));
            }

            dataTemp.Username = userDataDTO.Username ?? dataTemp.Username;
            dataTemp.Name = userDataDTO.Name ?? dataTemp.Name;
            dataTemp.Email = userDataDTO.Email ?? dataTemp.Email;
            dataTemp.Phone = userDataDTO.Phone ?? dataTemp.Phone;
            dataTemp.IsEnabled = userDataDTO.IsEnabled ?? dataTemp.IsEnabled;
            dataTemp.DisabledReason = userDataDTO.DisabledReason ?? dataTemp.DisabledReason;
            dataTemp.UserType = (UserType?)userDataDTO.UserType ?? dataTemp.UserType;
            dataTemp.DateModified = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            dataTemp.UserType = (UserType)userDataDTO.UserType.Value;


            // User [Update] - Encrypt password here!
            // User [Update] - Check if given password is valid.
            if (userDataDTO.Password != null)
            {
                if (userDataDTO.Password.Length < 6)
                    throw new ArgumentException("Password must be greater than 5 characters!");
                string pHash = Utils.RandomString(12);
                dataTemp.PasswordHash = pHash;
                dataTemp.Password = Utils.EncryptSHA256(string.Format("{0}{1}", userDataDTO.Password, pHash));
            }

            // User [Update] - Begin updating
            userService.UpdateUser(dataTemp);
        }

        private void ToggleUserEnabled(UserDataDTO userDataDTO, bool enabled)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(userDataDTO, reqArgList);

            User? user = userService.GetUserById(userDataDTO.ID.Value);
            if (user == null)
            {
                throw new ArgumentException(string.Format("User {0} is not exist!", userDataDTO.ID));
            }
            if (user.Username.ToLower() == "admin")
            {
                throw new UnauthorizedAccessException("'admin' account cannot be disabled or control here!");
            }

            // User [Toggle User] - Check if user exist
            var dataTemp = userService.GetUserById(userDataDTO.ID.Value);
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("User ID {0} is not exist!", userDataDTO.ID));
            }

            // User [Toggle User] - Begin toggling
            switch (enabled)
            {
                case true:
                    userService.EnableUser(dataTemp);
                    break;
                case false:
                    // If disable, will logout all user sessions.
                    DeleteAllSessionByUserID(dataTemp.ID);
                    userService.DisableUser(dataTemp);
                    break;
            }
        }

        private void ChangeUserType(UserDataDTO userDataDTO)
        {
            List<string> reqArgList = new List<string>() { "id", "usertype" };
            Utils.CheckRequiredArguments(userDataDTO, reqArgList);

            // User [Change User Type] - Check if "usertype" value is valid
            if (userDataDTO.UserType < 0 || userDataDTO.UserType > 2)
            {
                throw new BadHttpRequestException("Invalid \"usertype\" value!");
            }

            // User [Change User Type] - Check if user is exist
            var dataTemp = userService.GetUserById(userDataDTO.ID.Value);
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("User ID {0} is not exist!", userDataDTO.ID));
            }

            // User [Change User Type] - Begin changing
            userService.ChangeUserType(dataTemp, (UserType)userDataDTO.UserType);
        }

        private void ChangePassword(UserDataDTO userDataDTO)
        {
            List<string> reqArgList = new List<string>() { "id", "password" };
            Utils.CheckRequiredArguments(userDataDTO, reqArgList);

            // User [Change Password] - Check if user exist
            var dataTemp = userService.GetUserById(userDataDTO.ID.Value);
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("User ID {0} is not exist!", userDataDTO.ID));
            }

            // User [Change Password] - Encrypt password here! Make sure equals to Add user.
            string pHash = Utils.RandomString(12);
            dataTemp.PasswordHash = pHash;
            dataTemp.Password = Utils.EncryptSHA256(string.Format("{0}{1}", userDataDTO.Password, pHash));

            // User [Change Password] - Begin changing
            userService.UpdateUser(dataTemp);
        }
        #endregion

        #region User Address area
        private void AddUserAddress(UserAddressDataDTO data)
        {
            List<string> reqArgList = new List<string>() { "userid", "name", "address", "phone" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // User Address [Add] - CHeck if phone are valid!
            if (data.Phone != null)
            {
                if (!Utils.DataValidate.IsValidPhone(data.Phone))
                {
                    throw new BadHttpRequestException("Failed while validating 'phone' parameter!");
                }
            }
            // User Address [Add] - Check if user is exist!
            var dataTemp = userService.GetUserById(data.UserID.Value);
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("User ID {0} is not exist!", data.UserID));
            }

            // User Address [Add] - Begin adding
            userAddressService.AddUserAddress(new UserAddress()
            {
                UserID = data.UserID.Value,
                Name = data.Name,
                Address = data.Address,
                Phone = data.Phone
            });
        }

        private void UpdateUserAddress(UserAddressDataDTO data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // User Address [Update] - Check if address exist
            var dataTemp = userAddressService.GetUserAddressById(data.ID.Value);
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("UserAddress ID {0} is not exist!", data.ID));
            }
            // User Address [Update] - Check if user exist
            if (data.UserID != null)
            {
                if (userService.GetUserById(data.UserID.Value) == null)
                {
                    throw new BadHttpRequestException(string.Format("User ID {0} is not exist!", data.UserID));
                }
            }
            // User Address [Update] - Check if phone is valid.
            if (data.Phone != null)
            {
                if (!Utils.DataValidate.IsValidPhone(data.Phone))
                {
                    throw new BadHttpRequestException("Failed while validating 'phone' parameter!");
                }
            }

            // User Address [Update] - Begin updating
            dataTemp.UserID = data.UserID ?? dataTemp.UserID;
            dataTemp.Name = data.Name ?? dataTemp.Name;
            dataTemp.Address = data.Address ?? dataTemp.Address;
            dataTemp.Phone = data.Phone ?? dataTemp.Phone;
            userAddressService.UpdateUserAddress(dataTemp);

        }

        private void DeleteUserAddress(UserAddressDataDTO data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // User Address [Delete] - Begin deleting
            userAddressService.DeleteUserAddressById(data.ID.Value);
        }
        #endregion
    }
}
