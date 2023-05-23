package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.UserAddDTO;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.UserResetPassDTO;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.UserToggleEnableDTO;

public class AdminUserRepository extends RequestRepository {
    public RequestResult<JsonObject> getAllUsers(Map<String, String> header, Boolean includeHidden) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "user");
        parameters.put("includedisabled", includeHidden.toString());
        return getRequest("https://localhost:7053/api/users", parameters, header);
    }

    public RequestResult<JsonObject> getUser(Map<String, String> header, Integer id) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "user");
        parameters.put("includedisabled", "true");
        parameters.put("id", id.toString());
        return getRequest("https://localhost:7053/api/users", parameters, header);
    }

    public RequestResult<JsonObject> toggleUser(Map<String, String> header, UserToggleEnableDTO userToggleDTO) throws JsonProcessingException {
        header.put("content-type", "application/json; charset=UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userToggle = mapper.createObjectNode();
        userToggle.put("id", userToggleDTO.getId());
        ObjectNode bodyRoot = mapper.createObjectNode();
        bodyRoot.put("type", "user");
        bodyRoot.put("action", userToggleDTO.getEnabled().compareTo("1") == 0 ? "enable" : "disable");
        bodyRoot.set("data", userToggle);

        String postData = mapper.writeValueAsString(bodyRoot);
        return postRequest("https://localhost:7053/api/users", null, header, postData);
    }

    public RequestResult<JsonObject> resetPassword(Map<String, String> header, UserResetPassDTO userResetPassDTO) throws JsonProcessingException {
        header.put("content-type", "application/json; charset=UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userResetPass = mapper.createObjectNode();
        userResetPass.put("id", userResetPassDTO.getId());
        userResetPass.put("password", userResetPassDTO.getPassword());
        ObjectNode bodyRoot = mapper.createObjectNode();
        bodyRoot.put("type", "user");
        bodyRoot.put("action", "changepassword");
        bodyRoot.set("data", userResetPass);

        String postData = mapper.writeValueAsString(bodyRoot);
        return postRequest("https://localhost:7053/api/users", null, header, postData);
    }

    public RequestResult<JsonObject> addUser(Map<String, String> header, UserAddDTO userAddDTO)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userAdd = mapper.createObjectNode();
        userAdd.put("username", userAddDTO.getUsername());
        userAdd.put("password", userAddDTO.getPassword());
        userAdd.put("name", userAddDTO.getName());
        if (userAddDTO.getEmail() != null)
            userAdd.put("email", userAddDTO.getEmail());
        if (userAddDTO.getPhone() != null)
            userAdd.put("phone", userAddDTO.getPhone());
        userAdd.put("usertype", userAddDTO.getUsertype());
        ObjectNode bodyRoot = mapper.createObjectNode();
        bodyRoot.put("type", "user");
        bodyRoot.put("action", "add");
        bodyRoot.set("data", userAdd);

        String postData = mapper.writeValueAsString(bodyRoot);
        return postRequest("https://localhost:7053/api/users", null, header,
                postData);
    }

    public RequestResult<JsonObject> editUser(Map<String, String> header, UserAddDTO userEditDTO) throws JsonProcessingException {
        header.put("content-type", "application/json; charset=UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userAdd = mapper.createObjectNode();
        userAdd.put("id", userEditDTO.getId());
        userAdd.put("username", userEditDTO.getUsername());
        userAdd.put("name", userEditDTO.getName());
        if (userEditDTO.getEmail() != null)
            userAdd.put("email", userEditDTO.getEmail());
        if (userEditDTO.getPhone() != null)
            userAdd.put("phone", userEditDTO.getPhone());
        userAdd.put("usertype", userEditDTO.getUsertype());
        ObjectNode bodyRoot = mapper.createObjectNode();
        bodyRoot.put("type", "user");
        bodyRoot.put("action", "update");
        bodyRoot.set("data", userAdd);

        String postData = mapper.writeValueAsString(bodyRoot);
        return postRequest("https://localhost:7053/api/users", null, header, postData);
    }
}
