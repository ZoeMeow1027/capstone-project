using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IUserSessionService
    {
        string CreateAndStoreAccountToken(int userId, long expiresInDay);

        UserSession? GetUserSessionByToken(string token);

        void DeleteSessionByToken(string token);

        void LogOutAllByUserID(int userId);

        bool IsExpired(string token);

        bool IsAccountLocked(string token);
    }
}
