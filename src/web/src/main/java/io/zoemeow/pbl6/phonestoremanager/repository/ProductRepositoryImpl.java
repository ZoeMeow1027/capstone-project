package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.zoemeow.pbl6.phonestoremanager.model.bean.Product;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductCategory;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductManufacturer;
import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.RequestException;

@Repository
public class ProductRepositoryImpl extends RequestRepository implements ProductRepository {

    @Override
    public List<Product> getProducts(Map<String, String> header, String query, Boolean includehidden)
            throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "product");
        parameters.put("includehidden", includehidden.toString());
        if (query != null) {
            parameters.put("query", query);
        }
        RequestResult<JsonObject> reqResult = getRequestWithResult("/products", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/products",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<Product>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<Product>>() {}).getType()
        );
    }

    @Override
    public List<ProductCategory> getProductCategories(Map<String, String> header, String query, Boolean includehidden)
            throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "category");
        parameters.put("includehidden", includehidden.toString());
        if (query != null) {
            parameters.put("query", query);
        }
        RequestResult<JsonObject> reqResult = getRequestWithResult("/products", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/products",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<ProductCategory>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<ProductCategory>>() {}).getType()
        );
    }

    @Override
    public List<ProductManufacturer> getProductManufacturers(Map<String, String> header, String query,
            Boolean includehidden) throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "manufacturer");
        parameters.put("includehidden", includehidden.toString());
        if (query != null) {
            parameters.put("query", query);
        }
        RequestResult<JsonObject> reqResult = getRequestWithResult("/products", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/products",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<ProductManufacturer>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<ProductManufacturer>>() {}).getType()
        );
    }

    @Override
    public Product getProductById(Map<String, String> header, Integer id) throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "product");
        parameters.put("includehidden", "true");
        parameters.put("id", id.toString());

        RequestResult<JsonObject> reqResult = getRequestWithResult("/products", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/products",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonObject();
        if (data == null)
            return null;
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonObject(),
            (new TypeToken<Product>() {}).getType()
        );
    }

    @Override
    public RequestResult<JsonObject> addProduct(Map<String, String> header, Product product) {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("name", product.getName());
        productAdd.addProperty("categoryid", product.getCategoryId());
        productAdd.addProperty("manufacturerid", product.getManufacturerId());
        productAdd.addProperty("inventorycount", product.getInventoryCount());
        productAdd.addProperty("unit", product.getUnit());
        productAdd.addProperty("article", product.getArticle());
        productAdd.addProperty("specification", product.getSpecification());
        productAdd.addProperty("warrantymonth", product.getWarrantyMonth());
        productAdd.addProperty("price", product.getPrice());
        productAdd.addProperty("showinpage", product.getShowInPage());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "product");
        bodyRoot.addProperty("action", "add");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/products/", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> editProduct(Map<String, String> header, Product product) {
        JsonObject productUpdate = new JsonObject();
        productUpdate.addProperty("id", product.getId());
        productUpdate.addProperty("name", product.getName());
        productUpdate.addProperty("categoryid", product.getCategoryId());
        productUpdate.addProperty("manufacturerid", product.getManufacturerId());
        productUpdate.addProperty("inventorycount", product.getInventoryCount());
        productUpdate.addProperty("unit", product.getUnit());
        productUpdate.addProperty("article", product.getArticle());
        productUpdate.addProperty("specification", product.getSpecification());
        productUpdate.addProperty("warrantymonth", product.getWarrantyMonth());
        productUpdate.addProperty("price", product.getPrice());
        productUpdate.addProperty("showinpage", product.getShowInPage());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "product");
        bodyRoot.addProperty("action", "update");
        bodyRoot.add("data", productUpdate);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/products/", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> addProductCategory(Map<String, String> header, ProductCategory productCategory) {
        JsonObject productUpdate = new JsonObject();
        productUpdate.addProperty("name", productCategory.getName());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "category");
        bodyRoot.addProperty("action", "add");
        bodyRoot.add("data", productUpdate);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/products/", null, header, postData);
    }

    @Override
    public ProductCategory getProductCategoryById(Map<String, String> header, Integer id)
            throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "category");
        parameters.put("id", id.toString());

        RequestResult<JsonObject> reqResult = getRequestWithResult("/products", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/products",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonObject();
        if (data == null)
            return null;
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonObject(),
            (new TypeToken<ProductCategory>() {}).getType()
        );
    }

    @Override
    public RequestResult<JsonObject> updateProductCategory(Map<String, String> header,
            ProductCategory productCategory) {
        JsonObject productUpdate = new JsonObject();
        productUpdate.addProperty("id", productCategory.getId());
        productUpdate.addProperty("name", productCategory.getName());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "category");
        bodyRoot.addProperty("action", "update");
        bodyRoot.add("data", productUpdate);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/products/", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> addProductManufacturer(Map<String, String> header,
            ProductManufacturer productManufacturer) {
        JsonObject productUpdate = new JsonObject();
        productUpdate.addProperty("name", productManufacturer.getName());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "manufacturer");
        bodyRoot.addProperty("action", "add");
        bodyRoot.add("data", productUpdate);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/products/", null, header, postData);
    }

    @Override
    public ProductManufacturer getProductManufacturerById(Map<String, String> header, Integer id)
            throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "manufacturer");
        parameters.put("id", id.toString());

        RequestResult<JsonObject> reqResult = getRequestWithResult("/products", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/products",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonObject();
        if (data == null)
            return null;
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonObject(),
            (new TypeToken<ProductManufacturer>() {}).getType()
        );
    }

    @Override
    public RequestResult<JsonObject> updateProductManufacturer(Map<String, String> header,
            ProductManufacturer productManufacturer) {
        JsonObject productUpdate = new JsonObject();
        productUpdate.addProperty("id", productManufacturer.getId());
        productUpdate.addProperty("name", productManufacturer.getName());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "manufacturer");
        bodyRoot.addProperty("action", "update");
        bodyRoot.add("data", productUpdate);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/products/", null, header, postData);
    }

    @Override
    public byte[] getProductImage(Map<String, String> header, Integer id) throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("id", id.toString());
        return getRequestToImage("/products/img/blob", parameters, header);
    }

    @Override
    public RequestResult<JsonObject> uploadImage(Map<String, String> header, Integer productId, Resource resource) throws Exception {
        Map<String, String> body = new HashMap<String, String>();
        body.put("productid", productId.toString());
        return postRequestFromImage("/products/img/upload", null, header, resource, body);
    }

    @Override
    public RequestResult<JsonObject> deleteProductImage(Map<String, String> header, Integer id) throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("id", id.toString());
        return postRequestWithResult("/products/img/delete", parameters, header, null);
    }

}
