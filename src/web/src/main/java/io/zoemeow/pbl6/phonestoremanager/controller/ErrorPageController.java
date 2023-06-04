package io.zoemeow.pbl6.phonestoremanager.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorPageController implements ErrorController {
    @GetMapping("/error")
    public ModelAndView pageError() {
        ModelAndView view = new ModelAndView("redirect:/");
        return view;
    }
}
