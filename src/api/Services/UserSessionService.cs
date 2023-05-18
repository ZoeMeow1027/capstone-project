using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class UserSessionService : IUserSessionService
    {
        private readonly DataContext _context;

        public UserSessionService(DataContext context)
        {
            _context = context;
        }

        public string CreateAndStoreAccountToken(int userId, long expiresInDay = 7)
        {
            if (expiresInDay < 0)
            {
                throw new Exception(string.Format("Invalid expires day value! (Must be greater than 0) You entered {0}.", expiresInDay));
            }

            User? user = _context.Users.Where(p => p.ID == userId).FirstOrDefault();
            if (user != null)
            {
                // TODO: This is just a temporary solution. This needs be changed for security!!!
                string token = "";
                do
                {
                    var tokenPre = string.Format("{0}|{1}|{2}|{3}|{4}", user.Username, user.Email, user.Phone, user.Password, DateTime.Now.ToString("yyyy/MM/dd-hh:mm:ss-tt"));
                    token = Utils.EncryptSHA256(tokenPre);
                }
                // If is exist in any session, will create new token instead.
                while (_context.UserSessions.FirstOrDefault(p => p.Token == token) != null);

                UserSession userSession = new UserSession();
                userSession.UserID = userId;
                userSession.Token = token;
                userSession.DateCreated = DateTimeOffset.Now.ToUnixTimeMilliseconds();
                userSession.DateExpired = DateTimeOffset.Now.AddDays(expiresInDay).ToUnixTimeMilliseconds();

                _context.UserSessions.Add(userSession);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }

                return token;
            }
            else throw new Exception(string.Format("User with ID {0} is not exist!", userId));
        }

        public void DeleteSessionByToken(string token)
        {
            var data = GetUserSessionByToken(token);
            if (data != null)
            {
                _context.UserSessions.Remove(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else throw new Exception("Token is not exist!");
        }

        public UserSession? GetUserSessionByToken(string token)
        {
            var data = _context.UserSessions.Include(p => p.User).Where(p => p.Token == token).FirstOrDefault();
            if (data == null)
            {
                return null;
            }
            if (data.DateExpired < DateTimeOffset.Now.ToUnixTimeMilliseconds())
            {
                try { DeleteSessionByToken(token); } catch { }
                return null;
            }
            return data;
        }

        public bool HasTokenPermission(string? token, List<UserType> allowedType)
        {
            if (token == null)
                return false;

            var session = GetUserSessionByToken(token);
            if (session == null)
                return false;

            return allowedType.Contains(session.User.UserType);
        }

        public bool IsAccountLocked(string token)
        {
            var data = GetUserSessionByToken(token);
            if (data != null)
            {
                return !data.User.IsEnabled;
            }
            else throw new Exception("Token is not exist!");
        }

        public bool IsExpired(string token)
        {
            var data = GetUserSessionByToken(token);
            if (data != null)
            {
                return data.DateExpired < DateTimeOffset.Now.ToUnixTimeMilliseconds();
            }
            else throw new Exception("Token is not exist!");
        }

        public void LogOutAllByUserID(int userId)
        {
            var data = _context.UserSessions.Where(p => p.User.ID == userId).ToList();
            _context.UserSessions.RemoveRange(data);
            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            }
        }
    }
}
