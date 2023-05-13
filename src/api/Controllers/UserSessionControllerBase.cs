using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    public class UserSessionControllerBase : ControllerBase
    {
        private readonly IUserSessionService _userSessionService;

        public UserSessionControllerBase(IUserSessionService userSessionService) : base()
        {
            _userSessionService = userSessionService;
        }

        public void CheckPermission(string? token, List<UserType> allowedUserType)
        {
            if (!_userSessionService.HasTokenPermission(token, allowedUserType))
                throw new UnauthorizedAccessException("Not enough permission to do that!");
        }

        public bool HasPermission(string? token, List<UserType> allowedUserType)
        {
            return _userSessionService.HasTokenPermission(token, allowedUserType);
        }

        public User? GetUserByToken(string? token)
        {
            // Token null => Can't query user
            if (token == null) return null;

            // Token isn't exist => Can't query user
            var userSession = _userSessionService.GetUserSessionByToken(token);
            if (userSession == null) return null;

            // Return user (can be nullable)
            return userSession.User;
        }
    }
}
