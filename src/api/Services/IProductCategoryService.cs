using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IProductCategoryService
    {
        List<ProductCategory> GetAllProductCategories();

        List<ProductCategory> FindAllProductCategoriesByName(string name);

        ProductCategory GetProductCategoryById(int id);

        bool AddProductCategory(ProductCategory item);

        bool EditProductCategory(ProductCategory item);

        bool HideProductCategoryById(int id);

        bool HideProductCategory(ProductCategory item);
    }
}
