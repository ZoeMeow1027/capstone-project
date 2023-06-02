package io.zoemeow.pbl6.phonestoremanager.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.zoemeow.pbl6.phonestoremanager.model.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.dto.AdminUserResetPassDTO;
import io.zoemeow.pbl6.phonestoremanager.model.dto.AdminUserToggleDTO;
import io.zoemeow.pbl6.phonestoremanager.repository.AdminUserRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.AuthRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.RequestRepository;
import io.zoemeow.pbl6.phonestoremanager.utils.Validate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminUsersController extends RequestRepository {
    @Autowired
    AdminUserRepository _AdminUserRepository;

    @Autowired
    AuthRepository _AuthRepository;

    @GetMapping("/admin/users")
    public ModelAndView pageViewAllUsers(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/users/index");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("userList", _AdminUserRepository.getAllUsers(header, false));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
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

        ModelAndView view = null;
        try {
            if (enabled == null)
                throw new Exception("Invalid 'enabled' value!");
            if (!(enabled == 0 || enabled == 1))
                throw new Exception("Invalid 'enabled' value!");

            view = new ModelAndView("/admin/users/toggle");
            view.addObject("action", enabled == 0 ? "disable" : "enable");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("user", _AdminUserRepository.getUser(header, id));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/users/toggle")
    @ResponseBody
    public Object actionToggleUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AdminUserToggleDTO userToggleDTO) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult<JsonObject> reqResult;
        try {
            reqResult = _AdminUserRepository.toggleUser(header, userToggleDTO);
        } catch (Exception ex) {
            reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
        }

        return (new Gson().toJson(reqResult));
    }

    @GetMapping("/admin/users/add")
    public ModelAndView pageAddUser(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/users/add");
            view.addObject("action", "add");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/users/add")
    @ResponseBody
    public Object actionAddUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody User user) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            Validate.validateUser(user, "add");

            RequestResult<JsonObject> reqResult = _AdminUserRepository.addUser(header, user);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult<JsonObject> reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
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

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/users/add");
            view.addObject("action", "edit");
            view.addObject("id", id);

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("user", _AdminUserRepository.getUser(header, id));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/users/update")
    @ResponseBody
    public Object actionEditUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody User user) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            Validate.validateUser(user, "update");

            RequestResult<JsonObject> reqResult = _AdminUserRepository.editUser(header, user);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult<JsonObject> reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
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

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/users/resetPassword");
            view.addObject("id", id);

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("user", _AdminUserRepository.getUser(header, id));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/users/resetpassword")
    @ResponseBody
    public Object actionResetPassword(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AdminUserResetPassDTO userResetPassDTO) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            userResetPassDTO.validate();

            RequestResult<JsonObject> reqResult = _AdminUserRepository.resetPassword(header, userResetPassDTO);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult<JsonObject> reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }
}
