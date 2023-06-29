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
public class CheckoutController extends SessionController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/checkout")
    public ModelAndView pageCheckout(
        HttpServletRequest request,
        HttpServletResponse response,
        @ModelAttribute("barMsg") String barMsg
    ) {
        ModelAndView view = new ModelAndView("global/cart/checkout");

        try {
            User user = getUserInformation(request, response);
            view.addObject("user", user);
            view.addObject("name", user != null ? user.getName() : null);
            view.addObject("adminUser", user != null && (user.getUserType() != 0));

            if (user == null) {
                throw new SessionExpiredException("Session has expired!");
            }

            List<UserCart> cart = user != null ? _CartRepository.getAllItemsInCart(getCookieHeader(request), null, null) : null;
            view.addObject("cartList", cart);
            view.addObject("cartCount", cart != null ? cart.size() : null);

            view.addObject("cartTotal", cart != null ? cart.stream().mapToDouble(o -> o.getProduct().getPrice() * o.getCount()).sum() : null);
            view.addObject("shippingPrice", 3.00);
            view.addObject("userAddressList", _AccountRepository.getAllAddress(getCookieHeader(request)));
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @PostMapping("/checkout")
    public ModelAndView actionCheckout(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        @RequestParam("addressid") Integer addressId,
        @RequestParam("message") String message
    ) {
        ModelAndView view = new ModelAndView();

        try {
            if (message.length() == 0) message = null;
            var checkout = _CartRepository.checkout(getCookieHeader(request), addressId, message);
            if (checkout.getStatusCode() != 200) {
                throw new Exception(checkout.getMessage());
            }

            if (checkout.getData() == null) {
                view.setViewName("redirect:/account/delivery?activeonly=true");
            }
            var data = checkout.getData().getAsJsonObject().get("data").getAsJsonObject();
            if (data.get("orderid") != null) {
                view.setViewName(String.format("redirect:/payment-method?id=%d", data.get("orderid").getAsInt()));
            } else {
                view.setViewName("redirect:/account/delivery?activeonly=true");
            }

            redirectAttributes.addFlashAttribute("barMsg", "Successfully placed order! Check your order in Your current order.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                "barMsg",
                String.format("We ran into a problem prevent placing order item for you! Message: %s", ex.getMessage())
            );
            view.setViewName("redirect:/checkout");
        }

        return view;
    }
}
