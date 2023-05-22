using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class ProductManufacturerService : IProductManufacturerService
    {
        private readonly DataContext _context;

        public ProductManufacturerService(DataContext context)
        {
            this._context = context;
        }

        public void AddProductManufacturer(ProductManufacturer item)
        {
            _context.ProductManufacturers.Add(item);
            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            }
        }

        public void UpdateProductManufacturer(ProductManufacturer item)
        {
            var data = GetProductManufacturerById(item.ID);
            if (data != null)
            {
                data.Name = item.Name;

                _context.ProductManufacturers.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("ProductManufacturer with ID {0} is not exist!", item.ID));
            }
        }

        public List<ProductManufacturer> FindAllProductManufacturersByName(string name, bool includeHidden = false)
        {
            return _context.ProductManufacturers.Include(p => p.Products).Where(p =>
                // Filter by name
                p.Name.ToLower().Contains(name.ToLower()) && (includeHidden ? true : p.ShowInPage)
            ).ToList();
        }

        public List<ProductManufacturer> GetAllProductManufacturers(bool includeHidden = false)
        {
            // Include hidden. This is ignored by default.
            return _context.ProductManufacturers.Include(p => p.Products).Where(p => (includeHidden ? true : p.ShowInPage)).ToList();
        }

        public ProductManufacturer? GetProductManufacturerById(int id)
        {
            return _context.ProductManufacturers.Include(p => p.Products).FirstOrDefault(p => p.ID == id);
        }

        public void DeleteProductManufacturer(ProductManufacturer item)
        {
            var data = GetProductManufacturerById(item.ID);
            if (data != null)
            {
                int _productCheck = _context.Products.Where(p => p.ManufacturerID == item.ID).ToList().Count;
                if (_productCheck > 0)
                {
                    throw new Exception(string.Format("ProductManufacturer with ID {0} has been used in Products table with {1} record(s)!", item.ID, _productCheck));
                }

                _context.ProductManufacturers.Remove(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("ProductManufacturer with ID {0} is not exist!", item.ID));
            }
        }

        public void DeleteProductManufacturerById(int id)
        {
            var data = GetProductManufacturerById(id);
            if (data != null)
            {
                DeleteProductManufacturer(data);
            }
            else
            {
                throw new Exception(string.Format("ProductManufacturer with ID {0} is not exist!", id));
            }
        }
    }
}
