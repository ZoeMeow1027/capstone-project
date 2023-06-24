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
public class DeliveryController {
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
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        ModelAndView view = new ModelAndView("global/delivery/delivery-list");

        activeonly = activeonly == null ? false : activeonly;

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            view.addObject("user", user);
            view.addObject("name", user == null ? null : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);
            view.addObject("baseurl",
                    String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));

            view.addObject("cartCount", _CartRepository.getAllItemsInCart(header, null, null).size());
            view.addObject("activeonly", activeonly);
            view.addObject(
                    "deliverylist",
                    activeonly
                            ? _AccountRepository.getActiveBillSummaries(header)
                            : _AccountRepository.getBillSummaries(header));
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view = new ModelAndView("redirect:/");
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
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        ModelAndView view = new ModelAndView("global/delivery/delivery-detail");

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            view.addObject("user", user);
            view.addObject("name", user == null ? null : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);
            view.addObject("baseurl",
                    String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));

            view.addObject("cartCount", _CartRepository.getAllItemsInCart(header, null, null).size());

            var data = _AccountRepository.getBillSummaryById(header, id);
            view.addObject("orderitem", data);
            view.addObject("useraddress", String.format("%s\n%s, %s\n%s", data.getRecipient(), data.getRecipientAddress(), data.getRecipientCountryCode(),
                    data.getRecipientPhone()));
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view = new ModelAndView("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
            ex.printStackTrace();
        }

        return view;
    }

    @PostMapping("/account/delivery/cancel")
    public String actionCancelOrderAndReturn(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            String returnurl,
            @RequestParam("orderid") Integer orderid) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            var cancelOrder = _AccountRepository.cancelOrder(header, orderid);
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
            return String.format("redirect:%s", returnurl);
        }
        return String.format("redirect:/account/delivery?activeonly=true");
    }
}
