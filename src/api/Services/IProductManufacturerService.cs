using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IProductManufacturerService
    {
        List<ProductManufacturer> GetAllProductManufacturers(bool includeHidden);

        List<ProductManufacturer> FindAllProductManufacturersByName(string name, bool includeHidden);

        ProductManufacturer? GetProductManufacturerById(int id);

        bool AddProductManufacturer(ProductManufacturer item);

        bool UpdateProductManufacturer(ProductManufacturer item);

        bool HideProductManufacturerById(int id);

        bool HideProductManufacturer(ProductManufacturer item);
    }
}
