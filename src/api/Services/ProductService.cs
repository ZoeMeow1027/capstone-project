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
            return _context.Products.Include(p => p.Category).Include(p => p.Manufacturer).Include(p => p.Warranties).Where(p =>
                // Include hidden. This is ignored by default.
                (includeHidden ? true : p.ShowInPage == true) &&
                // Filter by name
                p.Name.ToLower().Contains(name.ToLower())
            ).ToList();
        }

        public List<Product> GetAllProducts(bool includeHidden)
        {
            return _context.Products.Include(p => p.Category).Include(p => p.Manufacturer).Include(p => p.Warranties).Where(p => (includeHidden ? true : p.ShowInPage == true)).ToList();
        }

        public Product? GetProductById(int id)
        {
            return _context.Products.Include(p => p.Category).Include(p => p.Manufacturer).Include(p => p.Warranties).Where(p => p.ID == id).FirstOrDefault();
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
            var data = GetProductById(item.ID);
            if (data != null)
            {
                data.Name = item.Name;
                data.ShowInPage = item.ShowInPage;

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
