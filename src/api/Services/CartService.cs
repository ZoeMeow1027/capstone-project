using PhoneStoreManager.Model;
using System.Diagnostics;

namespace PhoneStoreManager.Services
{
    public class CartService : ICartService
    {
        private readonly DataContext _context;

        public CartService(DataContext context)
        {
            _context = context;
        }

        public void AddItem(int userid, int productid, int count)
        {
            var item = _context.UserCarts.Where(p => p.UserID == userid && p.ProductID == p.ProductID).FirstOrDefault();
            if (item != null)
            {
                item.Count += count;
                _context.Update(item);
            }
            else
            {
                item = new UserCart();
                item.UserID = userid;
                item.ProductID = productid;
                _context.Add(item);
            }

            var _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("We ran a problem while adding item for you!"));
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
            return _context.UserCarts.Where(p => p.UserID == userid).ToList();
        }

        public List<UserCart> GetAllItems(User user)
        {
            return GetAllItems(user.ID);
        }

        public void RemoveItem(int userid, int productid)
        {
            var dataToDel = _context.UserCarts.Where(p => p.UserID == userid && p.ProductID == productid).FirstOrDefault();
            if (dataToDel != null)
            {
                _context.UserCarts.Remove(dataToDel);
                var _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("We ran a problem while removing item for you!"));
                }
            }
        }

        public void RemoveItem(User user, int productid)
        {
            RemoveItem(user.ID, productid);
        }

        public void RemoveItem(int userid, Product product)
        {
            RemoveItem(userid, product.ID);
        }

        public void RemoveItem(User user, Product product)
        {
            RemoveItem(user.ID, product.ID);
        }

        public void SubtractItemCount(int userid, int productid, int count)
        {
            AddItem(userid, productid, -count);
        }

        public void SubtractItemCount(User user, int productid, int count)
        {
            SubtractItemCount(user.ID, productid, count);
        }

        public void SubtractItemCount(int userid, Product product, int count)
        {
            SubtractItemCount(userid, product.ID, count);
        }

        public void SubtractItemCount(User user, Product product, int count)
        {
            SubtractItemCount(user.ID, product.ID, count);
        }

        public void ClearItemZeroCount()
        {
            var dataToDel = _context.UserCarts.Where(p => p.Count <= 0).ToList();
            try
            {
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
    }
}
