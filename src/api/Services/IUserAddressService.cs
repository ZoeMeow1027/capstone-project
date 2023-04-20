using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IUserAddressService
    {
        List<UserAddress> GetAllUserAddresses();

        List<UserAddress> FindAllUserAddressesByAddress(string address);

        UserAddress GetUserAddressById(int id);

        bool AddUserAddress(UserAddress item);

        bool EditUserAddress(UserAddress item);

        bool HideUserAddressById(int id);

        bool HideUserAddress(UserAddress item);
    }
}
