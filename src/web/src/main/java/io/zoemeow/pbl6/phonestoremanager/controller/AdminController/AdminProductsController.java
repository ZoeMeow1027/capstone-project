package io.zoemeow.pbl6.phonestoremanager.controller.AdminController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.zoemeow.pbl6.phonestoremanager.model.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.Product;
import io.zoemeow.pbl6.phonestoremanager.model.ProductCategory;
import io.zoemeow.pbl6.phonestoremanager.model.ProductManufacturer;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.repository.AdminProductRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.AuthRepository;
import io.zoemeow.pbl6.phonestoremanager.utils.Validate;
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
            HttpServletResponse response,
            String query,
            Boolean includehidden) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/products/index");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("productList", _AdminProductRepository.getProducts(header, query, includehidden == null ? false : includehidden));
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
            view = new ModelAndView("/admin/products/add");
            view.addObject("action", "add");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("productCategoryList", _AdminProductRepository.getProductCategories(header, null, false));
            view.addObject("productManufacturerList", _AdminProductRepository.getProductManufacturers(header, null, false));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/products/add")
    @ResponseBody
    public Object actionAddProduct(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Product product) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            Validate.validateProduct(product, "add");

            RequestResult<JsonObject> reqResult = _AdminProductRepository.addProduct(header, product);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult<JsonObject> reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }

    @GetMapping("/admin/products/update")
    public ModelAndView pageUpdateProduct(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/products/add");
            view.addObject("action", "edit");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("productCategoryList", _AdminProductRepository.getProductCategories(header, null, false));
            view.addObject("productManufacturerList", _AdminProductRepository.getProductManufacturers(header, null, false));
            view.addObject("product", _AdminProductRepository.getProductById(header, id));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/products/update")
    @ResponseBody
    public Object actionUpdateProduct(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody Product product) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            Validate.validateProduct(product, "update");

            RequestResult<JsonObject> reqResult = _AdminProductRepository.editProduct(header, product);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult<JsonObject> reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }

    @GetMapping("/admin/products/categories")
    public ModelAndView pageProductCategories(
            HttpServletRequest request,
            HttpServletResponse response,
            String query,
            Boolean includehidden) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/productCategory/index");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("productCategoryList", _AdminProductRepository.getProductCategories(header, null, false));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @GetMapping("/admin/products/categories/add")
    public ModelAndView pageAddProductCategory(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/productCategory/add");
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

    @PostMapping("/admin/products/categories/add")
    @ResponseBody
    public Object actionAddProductCategory(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody ProductCategory productCategory) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            Validate.validateProductCategory(productCategory, "add");

            RequestResult<JsonObject> reqResult = _AdminProductRepository.addProductCategory(header, productCategory);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult<JsonObject> reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }

    @GetMapping("/admin/products/categories/update")
    public ModelAndView pageUpdateProductCategory(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/productCategory/add");
            view.addObject("action", "edit");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("productCategory", _AdminProductRepository.getProductCategoryById(header, id));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/products/categories/update")
    @ResponseBody
    public Object actionUpdateProductCategory(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody ProductCategory productCategory) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            Validate.validateProductCategory(productCategory, "update");

            RequestResult<JsonObject> reqResult = _AdminProductRepository.updateProductCategory(header, productCategory);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult<JsonObject> reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }

    @GetMapping("/admin/products/manufacturers")
    public ModelAndView pageProductManufacturers(
            HttpServletRequest request,
            HttpServletResponse response,
            String query,
            Boolean includehidden) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/productManufacturer/index");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("productManufacturerList", _AdminProductRepository.getProductManufacturers(header, null, false));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @GetMapping("/admin/products/manufacturers/add")
    public ModelAndView pageAddProductManufacturer(
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/productManufacturer/add");
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

    @PostMapping("/admin/products/manufacturers/add")
    @ResponseBody
    public Object actionAddProductManufacturer(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody ProductManufacturer productManufacturer) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            Validate.validateProductManufacturer(productManufacturer, "add");

            RequestResult<JsonObject> reqResult = _AdminProductRepository.addProductManufacturer(header, productManufacturer);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult<JsonObject> reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }

    @GetMapping("/admin/products/manufacturers/update")
    public ModelAndView pageUpdateProductManufacturer(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/productManufacturer/add");
            view.addObject("action", "edit");

            RequestResult<JsonObject> reqResult = _AuthRepository.getUserInformation(header,
                    new ArrayList<Integer>(Arrays.asList(2)));
            if (reqResult.getData() != null) {
                view.addObject("name", reqResult.getData().get("data").getAsJsonObject().get("name").getAsString());
            } else {
                view.addObject("name", "(Unknown)");
            }

            view.addObject("productManufacturer", _AdminProductRepository.getProductManufacturerById(header, id));
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/products/manufacturers/update")
    @ResponseBody
    public Object actionUpdateProductManufacturer(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody ProductManufacturer productManufacturer) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            Validate.validateProductManufacturer(productManufacturer, "update");

            RequestResult<JsonObject> reqResult = _AdminProductRepository.updateProductManufacturer(header, productManufacturer);
            return (new Gson().toJson(reqResult));
        } catch (Exception ex) {
            RequestResult<JsonObject> reqResult = new RequestResult<JsonObject>(false, null, null, ex.getMessage());
            return (new Gson().toJson(reqResult));
        }
    }
}
