package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;

public class AuthRepository extends RequestRepository {
    public RequestResult<JsonObject> getUserInformation(Map<String, String> header, ArrayList<Integer> allowedUserType) throws Exception {
        RequestResult<JsonObject> reqResult = getRequest("/api/account/my", null, header);
        
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        } else if (reqResult.getStatusCode() != 200) {
            throw new Exception(String.format("API was returned with code %d. Maybe you wasn't logged in?", reqResult.getStatusCode()));
        } else if (allowedUserType != null) {
            if (!allowedUserType
                    .contains(reqResult.getData().get("data").getAsJsonObject().get("usertype").getAsInt())) {
                throw new Exception("This user isn't have enough permission to do that!");
            }
        }

        return reqResult;
    }
}
