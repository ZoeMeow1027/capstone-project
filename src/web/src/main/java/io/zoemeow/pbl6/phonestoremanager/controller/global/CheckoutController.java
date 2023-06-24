package io.zoemeow.pbl6.phonestoremanager.controller.global;

import java.util.HashMap;
import java.util.Map;

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
public class CheckoutController {
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
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        
        ModelAndView view = new ModelAndView("global/cart/checkout");

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            view.addObject("user", user);
            view.addObject("name", user == null ? null : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);
            view.addObject("baseurl", String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));

            var cartList = _CartRepository.getAllItemsInCart(header, null, null);
            view.addObject("cartCount", cartList.size());

            view.addObject("cartList", cartList);
            view.addObject("cartTotal", cartList.stream().mapToDouble(o -> o.getProduct().getPrice() * o.getCount()).sum());

            view.addObject("shippingPrice", 3.00);

            view.addObject("userAddressList", _AccountRepository.getAllAddress(header));
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view = new ModelAndView("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @PostMapping("/checkout")
    public String actionCheckoutAndReturn(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        @RequestParam("addressid") Integer addressId,
        @RequestParam("message") String message
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            if (message.length() == 0) message = null;
            var checkout = _CartRepository.checkout(header, addressId, message);
            if (checkout.getStatusCode() != 200) {
                throw new Exception(checkout.getMessage());
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully placed order! Check your order in Your current order.");
            String returnUrl = "redirect:/account/delivery?activeonly=true";
            if (checkout.getData() != null) {
                var data = checkout.getData().getAsJsonObject().get("data").getAsJsonObject();
                if (data.get("orderid") != null) {
                    returnUrl = String.format("redirect:/payment-method?id=%d", data.get("orderid").getAsInt());
                }
            }
            return returnUrl;
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                "barMsg",
                String.format("We ran into a problem prevent placing order item for you! Message: %s", ex.getMessage())
            );
            return String.format("redirect:/checkout");
        }
    }
}
