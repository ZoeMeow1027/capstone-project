package io.zoemeow.pbl6.phonestoremanager.repository;

import java.util.List;
import java.util.Map;

import io.zoemeow.pbl6.phonestoremanager.model.bean.Product;

public interface FeaturedRepository {
    public List<Product> getNewProduct(Map<String, String> headers, Integer days, Integer limit) throws Exception;

    public List<Product> getMostedViewProduct(Map<String, String> headers, Integer limit) throws Exception;
}
