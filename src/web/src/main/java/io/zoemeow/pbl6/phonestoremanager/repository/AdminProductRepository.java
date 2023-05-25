package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;

public class AdminProductRepository extends RequestRepository {
    public RequestResult<JsonObject> getProducts(Map<String, String> header, String query, Boolean includehidden) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "product");
        parameters.put("includehidden", includehidden.toString());
        if (query != null) {
            parameters.put("query", query);
        }
        return getRequest("https://localhost:7053/api/products", parameters, header);
    }

    public RequestResult<JsonObject> getProductCategories(Map<String, String> header, String query, Boolean includehidden) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "category");
        parameters.put("includehidden", includehidden.toString());
        if (query != null) {
            parameters.put("query", query);
        }
        return getRequest("https://localhost:7053/api/products", parameters, header);
    }

    public RequestResult<JsonObject> getProductManufacturers(Map<String, String> header, String query, Boolean includehidden) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "manufacturer");
        parameters.put("includehidden", includehidden.toString());
        if (query != null) {
            parameters.put("query", query);
        }
        return getRequest("https://localhost:7053/api/products", parameters, header);
    }
}
