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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.AdminUserAddDTO;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.AdminUserResetPassDTO;
import io.zoemeow.pbl6.phonestoremanager.model.DTO.AdminUserToggleDTO;
import io.zoemeow.pbl6.phonestoremanager.repository.AdminUserRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.AuthRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.RequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@Configuration
@Import({ SimpleDateFormat.class })
public class AdminUsersController extends RequestRepository {
    AdminUserRepository _AdminUserRepository;
    AuthRepository _AuthRepository;

    public AdminUsersController() {
        _AdminUserRepository = new AdminUserRepository();
        _AuthRepository = new AuthRepository();
    }

    @GetMapping("/admin/users")
    public ModelAndView pageViewAllUsers(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/users");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            reqResult = _AdminUserRepository.getAllUsers(header, true);
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

            view = new ModelAndView("/admin/userToggle");
            view.addObject("action", enabled == 0 ? "disable" : "enable");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            reqResult = _AdminUserRepository.getUser(header, id);
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
            @RequestBody AdminUserToggleDTO userEnableDTO) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        RequestResult<JsonObject> reqResult;
        try {
            reqResult = _AdminUserRepository.toggleUser(header, userEnableDTO);
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
            view = new ModelAndView("/admin/userAdd.html");
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
            @RequestBody AdminUserAddDTO userAddDTO) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            userAddDTO.validate("add");

            RequestResult<JsonObject> reqResult = _AdminUserRepository.addUser(header, userAddDTO);
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
            view = new ModelAndView("/admin/userAdd.html");
            view.addObject("action", "edit");
            view.addObject("id", id);

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            reqResult = _AdminUserRepository.getUser(header, id);
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
            @RequestBody AdminUserAddDTO userAddDTO) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            userAddDTO.validate("update");

            RequestResult<JsonObject> reqResult = _AdminUserRepository.editUser(header, userAddDTO);
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
            view = new ModelAndView("/admin/userResetPassword.html");
            view.addObject("id", id);

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            reqResult = _AdminUserRepository.getUser(header, id);
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
