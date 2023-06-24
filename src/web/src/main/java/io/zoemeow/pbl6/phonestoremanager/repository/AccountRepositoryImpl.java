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

import io.zoemeow.pbl6.phonestoremanager.model.bean.BillSummary;
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
        RequestResult<JsonObject> reqResult = getRequestWithResult("/account/address", null, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/account/address",
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
    public UserAddress getAddressById(Map<String, String> header, Integer id) throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("id", Integer.toString(id));

        RequestResult<JsonObject> reqResult = getRequestWithResult("/account/address", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/account/address",
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
        return getRequestToImage("/account/avatar", null, header);
    }

    @Override
    public RequestResult<JsonObject> setAvatar(Map<String, String> header, Resource resource) throws Exception {
        return postRequestFromImage("/account/avatar", null, header, resource, null);
    }

    @Override
    public User getUserInformation(Map<String, String> header, ArrayList<Integer> allowedUserType)
            throws Exception {
        RequestResult<JsonObject> reqResult = getRequestWithResult("/account/my", null, header);
        
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
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("name", userAddress.getName());
        productAdd.addProperty("phone", userAddress.getPhone());
        productAdd.addProperty("address", userAddress.getAddress());
        productAdd.addProperty("countrycode", userAddress.getCountryCode());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "add");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/account/address", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> updateAddress(Map<String, String> header, UserAddress userAddress) {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("id", userAddress.getId());
        productAdd.addProperty("name", userAddress.getName());
        productAdd.addProperty("phone", userAddress.getPhone());
        productAdd.addProperty("address", userAddress.getAddress());
        productAdd.addProperty("countrycode", userAddress.getCountryCode());
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "update");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/ess", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> deleteAddress(Map<String, String> header, UserAddress userAddress) {
        return deleteAddress(header, userAddress.getId());
    }

    @Override
    public RequestResult<JsonObject> deleteAddress(Map<String, String> header, Integer id) {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("id", id);
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "delete");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/account/address", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> setUserInformation(Map<String, String> header, User user) throws Exception {
        String postData = new Gson().toJson(user);
        return postRequestWithResult("/account/my", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> changePassword(Map<String, String> header, ChangePasswordDTO changePassDTO)
            throws Exception {
        String postData = new Gson().toJson(changePassDTO);
        return postRequestWithResult("/account/change-password", null, header, postData);
    }

    @Override
    public List<BillSummary> getActiveBillSummaries(Map<String, String> header) throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("activeonly", "true");

        RequestResult<JsonObject> reqResult = getRequestWithResult("/account/delivery", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/account/delivery",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<BillSummary>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<BillSummary>>() {}).getType()
        );
    }

    @Override
    public List<BillSummary> getBillSummaries(Map<String, String> header) throws NoInternetException, RequestException {
        RequestResult<JsonObject> reqResult = getRequestWithResult("/account/delivery", null , header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/account/delivery",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<BillSummary>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<BillSummary>>() {}).getType()
        );
    }

    @Override
    public BillSummary getBillSummaryById(Map<String, String> header, Integer id) throws NoInternetException, RequestException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("id", Integer.toString(id));

        RequestResult<JsonObject> reqResult = getRequestWithResult("/account/delivery", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/account/delivery",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonObject();
        if (data == null)
            return new BillSummary();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonObject(),
            (new TypeToken<BillSummary>() {}).getType()
        );
    }

    @Override
    public RequestResult<JsonObject> cancelOrder(Map<String, String> header, Integer orderid) throws Exception {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("orderid", orderid);
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "cancel");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/account/delivery", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> markOrderPaid(Map<String, String> header, Integer orderid, Integer paymentMethod, String paymentId) throws Exception {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("orderid", orderid);
        productAdd.addProperty("transactionid", paymentId);
        productAdd.addProperty("paymentmethod", paymentMethod);
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "paid");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/account/delivery", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> addReview(Map<String, String> header, Integer productId, Integer rating,
            String comment) throws Exception {
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("productid", productId);
        bodyRoot.addProperty("rating", rating);
        bodyRoot.addProperty("comment", comment);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/account/review", null, header, postData);
    }
}
