package io.zoemeow.pbl6.phonestoremanager.model.bean;

import com.google.gson.annotations.SerializedName;

public class ProductImageMetadata {
    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("productid")
    Integer productId;

    @SerializedName("datecreated")
    Long dateCreated;

    @SerializedName("datemodified")
    Long dateModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getDateModified() {
        return dateModified;
    }

    public void setDateModified(Long dateModified) {
        this.dateModified = dateModified;
    }

    
}
