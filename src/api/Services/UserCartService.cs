using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;
using System.Diagnostics;

namespace PhoneStoreManager.Services
{
    public class UserCartService : IUserCartService
    {
        private readonly DataContext _context;

        public UserCartService(DataContext context)
        {
            _context = context;
        }

        public void AddItem(int userid, int productid, int count)
        {
            var product = _context.Products.Where(p => p.ID == productid && p.ShowInPage == true).FirstOrDefault();
            if (product == null)
            {
                throw new ArgumentException(string.Format("Product with ID {0} is not exist!", productid));
            }

            var item = _context.UserCarts.Where(p => p.UserID == userid && p.ProductID == product.ID).FirstOrDefault();
            if (item != null)
            {
                item.Count += count;
                _context.UserCarts.Update(item);
            }
            else if (product.InventoryCount < count)
            {
                throw new ArgumentException(string.Format("Product with ID {0} has not enough for {1}!", productid, count));
            }
            else
            {
                item = new UserCart();
                item.UserID = userid;
                item.ProductID = product.ID;
                item.Count = count;
                _context.UserCarts.Add(item);
            }

            var _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("We ran into a problem while adding item for you!"));
            }
            ClearItemZeroCount();
        }

        public void AddItem(User user, int productid, int count)
        {
            AddItem(user.ID, productid, count);
        }

        public void AddItem(int userid, Product product, int count)
        {
            AddItem(userid, product.ID, count);
        }

        public void AddItem(User user, Product product, int count)
        {
            AddItem(user.ID, product.ID, count);
        }

        public List<UserCart> GetAllItems(int userid)
        {
            return _context.UserCarts
                .Include(p => p.Product)
                .Include(p => p.Product.Images)
                .Include(p => p.Product.Category)
                .Include(p => p.Product.Manufacturer)
                .Where(p => p.UserID == userid)
                .ToList();
        }

        public List<UserCart> GetAllItems(User user)
        {
            return GetAllItems(user.ID);
        }

        public void RemoveItem(int userId, int cartId)
        {
            var dataToDel = _context.UserCarts
                .Where(p => p.UserID == userId && p.ID == p.ID)
                .FirstOrDefault();

            if (dataToDel == null)
            {
                throw new ArgumentException(string.Format("Cart item with ID {0} is not exist or you don't have permission with this cart!", cartId));
            }

            _context.UserCarts.Remove(dataToDel);
            var _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("We ran into a problem while removing item for you!"));
            }
            ClearItemZeroCount();
        }

        public void RemoveItem(User user, int cartId)
        {
            RemoveItem(user.ID, cartId);
        }

        public void ClearItemZeroCount()
        {
            var dataToDel = _context.UserCarts.Where(p => p.Count <= 0).ToList();
            try
            {
                if (dataToDel.Count < 0)
                    return;

                _context.UserCarts.RemoveRange(dataToDel);
                var _affectedRows = _context.SaveChanges();

                if (dataToDel.Count != _affectedRows)
                {
                    throw new Exception(string.Format("Data want to remove: {0} is different from affected rows: {1}", dataToDel.Count, _affectedRows));
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine(string.Format("Some items were not removed from UserCart table. You can check them in Administrator site.\n\nMessage: {0}", ex.Message));
            }
        }

        public void ClearCart(int userId)
        {
            var data = _context.UserCarts
                .Where(p => p.UserID == userId)
                .ToList();
            _context.UserCarts.RemoveRange(data);
            var _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("We ran into a problem while removing item for you!"));
            }
            ClearItemZeroCount();
        }

        public void ClearCart(User user)
        {
            ClearCart(user.ID);
        }

        public void UpdateItem(int userid, int id, int count)
        {
            var item = _context.UserCarts
                .Include(p => p.Product)
                .Where(p => p.UserID == userid && p.ID == id)
                .FirstOrDefault();
            if (item == null)
            {
                throw new ArgumentException(string.Format("Cart item with ID {0} is not exist or you don't have permission with this cart!", id));
            }
            if (count <= 0)
            {
                throw new ArgumentException("Invalid 'count' value!");
            }
            else if (item.Product.InventoryCount < count)
            {
                throw new ArgumentException(string.Format("Product with ID {0} has not enough for {1}!", item.ProductID, count));
            }

            item.Count = count;
            _context.UserCarts.Update(item);

            var _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("We ran into a problem while updating item for you!"));
            }
            ClearItemZeroCount();
        }

        public void UpdateItem(User user, int id, int count)
        {
            UpdateItem(user.ID, id, count);
        }
    }
}
