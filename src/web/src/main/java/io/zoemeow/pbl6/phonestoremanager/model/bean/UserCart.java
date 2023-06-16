package io.zoemeow.pbl6.phonestoremanager.model.bean;

import com.google.gson.annotations.SerializedName;

public class UserCart {
    @SerializedName("id")
    Integer id;

    @SerializedName("userid")
    Integer userId;

    @SerializedName("productid")
    Integer productId;

    @SerializedName("count")
    Integer count;

    @SerializedName("product")
    Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    
}
