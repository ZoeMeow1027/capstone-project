package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.zoemeow.pbl6.phonestoremanager.model.bean.BillSummary;
import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.RequestException;

@Repository
public class BillRepositoryImpl extends RequestRepository implements BillRepository {

    @Override
    public List<BillSummary> getAllBillSummaries(Map<String, String> header) throws Exception {
        RequestResult<JsonObject> reqResult = getRequestWithResult("/api/bills", null, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/api/bills",
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
    public BillSummary getBillSummaryById(Map<String, String> header, Integer id) throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("id", id.toString());

        RequestResult<JsonObject> reqResult = getRequestWithResult("/api/bills", parameters, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/api/bills",
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
    public RequestResult<JsonObject> cancelOrder(Map<String, String> header, Integer orderId) throws Exception {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("orderid", orderId);
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "cancel");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/api/bills", null, header, postData);
    }

    @Override
    public RequestResult<JsonObject> updateOrder(Map<String, String> header, Integer orderId, Integer status, String statusAddress, String statusAdditional) throws Exception {
        JsonObject productAdd = new JsonObject();
        productAdd.addProperty("orderid", orderId);
        productAdd.addProperty("status", status);
        productAdd.addProperty("statusaddress", statusAddress);
        productAdd.addProperty("statusadditional", statusAdditional);
        JsonObject bodyRoot = new JsonObject();
        bodyRoot.addProperty("action", "updatestatus");
        bodyRoot.add("data", productAdd);

        String postData = bodyRoot.toString();
        return postRequestWithResult("/api/bills", null, header, postData);
    }

}
