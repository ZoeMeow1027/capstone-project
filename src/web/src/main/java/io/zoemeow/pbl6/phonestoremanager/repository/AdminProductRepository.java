package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;

public class AdminProductRepository extends RequestRepository {
    public RequestResult<JsonObject> getAllProducts(Map<String, String> header, Boolean includehidden) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "product");
        parameters.put("includedisabled", includehidden.toString());
        return getRequest("https://localhost:7053/api/products", parameters, header);
    }

    public RequestResult<JsonObject> getAllProductCategories(Map<String, String> header, Boolean includehidden) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "category");
        parameters.put("includedisabled", includehidden.toString());
        return getRequest("https://localhost:7053/api/products", parameters, header);
    }

    public RequestResult<JsonObject> getAllProductManufacturers(Map<String, String> header, Boolean includehidden) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "manufacturer");
        parameters.put("includedisabled", includehidden.toString());
        return getRequest("https://localhost:7053/api/products", parameters, header);
    }
}
