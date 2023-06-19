package io.zoemeow.pbl6.phonestoremanager.model.bean;

import com.google.gson.annotations.SerializedName;

public class BillDetails {
    @SerializedName("id")
    Integer id;

    @SerializedName("billid")
    Integer billId;

    @SerializedName("productid")
    Integer productId;

    @SerializedName("count")
    Long count;

    @SerializedName("product")
    Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    
}
