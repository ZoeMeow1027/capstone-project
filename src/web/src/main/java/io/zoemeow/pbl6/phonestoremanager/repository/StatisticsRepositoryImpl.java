package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.zoemeow.pbl6.phonestoremanager.model.bean.Dashboard;
import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.StatisticsByMonth;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.RequestException;

@Repository
public class StatisticsRepositoryImpl extends RequestRepository implements StatisticsRepository {

    @Override
    public StatisticsByMonth getStatisticsByMonth(Map<String, String> header, Integer year, Integer month) throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("year", year == null ? null : year.toString());
        parameters.put("month", month == null ? null : month.toString());
        RequestResult<JsonObject> reqResult = getRequestWithResult("/statistics/bymonth", parameters, header);

        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/statistics/bymonth",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonObject();
        if (data == null)
            return null;
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonObject(),
            (new TypeToken<StatisticsByMonth>() {}).getType()
        );
    }

    @Override
    public Dashboard getDashboard(Map<String, String> header) throws Exception {
        RequestResult<JsonObject> reqResult = getRequestWithResult("/statistics/dashboard", null, header);

        if (!reqResult.getIsSuccessfulRequest()) {
            throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
        }
        if (reqResult.getStatusCode() != 200) {
            throw new RequestException(
                "/statistics/dashboard",
                reqResult.getStatusCode(),
                reqResult.getMessage()
            );
        }

        var data = reqResult.getData().get("data").getAsJsonObject();
        if (data == null)
            return null;
        return new Gson().fromJson(
            reqResult.getData().get("data").getAsJsonObject(),
            (new TypeToken<Dashboard>() {}).getType()
        );
    }
    
}
