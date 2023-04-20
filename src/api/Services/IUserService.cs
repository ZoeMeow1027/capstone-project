using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IUserService
    {
        List<User> GetAllUsers();

        List<User> FindAllUsersByName(string name);

        User GetUserById(int id);

        bool AddUser(User item);

        bool EditUser(User item);

        bool HideUserById(int id);

        bool HideUser(User item);
    }
}
