package io.zoemeow.pbl6.phonestoremanager.controller.global.account;

import io.zoemeow.pbl6.phonestoremanager.controller.SessionController;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.bean.UserCart;
import io.zoemeow.pbl6.phonestoremanager.model.dto.ChangePasswordDTO;
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
public class ChangePasswordController extends SessionController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/account/change-password")
    public ModelAndView pageChangePassword(
        HttpServletRequest request,
        HttpServletResponse response,
        @ModelAttribute("barMsg") String barMsg
    ) {
        ModelAndView view = new ModelAndView("global/account/change-password");

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
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view.setViewName("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @PostMapping("/account/change-password")
    public ModelAndView actionChangePasswordAndReturn(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        @RequestParam("old-pass") String oldPass,
        @RequestParam("new-pass") String newPass,
        @RequestParam("re-new-pass") String reNewPass
    ) {
        ModelAndView view = new ModelAndView("redirect:/account/change-password");

        try {
            _AccountRepository.getUserInformation(getCookieHeader(request), null);
            ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
            changePasswordDTO.setOldPass(oldPass);
            changePasswordDTO.setNewPass(newPass);
            changePasswordDTO.setReNewPass(reNewPass);
            var result = _AccountRepository.changePassword(getCookieHeader(request), changePasswordDTO);
            if (result.getStatusCode() != 200) {
                throw new Exception(result.getMessage());
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully changed your password!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", ex.getMessage());
        }

        return view;
    }
}
