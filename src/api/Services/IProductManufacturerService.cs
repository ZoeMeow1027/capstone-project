using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IProductManufacturerService
    {
        List<ProductManufacturer> GetAllProductManufacturers(bool includeHidden);

        List<ProductManufacturer> FindAllProductManufacturersByName(string name, bool includeHidden);

        ProductManufacturer? GetProductManufacturerById(int id);

        void AddProductManufacturer(ProductManufacturer item);

        void UpdateProductManufacturer(ProductManufacturer item);

        void DeleteProductManufacturerById(int id);

        void DeleteProductManufacturer(ProductManufacturer item);
    }
}
