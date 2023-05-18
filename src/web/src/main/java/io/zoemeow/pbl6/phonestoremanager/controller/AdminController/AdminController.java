package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.controller.BasicAPIRequestController;
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

        JsonObject jObject = null;
        try {
            ModelAndView view = new ModelAndView("redirect:/admin/dashboard");
            jObject = getRequest("https://localhost:7053/api/account/my", null, header);
            if (jObject == null)
                throw new Exception("No data");
            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/auth/login");
            Cookie cookie = new Cookie("token", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return view;
        }
    }
}
