package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.zoemeow.pbl6.phonestoremanager.model.bean.Product;
import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.RequestException;

@Repository
public class FeaturedRepositoryImpl extends RequestRepository implements FeaturedRepository {

    @Override
    public List<Product> getNewProduct(Map<String, String> headers, Integer days, Integer limit) throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        if (days != null) {
            parameters.put("days", days.toString());
        }
        if (limit != null) {
            parameters.put("limit", limit.toString());
        }

        RequestResult<JsonObject> reqResult = getRequestWithResult("/api/featured/new", parameters, headers);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/api/featured/new",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<Product>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<Product>>() {}).getType()
        );
    }

    @Override
    public List<Product> getMostedViewProduct(Map<String, String> headers, Integer limit) throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        if (limit != null) {
            parameters.put("limit", limit.toString());
        }

        RequestResult<JsonObject> reqResult = getRequestWithResult("/api/featured/mostview", parameters, headers);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/api/featured/mostview",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonArray();
        if (data == null)
            return new ArrayList<Product>();
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonArray(),
            (new TypeToken<List<Product>>() {}).getType()
        );
    }
    
}
