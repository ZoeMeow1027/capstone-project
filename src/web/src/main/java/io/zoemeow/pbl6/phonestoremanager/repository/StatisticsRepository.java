package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.Map;

import io.zoemeow.pbl6.phonestoremanager.model.bean.Dashboard;
import io.zoemeow.pbl6.phonestoremanager.model.bean.StatisticsByMonth;

public interface StatisticsRepository {
    public StatisticsByMonth getStatisticsByMonth(Map<String, String> header, Integer year, Integer month) throws Exception;

    public Dashboard getDashboard(Map<String, String> header) throws Exception;
}
