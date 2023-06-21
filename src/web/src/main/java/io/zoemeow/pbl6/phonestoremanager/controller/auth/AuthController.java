package io.zoemeow.pbl6.phonestoremanager.controller.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.dto.RegisterDTO;
import io.zoemeow.pbl6.phonestoremanager.repository.AuthRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.RequestRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController extends RequestRepository {
    @Autowired
    AuthRepository _AuthRepository;

    @GetMapping("/auth/login")
    public ModelAndView pageLogin(
            HttpServletRequest request,
            HttpServletResponse response) {
        String uriRedirect = "/auth/login";

        try {
            Map<String, String> header = new HashMap<String, String>();
            header.put("cookie", request.getHeader("cookie"));

            RequestResult<JsonObject> reqResult = getRequestWithResult("/api/account/my", null, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            switch (reqResult.getData().get("data").getAsJsonObject().get("usertype").getAsInt()) {
                case 2:
                    uriRedirect = "redirect:/admin";
                    break;
                case 1:
                    // TODO: Return staff page here
                    uriRedirect = "redirect:/admin";
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
    public ModelAndView actionLogin(HttpServletRequest request, HttpServletResponse response, String username,
            String password) {
        ModelAndView modelAndView;

        try {
            Map<String, String> header = new HashMap<String, String>();
            header.put("content-type", "application/json; charset=UTF-8");
            header.put("cookie", request.getHeader("cookie"));

            JsonObject auth = new JsonObject();
            auth.addProperty("username", username);
            auth.addProperty("password", password);

            RequestResult<JsonObject> reqResult = _AuthRepository.login(header, username, password);

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
    public ModelAndView pageRegister(HttpServletRequest request, HttpServletResponse response) {
        String uriRedirect = "/auth/register";

        try {
            Map<String, String> header = new HashMap<String, String>();
            header.put("cookie", request.getHeader("cookie"));

            RequestResult<JsonObject> reqResult = getRequestWithResult("/api/account/my", null, header);
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
            uriRedirect = "/auth/register";
        }

        ModelAndView view = new ModelAndView(uriRedirect);
        return view;
    }

    @PostMapping(value = "/auth/register")
    @ResponseBody
    public Object actionRegister(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RegisterDTO registerDTO
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult<JsonObject> reqResult;
        try {
            reqResult = _AuthRepository.register(header, registerDTO);

            Cookie cookie = new Cookie("token",
                    reqResult.getData().get("data").getAsJsonObject().get("token").getAsString());
            cookie.setPath("/");
            // 1-year
            cookie.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(cookie);

        } catch (Exception ex) {
            reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
        }

        return (new Gson().toJson(reqResult));
    }

    @GetMapping("/auth/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        try {
            _AuthRepository.logout(header);
        } catch (Exception ex) {

        } finally {
            Cookie cookie = new Cookie("token", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        ModelAndView view = new ModelAndView("redirect:/");
        return view;
    }
}
