package io.zoemeow.pbl6.phonestoremanager.controller.global.account;

import io.zoemeow.pbl6.phonestoremanager.controller.SessionController;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserAddress;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserCart;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.CartRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AccountAddressController extends SessionController {
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
        ModelAndView view = new ModelAndView("global/account/address");

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

            var userAddress = _AccountRepository.getAllAddress(getCookieHeader(request));
            view.addObject("userAddress", userAddress);
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
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
        ModelAndView view = new ModelAndView("global/account/address-modify");

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

            view.addObject("action", "add");
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @PostMapping("/account/address/add")
    public ModelAndView actionAddressAdd(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("countrycode") String countryCode
    ) {
        ModelAndView view = new ModelAndView("redirect:/account/address");
        try {
            _AccountRepository.getUserInformation(getCookieHeader(request), null);
            UserAddress userAddress = new UserAddress();
            userAddress.setName(name);
            userAddress.setPhone(phone);
            userAddress.setAddress(address);
            userAddress.setCountryCode(countryCode);
            var result = _AccountRepository.addAddress(getCookieHeader(request), userAddress);
            if (result.getStatusCode() != 200) {
                throw new Exception(result.getMessage());
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully added your address!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", ex.getMessage());
            view.setViewName("redirect:/account/address/add");
        }
        return view;
    }

    @GetMapping("/account/address/update")
    public ModelAndView pageAddressUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id
    ) {
        ModelAndView view = new ModelAndView("global/account/address-modify");

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

            // _AccountRepository.getAddressById(header, id);
            view.addObject("action", "update");
            var userAddress = _AccountRepository.getAddressById(getCookieHeader(request), id);
            if (userAddress == null) {
                throw new Exception();
            }
            view.addObject("userAddress", userAddress);
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
        } catch (Exception ex) {
            view.setViewName("redirect:/account/address");
        }

        return view;
    }

    @PostMapping("/account/address/update")
    public ModelAndView actionAddressUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestParam("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("countrycode") String countryCode
    ) {
        ModelAndView view = new ModelAndView("redirect:/account/address");
        try {
            _AccountRepository.getUserInformation(getCookieHeader(request), null);
            UserAddress userAddress = new UserAddress();
            userAddress.setId(id);
            userAddress.setName(name);
            userAddress.setPhone(phone);
            userAddress.setAddress(address);
            userAddress.setCountryCode(countryCode);
            var result = _AccountRepository.updateAddress(getCookieHeader(request), userAddress);
            if (result.getStatusCode() != 200) {
                throw new Exception(result.getMessage());
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully updated your address!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "We ran into a problem prevent you updating your address!");
            view.setViewName(String.format("redirect:/account/address/update?id=%d", id));
        }
        return view;
    }

    @PostMapping("/account/address/delete")
    public ModelAndView actionAddressDelete(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestParam("id") Integer id
    ) {
        ModelAndView view = new ModelAndView("redirect:/account/address");
        try {
            _AccountRepository.getUserInformation(getCookieHeader(request), null);
            UserAddress userAddress = new UserAddress();
            userAddress.setId(id);
            var result = _AccountRepository.deleteAddress(getCookieHeader(request), userAddress);
            if (result.getStatusCode() != 200)
                throw new Exception(result.getMessage());
            redirectAttributes.addFlashAttribute("barMsg", "Successfully deleted your address!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "We ran into a problem prevent you deleting your address!");
        }

        return view;
    }
}
