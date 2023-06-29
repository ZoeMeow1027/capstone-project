package io.zoemeow.pbl6.phonestoremanager.controller.global;

import io.zoemeow.pbl6.phonestoremanager.controller.SessionController;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserCart;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
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
public class CartController extends SessionController {
    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/cart")
    public ModelAndView pageCart(
        HttpServletRequest request,
        HttpServletResponse response,
        @ModelAttribute("barMsg") String barMsg
    ) {
        ModelAndView view = new ModelAndView("global/cart/cart");

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

            view.addObject("cartTotal", cart != null ? cart.stream().mapToDouble(o -> o.getProduct().getPrice() * o.getCount()).sum() : null);
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @PostMapping("/cart/update")
    public ModelAndView actionUpdateItemInCart(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        @RequestParam("productid") Integer productId,
        @RequestParam("count") Integer count
    ) {
        ModelAndView view = new ModelAndView("redirect:/cart");

        try {
            _CartRepository.updateItem(getCookieHeader(request), productId, count);
            redirectAttributes.addFlashAttribute("barMsg", "Successfully updated item in your cart!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "A problem prevent you to update item in your cart.");
        }

        return view;
    }

    @PostMapping("/cart/remove")
    public ModelAndView actionRemoveItemInCart(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        @RequestParam("productid") Integer productId
    ) {
        ModelAndView view = new ModelAndView("redirect:/cart");

        try {
            _CartRepository.removeItem(getCookieHeader(request), productId);
            redirectAttributes.addFlashAttribute("barMsg", "Successfully updated item in your cart!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "A problem prevent you to update item in your cart.");
        }

        return view;
    }

    @PostMapping("/cart/remove-all")
    public ModelAndView actionRemoveAllInCart(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes
    ) {
        ModelAndView view = new ModelAndView("redirect:/cart");

        try {
            _CartRepository.removeAllItems(getCookieHeader(request));
            redirectAttributes.addFlashAttribute("barMsg", "Successfully removed all items in your cart!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "A problem prevent you to empty your cart.");
        }

        return view;
    }
}
