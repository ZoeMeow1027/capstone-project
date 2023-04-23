using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IProductCategoryService
    {
        List<ProductCategory> GetAllProductCategories();

        List<ProductCategory> FindAllProductCategoriesByName(string name);

        ProductCategory? GetProductCategoryById(int id);

        void AddProductCategory(ProductCategory item);

        void UpdateProductCategory(ProductCategory item);

        void DeleteProductCategoryById(int id);

        void DeleteProductCategory(ProductCategory item);
    }
}
