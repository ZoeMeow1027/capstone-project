package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserAddress;

public interface AccountRepository {
    public List<UserAddress> getAllAddress(Map<String, String> header) throws Exception;
    
    public UserAddress getAddressById(Map<String, String> header,int id) throws Exception;
    
    public RequestResult<JsonObject> addAddress(Map<String, String> header,UserAddress userAddress);
    
    public RequestResult<JsonObject> updateAddress(Map<String, String> header,UserAddress userAddress);
    
    public RequestResult<JsonObject> deleteAddress(Map<String, String> header,UserAddress userAddress);

    public RequestResult<JsonObject> deleteAddress(Map<String, String> header,int id);
}
