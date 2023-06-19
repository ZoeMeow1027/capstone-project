using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class UserAddressService : IUserAddressService
    {
        private readonly DataContext _context;

        public UserAddressService(DataContext context)
        {
            _context = context;
        }

        public void AddUserAddress(UserAddress item)
        {
            _context.UserAddresses.Add(item);
            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            }
        }

        public void DeleteUserAddress(UserAddress item)
        {
            var data = GetUserAddressById(item.ID);
            if (data != null)
            {
                _context.UserAddresses.Remove(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new ArgumentException(string.Format("UserAddress with ID {0} is not exist!", item.ID));
            }
        }

        public void DeleteUserAddressById(int id)
        {
            var data = GetUserAddressById(id);
            if (data != null)
            {
                DeleteUserAddress(data);
            }
            else
            {
                throw new ArgumentException(string.Format("UserAddress with ID {0} is not exist!", id));
            }
        }

        public List<UserAddress> FindAllUserAddressesByAddress(string address, int? userId = null)
        {
            return _context.UserAddresses
                .Include(p => p.User)
                .Where(p =>
                    p.Address.ToLower().Contains(address) &&
                    (userId == null ? true : p.UserID == userId.Value)
                ).ToList();
        }

        public List<UserAddress> GetUserAddresses(int? userId = null)
        {
            return _context.UserAddresses
                .Include(p => p.User)
                .Where(p => userId == null ? true : p.UserID == userId.Value)
                .ToList();
        }

        public UserAddress? GetUserAddressById(int id, int? userId = null)
        {
            return _context.UserAddresses
                .Include(p => p.User)
                .Where(
                    p => p.ID == id &&
                    (userId == null ? true : p.UserID == userId.Value)
                ).FirstOrDefault();
        }

        public void UpdateUserAddress(UserAddress item)
        {
            var data = GetUserAddressById(item.ID);
            if (data != null)
            {
                data.Name = item.Name;
                data.Address = item.Address;
                data.Phone = item.Phone;
                data.CountryCode = item.CountryCode;

                _context.UserAddresses.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new ArgumentException(string.Format("UserAddress with ID {0} is not exist!", item.ID));
            }
        }
    }
}
