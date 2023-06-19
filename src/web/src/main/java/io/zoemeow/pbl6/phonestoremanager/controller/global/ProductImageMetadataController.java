package io.zoemeow.pbl6.phonestoremanager.controller.global;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.zoemeow.pbl6.phonestoremanager.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductImageMetadataController {
    @Autowired
    ProductRepository _ProductRepository;

    @GetMapping(value = "/products/img/blob", produces = "image/jpeg")
    @ResponseBody
    public Object actionGetProductImageBlob(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer id) throws IOException {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            var data = _ProductRepository.getProductImage(header, id);
            if (data == null)
                throw new Exception("No image here");

            return data;
        } catch (Exception ex) {
            Resource resource = new ClassPathResource("static/img/person-circle.jpg");
            InputStream input = resource.getInputStream();
            return input.readAllBytes();
        }
    }

    @PostMapping(value = "/products/img/delete")
    public String actionDeleteProductImageAndReturn(
            HttpServletRequest request,
            HttpServletResponse response,
            String returnurl,
            @RequestParam("id") Integer id
    ) throws IOException {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            var deleteImage = _ProductRepository.deleteProductImage(header, id);
            if (returnurl.length() == 0) {
                returnurl = null;
            }
            if (returnurl != null) {
                return String.format("redirect:%s", returnurl);
            }

            return "redirect:/admin/products";
        } catch (Exception ex) {
            Resource resource = new ClassPathResource("static/img/person-circle.jpg");
            return "redirect:/admin/products";
        }
    }

    @PostMapping(value = "/products/img/upload")
    public String actionUploadProductImageAndReturn(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            String returnurl,
            @RequestParam("id") Integer id,
            @RequestParam("file") MultipartFile file) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("cookie", request.getHeader("cookie"));

        try {
            var data = _ProductRepository.uploadImage(header, id, file.getResource());
            redirectAttributes.addFlashAttribute("barMsg", "Successfully uploaded product image!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("barMsg", "We ran into a problem prevent you uploading product image!");
        }

        if (returnurl.length() == 0) {
            returnurl = null;
        }
        if (returnurl != null) {
            return String.format("redirect:%s", returnurl);
        }
        return "redirect:/account/profile";
    }
}
