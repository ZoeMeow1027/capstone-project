package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserAddress;
import io.zoemeow.pbl6.phonestoremanager.model.dto.ChangePasswordDTO;

public interface AccountRepository {
    public User getUserInformation(Map<String, String> header, ArrayList<Integer> allowedUserType) throws Exception;

    public RequestResult<JsonObject> setUserInformation(Map<String, String> header, User user) throws Exception;

    public byte[] getAvatar(Map<String, String> header) throws Exception;

    public RequestResult<JsonObject> setAvatar(Map<String, String> header, Resource resource) throws Exception;

    public RequestResult<JsonObject> changePassword(Map<String, String> header, ChangePasswordDTO changePassDTO) throws Exception;

    public List<UserAddress> getAllAddress(Map<String, String> header) throws Exception;

    public UserAddress getAddressById(Map<String, String> header, int id) throws Exception;

    public RequestResult<JsonObject> addAddress(Map<String, String> header, UserAddress userAddress);

    public RequestResult<JsonObject> updateAddress(Map<String, String> header, UserAddress userAddress);

    public RequestResult<JsonObject> deleteAddress(Map<String, String> header, UserAddress userAddress);

    public RequestResult<JsonObject> deleteAddress(Map<String, String> header, int id);
}
