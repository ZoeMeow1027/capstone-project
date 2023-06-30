package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.Product;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductCategory;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductManufacturer;
import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import org.springframework.core.io.Resource;

public interface ProductRepository {
    public List<Product> getProducts(Map<String, String> header, String query, Boolean includehidden) throws Exception;

    public List<ProductCategory> getProductCategories(Map<String, String> header, String query, Boolean includehidden) throws Exception;

    public List<ProductManufacturer> getProductManufacturers(Map<String, String> header, String query, Boolean includehidden) throws Exception;

    public Product getProductById(Map<String, String> header, Integer id) throws Exception;

    public RequestResult<JsonObject> addProduct(Map<String, String> header, Product product);

    public RequestResult<JsonObject> editProduct(Map<String, String> header, Product product);

    public RequestResult<JsonObject> deleteProduct(Map<String, String> header, Integer productId);

    public RequestResult<JsonObject> addProductCategory(Map<String, String> header, ProductCategory productCategory);

    public ProductCategory getProductCategoryById(Map<String, String> header, Integer id) throws Exception;

    public RequestResult<JsonObject> updateProductCategory(Map<String, String> header, ProductCategory productCategory);

    public RequestResult<JsonObject> deleteProductCategory(Map<String, String> header, Integer productId);

    public RequestResult<JsonObject> addProductManufacturer(Map<String, String> header, ProductManufacturer productManufacturer);

    public ProductManufacturer getProductManufacturerById(Map<String, String> header, Integer id) throws Exception;

    public RequestResult<JsonObject> updateProductManufacturer(Map<String, String> header, ProductManufacturer productManufacturer);

    public RequestResult<JsonObject> deleteProductManufacturer(Map<String, String> header, Integer productId);

    public byte[] getProductImage(Map<String, String> header, Integer id) throws Exception;

    public RequestResult<JsonObject> uploadImage(Map<String, String> header, Integer productId, Resource resource) throws Exception;

    public RequestResult<JsonObject> deleteProductImage(Map<String, String> header, Integer id) throws Exception;
}
