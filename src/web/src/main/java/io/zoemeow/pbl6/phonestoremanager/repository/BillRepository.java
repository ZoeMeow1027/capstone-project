package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import io.zoemeow.pbl6.phonestoremanager.model.bean.BillSummary;
import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;

public interface BillRepository {
    public List<BillSummary> getAllBillSummaries(Map<String, String> headers) throws Exception;

    public BillSummary getBillSummaryById(Map<String, String> header, Integer id) throws Exception;

    public RequestResult<JsonObject> cancelOrder(Map<String, String> header, Integer orderId) throws Exception;

    public RequestResult<JsonObject> updateOrder(
            Map<String, String> header, Integer orderId, Integer status, String statusAddress, String statusAdditional
    ) throws Exception;
}
