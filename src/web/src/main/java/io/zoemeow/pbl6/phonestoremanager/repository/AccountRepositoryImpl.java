package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserAddress;
import io.zoemeow.pbl6.phonestoremanager.model.dto.ChangePasswordDTO;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoPermissionException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.RequestException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;

@Repository
public class AccountRepositoryImpl extends RequestRepository implements AccountRepository {

    @Override
    public List<UserAddress> getAllAddress(Map<String, String> header) throws NoInternetException, RequestException {
        RequestResult<JsonObject> reqResult = getRequestWithResult("/api/account/address", null, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/api/account/address",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<UserAddress>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<UserAddress>>() {}).getType()
        );
    }

    @Override
    public UserAddress getAddressById(Map<String, String> header, int id) throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("id", Integer.toString(id));

        RequestResult<JsonObject> reqResult = getRequestWithResult("/api/account/address", null, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/api/account/address",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonObject();
        if (data == null)
            return null;
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonObject(),
            (new TypeToken<UserAddress>() {}).getType()
        );
    }

    @Override
    public byte[] getAvatar(Map<String, String> header) throws Exception {
        return getRequestToImage("/api/account/avatar", null, header);
    }

    @Override
    public RequestResult<JsonObject> setAvatar(Map<String, String> header, Resource resource) throws Exception {
        return postRequestFromImage("/api/account/avatar", null, header, resource);
    }

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
    public RequestResult<JsonObject> addAddress(Map<String, String> header, UserAddress userAddress) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAddress'");
    }

    @Override
    public RequestResult<JsonObject> updateAddress(Map<String, String> header, UserAddress userAddress) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAddress'");
    }

    @Override
    public RequestResult<JsonObject> deleteAddress(Map<String, String> header, UserAddress userAddress) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAddress'");
    }

    @Override
    public RequestResult<JsonObject> deleteAddress(Map<String, String> header, int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAddress'");
    }

    @Override
    public RequestResult<JsonObject> setUserInformation(Map<String, String> header, User user) throws Exception {
        String postData = new Gson().toJson(user);
        return postRequestWithResult("/api/account/my", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> changePassword(Map<String, String> header, ChangePasswordDTO changePassDTO)
            throws Exception {
        String postData = new Gson().toJson(changePassDTO);
        return postRequestWithResult("/api/account/change-password", null, header, postData);
    }
}
