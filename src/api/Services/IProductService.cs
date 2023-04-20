using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IProductService
    {
        List<Product> GetAllProducts();

        List<Product> FindAllProductsByName(string name);

        Product GetProductById(int id);

        bool AddProduct(Product item);

        bool EditProduct(Product item);

        bool HideProductById(int id);

        bool HideProduct(Product item);
    }
}
