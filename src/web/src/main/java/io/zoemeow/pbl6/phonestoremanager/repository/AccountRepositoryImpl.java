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
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserAddress;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.RequestException;

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
    
}
