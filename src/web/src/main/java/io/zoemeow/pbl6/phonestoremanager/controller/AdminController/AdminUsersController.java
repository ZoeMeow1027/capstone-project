package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.controller.BasicAPIRequestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@Configuration
@Import({ SimpleDateFormat.class })
public class AdminUsersController extends BasicAPIRequestController {
    @GetMapping("/admin/users")
    public ModelAndView index(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        JsonObject jObject = null;
        try {
            ModelAndView view = new ModelAndView("/admin/users");

            jObject = getRequest("https://localhost:7053/api/account/my", null, header);
            if (jObject == null)
                throw new Exception("No data");
            view.addObject("name", jObject.get("data").getAsJsonObject().get("name").getAsString());

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("type", "user");
            parameters.put("includedisabled", "true");
            jObject = getRequest("https://localhost:7053/api/users", parameters, header);
            if (jObject != null) {
                view.addObject("userList", jObject.get("data").getAsJsonArray());
            }
            else {
                view.addObject("userList", null);
            }

            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/auth/login");
            return view;
        }
    }
}
