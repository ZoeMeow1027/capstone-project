package io.zoemeow.pbl6.phonestoremanager.controller.global.account;

import java.util.HashMap;
import java.util.Map;

import io.zoemeow.pbl6.phonestoremanager.model.bean.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.CartRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountAddressController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/account/address")
    public ModelAndView pageAddressList(
        HttpServletRequest request,
        HttpServletResponse response,
        @ModelAttribute("barMsg") String barMsg
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        ModelAndView view = new ModelAndView("/global/account/address");

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            var userAddress = _AccountRepository.getAllAddress(header);
            // _AccountRepository.getAddressById(header, id);
            view.addObject("user", user);
            view.addObject("name", user == null ? "(Unknown)" : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);
            view.addObject("cartCount", _CartRepository.getAllItemsInCart(header, null, null).size());
            view.addObject("userAddress", userAddress);
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view = new ModelAndView("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @GetMapping("/account/address/add")
    public ModelAndView pageAddressAdd(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        ModelAndView view = new ModelAndView("/global/account/address-modify");

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            view.addObject("user", user);
            view.addObject("name", user == null ? "(Unknown)" : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);

            view.addObject("cartCount", _CartRepository.getAllItemsInCart(header, null, null).size());

            // _AccountRepository.getAddressById(header, id);
            view.addObject("action", "add");
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view = new ModelAndView("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @PostMapping("/account/address/add")
    public String actionAddressAdd(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            _AccountRepository.getUserInformation(header, null);
            UserAddress userAddress = new UserAddress();
            userAddress.setName(name);
            userAddress.setPhone(phone);
            userAddress.setAddress(address);
            var result = _AccountRepository.addAddress(header, userAddress);
            if (result.getStatusCode() != 200)
                throw new Exception(result.getMessage());
            redirectAttributes.addFlashAttribute("barMsg", "Successfully added your address!");
            return "redirect:/account/address";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", ex.getMessage());
            return "redirect:/account/address/add";
        }
    }

    @GetMapping("/account/address/update")
    public ModelAndView pageAddressUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        ModelAndView view = new ModelAndView("/global/account/address-modify");

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            view.addObject("user", user);
            view.addObject("name", user == null ? "(Unknown)" : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);

            view.addObject("cartCount", _CartRepository.getAllItemsInCart(header, null, null).size());

            // _AccountRepository.getAddressById(header, id);
            view.addObject("action", "update");
            var userAddress = _AccountRepository.getAddressById(header, id);
            if (userAddress == null) {
                throw new Exception();
            }
            view.addObject("userAddress", userAddress);
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view = new ModelAndView("redirect:/");
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/account/address");
        }

        return view;
    }

    @PostMapping("/account/address/update")
    public String actionAddressUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestParam("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            _AccountRepository.getUserInformation(header, null);
            UserAddress userAddress = new UserAddress();
            userAddress.setId(id);
            userAddress.setName(name);
            userAddress.setPhone(phone);
            userAddress.setAddress(address);
            var result = _AccountRepository.updateAddress(header, userAddress);
            if (result.getStatusCode() != 200)
                throw new Exception(result.getMessage());
            redirectAttributes.addFlashAttribute("barMsg", "Successfully updated your address!");
            return "redirect:/account/address";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "We ran into a problem prevent you updating your address!");
            return String.format("redirect:/account/address/update?id=%d", id);
        }
    }

    @PostMapping("/account/address/delete")
    public String actionAddressUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestParam("id") Integer id
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            _AccountRepository.getUserInformation(header, null);
            UserAddress userAddress = new UserAddress();
            userAddress.setId(id);
            var result = _AccountRepository.deleteAddress(header, userAddress);
            if (result.getStatusCode() != 200)
                throw new Exception(result.getMessage());
            redirectAttributes.addFlashAttribute("barMsg", "Successfully deleted your address!");
            return "redirect:/account/address";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "We ran into a problem prevent you deleting your address!");
            return "redirect:/account/address";
        }
    }
}
