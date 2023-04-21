using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class UserService : IUserService
    {
        private readonly DataContext _context;

        public UserService(DataContext context)
        {
            _context = context;
        }

        public void AddUser(User item)
        {
            _context.Users.Add(item);
            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            }
        }

        public void ChangeUserType(User item, UserType type)
        {
            var data = _context.Users.Where(p => p.ID == item.ID).FirstOrDefault();
            if (data != null)
            {
                data.UserType = type;

                _context.Users.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }

            }
            else
            {
                throw new Exception(string.Format("User with ID {0} is not exist!", item.ID));
            }
        }

        public List<User> FindAllUsersByUsernameAndName(string name, bool includeDisabled)
        {
            return _context.Users.Where(p =>
                // Include hidden. This is ignored by default.
                (includeDisabled ? true : p.IsEnabled == true) &&
                // Filter by name and username
                (p.Name.ToLower().Contains(name.ToLower()) || p.Username.ToLower().Contains(name.ToLower()))
            ).ToList();
        }

        public List<User> GetAllUsers(bool includeDisabled = false)
        {
            return _context.Users.Where(p => includeDisabled ? true : p.IsEnabled == true).ToList();
        }

        public User? GetUserById(int id)
        {
            return _context.Users.Where(p => p.ID == id).FirstOrDefault();
        }

        public void EnableUser(User item)
        {
            var data = _context.Users.Where(p => p.ID == item.ID).FirstOrDefault();
            if (data != null)
            {
                data.IsEnabled = true;
                _context.Users.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("User with ID {0} is not exist!", item.ID));
            }
        }

        public void DisableUser(User item)
        {
            var data = _context.Users.Where(p => p.ID == item.ID).FirstOrDefault();
            if (data != null)
            {
                data.IsEnabled = false;
                _context.Users.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("User with ID {0} is not exist!", item.ID));
            }
        }

        public void UpdateUser(User item)
        {
            var data = _context.Users.Where(p => p.ID == item.ID).FirstOrDefault();
            if (data != null)
            {
                data.Username = item.Username;
                data.Password = item.Password;
                data.Name = item.Name;
                data.Email = item.Email;
                data.Phone = item.Phone;
                data.IsEnabled = item.IsEnabled;
                data.UserType = item.UserType;

                _context.Users.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("User with ID {0} is not exist!", item.ID));
            }
        }
    }
}
