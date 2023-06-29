package io.zoemeow.pbl6.phonestoremanager.controller.global;

import java.util.List;
import io.zoemeow.pbl6.phonestoremanager.controller.SessionController;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.CartRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DeliveryController extends SessionController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/account/delivery")
    public ModelAndView pageDelivery(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg,
            Boolean activeonly) {
        ModelAndView view = new ModelAndView("global/delivery/delivery-list");

        try {
            User user = getUserInformation(request, response);
            view.addObject("user", user);
            view.addObject("name", user != null ? user.getName() : null);
            view.addObject("adminUser", user != null && (user.getUserType() != 0));
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);

            List<UserCart> cart = user != null ? _CartRepository.getAllItemsInCart(getCookieHeader(request), null, null) : null;
            view.addObject("cartList", cart);
            view.addObject("cartCount", cart != null ? cart.size() : null);

            view.addObject("activeonly", activeonly == null ? false : activeonly);
            view.addObject(
                    "deliverylist",
                    activeonly
                            ? _AccountRepository.getActiveBillSummaries(getCookieHeader(request))
                            : _AccountRepository.getBillSummaries(getCookieHeader(request)));
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
            ex.printStackTrace();
        }

        return view;
    }

    @GetMapping("/account/delivery/detail")
    public ModelAndView pageDeliveryDetail(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg,
            Integer id) {
        ModelAndView view = new ModelAndView("global/delivery/delivery-detail");

        try {
            User user = getUserInformation(request, response);
            view.addObject("user", user);
            view.addObject("name", user != null ? user.getName() : null);
            view.addObject("adminUser", user != null && (user.getUserType() != 0));
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);

            if (user == null) {
                throw new SessionExpiredException("Session has expired!");
            }

            List<UserCart> cart = user != null ? _CartRepository.getAllItemsInCart(getCookieHeader(request), null, null) : null;
            view.addObject("cartList", cart);
            view.addObject("cartCount", cart != null ? cart.size() : null);

            var data = _AccountRepository.getBillSummaryById(getCookieHeader(request), id);
            view.addObject("orderitem", data);
            view.addObject(
                    "useraddress",
                    String.format(
                            "%s\n%s, %s\n%s",
                            data.getRecipient(),
                            data.getRecipientAddress(),
                            data.getRecipientCountryCode(),
                            data.getRecipientPhone()
                    )
            );
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
            ex.printStackTrace();
        }

        return view;
    }

    @PostMapping("/account/delivery/cancel")
    public ModelAndView actionCancelOrder(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            String returnurl,
            @RequestParam("orderid") Integer orderid) {
        ModelAndView view = new ModelAndView("redirect:/account/delivery?activeonly=true");

        try {
            var cancelOrder = _AccountRepository.cancelOrder(getCookieHeader(request), orderid);
            if (cancelOrder.getStatusCode() != 200) {
                throw new Exception(cancelOrder.getMessage());
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully cancelled order!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "barMsg",
                    String.format("We ran into a problem prevent cancelling your order! Message: %s", ex.getMessage()));
        }

        if (returnurl != null) {
            view.setViewName(String.format("redirect:%s", returnurl));
        }
        return view;
    }
}
