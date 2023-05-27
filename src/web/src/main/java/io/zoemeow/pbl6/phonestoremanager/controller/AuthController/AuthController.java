package io.zoemeow.pbl6.phonestoremanager.controller.AuthController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.repository.RequestRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController extends RequestRepository {
    @GetMapping("/auth/login")
    public ModelAndView loginPage(
            HttpServletRequest request,
            HttpServletResponse response) {
        String uriRedirect = "";

        try {
            Map<String, String> header = new HashMap<String, String>();
            header.put("cookie", request.getHeader("cookie"));

            RequestResult<JsonObject> reqResult = getRequest("/api/account/my", null, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            switch (reqResult.getData().get("data").getAsJsonObject().get("usertype").getAsInt()) {
                case 2:
                    uriRedirect = "redirect:/admin/dashboard";
                    break;
                case 1:
                    // TODO: Return staff page here
                    uriRedirect = "redirect:/admin/dashboard";
                    break;
                default:
                    uriRedirect = "redirect:/";
                    break;
            }
        } catch (Exception ex) {
            uriRedirect = "/auth/login";
        }

        ModelAndView view = new ModelAndView(uriRedirect);
        return view;
    }

    @PostMapping("/auth/login")
    public ModelAndView loginPostRequest(HttpServletRequest request, HttpServletResponse response, String username,
            String password) {
        ModelAndView modelAndView;

        try {
            JsonObject auth = new JsonObject();
            auth.addProperty("username", username);
            auth.addProperty("password", password);

            Map<String, String> header = new HashMap<String, String>();
            header.put("content-type", "application/json; charset=UTF-8");
            header.put("cookie", request.getHeader("cookie"));

            RequestResult<JsonObject> reqResult = postRequest("/api/auth/login", null,
                    header, auth.toString());

            if (!reqResult.getIsSuccessfulRequest()) {
                throw new Exception("Cannot receive information from API. Please try again.");
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(reqResult.getMessage());
            }

            Cookie cookie = new Cookie("token",
                    reqResult.getData().get("data").getAsJsonObject().get("token").getAsString());
            cookie.setPath("/");
            // 1-year
            cookie.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(cookie);

            String urlRedirect;
            switch (reqResult.getData().get("data").getAsJsonObject().get("usertype").getAsInt()) {
                case 2:
                    urlRedirect = "redirect:/admin/dashboard";
                    break;
                case 1:
                    // TODO: Return staff page here
                    urlRedirect = "redirect:/admin/dashboard";
                    break;
                default:
                    urlRedirect = "redirect:/";
                    break;
            }
            modelAndView = new ModelAndView(urlRedirect);
        } catch (Exception ex) {
            modelAndView = new ModelAndView("/auth/login");
            modelAndView.addObject("errMsg", ex.getMessage());
        }

        return modelAndView;
    }

    @GetMapping("/auth/register")
    public ModelAndView registerPage() {
        ModelAndView view = new ModelAndView("auth/register");
        // view.addObject(null, view);
        return view;
    }

    @GetMapping("/auth/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        try {
            postRequest("/api/account/logout", null, header, null);
        } catch (Exception ex) {

        } finally {
            Cookie cookie = new Cookie("token", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        ModelAndView view = new ModelAndView("redirect:/auth/login");
        return view;
    }
}
