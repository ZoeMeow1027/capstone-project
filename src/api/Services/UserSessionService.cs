﻿using PhoneStoreManager.Model;
using System.Security.Cryptography;
using System.Text;

namespace PhoneStoreManager.Services
{
    public class UserSessionService : IUserSessionService
    {
        private readonly DataContext _context;

        public UserSessionService(DataContext context)
        {
            _context = context;
        }

        //public static String sha256_hash(String value)
        //{
        //    StringBuilder Sb = new StringBuilder();

        //    using (SHA256 hash = SHA256Managed.Create())
        //    {
        //        Encoding enc = Encoding.UTF8;
        //        Byte[] result = hash.ComputeHash(enc.GetBytes(value));

        //        foreach (Byte b in result)
        //            Sb.Append(b.ToString("x2"));
        //    }

        //    return Sb.ToString();
        //}

        // Note: this is available only with .NET 5 or higher.
        // Source: https://stackoverflow.com/a/17001289
        private static string sha256_hash(string value)
        {
            StringBuilder Sb = new StringBuilder();

            using (var hash = SHA256.Create())
            {
                Encoding enc = Encoding.UTF8;
                byte[] result = hash.ComputeHash(enc.GetBytes(value));

                foreach (byte b in result)
                    Sb.Append(b.ToString("x2"));
            }

            return Sb.ToString();
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
                    token = sha256_hash(tokenPre);
                }
                // If is exist in any session, will create new token instead.
                while (_context.UserSessions.FirstOrDefault(p => p.Token == token) != null);

                UserSession userSession = new UserSession();
                userSession.ID = userId;
                userSession.Token = token;
                userSession.DateCreated = DateTime.Now;
                userSession.DateExpired = DateTime.Now.AddDays(expiresInDay);

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
            return _context.UserSessions.Where(p => p.Token == token).FirstOrDefault();
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
                return data.DateExpired.ToLocalTime() < DateTime.Now.ToLocalTime();
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
