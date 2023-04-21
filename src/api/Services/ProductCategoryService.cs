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
            var data = _context.ProductCategories.Where(p => p.ID == item.ID).FirstOrDefault();
            if (data != null)
            {
                data.Name = item.Name;
                data.ShowInPage = item.ShowInPage;

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

        public List<ProductCategory> FindAllProductCategoriesByName(string name, bool includeHidden)
        {
            return _context.ProductCategories.Where(p =>
                // Include hidden. This is ignored by default.
                (includeHidden ? true : p.ShowInPage == true) &&
                // Filter by name
                p.Name.ToLower().Contains(name.ToLower())
            ).ToList();
        }

        public List<ProductCategory> GetAllProductCategories(bool includeHidden)
        {
            // Include hidden. This is ignored by default.
            return _context.ProductCategories.Where(p => (includeHidden ? true : p.ShowInPage == true)).ToList();
        }

        public ProductCategory? GetProductCategoryById(int id)
        {
            return _context.ProductCategories.Where(p => p.ID == id).FirstOrDefault();
        }

        public void HideProductCategory(ProductCategory item)
        {
            var data = _context.ProductCategories.Where(p => p.ID == item.ID).FirstOrDefault();
            if (data != null)
            {
                data.ShowInPage = false;
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

        public void HideProductCategoryById(int id)
        {
            var data = _context.ProductCategories.Where(p => p.ID == id).FirstOrDefault();
            if (data != null)
            {
                HideProductCategory(data);
            }
            else
            {
                throw new Exception(string.Format("ProductCategory with ID {0} is not exist!", id));
            }
        }
    }
}
