package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserCart;

public interface CartRepository {
    public List<UserCart> getAllItemsInCart(Map<String, String> header, String query, Boolean includehidden) throws Exception;

    public RequestResult<JsonObject> addItem(Map<String, String> header, Integer productId, Integer count) throws Exception;

    public RequestResult<JsonObject> updateItem(Map<String, String> header, Integer cartId, Integer count) throws Exception;

    public RequestResult<JsonObject> removeItem(Map<String, String> header, Integer cartId) throws Exception;

    public RequestResult<JsonObject> removeAllItems(Map<String, String> header) throws Exception;
}
