package io.zoemeow.pbl6.phonestoremanager.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.zoemeow.pbl6.phonestoremanager.model.bean.Product;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductCategory;
import io.zoemeow.pbl6.phonestoremanager.model.bean.ProductManufacturer;
import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.ProductRepository;
import io.zoemeow.pbl6.phonestoremanager.utils.Validate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminProductsController {
    @Autowired
    ProductRepository _AdminProductRepository;
    
    @Autowired
    AccountRepository _AccountRepository;

    private final Integer TAB_BASIC = 0;
    private final Integer TAB_ARTICLE = 1;
    private final Integer TAB_SPECIFICATION = 2;
    private final Integer TAB_IMAGE = 3;

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

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

            view.addObject("productList", _AdminProductRepository.getProducts(header, query, includehidden == null ? false : includehidden));
            view.addObject("baseurl",
                    String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));
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

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

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

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

            view.addObject("productCategoryList", _AdminProductRepository.getProductCategories(header, null, false));
            view.addObject("productManufacturerList", _AdminProductRepository.getProductManufacturers(header, null, false));
            view.addObject("product", _AdminProductRepository.getProductById(header, id));
            view.addObject("tab", TAB_BASIC);
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @GetMapping("/admin/products/article")
    public ModelAndView pageUpdateProductArticle(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg,
            Integer id) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/products/article");
            view.addObject("action", "edit");
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

            var data = _AdminProductRepository.getProductById(header, id);
            view.addObject("product", data);
            view.addObject("tab", TAB_ARTICLE);
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/products/article")
    public String actionUpdateProductArticle(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id,
            RedirectAttributes redirectAttributes,
            String articlecontent) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            var product = _AdminProductRepository.getProductById(header, id);
            product.setArticle(articlecontent);
            RequestResult<JsonObject> reqResult = _AdminProductRepository.editProduct(header, product);
            if (reqResult.getStatusCode() != 200) {
                throw new Exception();
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully updated product article.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "We ran into a problem prevent you updating product article.");
        }
        return String.format("redirect:/admin/products/article?id=%d", id);
    }

    @GetMapping("/admin/products/specifications")
    public ModelAndView pageUpdateProductSpecification(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("barMsg") String barMsg,
            Integer id) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/products/specification");
            view.addObject("action", "edit");
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

            var data = _AdminProductRepository.getProductById(header, id);
            view.addObject("product", data);
            view.addObject("tab", TAB_SPECIFICATION);
        } catch (NoInternetException niEx) {
            // TODO: No internet connection
        } catch (Exception ex) {
            view = new ModelAndView("redirect:/admin");
        }
        return view;
    }

    @PostMapping("/admin/products/specifications")
    public String actionUpdateProductSpecification(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            Integer id,
            String speccontent) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            var product = _AdminProductRepository.getProductById(header, id);
            product.setSpecification(speccontent);
            RequestResult<JsonObject> reqResult = _AdminProductRepository.editProduct(header, product);
            if (reqResult.getStatusCode() != 200) {
                throw new Exception();
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully updated product specification.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "We ran into a problem prevent you updating product specification.");
        }
        return String.format("redirect:/admin/products/specifications?id=%d", id);
    }

    @GetMapping("/admin/products/images")
    public ModelAndView pageProductImages(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        ModelAndView view = null;
        try {
            view = new ModelAndView("/admin/products/images");
            view.addObject("action", "edit");

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());
            view.addObject("baseurl",
                    String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));

            view.addObject("product", _AdminProductRepository.getProductById(header, id));
            view.addObject("tab", TAB_IMAGE);
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

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

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

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());
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

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

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

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

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

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());
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

            User user = _AccountRepository.getUserInformation(header, new ArrayList<Integer>(Arrays.asList(2)));
            view.addObject("name", user == null ? null : user.getName());

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
