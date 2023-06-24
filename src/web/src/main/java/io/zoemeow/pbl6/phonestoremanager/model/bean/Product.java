package io.zoemeow.pbl6.phonestoremanager.model.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

    @SerializedName("categoryid")
    Integer categoryid;

    @SerializedName("manufacturerid")
    Integer manufacturerid;

    @SerializedName("inventorycount")
    Integer inventorycount = 0;

    @SerializedName("unit")
    String unit = "item";

    @SerializedName("article")
    String article = null;

    @SerializedName("specification")
    String specification = null;

    @SerializedName("views")
    Long views = 0L;

    @SerializedName("warrantymonth")
    Integer warrantymonth = 12;

    @SerializedName("price")
    Double price;
    
    @SerializedName("datecreated")
    Long datecreated = 0L;

    @SerializedName("datemodified")
    Long datemodified = 0L;

    @SerializedName("showinpage")
    Boolean showinpage = true;

    @SerializedName("manufacturer")
    ProductManufacturer manufacturer;

    @SerializedName("category")
    ProductCategory category;

    @SerializedName("comments")
    List<ProductComment> comments;

    @SerializedName("images")
    List<ProductImageMetadata> images;

    public List<ProductImageMetadata> getImages() {
        return images;
    }

    public void setImages(List<ProductImageMetadata> images) {
        this.images = images;
    }

    public List<ProductComment> getComments() {
        return comments;
    }

    public void setComments(List<ProductComment> comments) {
        this.comments = comments;
    }

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

    public Integer getCategoryId() {
        return categoryid;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryid = categoryId;
    }

    public Integer getManufacturerId() {
        return manufacturerid;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerid = manufacturerId;
    }

    public Integer getInventoryCount() {
        return inventorycount;
    }

    public void setInventoryCount(Integer inventoryCount) {
        this.inventorycount = inventoryCount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Integer getWarrantyMonth() {
        return warrantymonth;
    }

    public void setWarrantyMonth(Integer warrantyMonth) {
        this.warrantymonth = warrantyMonth;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getViews() {
        return views;
    }

    public Long getDateCreated() {
        return datecreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.datecreated = dateCreated;
    }

    public Long getDateModified() {
        return datemodified;
    }

    public void setDateModified(Long dateModified) {
        this.datemodified = dateModified;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String metadata) {
        this.specification = metadata;
    }

    public Boolean getShowInPage() {
        return showinpage;
    }

    public void setShowInPage(Boolean showInPage) {
        this.showinpage = showInPage;
    }

    public ProductManufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ProductManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }
}
