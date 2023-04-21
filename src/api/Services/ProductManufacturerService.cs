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
            var data = _context.ProductManufacturers.Where(p => p.ID == item.ID).FirstOrDefault();
            if (data != null)
            {
                data.Name = item.Name;
                data.ShowInPage = item.ShowInPage;

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
            return _context.ProductManufacturers.Where(p =>
                // Include hidden. This is ignored by default.
                (includeHidden ? true : p.ShowInPage == true) &&
                // Filter by name
                p.Name.ToLower().Contains(name.ToLower())
            ).ToList();
        }

        public List<ProductManufacturer> GetAllProductManufacturers(bool includeHidden = false)
        {
            // Include hidden. This is ignored by default.
            return _context.ProductManufacturers.Where(p => (includeHidden ? true : p.ShowInPage == true)).ToList();
        }

        public ProductManufacturer? GetProductManufacturerById(int id)
        {
            return _context.ProductManufacturers.FirstOrDefault(p => p.ID == id);
        }

        public void HideProductManufacturer(ProductManufacturer item)
        {
            var data = _context.ProductManufacturers.Where(p => p.ID == item.ID).FirstOrDefault();
            if (data != null)
            {
                data.ShowInPage = false;
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

        public void HideProductManufacturerById(int id)
        {
            var data = _context.ProductManufacturers.Where(p => p.ID == id).FirstOrDefault();
            if (data != null)
            {
                HideProductManufacturer(data);
            }
            else
            {
                throw new Exception(string.Format("ProductManufacturer with ID {0} is not exist!", id));
            }
        }
    }
}
