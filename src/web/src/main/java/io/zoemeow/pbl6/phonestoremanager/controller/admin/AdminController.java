package io.zoemeow.pbl6.phonestoremanager.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoPermissionException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminController {

    @Autowired
    AccountRepository _AccountRepository;

    @GetMapping(value = {"/admin" , "/admin/"})
    public ModelAndView index(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("redirect:/admin/products");
            _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (NoPermissionException npEx) {
            view = new ModelAndView("redirect:/");
        } catch (SessionExpiredException seEx) {
            Cookie cookie = new Cookie("token", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            view = new ModelAndView("redirect:/auth/login");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }
        
        return view;
    }
}
