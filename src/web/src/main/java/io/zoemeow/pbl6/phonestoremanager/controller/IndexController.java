package io.zoemeow.pbl6.phonestoremanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.CartRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.FeaturedRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.ProductRepository;
import io.zoemeow.pbl6.phonestoremanager.utils.RequestAndResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class IndexController {
    @Autowired
    FeaturedRepository _FeaturedRepository;

    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    ProductRepository _AdminProductRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/")
    public ModelAndView index(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        var header = RequestAndResponse.getCookieHeader(request);
        ModelAndView view = new ModelAndView("global/index");

        try {
            User user = null;
            try {
                user = _AccountRepository.getUserInformation(header, null);
                view.addObject("cartCount", _CartRepository.getAllItemsInCart(header, null, null).size());
                view.addObject("name", user == null ? null : user.getName());
                view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
            } catch (Exception ex) {
                RequestAndResponse.clearCookieHeader(response);
            }

            view.addObject("baseurl", String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));
            
            var data1 = _FeaturedRepository.getNewProduct(header, null, null);
            var data2 = _FeaturedRepository.getMostedViewProduct(header, null);
            view.addObject("newProduct", data1);
            view.addObject("mostView", data2);
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
        var header = RequestAndResponse.getCookieHeader(request);
        ModelAndView view = new ModelAndView("global/search");

        try {
            User user = null;
            try {
                user = _AccountRepository.getUserInformation(header, null);
                view.addObject("name", user == null ? null : user.getName());
                view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
                view.addObject("cartCount", user == null ? null : _CartRepository.getAllItemsInCart(header, null, null).size());
            } catch (Exception ex) {
                RequestAndResponse.clearCookieHeader(response);
            }
            
            view.addObject("baseurl", String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));
            view.addObject("productFilter", _AdminProductRepository.getProducts(header, q, false));
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        view.addObject("query", q);
        return view;
    }

    @GetMapping("/about")
    public ModelAndView pageAbout(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        var header = RequestAndResponse.getCookieHeader(request);
        ModelAndView view = new ModelAndView("global/about");

        try {
            User user = null;
            try {
                user = _AccountRepository.getUserInformation(header, null);
                view.addObject("name", user == null ? null : user.getName());
                view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
                view.addObject("cartCount", user == null ? null : _CartRepository.getAllItemsInCart(header, null, null).size());
            } catch (Exception ex) {
                RequestAndResponse.clearCookieHeader(response);
            }
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }
}
