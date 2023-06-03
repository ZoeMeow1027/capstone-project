package io.zoemeow.pbl6.phonestoremanager.controller.global;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.repository.AuthRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ProfileController {
@Autowired
    AuthRepository _AuthRepository;

    @GetMapping("/account/profile")
    public ModelAndView pageProfile(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        ModelAndView view = new ModelAndView("/global/account/profile");

        try {
            User user = _AuthRepository.getUserInformation(header, null);
            view.addObject("name", user == null ? "(Unknown)" : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
        } catch (SessionExpiredException seEx) {
            view = new ModelAndView("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }
}
