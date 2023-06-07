package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.dto.RegisterDTO;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoPermissionException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;

@Repository
public class AuthRepositoryImpl extends RequestRepository implements AuthRepository {

    @Override
    public User getUserInformation(Map<String, String> header, ArrayList<Integer> allowedUserType)
            throws Exception {
        RequestResult<JsonObject> reqResult = getRequestWithResult("/api/account/my", null, header);
        
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        } else if (reqResult.getStatusCode() != 200) {
            throw new SessionExpiredException(String.format("API was returned with code %d. Maybe your session has expired?", reqResult.getStatusCode()));
        } else if (allowedUserType != null) {
            if (!allowedUserType
                    .contains(reqResult.getData().get("data").getAsJsonObject().get("usertype").getAsInt())) {
                throw new NoPermissionException("This user isn't have enough permission to do that!");
            }
        }

        var data = reqResult.getData().get("data").getAsJsonObject();
        if (data == null) {
            return null;
        } else {
            return new Gson().fromJson(
                data,
                (new TypeToken<User>() {}).getType()
            );
        }
    }

    @Override
    public RequestResult<JsonObject> login(Map<String, String> header, String username, String password)
            throws Exception {
        JsonObject auth = new JsonObject();
        auth.addProperty("username", username);
        auth.addProperty("password", password);

        header.put("content-type", "application/json; charset=UTF-8");
        RequestResult<JsonObject> reqResult = postRequestWithResult("/api/auth/login", null, header, auth.toString());

        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        } else if (reqResult.getStatusCode() != 200) {
            throw new Exception(String.format("API was returned with code %d. Maybe your information is incorrect?", reqResult.getStatusCode()));
        }

        return reqResult;
    }

    @Override
    public RequestResult<JsonObject> logout(Map<String, String> header) throws Exception {
        return postRequestWithResult("/api/account/logout", null, header, null);
    }

    @Override
    public RequestResult<JsonObject> register(Map<String, String> header, RegisterDTO registerDTO) throws Exception {
        JsonObject auth = new JsonObject();
        auth.addProperty("username", registerDTO.getUsername());
        auth.addProperty("name", registerDTO.getName());
        auth.addProperty("password", registerDTO.getPassword());
        auth.addProperty("email", registerDTO.getEmail());
        auth.addProperty("phone", registerDTO.getPhone());

        header.put("content-type", "application/json; charset=UTF-8");
        RequestResult<JsonObject> reqResult = postRequestWithResult("/api/auth/register", null, header, auth.toString());

        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        } else if (reqResult.getStatusCode() != 200) {
            throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
        }

        return reqResult;
    }

    @Override
    public byte[] getAvatar(Map<String, String> header) throws Exception {
        return getRequestToImage("/api/account/avatar", null, header);
    }

    @Override
    public RequestResult<JsonObject> setAvatar(Map<String, String> header, Resource resource) throws Exception {
        return postRequestFromImage("/api/account/avatar", null, header, resource);
    }
}
