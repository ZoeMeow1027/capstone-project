using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class ProductCategoryService : IProductCategoryService
    {
        private readonly DataContext _context;

        public ProductCategoryService(DataContext context)
        {
            this._context = context;
        }

        public void AddProductCategory(ProductCategory item)
        {
            _context.ProductCategories.Add(item);
            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            };
        }

        public void UpdateProductCategory(ProductCategory item)
        {
            var data = GetProductCategoryById(item.ID);
            if (data != null)
            {
                data.Name = item.Name;

                _context.ProductCategories.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("ProductCategory with ID {0} is not exist!", item.ID));
            }
        }

        public List<ProductCategory> FindAllProductCategoriesByName(string name)
        {
            return _context.ProductCategories.Include(p => p.Products).Where(p =>
                // Filter by name
                p.Name.ToLower().Contains(name.ToLower())
            ).ToList();
        }

        public List<ProductCategory> GetAllProductCategories()
        {
            // Include hidden. This is ignored by default.
            return _context.ProductCategories.Include(p => p.Products).ToList();
        }

        public ProductCategory? GetProductCategoryById(int id)
        {
            return _context.ProductCategories.Include(p => p.Products).Where(p => p.ID == id).FirstOrDefault();
        }

        public void DeleteProductCategory(ProductCategory item)
        {
            var data = GetProductCategoryById(item.ID);
            if (data != null)
            {
                int _productCheck = _context.Products.Where(p => p.CategoryID == item.ID).ToList().Count;
                if (_productCheck > 0)
                {
                    throw new Exception(string.Format("ProductCategory with ID {0} has been used in Products table with {1} record(s)!", item.ID, _productCheck));
                }

                _context.ProductCategories.Remove(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("ProductCategory with ID {0} is not exist!", item.ID));
            }
        }

        public void DeleteProductCategoryById(int id)
        {
            var data = GetProductCategoryById(id);
            if (data != null)
            {
                DeleteProductCategory(data);
            }
            else
            {
                throw new Exception(string.Format("ProductCategory with ID {0} is not exist!", id));
            }
        }
    }
}
