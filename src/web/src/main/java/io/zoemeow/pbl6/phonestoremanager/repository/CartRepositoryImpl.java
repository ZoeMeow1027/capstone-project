package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserCart;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.RequestException;

@Repository
public class CartRepositoryImpl extends RequestRepository implements CartRepository {

    @Override
    public List<UserCart> getAllItemsInCart(Map<String, String> header, String query, Boolean includehidden)
            throws Exception {
        RequestResult<JsonObject> reqResult = getRequestWithResult("/api/cart", null, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/api/cart",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<UserCart>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<UserCart>>() {}).getType()
        );
    }

    @Override
    public RequestResult<JsonObject> addItem(Map<String, String> header, Integer productId, Integer count)
            throws Exception {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("productid", productId);
        productAdd.addProperty("count", count);
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "add");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/api/cart", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> removeItem(Map<String, String> header, Integer cartId)
            throws Exception {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("id", cartId);
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "remove");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/api/cart", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> removeAllItems(Map<String, String> header) throws Exception {
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "removeall");

        String postData = bodyRoot.toString();
        return postRequestWithResult("/api/cart", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> updateItem(Map<String, String> header, Integer cartId, Integer count)
            throws Exception {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("id", cartId);
        productAdd.addProperty("count", count);
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "update");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/api/cart", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> checkout(Map<String, String> header, Integer userAddressId, String userMessage) {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("addressid", userAddressId);
        productAdd.addProperty("message", userMessage);
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "checkout");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/api/cart", null, header, postData);
    }
}
