using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IUserAddressService
    {
        List<UserAddress> GetUserAddresses(int? userId = null);

        List<UserAddress> FindAllUserAddressesByAddress(string address, int? userId = null);

        UserAddress? GetUserAddressById(int id, int? userId = null);

        void AddUserAddress(UserAddress item);

        void UpdateUserAddress(UserAddress item);

        void DeleteUserAddressById(int id);

        void DeleteUserAddress(UserAddress item);
    }
}
