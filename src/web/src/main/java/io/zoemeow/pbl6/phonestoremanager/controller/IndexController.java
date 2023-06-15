package io.zoemeow.pbl6.phonestoremanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.ProductRepository;
import io.zoemeow.pbl6.phonestoremanager.utils.RequestAndResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class IndexController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    ProductRepository _AdminProductRepository;

    @GetMapping("/")
    public ModelAndView index(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        var header = RequestAndResponse.getCookieHeader(request);
        ModelAndView view = new ModelAndView("/global/index");

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            view.addObject("name", user == null ? "(Unknown)" : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
        } catch (SessionExpiredException seEx) {
            view.addObject("name", null);
            view.addObject("adminuser", false);

            // Cookie cookie = new Cookie("token", "");
            // cookie.setPath("/");
            // cookie.setMaxAge(0);
            // response.addCookie(cookie);
            RequestAndResponse.clearCookieHeader(response);

            view = new ModelAndView("/global/index");
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
        ModelAndView view = new ModelAndView("/global/search");

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            view.addObject("name", user == null ? "(Unknown)" : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
            view.addObject("baseurl", String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));
            view.addObject("productfilter", _AdminProductRepository.getProducts(header, q, false));
        } catch (SessionExpiredException seEx) {
            view.addObject("name", null);
            view.addObject("adminuser", false);

            // Cookie cookie = new Cookie("token", "");
            // cookie.setPath("/");
            // cookie.setMaxAge(0);
            // response.addCookie(cookie);
            RequestAndResponse.clearCookieHeader(response);

            view = new ModelAndView("/global/index");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        view.addObject("query", q);
        return view;
    }
}
