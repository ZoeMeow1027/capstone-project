package io.zoemeow.pbl6.phonestoremanager.controller;

import io.zoemeow.pbl6.phonestoremanager.model.bean.UserCart;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.repository.CartRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.FeaturedRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Controller
public class IndexController extends SessionController {
    @Autowired
    FeaturedRepository _FeaturedRepository;

    @Autowired
    ProductRepository _ProductRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/")
    public ModelAndView index(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ModelAndView view = new ModelAndView("global/index");

        try {
            User user = getUserInformation(request, response);
            view.addObject("user", user);
            view.addObject("name", user != null ? user.getName() : null);
            view.addObject("adminUser", user != null && (user.getUserType() != 0));

            List<UserCart> cart = user != null ? _CartRepository.getAllItemsInCart(getCookieHeader(request), null, null) : null;
            view.addObject("cartList", cart);
            view.addObject("cartCount", cart != null ? cart.size() : null);

            view.addObject("newProduct", _FeaturedRepository.getNewProduct(getCookieHeader(request), null, null));
            view.addObject("mostView", _FeaturedRepository.getMostedViewProduct(getCookieHeader(request), null));
        } catch (NoInternetException niEx) {

        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @GetMapping("/search")
    public ModelAndView pageSearch(
        HttpServletRequest request,
        HttpServletResponse response,
        String q
    ) {
        ModelAndView view = new ModelAndView("global/search");
        view.addObject("query", q);

        try {
            User user = getUserInformation(request, response);
            view.addObject("user", user);
            view.addObject("name", user != null ? user.getName() : null);
            view.addObject("adminUser", user != null && (user.getUserType() != 0));

            List<UserCart> cart = user != null ? _CartRepository.getAllItemsInCart(getCookieHeader(request), null, null) : null;
            view.addObject("cartList", cart);
            view.addObject("cartCount", cart != null ? cart.size() : null);

            view.addObject("productFilter", _ProductRepository.getProducts(getCookieHeader(request), q, false));
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @GetMapping("/about")
    public ModelAndView pageAbout(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        ModelAndView view = new ModelAndView("global/about");

        try {
            User user = getUserInformation(request, response);
            view.addObject("user", user);
            view.addObject("name", user != null ? user.getName() : null);
            view.addObject("adminUser", user != null && (user.getUserType() != 0));

            List<UserCart> cart = user != null ? _CartRepository.getAllItemsInCart(getCookieHeader(request), null, null) : null;
            view.addObject("cartList", cart);
            view.addObject("cartCount", cart != null ? cart.size() : null);
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @GetMapping("/terms-of-service")
    public ModelAndView pageTermsOfService(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        ModelAndView view = new ModelAndView("global/terms-of-service");

        try {
            User user = getUserInformation(request, response);
            view.addObject("user", user);
            view.addObject("name", user != null ? user.getName() : null);
            view.addObject("adminUser", user != null && (user.getUserType() != 0));

            List<UserCart> cart = user != null ? _CartRepository.getAllItemsInCart(getCookieHeader(request), null, null) : null;
            view.addObject("cartList", cart);
            view.addObject("cartCount", cart != null ? cart.size() : null);
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @GetMapping("/privacy-notice")
    public ModelAndView pagePrivacyNotice(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        ModelAndView view = new ModelAndView("global/privacy-notice");

        try {
            User user = getUserInformation(request, response);
            view.addObject("user", user);
            view.addObject("name", user != null ? user.getName() : null);
            view.addObject("adminUser", user != null && (user.getUserType() != 0));

            List<UserCart> cart = user != null ? _CartRepository.getAllItemsInCart(getCookieHeader(request), null, null) : null;
            view.addObject("cartList", cart);
            view.addObject("cartCount", cart != null ? cart.size() : null);
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @GetMapping("/contact-support")
    public ModelAndView pageContactSupport(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        ModelAndView view = new ModelAndView("global/contact-support");

        try {
            User user = getUserInformation(request, response);
            view.addObject("user", user);
            view.addObject("name", user != null ? user.getName() : null);
            view.addObject("adminUser", user != null && (user.getUserType() != 0));

            List<UserCart> cart = user != null ? _CartRepository.getAllItemsInCart(getCookieHeader(request), null, null) : null;
            view.addObject("cartList", cart);
            view.addObject("cartCount", cart != null ? cart.size() : null);
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }
}
