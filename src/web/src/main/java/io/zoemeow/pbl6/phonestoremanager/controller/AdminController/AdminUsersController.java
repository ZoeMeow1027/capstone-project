package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.controller.BasicAPIRequestController;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.UserAddDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@Configuration
@Import({ SimpleDateFormat.class })
public class AdminUsersController extends BasicAPIRequestController {
    @GetMapping("/admin/users")
    public ModelAndView index(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult reqResult = null;
        try {
            ModelAndView view = new ModelAndView("/admin/users");

            reqResult = getRequest("https://localhost:7053/api/account/my", null, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("type", "user");
            parameters.put("includedisabled", "true");
            reqResult = getRequest("https://localhost:7053/api/users", parameters, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("userList", reqResult.getData().get("data").getAsJsonArray());
            } else {
                view.addObject("userList", null);
            }

            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/admin");
            return view;
        }
    }

    @PostMapping("/admin/users/new")
    @ResponseBody
    public Object newUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserAddDTO userAddDTO) {
        try {
            userAddDTO.validate();

            Map<String, String> header = new HashMap<String, String>();
            header.put("content-type", "application/json; charset=UTF-8");
            header.put("cookie", request.getHeader("cookie"));

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode userAdd = mapper.createObjectNode();
            userAdd.put("username", userAddDTO.getUsername());
            userAdd.put("password", userAddDTO.getPassword());
            userAdd.put("name", userAddDTO.getName());
            if (userAddDTO.getEmail() != null)
                userAdd.put("email", userAddDTO.getEmail());
            if (userAddDTO.getPhone() != null)
                userAdd.put("phone", userAddDTO.getPhone());
            ObjectNode bodyRoot = mapper.createObjectNode();
            bodyRoot.put("type", userAddDTO.getType());
            bodyRoot.put("action", "add");
            bodyRoot.set("data", userAdd);

            String putData = mapper.writeValueAsString(bodyRoot);
            RequestResult reqResult = postRequest("https://localhost:7053/api/users", null, header,
                    putData);
            return (new Gson()).toJson(reqResult);
            // return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult reqResult = new RequestResult(false, null, null, ex.getMessage());
            return (new Gson()).toJson(reqResult);
        }
    }
}
