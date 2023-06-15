package io.zoemeow.pbl6.phonestoremanager.controller.admin;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.zoemeow.pbl6.phonestoremanager.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
}
