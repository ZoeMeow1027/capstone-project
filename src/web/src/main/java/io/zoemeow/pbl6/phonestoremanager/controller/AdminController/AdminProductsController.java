package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

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
public class AdminProductsController extends BasicAPIRequestController {
    @GetMapping("/admin/products")
    public ModelAndView index(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult reqResult = null;
        try {
            ModelAndView view = new ModelAndView("/admin/products");

            reqResult = getRequest("https://localhost:7053/api/account/my", null, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("includeHidden", "true");
            reqResult = getRequest("https://localhost:7053/api/products", parameters, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("productList", reqResult.getData().get("data").getAsJsonArray());
            } else {
                view.addObject("productList", null);
            }

            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/admin");
            return view;
        }
    }
}
