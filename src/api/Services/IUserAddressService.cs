using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IUserAddressService
    {
        List<UserAddress> GetAllUserAddresses();

        List<UserAddress> FindAllUserAddressesByAddress(string address);

        UserAddress? GetUserAddressById(int id);

        void AddUserAddress(UserAddress item);

        void UpdateUserAddress(UserAddress item);

        void DeleteUserAddressById(int id);

        void DeleteUserAddress(UserAddress item);
    }
}
