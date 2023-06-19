package io.zoemeow.pbl6.phonestoremanager.controller.global;

import java.util.*;

import io.zoemeow.pbl6.phonestoremanager.model.dto.PaymentMethodResponseDTO;
import io.zoemeow.pbl6.phonestoremanager.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.CartRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PaymentMethodController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/payment-method")
    public ModelAndView pageDeliveryDetail(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg,
            Integer id) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        ModelAndView view = new ModelAndView("/global/cart/payment-method");

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            view.addObject("user", user);
            view.addObject("name", user == null ? "(Unknown)" : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);
            view.addObject("baseurl",
                    String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));

            view.addObject("cartCount", _CartRepository.getAllItemsInCart(header, null, null).size());

            var data = _AccountRepository.getBillSummaryById(header, id);
            if (data == null) {
                throw new Exception("No data for this order ID found!");
            }
            view.addObject("alreadydone", !Arrays.asList(0).contains(data.getDeliverStatus()));
            view.addObject("orderitem", data);
            view.addObject("useraddress", String.format("%s\n%s, %s\n%s", data.getRecipient(), data.getRecipientAddress(), data.getRecipientCountryCode(),
                    data.getRecipientPhone()));
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view = new ModelAndView("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
            view = new ModelAndView("redirect:/");
        }

        return view;
    }

    @PostMapping("/payment-method/paypal")
    public String actionPaypalMethod(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody PaymentMethodResponseDTO data
            ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

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

            var markOrderPaid = _AccountRepository.markOrderPaid(header, data.getOrderid(), 3, transID);
            if (!data1.getIsSuccessfulRequest()) {
                throw new Exception("Request isn't successful");
            }
            if (data1.getStatusCode() != 200) {
                throw new Exception("Request isn't successful");
            }
        } catch (Exception ex) {

        }

        return "redirect:/account/delivery?activeonly=true";
    }
}
