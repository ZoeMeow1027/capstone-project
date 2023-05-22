package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.controller.BasicAPIRequestController;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminDashboardController extends BasicAPIRequestController {
    @GetMapping("/admin/dashboard")
    public ModelAndView index(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult reqResult = null;
        try {
            ModelAndView view = new ModelAndView("/admin/dashboard");

            reqResult = getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/admin");
            return view;
        }
    }
}
