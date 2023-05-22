package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import io.zoemeow.pbl6.phonestoremanager.controller.BasicAPIRequestController;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.UserAddDTO;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.UserResetPassDTO;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.UserToggleEnableDTO;
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

            reqResult = getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
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

    @GetMapping("/admin/users/toggle")
    public ModelAndView pageToggleUser(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id,
            Integer enabled) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("content-type", "application/json; charset=UTF-8");
        header.put("cookie", request.getHeader("cookie"));

        RequestResult reqResult = null;
        try {
            if (enabled == null)
                throw new Exception("Invalid 'enabled' value!");
            if (!(enabled == 0 || enabled == 1))
                throw new Exception("Invalid 'enabled' value!");

            ModelAndView view = new ModelAndView("/admin/userToggle");
            view.addObject("action", enabled == 0 ? "disable" : "enable");

            reqResult = getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("type", "user");
            parameters.put("includedisabled", "true");
            parameters.put("id", id.toString());
            reqResult = getRequest("https://localhost:7053/api/users", parameters, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("user", reqResult.getData().get("data").getAsJsonObject());
            } else {
                view.addObject("user", null);
            }

            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/admin");
            return view;
        }
    }

    @PostMapping("/admin/users/toggle")
    @ResponseBody
    public Object actionToggleUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserToggleEnableDTO userEnableDTO) {
        RequestResult reqResult = null;
        try {
            Map<String, String> header = new HashMap<String, String>();
            header.put("content-type", "application/json; charset=UTF-8");
            header.put("cookie", request.getHeader("cookie"));

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode userToggle = mapper.createObjectNode();
            userToggle.put("id", userEnableDTO.getId());
            ObjectNode bodyRoot = mapper.createObjectNode();
            bodyRoot.put("type", "user");
            bodyRoot.put("action", userEnableDTO.getEnabled().compareTo("1") == 0 ? "enable" : "disable");
            bodyRoot.set("data", userToggle);

            String putData = mapper.writeValueAsString(bodyRoot);
            reqResult = postRequest("https://localhost:7053/api/users", null, header,
                    putData);
        } catch (Exception ex) {
            reqResult = new RequestResult(false, null, null, ex.getMessage());
        }

        return (new Gson().toJson(reqResult));
    }

    @GetMapping("/admin/users/add")
    public ModelAndView pageAddUser(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult reqResult = null;
        try {
            ModelAndView view = new ModelAndView("/admin/userAdd.html");
            view.addObject("action", "add");

            reqResult = getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/admin");
            return view;
        }
    }

    @PostMapping("/admin/users/add")
    @ResponseBody
    public Object actionAddUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserAddDTO userAddDTO) {
        try {
            userAddDTO.validate("add");

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
            userAdd.put("usertype", userAddDTO.getUsertype());
            ObjectNode bodyRoot = mapper.createObjectNode();
            bodyRoot.put("type", "user");
            bodyRoot.put("action", "add");
            bodyRoot.set("data", userAdd);

            String putData = mapper.writeValueAsString(bodyRoot);
            RequestResult reqResult = postRequest("https://localhost:7053/api/users", null, header,
                    putData);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult reqResult = new RequestResult(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }

    @GetMapping("/admin/users/update")
    public ModelAndView pageEditUser(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult reqResult = null;
        try {
            ModelAndView view = new ModelAndView("/admin/userAdd.html");
            view.addObject("action", "edit");
            view.addObject("id", id);

            reqResult = getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("type", "user");
            parameters.put("includedisabled", "true");
            parameters.put("id", id.toString());
            reqResult = getRequest("https://localhost:7053/api/users", parameters, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("user", reqResult.getData().get("data").getAsJsonObject());
            } else {
                view.addObject("user", null);
            }

            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/admin");
            return view;
        }
    }

    @PostMapping("/admin/users/update")
    @ResponseBody
    public Object actionEditUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserAddDTO userAddDTO) {
        try {
            userAddDTO.validate("update");

            Map<String, String> header = new HashMap<String, String>();
            header.put("content-type", "application/json; charset=UTF-8");
            header.put("cookie", request.getHeader("cookie"));

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode userAdd = mapper.createObjectNode();
            userAdd.put("id", userAddDTO.getId());
            userAdd.put("username", userAddDTO.getUsername());
            userAdd.put("name", userAddDTO.getName());
            if (userAddDTO.getEmail() != null)
                userAdd.put("email", userAddDTO.getEmail());
            if (userAddDTO.getPhone() != null)
                userAdd.put("phone", userAddDTO.getPhone());
            userAdd.put("usertype", userAddDTO.getUsertype());
            ObjectNode bodyRoot = mapper.createObjectNode();
            bodyRoot.put("type", "user");
            bodyRoot.put("action", "update");
            bodyRoot.set("data", userAdd);

            String putData = mapper.writeValueAsString(bodyRoot);
            RequestResult reqResult = postRequest("https://localhost:7053/api/users", null, header,
                    putData);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult reqResult = new RequestResult(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }

    @GetMapping("/admin/users/resetpassword")
    public ModelAndView pageResetPassword(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult reqResult = null;
        try {
            ModelAndView view = new ModelAndView("/admin/userResetPassword.html");
            view.addObject("id", id);

            reqResult = getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("type", "user");
            parameters.put("includedisabled", "true");
            parameters.put("id", id.toString());
            reqResult = getRequest("https://localhost:7053/api/users", parameters, header);
            if (!reqResult.getIsSuccessfulRequest()) {
                // TODO: Check if not successful request here!
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("user", reqResult.getData().get("data").getAsJsonObject());
            } else {
                view.addObject("user", null);
            }

            return view;
        } catch (Exception ex) {
            ModelAndView view = new ModelAndView("redirect:/admin");
            return view;
        }
    }

    @PostMapping("/admin/users/resetpassword")
    @ResponseBody
    public Object actionResetPassword(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserResetPassDTO userResetPassDTO) {
        try {
            userResetPassDTO.validate();

            Map<String, String> header = new HashMap<String, String>();
            header.put("content-type", "application/json; charset=UTF-8");
            header.put("cookie", request.getHeader("cookie"));

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode userResetPass = mapper.createObjectNode();
            userResetPass.put("id", userResetPassDTO.getId());
            userResetPass.put("password", userResetPassDTO.getPassword());
            ObjectNode bodyRoot = mapper.createObjectNode();
            bodyRoot.put("type", "user");
            bodyRoot.put("action", "changepassword");
            bodyRoot.set("data", userResetPass);

            String putData = mapper.writeValueAsString(bodyRoot);
            RequestResult reqResult = postRequest("https://localhost:7053/api/users", null, header,
                    putData);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult reqResult = new RequestResult(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }
}
