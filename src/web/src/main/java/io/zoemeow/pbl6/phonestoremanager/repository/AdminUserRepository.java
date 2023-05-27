package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.zoemeow.pbl6.phonestoremanager.model.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.User;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.AdminUserResetPassDTO;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.AdminUserToggleDTO;

public class AdminUserRepository extends RequestRepository {
    public List<User> getAllUsers(Map<String, String> header, Boolean includeHidden) throws RequestException, NoInternetException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "user");
        parameters.put("includedisabled", includeHidden.toString());
        
        RequestResult<JsonObject> reqResult = getRequest("/api/users", parameters, header);
                if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/api/products",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<User>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<User>>() {}).getType()
        );
    }

    public User getUser(Map<String, String> header, Integer id) throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "user");
        parameters.put("includedisabled", "true");
        parameters.put("id", id.toString());

        RequestResult<JsonObject> reqResult = getRequest("/api/users", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/api/products",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonObject();
        if (data == null)
            return null;
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonObject(),
            (new TypeToken<User>() {}).getType()
        );
    }

    public RequestResult<JsonObject> toggleUser(Map<String, String> header, AdminUserToggleDTO userToggleDTO) {
        JsonObject userToggle = new JsonObject();
        userToggle.addProperty("id", userToggleDTO.getId());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "user");
        bodyRoot.addProperty("action", userToggleDTO.getEnabled().compareTo("1") == 0 ? "enable" : "disable");
        bodyRoot.add("data", userToggle);

        String postData = bodyRoot.toString();
        return postRequest("/api/users", null, header, postData);
    }

    public RequestResult<JsonObject> resetPassword(Map<String, String> header, AdminUserResetPassDTO userResetPassDTO) {
        JsonObject userResetPass = new JsonObject();
        userResetPass.addProperty("id", userResetPassDTO.getId());
        userResetPass.addProperty("password", userResetPassDTO.getPassword());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "user");
        bodyRoot.addProperty("action", "changepassword");
        bodyRoot.add("data", userResetPass);

        String postData = bodyRoot.toString();
        return postRequest("/api/users", null, header, postData);
    }

    public RequestResult<JsonObject> addUser(Map<String, String> header, User user) {
        JsonObject userAdd = new JsonObject();
        userAdd.addProperty("username", user.getUsername());
        userAdd.addProperty("password", user.getPassword());
        userAdd.addProperty("name", user.getName());
        if (user.getEmail() != null)
            userAdd.addProperty("email", user.getEmail());
        if (user.getPhone() != null)
            userAdd.addProperty("phone", user.getPhone());
        userAdd.addProperty("usertype", user.getUserType());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "user");
        bodyRoot.addProperty("action", "add");
        bodyRoot.add("data", userAdd);

        String postData = bodyRoot.toString();
        return postRequest("/api/users", null, header, postData);
    }

    public RequestResult<JsonObject> editUser(Map<String, String> header, User user) {
        JsonObject userAdd = new JsonObject();
        userAdd.addProperty("id", user.getId());
        userAdd.addProperty("username", user.getUsername());
        userAdd.addProperty("name", user.getName());
        if (user.getEmail() != null)
            userAdd.addProperty("email", user.getEmail());
        if (user.getPhone() != null)
            userAdd.addProperty("phone", user.getPhone());
        userAdd.addProperty("usertype", user.getUserType());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "user");
        bodyRoot.addProperty("action", "update");
        bodyRoot.add("data", userAdd);

        String postData = bodyRoot.toString();
        return postRequest("/api/users", null, header, postData);
    }
}
