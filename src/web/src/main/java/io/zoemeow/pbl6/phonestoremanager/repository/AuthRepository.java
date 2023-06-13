package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.Map;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.dto.RegisterDTO;

public interface AuthRepository {
    public RequestResult<JsonObject> login(Map<String, String> header, String username, String password) throws Exception;

    public RequestResult<JsonObject> register(Map<String, String> header, RegisterDTO registerDTO) throws Exception;

    public RequestResult<JsonObject> logout(Map<String, String> header) throws Exception;
}
