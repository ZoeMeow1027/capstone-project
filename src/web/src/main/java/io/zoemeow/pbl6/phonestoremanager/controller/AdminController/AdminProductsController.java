package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.controller.BasicAPIRequestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminProductsController extends BasicAPIRequestController {
    @GetMapping("/admin/products")
    public ModelAndView index(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        JsonObject jObject = null;
        try {
            ModelAndView view = new ModelAndView("/admin/products");

            jObject = getRequest("https://localhost:7053/api/account/my", null, header);
            if (jObject == null)
                throw new Exception("No data");
            view.addObject("name", jObject.get("data").getAsJsonObject().get("name").getAsString());

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("includeHidden", "true");
            jObject = getRequest("https://localhost:7053/api/products", parameters, header);
            if (jObject != null) {
                view.addObject("productList", jObject.get("data").getAsJsonArray());
            }
            else {
                view.addObject("productList", null);
            }

            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/auth/login");
            return view;
        }
    }
}
