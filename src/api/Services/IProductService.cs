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

        void AddComment(int userId, int productId, int rating, string comment);

        void AddComment(User user, int productId, int rating, string comment);
    }
}
