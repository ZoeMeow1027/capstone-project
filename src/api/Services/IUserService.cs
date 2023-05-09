using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IUserService
    {
        List<User> GetAllUsers(bool includeDisabled);

        List<User> FindAllUsersByUsername(string name, bool includeDisabled);

        User? GetUserById(int id);

        void AddUser(User item);

        void UpdateUser(User item);

        void DisableUser(User item);

        void EnableUser(User item);

        void ChangeUserType(User item, UserType type);
    }
}
