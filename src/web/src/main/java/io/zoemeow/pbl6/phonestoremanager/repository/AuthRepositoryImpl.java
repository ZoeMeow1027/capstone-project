package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;
import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.dto.RegisterDTO;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;

@Repository
public class AuthRepositoryImpl extends RequestRepository implements AuthRepository {
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
}
