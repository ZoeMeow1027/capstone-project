package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.dto.AdminUserResetPassDTO;
import io.zoemeow.pbl6.phonestoremanager.model.dto.AdminUserToggleDTO;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.RequestException;

@Repository
public class UserRepositoryImpl extends RequestRepository implements UserRepository {

    @Override
    public List<User> getAllUsers(Map<String, String> header, String query, Boolean includeHidden)
            throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "user");
        parameters.put("includedisabled", includeHidden == null ? "false" : includeHidden.toString());
        if (query != null) {
            parameters.put("query", query);
        }
        
        RequestResult<JsonObject> reqResult = getRequestWithResult("/users", parameters, header);
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
            return new ArrayList<User>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<User>>() {}).getType()
        );
    }

    @Override
    public User getUser(Map<String, String> header, Integer id) throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "user");
        parameters.put("includedisabled", "true");
        parameters.put("id", id.toString());

        RequestResult<JsonObject> reqResult = getRequestWithResult("/users", parameters, header);
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
            (new TypeToken<User>() {}).getType()
        );
    }

    @Override
    public RequestResult<JsonObject> toggleUser(Map<String, String> header, AdminUserToggleDTO userToggleDTO) {
        JsonObject userToggle = new JsonObject();
        userToggle.addProperty("id", userToggleDTO.getId());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "user");
        bodyRoot.addProperty("action", userToggleDTO.getEnabled().compareTo("1") == 0 ? "enable" : "disable");
        bodyRoot.add("data", userToggle);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/users", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> resetPassword(Map<String, String> header, AdminUserResetPassDTO userResetPassDTO) {
        JsonObject userResetPass = new JsonObject();
        userResetPass.addProperty("id", userResetPassDTO.getId());
        userResetPass.addProperty("password", userResetPassDTO.getPassword());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("type", "user");
        bodyRoot.addProperty("action", "changepassword");
        bodyRoot.add("data", userResetPass);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/users", null, header, postData);
    }

    @Override
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
        return postRequestWithResult("/users", null, header, postData);
    }

    @Override
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
        return postRequestWithResult("/users", null, header, postData);
    }
    
}
