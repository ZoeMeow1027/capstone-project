package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.dto.AdminUserResetPassDTO;
import io.zoemeow.pbl6.phonestoremanager.model.dto.AdminUserToggleDTO;

public interface UserRepository {
    public List<User> getAllUsers(Map<String, String> header, String query, Boolean includeHidden) throws Exception;

    public User getUser(Map<String, String> header, Integer id) throws Exception;

    public RequestResult<JsonObject> toggleUser(Map<String, String> header, AdminUserToggleDTO userToggleDTO);

    public RequestResult<JsonObject> resetPassword(Map<String, String> header, AdminUserResetPassDTO userResetPassDTO);

    public RequestResult<JsonObject> addUser(Map<String, String> header, User user);

    public RequestResult<JsonObject> editUser(Map<String, String> header, User user);
}
