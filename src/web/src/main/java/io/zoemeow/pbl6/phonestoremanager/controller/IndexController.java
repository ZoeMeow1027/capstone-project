package io.zoemeow.pbl6.phonestoremanager.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.repository.AuthRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class IndexController {
    @Autowired
    AuthRepository _AuthRepository;

    @GetMapping("/")
    public ModelAndView index(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        ModelAndView view = new ModelAndView("/global/index");

        try {
            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header, null);
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }
        } catch (Exception ex) {
            Cookie cookie = new Cookie("token", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return view;
    }
}
