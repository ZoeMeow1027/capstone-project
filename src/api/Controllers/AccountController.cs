using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/account")]
    public class AccountController : UserSessionControllerBase
    {
        private readonly IUserAvatarService _userAvatarService;
        private readonly IUserAddressService _userAddressService;
        private readonly IUserService _userService;

        public AccountController(
            IUserSessionService userSessionService,
            IUserAvatarService userAvatarService,
            IUserAddressService userAddressService,
            IUserService userService
            ) : base(userSessionService)
        {
            _userAvatarService = userAvatarService;
            _userAddressService = userAddressService;
            _userService = userService;
        }

        [HttpGet("my")]
        public ActionResult MyInfo()
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                var token = Request.Cookies["token"];
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

        [HttpPost("my")]
        public ActionResult ActionUpdateMyInfo(JToken args)
        {
            ReturnResultTemplate result = new ReturnResultTemplate
            {
                StatusCode = 200
            };

            try
            {
                var token = Request.Cookies["token"];
                User? user = GetUserByToken(token);
                if (user == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                User? userDTO = args.ToObject<User>();
                if (userDTO == null)
                    throw new Exception("Error while converting parameters!");

                List<string> reqArgList = new List<string>() { "id" };
                Utils.CheckRequiredArguments(userDTO, reqArgList);

                if (userDTO.Phone != null)
                {
                    if (!Utils.DataValidate.IsValidPhone(userDTO.Phone))
                    {
                        throw new BadHttpRequestException("Failed while validating 'phone' parameter!");
                    }
                }
                if (userDTO.Email != null)
                {
                    if (!Utils.DataValidate.IsValidEmail(userDTO.Email))
                    {
                        throw new BadHttpRequestException("Failed while validating 'email' parameter!");
                    }
                }

                // User [Update] - Check if exist
                var dataTemp = _userService.GetUserById(user.ID);
                if (dataTemp == null)
                {
                    throw new BadHttpRequestException("Your account is not accessible. May be logged out or disabled?");
                }

                dataTemp.Name = userDTO.Name ?? dataTemp.Name;
                dataTemp.Email = userDTO.Email ?? dataTemp.Email;
                dataTemp.Phone = userDTO.Phone ?? dataTemp.Phone;

                // User [Update] - Begin updating
                _userService.UpdateUser(dataTemp);

                result.StatusCode = 200;
                result.Message = "Successful";
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

        [HttpGet("avatar")]
        public ActionResult GetAvatar()
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                var token = Request.Cookies["token"];
                User? user = GetUserByToken(token);
                if (user == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                var data = _userAvatarService.GetAvatar(user.ID);
                if (data == null)
                    throw new BadHttpRequestException("User has no avatar!");

                return File(data, "image/jpeg");
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

        [HttpPost("avatar")]
        public ActionResult SetAvatar([FromForm] UserAvatarDTO userAvatarDTO)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                var token = Request.Cookies["token"];
                User? user = GetUserByToken(token);
                if (user == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                _userAvatarService.SetAvatar(user.ID, userAvatarDTO.File);

                result.StatusCode = 200;
                result.Message = "Successful";
                result.Data = null;
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

            return StatusCode(result.StatusCode, result);
        }

        [HttpDelete("avatar")]
        public ActionResult DeleteAvatar()
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                var token = Request.Cookies["token"];
                User? user = GetUserByToken(token);
                if (user == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                _userAvatarService.RemoveAvatar(user.ID);

                result.StatusCode = 200;
                result.Message = "Successful";
                result.Data = null;
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

        [HttpPost("logout")]
        public ActionResult Logout()
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                string? token = Request.Cookies["token"];
                User? user = GetUserByToken(token);
                if (user == null)
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

        [HttpPost("change-password")]
        public ActionResult ChangePassword(JToken args)
        {
            ReturnResultTemplate result = new ReturnResultTemplate
            {
                StatusCode = 200
            };

            try
            {
                var token = Request.Cookies["token"];
                User? user = GetUserByToken(token);
                if (user == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                ChangePasswordDTO? changePassDTO = args.ToObject<ChangePasswordDTO>();
                if (changePassDTO == null)
                    throw new Exception("Error while converting parameters!");

                List<string> reqArgList = new List<string>() { "old-pass", "new-pass", "re-new-pass" };
                Utils.CheckRequiredArguments(changePassDTO, reqArgList);

                if (changePassDTO.NewPassword.Length < 6)
                {
                    throw new BadHttpRequestException("Password must be at least 6 characters!");
                }
                if (changePassDTO.NewPassword.CompareTo(changePassDTO.ConfirmPassword) != 0)
                {
                    throw new BadHttpRequestException("New password and reenter new password mismatch!");
                }

                // Check if exist
                var dataTemp = _userService.GetUserById(user.ID);
                if (dataTemp == null)
                {
                    throw new BadHttpRequestException("Your account is not accessible. May be logged out or disabled?");
                }

                // Check old password
                if (Utils.EncryptSHA256(string.Format("{0}{1}", changePassDTO.OldPassword, user.PasswordHash)).CompareTo(user.Password) != 0)
                {
                    throw new BadHttpRequestException("Your old password is incorrect!");
                }

                // Encrypt password here! Make sure equals to Add user.
                string pHash = Utils.RandomString(12);
                dataTemp.PasswordHash = pHash;
                dataTemp.Password = Utils.EncryptSHA256(string.Format("{0}{1}", changePassDTO.NewPassword, pHash));


                // Begin updating
                _userService.UpdateUser(dataTemp);

                result.StatusCode = 200;
                result.Message = "Successful";
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

        [HttpGet("address")]
        public ActionResult GetAddress(int? id = null)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                string? token = Request.Cookies["token"];
                User? user = GetUserByToken(token);
                if (user == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                result.StatusCode = 200;
                result.Message = "Successful!";
                result.Data = id == null
                    ? _userAddressService.GetUserAddresses(user.ID)
                    : _userAddressService.GetUserAddressById(id.Value, user.ID);

                if (result.Data == null)
                {
                    throw new BadHttpRequestException("Data not found!");
                }
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

        [HttpPost("address")]
        public ActionResult ActionAddress(JToken args)
        {
            ReturnResultTemplate result = new ReturnResultTemplate
            {
                StatusCode = 200,
                Message = "Successful!"
            };

            try
            {
                string? token = Request.Cookies["token"];
                User? user = GetUserByToken(token);
                if (user == null)
                    throw new UnauthorizedAccessException("Session has expired.");

                RequestDTO? userDTO = args.ToObject<RequestDTO>();
                if (userDTO == null)
                    throw new Exception("Error while converting parameters!");

                if (userDTO.Action == null || userDTO.Data == null)
                {
                    throw new BadHttpRequestException("Missing parameters!");
                }

                switch (userDTO.Action.ToLower())
                {
                    case "add":
                        AddressAdd(userDTO.Data.ToObject<UserAddress>(), user.ID);
                        break;
                    case "update":
                        AddressUpdate(userDTO.Data.ToObject<UserAddress>(), user.ID);
                        break;
                    case "delete":
                        AddressDelete(userDTO.Data.ToObject<UserAddress>(), user.ID);
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


        #region User Address area
        private void AddressAdd(UserAddress userAddress, int userID)
        {
            List<string> reqArgList = new List<string>() { "name", "address", "phone" };
            Utils.CheckRequiredArguments(userAddress, reqArgList);

            if (userAddress.Name.Length < 5)
            {
                throw new ArgumentException("Name must be at least 5 characters!");
            }
            if (userAddress.Address.Length < 15)
            {
                throw new ArgumentException("Address must be at least 15 characters!");
            }
            if (!Utils.DataValidate.IsValidPhone(userAddress.Phone))
            {
                throw new BadHttpRequestException("Failed while validating Phone parameter!");
            }
            userAddress.UserID = userID;

            _userAddressService.AddUserAddress(userAddress);
        }

        private void AddressUpdate(UserAddress userAddress, int userID)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(userAddress, reqArgList);

            if (userAddress.Name.Length < 5)
            {
                throw new ArgumentException("Name must be at least 5 characters!");
            }
            if (userAddress.Address.Length < 15)
            {
                throw new ArgumentException("Address must be at least 15 characters!");
            }
            if (!Utils.DataValidate.IsValidPhone(userAddress.Phone))
            {
                throw new BadHttpRequestException("Failed while validating Phone parameter!");
            }

            var dataTemp = _userAddressService.GetUserAddressById(userAddress.ID, userID);
            // Address - Check if exist
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("User Address with ID {0} is not exist!", userAddress.ID));
            }
            // Address - Check if have permission
            if (dataTemp.UserID != userID)
            {
                throw new UnauthorizedAccessException(string.Format("You have not permission to access address with ID {0}!", userAddress.ID));
            }

            dataTemp.Name = userAddress.Name;
            dataTemp.Address = userAddress.Address;
            dataTemp.Phone = userAddress.Phone;

            _userAddressService.UpdateUserAddress(dataTemp);
        }

        private void AddressDelete(UserAddress userAddress, int userID)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(userAddress, reqArgList);

            var dataTemp = _userAddressService.GetUserAddressById(userAddress.ID, userID);
            // Address - Check if exist
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("User Address with ID {0} is not exist!", userAddress.ID));
            }
            // Address - Check if have permission
            if (dataTemp.UserID != userID)
            {
                throw new UnauthorizedAccessException(string.Format("You have not permission to access address with ID {0}!", userAddress.ID));
            }

            _userAddressService.DeleteUserAddress(userAddress);
        }
        #endregion
    }
}
