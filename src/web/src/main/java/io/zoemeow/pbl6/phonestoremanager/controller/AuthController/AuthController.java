package io.zoemeow.pbl6.phonestoremanager.controller.AuthController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import io.zoemeow.pbl6.phonestoremanager.controller.BasicAPIRequestController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController extends BasicAPIRequestController {
    @GetMapping("/auth/login")
    public ModelAndView loginPage(
            @CookieValue(name = "token", required = false) String token,
            HttpServletRequest request,
            HttpServletResponse response) {
        String uriRedirect = "";

        try {
            if (token != null) {
                Map<String, String> header = new HashMap<String, String>();
                header.put("cookie", request.getHeader("cookie"));

                JsonObject jObject = getRequest("https://localhost:7053/api/account/my", null, header);
                if (jObject == null)
                    throw new Exception("No data");

                int code = jObject.get("code").getAsInt();
                if (code == 200) {
                    if (jObject.get("data").getAsJsonObject().get("usertype").getAsInt() == 2)
                        uriRedirect = "redirect:/admin/dashboard";
                    else uriRedirect = "redirect:/auth/login";
                } else {
                    uriRedirect = "/auth/login";
                }
            } else {
                uriRedirect = "/auth/login";
            }
        } catch (Exception ex) {
            uriRedirect = "/auth/login";
        }

        ModelAndView view = new ModelAndView(uriRedirect);
        return view;
    }

    @PostMapping("/auth/login")
    public String loginPostRequest(HttpServletRequest request, HttpServletResponse response, String username,
            String password) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode auth = mapper.createObjectNode();
            auth.put("username", username);
            auth.put("password", password);

            Map<String, String> header = new HashMap<String, String>();
            header.put("content-type", "application/json; charset=UTF-8");
            header.put("cookie", request.getHeader("cookie"));

            JsonObject jObject = postRequest("https://localhost:7053/api/auth/login", mapper.writeValueAsString(auth),
                    header);
            if (jObject == null)
                throw new Exception("No data");

            int code = jObject.get("code").getAsInt();
            if (code != 200) {
                throw new Exception(String.format("API was returned with code %d", code));
            } else {
                Cookie cookie = new Cookie("token", jObject.get("data").getAsJsonObject().get("token").getAsString());
                cookie.setPath("/");
                // 1-year
                cookie.setMaxAge(365 * 24 * 60 * 60);
                response.addCookie(cookie);

                if (jObject.get("data").getAsJsonObject().get("usertype").getAsInt() == 2)
                    return "redirect:/admin/dashboard";
                else return "redirect:/auth/login";
            }
        } catch (Exception ex) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/auth/register")
    public ModelAndView registerPage() {
        ModelAndView view = new ModelAndView("auth/register");
        // view.addObject(null, view);
        return view;
    }

    @GetMapping("/auth/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        try {
            postRequest("https://localhost:7053/api/account/logout", null, header);
        } catch (Exception ex) {

        } finally {
            Cookie cookie = new Cookie("token", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        ModelAndView view = new ModelAndView("redirect:/auth/login");
        // view.addObject(null, view);
        return view;
    }
}
