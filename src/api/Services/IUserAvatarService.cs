using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IUserAvatarService
    {
        void SetAvatar(int userid, IFormFile avatar);

        void SetAvatar(User user, IFormFile avatar);

        void RemoveAvatar(int userid);

        void RemoveAvatar(User user);

        byte[]? GetAvatar(int userid);

        byte[]? GetAvatar(User user);
    }
}
