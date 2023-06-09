package io.zoemeow.pbl6.phonestoremanager.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.StatisticsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminDashboardController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    StatisticsRepository _StatisticsRepository;

    @GetMapping("/admin/dashboard")
    public ModelAndView index(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("admin/dashboard");
            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

            view.addObject("statistics", _StatisticsRepository.getStatisticsByMonth(header, null, null));
            view.addObject("dashboard", _StatisticsRepository.getDashboard(header));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }
}
