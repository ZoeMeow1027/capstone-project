﻿using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IProductImageMetadataService
    {
        List<ProductImageMetadata> GetAll();

        ProductImageMetadata? Get(int id);

        ProductImageMetadata? Get(string name);

        void Add(ProductImageMetadata item);

        void Update(ProductImageMetadata item);

        void Delete(ProductImageMetadata item);
    }
}
