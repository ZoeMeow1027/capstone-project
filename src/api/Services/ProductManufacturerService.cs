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

        public bool AddProductManufacturer(ProductManufacturer item)
        {
            try
            {
                _context.ProductManufacturers.Add(item);
                return _context.SaveChanges() == 1;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return false;
            }
        }

        public bool UpdateProductManufacturer(ProductManufacturer item)
        {
            try
            {
                var data = _context.ProductManufacturers.Where(p => p.ID == item.ID).FirstOrDefault();
                if (data != null)
                {
                    data.Name = item.Name;
                    data.ShowInPage = item.ShowInPage;

                    _context.ProductManufacturers.Update(data);
                    int numAffected = _context.SaveChanges();
                    return numAffected == 1;
                }
                else
                {
                    throw new Exception(string.Format("ProductManufacturer with ID {0} is not exist!", item.ID));
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return false;
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

        public bool HideProductManufacturer(ProductManufacturer item)
        {
            try
            {
                var data = _context.ProductManufacturers.Where(p => p.ID == item.ID).FirstOrDefault();
                if (data != null)
                {
                    data.ShowInPage = false;
                    _context.ProductManufacturers.Update(data);
                    int numAffected = _context.SaveChanges();
                    return numAffected == 1;
                }
                else
                {
                    throw new Exception(string.Format("ProductManufacturer with ID {0} is not exist!", item.ID));
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return false;
            }
        }

        public bool HideProductManufacturerById(int id)
        {
            var data = _context.ProductManufacturers.Where(p => p.ID == id).FirstOrDefault();
            return data == null ? false : HideProductManufacturer(data);
        }
    }
}
