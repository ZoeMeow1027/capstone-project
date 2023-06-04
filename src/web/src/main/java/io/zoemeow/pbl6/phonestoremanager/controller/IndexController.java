package io.zoemeow.pbl6.phonestoremanager.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
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
            User user = _AuthRepository.getUserInformation(header, null);
            view.addObject("name", user == null ? "(Unknown)" : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
        } catch (SessionExpiredException seEx) {
            view.addObject("name", null);
            view.addObject("adminuser", false);

            Cookie cookie = new Cookie("token", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            view = new ModelAndView("/global/index");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }
}
