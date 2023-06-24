using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class ProductService : IProductService
    {
        private readonly DataContext _context;

        public ProductService(DataContext context)
        {
            this._context = context;
        }

        public void AddComment(int userId, int productId, int rating, string comment)
        {
            var data = GetProductById(productId);
            if (rating < 1 || rating > 5)
            {
                throw new ArgumentException(string.Format("Invalid rating value! (Must between 1 - 5, you inputed {0})", rating));
            }
            if (data != null)
            {
                if (_context.Users.Where(p => p.ID == userId).FirstOrDefault() != null)
                {
                    _context.ProductComments.Add(new ProductComment()
                    {
                        UserID = userId,
                        ProductID = productId,
                        Comment = comment,
                        Rating = rating,
                        Disabled = false,
                    });
                    _context.SaveChanges();
                }
                else throw new ArgumentException(string.Format("User with ID {0} is not exist!", userId));
            }
            else throw new ArgumentException(string.Format("Product with ID {0} is not exist!", productId));
        }

        public void AddComment(User user, int productId, int rating, string comment)
        {
            AddComment(user.ID, productId, rating, comment);
        }

        public void AddProduct(Product item)
        {
            _context.Products.Add(item);
            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            }
        }

        public List<Product> FindAllProductsByName(string name, bool includeHidden)
        {
            return _context.Products
                .Include(p => p.Category)
                .Include(p => p.Manufacturer)
                .Include(p => p.Warranties)
                .Include(p => p.Images)
                .Include(p => p.Comments).ThenInclude(p => p.User)
                .Where(p =>
                    // Include hidden. This is ignored by default.
                    (includeHidden ? true : p.ShowInPage == true) &&
                    // Filter by name
                    (p.Name.ToLower().Contains(name.ToLower()) ||
                    p.Manufacturer.Name.ToLower().Contains(name.ToLower()))
                ).ToList();
        }

        public List<Product> GetAllProducts(bool includeHidden)
        {
            return _context.Products
                .Include(p => p.Category)
                .Include(p => p.Manufacturer)
                .Include(p => p.Warranties)
                .Include(p => p.Images)
                .Include(p => p.Comments).ThenInclude(p => p.User)
                .Where(p => (includeHidden ? true : p.ShowInPage == true))
                .ToList();
        }

        public Product? GetProductById(int id)
        {
            var data = _context.Products
                .Include(p => p.Category)
                .Include(p => p.Manufacturer)
                .Include(p => p.Warranties)
                .Include(p => p.Images)
                .Include(p => p.Comments).ThenInclude(p => p.User)
                .Where(p => p.ID == id)
                .FirstOrDefault();
            if (data != null)
            {
                data.Views += 1;
                _context.Products.Update(data);
                _context.SaveChanges();
            }
            return data;
        }

        public void HideProduct(Product item)
        {
            var data = GetProductById(item.ID);
            if (data != null)
            {
                data.ShowInPage = false;
                _context.Products.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("Product with ID {0} is not exist!", item.ID));
            }
        }

        public void HideProductById(int id)
        {
            var data = GetProductById(id);
            if (data != null)
            {
                HideProduct(data);
            }
            else
            {
                throw new Exception(string.Format("Product with ID {0} is not exist!", id));
            }
        }

        public void UpdateProduct(Product item)
        {
            var data = _context.Products
                .Include(p => p.Category)
                .Include(p => p.Manufacturer)
                .Include(p => p.Warranties)
                .Include(p => p.Images)
                .Include(p => p.Comments).ThenInclude(p => p.User)
                .Where(p => p.ID == item.ID)
                .FirstOrDefault();
            if (data != null)
            {
                data.Name = item.Name;
                data.ShowInPage = item.ShowInPage;
                data.Views = item.Views;

                _context.Products.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("Product with ID {0} is not exist!", item.ID));
            }
        }
    }
}
