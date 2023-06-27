package io.zoemeow.pbl6.phonestoremanager.controller.auth;

import com.google.gson.JsonObject;
import io.zoemeow.pbl6.phonestoremanager.controller.SessionController;
import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.dto.RegisterDTO;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.AuthRepository;
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

@Controller
public class AuthController extends SessionController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    AuthRepository _AuthRepository;

    @GetMapping("/auth/login")
    public ModelAndView pageLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg,
            @ModelAttribute("username") String username,
            @ModelAttribute("password") String password) {
        ModelAndView view = new ModelAndView("auth/login");
        view.addObject("username", username);
        view.addObject("password", password);
        view.addObject("barMsg", barMsg);

        try {
            User user = _AccountRepository.getUserInformation(getCookieHeader(request), null);
            if (user != null) {
                switch (user.getUserType()) {
                    case 2:
                        view.setViewName("redirect:/admin");
                        break;
                    case 1:
                        // TODO: Return staff page here
                        view.setViewName("redirect:/admin");
                        break;
                    default:
                        view.setViewName("redirect:/");
                        break;
                }
            }
        } catch (Exception ex) {
            view.setViewName("auth/login");
        }

        return view;
    }

    @PostMapping("/auth/login")
    public ModelAndView actionLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            String username,
            String password
    ) {
        ModelAndView view = new ModelAndView("auth/login");

        try {
            JsonObject auth = new JsonObject();
            auth.addProperty("username", username);
            auth.addProperty("password", password);

            RequestResult<JsonObject> reqResult = _AuthRepository.login(getCookieHeader(request), username, password);

            setCookieHeader(response, reqResult.getData().get("data").getAsJsonObject().get("token").getAsString());

            switch (reqResult.getData().get("data").getAsJsonObject().get("usertype").getAsInt()) {
                case 2:
                    view.setViewName("redirect:/admin");
                    break;
                case 1:
                    // TODO: Return staff page here
                    view.setViewName("redirect:/admin");
                    break;
                default:
                    view.setViewName("redirect:/");
                    break;
            }
        } catch (Exception ex) {
            view.setViewName("redirect:/auth/login");
            redirectAttributes.addFlashAttribute("errMsg", ex.getMessage());
            redirectAttributes.addFlashAttribute("username", username);
            redirectAttributes.addFlashAttribute("password", password);
        }

        return view;
    }

    @GetMapping("/auth/register")
    public ModelAndView pageRegister(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg
    ) {
        ModelAndView view = new ModelAndView("auth/register");
        view.addObject("barMsg", barMsg);

        try {
            User user = _AccountRepository.getUserInformation(getCookieHeader(request), null);
            if (user != null) {
                switch (user.getUserType()) {
                    case 2:
                        view.setViewName("redirect:/admin");
                        break;
                    case 1:
                        // TODO: Return staff page here
                        view.setViewName("redirect:/admin");
                        break;
                    default:
                        view.setViewName("redirect:/");
                        break;
                }
            }
        } catch (Exception ex) {
            view.setViewName("auth/register");
        }

        return view;
    }

    @PostMapping(value = "/auth/register")
    public ModelAndView actionRegister(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("rePassword") String rePassword
    ) {
        ModelAndView view = new ModelAndView("auth/register");

        RequestResult<JsonObject> reqResult;
        try {
            RegisterDTO registerDTO = new RegisterDTO();
            registerDTO.setName(name);
            registerDTO.setEmail(email);
            registerDTO.setPhone(phone);
            registerDTO.setUsername(username);
            registerDTO.setPassword(password);
            registerDTO.setReEnterPassword(rePassword);

            reqResult = _AuthRepository.register(getCookieHeader(request), registerDTO);

            setCookieHeader(response, reqResult.getData().get("data").getAsJsonObject().get("token").getAsString());

            view.setViewName("redirect:/");
        } catch (Exception ex) {
            view.setViewName("auth/register");
            redirectAttributes.addFlashAttribute("errMsg", ex.getMessage());
        }

        return view;
    }

    @GetMapping("/auth/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("redirect:/");

        try {
            _AuthRepository.logout(getCookieHeader(request));
        } catch (Exception ex) {

        } finally {
            clearCookieHeader(response);
        }

        return view;
    }
}
