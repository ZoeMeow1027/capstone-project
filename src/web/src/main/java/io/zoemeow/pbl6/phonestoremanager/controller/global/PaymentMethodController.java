package io.zoemeow.pbl6.phonestoremanager.controller.global;

import java.util.*;

import io.zoemeow.pbl6.phonestoremanager.controller.SessionController;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserCart;
import io.zoemeow.pbl6.phonestoremanager.model.dto.PayPalResponseDataDTO;
import io.zoemeow.pbl6.phonestoremanager.model.dto.PaymentMethodResponseDTO;
import io.zoemeow.pbl6.phonestoremanager.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
public class PaymentMethodController extends SessionController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/payment-method")
    public ModelAndView pagePaymentMethod(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg,
            Integer id) {
        ModelAndView view = new ModelAndView("global/cart/payment-method");

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
            if (data == null) {
                throw new Exception("No data for this order ID found!");
            }
            view.addObject("alreadydone", !Arrays.asList(0).contains(data.getDeliverStatus()));
            view.addObject("orderitem", data);
            view.addObject("useraddress", String.format("%s\n%s, %s\n%s", data.getRecipient(), data.getRecipientAddress(), data.getRecipientCountryCode(),
                    data.getRecipientPhone()));

            view.addObject("paypalClientIDSandbox", System.getenv("PAYPAL_CLIENTID_SANDBOX"));
            view.addObject("paypalClientIDLive", System.getenv("PAYPAL_CLIENTID_LIVE"));
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
            view.setViewName("redirect:/");
        }

        return view;
    }

    @PostMapping("/payment-method/paypal")
    public ModelAndView actionPaypalMethod(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestBody PaymentMethodResponseDTO<PayPalResponseDataDTO> data
            ) {
        ModelAndView view = new ModelAndView("redirect:/");

        try {
            var repo = new RequestRepository();

            Map<String, String> headerPaypal = new HashMap<String, String>();
            headerPaypal.put("Authorization", String.format("Bearer %s", data.getData().getFacilitatorAccessToken()));
            var data1 = repo.getRequestWithResult(
                    "https://api.sandbox.paypal.com",
                    "/v2/checkout/orders/" + data.getData().getOrderID(),
                    null,
                    headerPaypal);
            if (!data1.getIsSuccessfulRequest()) {
                throw new Exception("Request isn't successful");
            }
            if (data1.getStatusCode() != 200) {
                throw new Exception("Request isn't successful");
            }

            String transID = null;
            if (data1.getData() != null) {
                transID = data1.getData().getAsJsonObject()
                        .get("purchase_units").getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get("payments").getAsJsonObject()
                        .get("captures").getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get("id").getAsString();
            }

            if (!data1.getIsSuccessfulRequest()) {
                throw new Exception("Request isn't successful");
            }
            if (data1.getStatusCode() != 200) {
                throw new Exception("Request isn't successful");
            }

            var markOrderPaid = _AccountRepository.markOrderPaid(getCookieHeader(request), data.getOrderid(), 3, transID);
            if (!markOrderPaid.getIsSuccessfulRequest()) {
                throw new Exception("Something went wrong while completing your payment. If you sure you have done this payment, please contact us for support.");
            }

            redirectAttributes.addFlashAttribute("barMsg", String.format("You have done payment order %d with %s method! You can check your payment in Your active order page.", data.getOrderid(), "PayPal"));

            view.setViewName(String.format("redirect:/account/delivery/detail?id=%d", data.getOrderid()));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", ex.getMessage());
            view.setViewName("redirect:/account/delivery?activeonly=true");
        }

        return view;
    }

    @PostMapping("/payment-method/cod")
    public ModelAndView actionCoDMethod(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestBody PaymentMethodResponseDTO<String> data
    ) {
        ModelAndView view = new ModelAndView("redirect:/");

        try {
            var markOrderPaid = _AccountRepository.markOrderPaid(getCookieHeader(request), data.getOrderid(), 0, "");
            if (!markOrderPaid.getIsSuccessfulRequest()) {
                throw new Exception("Something went wrong while completing your payment. If you sure you have done this payment, please contact us for support.");
            }

            redirectAttributes.addFlashAttribute("barMsg", String.format("You have done payment order %d with %s method! You can check your payment in Your active order page.", data.getOrderid(), "CoD"));
            view.setViewName(String.format("redirect:/account/delivery/detail?id=%d", data.getOrderid()));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", ex.getMessage());
            view.setViewName("redirect:/account/delivery?activeonly=true");
        }

        return view;
    }
}
