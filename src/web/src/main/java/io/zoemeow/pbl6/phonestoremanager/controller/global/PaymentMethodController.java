package io.zoemeow.pbl6.phonestoremanager.controller.global;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
            view.addObject("useraddress", String.format("%s\n%s\n%s", data.getRecipient(), data.getRecipientAddress(),
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
}
