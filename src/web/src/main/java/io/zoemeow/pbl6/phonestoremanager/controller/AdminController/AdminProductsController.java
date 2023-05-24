package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.repository.AdminProductRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.AuthRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminProductsController {
    AdminProductRepository _AdminProductRepository;
    AuthRepository _AuthRepository;

    public AdminProductsController() {
        _AdminProductRepository = new AdminProductRepository();
        _AuthRepository = new AuthRepository();
    }

    @GetMapping("/admin/products")
    public ModelAndView pageProducts(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/products");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            reqResult = _AdminProductRepository.getAllProducts(header, false);
            if (!reqResult.getIsSuccessfulRequest()) {
                throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("productList", reqResult.getData().get("data").getAsJsonArray());
            } else {
                view.addObject("productList", null);
            }
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @GetMapping("/admin/products/categories")
    public ModelAndView pageProductCategories(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/productCategory");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            reqResult = _AdminProductRepository.getAllProductCategories(header, false);
            if (!reqResult.getIsSuccessfulRequest()) {
                throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("productCategoryList", reqResult.getData().get("data").getAsJsonArray());
            } else {
                view.addObject("productCategoryList", null);
            }
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @GetMapping("/admin/products/manufacturers")
    public ModelAndView pageProductManufacturers(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/productManufacturer");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            reqResult = _AdminProductRepository.getAllProductManufacturers(header, false);
            if (!reqResult.getIsSuccessfulRequest()) {
                throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("productManufacturerList", reqResult.getData().get("data").getAsJsonArray());
            } else {
                view.addObject("productManufacturerList", null);
            }
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @GetMapping("/admin/products/add")
    public ModelAndView pageAddProduct(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/productAdd");
            view.addObject("action", "add");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            reqResult = _AdminProductRepository.getAllProductCategories(header, false);
            if (!reqResult.getIsSuccessfulRequest()) {
                throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("productCategoryList", reqResult.getData().get("data").getAsJsonArray());
            } else {
                view.addObject("productCategoryList", null);
            }

            reqResult = _AdminProductRepository.getAllProductManufacturers(header, false);
            if (!reqResult.getIsSuccessfulRequest()) {
                throw new NoInternetException("Cannot fetch data from API. Wait a few minutes, and try again.");
            }
            if (reqResult.getStatusCode() != 200) {
                throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
            }
            if (reqResult.getData() != null) {
                view.addObject("productManufacturerList", reqResult.getData().get("data").getAsJsonArray());
            } else {
                view.addObject("productManufacturerList", null);
            }
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    // @PostMapping("/admin/products/add")
    // public ModelAndView actionAddProduct(
    //         HttpServletRequest request,
    //         HttpServletResponse response) {
        
    // }
}
