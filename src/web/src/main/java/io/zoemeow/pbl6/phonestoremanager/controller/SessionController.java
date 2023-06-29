package io.zoemeow.pbl6.phonestoremanager.controller;

import java.util.HashMap;
import java.util.Map;

import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoPermissionException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;

public class SessionController {
    @Autowired
    AccountRepository _AccountRepository;

    public User getUserInformation(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = null;
        try {
            user = _AccountRepository.getUserInformation(getCookieHeader(request), null);
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            clearCookieHeader(response);
        } catch (NoPermissionException npEx) {

        } catch (Exception ex) {

        }
        return user;
    }

    public static Map<String, String> getCookieHeader(HttpServletRequest request) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        return header;
    }

    public static void setCookieHeader(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        // 1-year
        cookie.setMaxAge(365 * 24 * 60 * 60);
        response.addCookie(cookie);
    }

    public static void clearCookieHeader(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
