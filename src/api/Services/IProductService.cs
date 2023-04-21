using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IProductService
    {
        List<Product> GetAllProducts(bool includeHidden);

        List<Product> FindAllProductsByName(string name, bool includeHidden);

        Product? GetProductById(int id);

        void AddProduct(Product item);

        void UpdateProduct(Product item);

        void HideProductById(int id);

        void HideProduct(Product item);
    }
}
