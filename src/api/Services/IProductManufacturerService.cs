using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IProductManufacturerService
    {
        List<ProductManufacturer> GetAllProductManufacturers();

        List<ProductManufacturer> FindAllProductManufacturersByName(string name);

        ProductManufacturer GetProductManufacturerById(int id);

        bool AddProductManufacturer(ProductManufacturer item);

        bool EditProductManufacturer(ProductManufacturer item);

        bool HideProductManufacturerById(int id);

        bool HideProductManufacturer(ProductManufacturer item);
    }
}
