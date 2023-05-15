package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminProductsController {
    @GetMapping("/admin/products")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("admin/products");
        // view.addObject(null, view);
        return view;
    }
}
