package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.dto.RegisterDTO;

public interface AuthRepository {
    public byte[] getAvatar(Map<String, String> header) throws Exception;

    public RequestResult<JsonObject> setAvatar(Map<String, String> header, Resource resource) throws Exception;

    public User getUserInformation(Map<String, String> header, ArrayList<Integer> allowedUserType) throws Exception;

    public RequestResult<JsonObject> login(Map<String, String> header, String username, String password) throws Exception;

    public RequestResult<JsonObject> register(Map<String, String> header, RegisterDTO registerDTO) throws Exception;

    public RequestResult<JsonObject> logout(Map<String, String> header) throws Exception;
}
