package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import io.zoemeow.pbl6.phonestoremanager.model.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.Product;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductCategory;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductManufacturer;

public interface AdminProductRepository {
    public List<Product> getProducts(Map<String, String> header, String query, Boolean includehidden) throws NoInternetException, RequestException;

    public List<ProductCategory> getProductCategories(Map<String, String> header, String query, Boolean includehidden) throws NoInternetException, RequestException;

    public List<ProductManufacturer> getProductManufacturers(Map<String, String> header, String query, Boolean includehidden) throws NoInternetException, RequestException;

    public Product getProductById(Map<String, String> header, Integer id) throws NoInternetException, RequestException;

    public RequestResult<JsonObject> addProduct(Map<String, String> header, Product product);

    public RequestResult<JsonObject> editProduct(Map<String, String> header, Product product);

    public RequestResult<JsonObject> addProductCategory(Map<String, String> header, ProductCategory productCategory);

    public ProductCategory getProductCategoryById(Map<String, String> header, Integer id) throws NoInternetException, RequestException;

    public RequestResult<JsonObject> updateProductCategory(Map<String, String> header, ProductCategory productCategory);

    public RequestResult<JsonObject> addProductManufacturer(Map<String, String> header, ProductManufacturer productManufacturer);

    public ProductManufacturer getProductManufacturerById(Map<String, String> header, Integer id) throws NoInternetException, RequestException;

    public RequestResult<JsonObject> updateProductManufacturer(Map<String, String> header, ProductManufacturer productManufacturer);
}
