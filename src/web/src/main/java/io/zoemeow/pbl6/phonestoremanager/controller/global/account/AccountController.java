package io.zoemeow.pbl6.phonestoremanager.controller.global.account;

import io.zoemeow.pbl6.phonestoremanager.controller.SessionController;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController extends SessionController {
    @Autowired
    AccountRepository _AccountRepository;

    @GetMapping("/account")
    public ModelAndView pageAccount(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        ModelAndView view = new ModelAndView("redirect:/account/profile");

        try {
            _AccountRepository.getUserInformation(getCookieHeader(request), null);
        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }
}
