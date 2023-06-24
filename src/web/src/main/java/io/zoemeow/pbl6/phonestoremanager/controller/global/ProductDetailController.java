package io.zoemeow.pbl6.phonestoremanager.controller.global;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.zoemeow.pbl6.phonestoremanager.model.bean.Product;
import io.zoemeow.pbl6.phonestoremanager.model.bean.User;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.NoInternetException;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.SessionExpiredException;
import io.zoemeow.pbl6.phonestoremanager.repository.AccountRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.CartRepository;
import io.zoemeow.pbl6.phonestoremanager.repository.ProductRepository;
import io.zoemeow.pbl6.phonestoremanager.utils.RequestAndResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ProductDetailController {
    @Autowired
    AccountRepository _AccountRepository;

    @Autowired
    ProductRepository _ProductRepository;

    @Autowired
    CartRepository _CartRepository;

    @GetMapping("/product")
    public ModelAndView pageCart(
        HttpServletRequest request,
        HttpServletResponse response,
        @ModelAttribute("barMsg") String barMsg,
        Integer id
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));
        
        ModelAndView view = new ModelAndView("/global/product-detail");

        try {
            User user = null;
            try {
                user = _AccountRepository.getUserInformation(header, null);
            } catch (Exception ex) {
                RequestAndResponse.clearCookieHeader(response);
            }
            Integer userId = user == null ? null : user.getId();
            view.addObject("user", user);
            view.addObject("name", user == null ? null : user.getName());
            view.addObject("adminuser", user == null ? false : user.getUserType() != 0);
            view.addObject("barMsg", barMsg.length() == 0 ? null : barMsg);
            view.addObject("baseurl", String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()));
            view.addObject("cartCount", user == null ? 0 : _CartRepository.getAllItemsInCart(header, null, null).size());
            Product product = _ProductRepository.getProductById(header, id);
            view.addObject("product", product);
            double rating = product.getComments().size() == 0 ? 0 : product.getComments().stream().mapToDouble(p -> p.getRating()).average().getAsDouble();
            view.addObject("rating", product.getComments().size() == 0 ? "No ratings due to no comments" : rating);

            view.addObject("loggedIn", user != null ? true : false);
            view.addObject("purchased", (id == null || user == null) ? false : _AccountRepository.getBillSummaries(header).stream().anyMatch(p -> p.getBillDetails().stream().anyMatch(q -> q.getProductId() == id)));
            view.addObject("alreadyCommented", user == null ? false : product.getComments().stream().anyMatch(p -> p.getUserId() == userId));
        } catch (NoInternetException niEx) {

        } catch (SessionExpiredException seEx) {
            RequestAndResponse.clearCookieHeader(response);
        } catch (Exception ex) {
            // TODO: 500 error code here!
        }

        return view;
    }

    @PostMapping("/product/add-review")
    public String actionAddReview(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        @RequestParam("productid") Integer productId,
        @RequestParam("rating") Integer rating,
        @RequestParam("comment") String comment
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        if (productId == null || rating == null || comment == null) {
            redirectAttributes.addFlashAttribute("barMsg", "Missing parameters!");
        }

        try {
            var review = _AccountRepository.addReview(header, productId, rating, comment);
            if (review.getStatusCode() != 200) {
                throw new Exception();
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully added review to product!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "A problem prevent you to add review to this product.");
        }

        return String.format("redirect:/product?id=%d", productId);
    }

    @PostMapping("/product/add-to-cart")
    public String actionAddToCartAndReturn(
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        @RequestParam("productid") Integer productId,
        @RequestParam("count") Integer count
    ) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        if (productId == null || count == null) {
            redirectAttributes.addFlashAttribute("barMsg", "Invalid Product ID or count value!");
        }

        try {
            var review = _CartRepository.addItem(header, productId, count);
            if (review.getStatusCode() != 200) {
                throw new Exception();
            }
            redirectAttributes.addFlashAttribute("barMsg", "Successfully added this product to cart!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "A problem prevent you to add this product to cart.");
        }

        return String.format("redirect:/product?id=%d", productId);
    }
}
