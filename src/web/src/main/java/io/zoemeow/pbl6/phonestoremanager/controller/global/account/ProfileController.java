package io.zoemeow.pbl6.phonestoremanager.controller.global.account;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.AuthRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.CartRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ProfileController {
    @Autowired
    AuthRepository _AuthRepository;

    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/account/profile")
    public ModelAndView pageProfile(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        ModelAndView view = new ModelAndView("/global/account/profile");

        try {
            User user = _AccountRepository.getUserInformation(header, null);
            view.addObject("user", user);
            view.addObject("name", user == null ? "(Unknown)" : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);
            view.addObject("cartCount", _CartRepository.getAllItemsInCart(header, null, null).size());
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            view = new ModelAndView("redirect:/");
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @PostMapping(value = "/account/profile")
    public String actionUpdateUserProfile(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            _AccountRepository.getUserInformation(header, null);
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            _AccountRepository.setUserInformation(header, user);
            redirectAttributes.addFlashAttribute("barMsg", "Successfully set your profile.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "A problem prevent you to save your profile information.");
        }

        return "redirect:/account/profile";
    }

    @GetMapping(value = "/account/avatar", produces = "image/jpeg")
    @ResponseBody
    public Object actionGetAvatar(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            var data = _AccountRepository.getAvatar(header);
            if (data == null)
                throw new Exception("No image here");

            return data;
        } catch (Exception ex) {
            Resource resource = new ClassPathResource("static/img/person-circle.jpg");
            InputStream input = resource.getInputStream();
            return input.readAllBytes();
        }
    }

    @PostMapping(value = "/account/avatar")
    @ResponseBody
    public Object actionSetAvatar(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult<JsonObject> reqResult;
        try {
            _AccountRepository.getUserInformation(header, null);
            reqResult = _AccountRepository.setAvatar(header, file.getResource());
        } catch (Exception ex) {
            reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
        }

        return (new Gson().toJson(reqResult));
    }

    @PostMapping(value = "/account/avatar/upload")
    public String actionSetAvatarAndReturnView(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestParam("file") MultipartFile file) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            _AccountRepository.getUserInformation(header, null);
            _AccountRepository.setAvatar(header, file.getResource());
            redirectAttributes.addFlashAttribute("barMsg", "Successfully set your avatar.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "A problem prevent you to set your avatar.");
        }

        return "redirect:/account/profile";
    }
}