package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.controller.BasicAPIRequestController;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminController extends BasicAPIRequestController {
    @GetMapping("/admin")
    public ModelAndView index(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult reqResult = null;
        try {
            ModelAndView view = new ModelAndView("redirect:/admin/dashboard");

            reqResult = getRequest("https://localhost:7053/api/account/my", null, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }

            return view;
        } catch (Exception ex) {
            Cookie cookie = new Cookie("token", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            ModelAndView view = new ModelAndView("redirect:/auth/login");
            return view;
        }
    }
}
