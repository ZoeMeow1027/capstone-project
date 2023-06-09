﻿using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IProductCategoryService
    {
        List<ProductCategory> GetAllProductCategories(bool includeHidden);

        List<ProductCategory> FindAllProductCategoriesByName(string name, bool includeHidden);

        ProductCategory? GetProductCategoryById(int id);

        void AddProductCategory(ProductCategory item);

        void UpdateProductCategory(ProductCategory item);

        void DeleteProductCategoryById(int id);

        void DeleteProductCategory(ProductCategory item);
    }
}
