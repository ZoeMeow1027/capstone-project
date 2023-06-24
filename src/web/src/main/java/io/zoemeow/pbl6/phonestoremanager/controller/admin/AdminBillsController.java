package io.zoemeow.pbl6.phonestoremanager.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.BillRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminBillsController {
    @Autowired
    BillRepository _BillRepository;
    
    @Autowired
    AccountRepository _AccountRepository;

    @GetMapping("/admin/bills")
    public ModelAndView pageBills(
            HttpServletRequest request,
            HttpServletResponse response,
            Boolean activeonly
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("admin/bills/index");

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

            view.addObject("billList", _BillRepository.getAllBillSummaries(header));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @GetMapping("/admin/bills/detail")
    public ModelAndView pageBillDetail(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg,
            Integer id
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("admin/bills/detail");

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());
            view.addObject("baseurl",
                    String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));

            var data = _BillRepository.getBillSummaryById(header, id);
            view.addObject("billItem", data);
            view.addObject("useraddress", String.format("%s\n%s, %s\n%s", data.getRecipient(), data.getRecipientAddress(), data.getRecipientCountryCode(),
                    data.getRecipientPhone()));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @GetMapping("/admin/bills/update")
    public ModelAndView pageBillUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            Integer id,
            String returnurl
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("admin/bills/update");

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());
            view.addObject("baseurl",
                    String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));
            view.addObject("returnurl", returnurl);

            var data = _BillRepository.getBillSummaryById(header, id);
            if (Arrays.asList(-2, -1, 4).contains(data.getDeliverStatus())) {
                redirectAttributes.addFlashAttribute("barMsg", "Order you want to update has been completed!");
                throw new Exception();
            }
            view.addObject("billItem", data);
            view.addObject("useraddress", String.format("%s\n%s, %s\n%s", data.getRecipient(), data.getRecipientAddress(), data.getRecipientCountryCode(),
                    data.getRecipientPhone()));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin/bills");
        }
        return view;
    }

    @PostMapping("/admin/bills/cancel")
    public String actionCancelOrderAndReturn(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            String returnurl,
            @RequestParam("orderid") Integer orderid) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            var cancelOrder = _BillRepository.cancelOrder(header, orderid);
            if (cancelOrder.getStatusCode() != 200) {
                throw new Exception(cancelOrder.getMessage());
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully cancelled order!");

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "barMsg",
                    String.format("We ran into a problem prevent cancelling this order! Message: %s", ex.getMessage()));
        }

        if (returnurl != null) {
            return String.format("redirect:%s", returnurl);
        }
        return String.format("redirect:/admin/bills");
    }

    @PostMapping("/admin/bills/update")
    public String actionUpdateOrderAndReturn(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            String returnurl,
            @RequestParam("orderid") Integer orderid,
            @RequestParam("status") Integer status,
            @RequestParam("statusAddress") String statusAddress,
            @RequestParam("statusAdditional") String statusAdditional) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            var cancelOrder = _BillRepository.updateOrder(header, orderid, status, statusAddress, statusAdditional);
            if (cancelOrder.getStatusCode() != 200) {
                throw new Exception(cancelOrder.getMessage());
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully updated order!");

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "barMsg",
                    String.format("We ran into a problem prevent updating this order! Message: %s", ex.getMessage()));
        }

        if (returnurl.length() == 0) {
            returnurl = null;
        }
        if (returnurl != null) {
            return String.format("redirect:%s", returnurl);
        }
        return "redirect:/admin/bills";
    }
}
