package io.zoemeow.pbl6.phonestoremanager.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

    @SerializedName("categoryid")
    Integer categoryId;

    @SerializedName("manufacturerid")
    Integer manufacturerId;

    @SerializedName("inventorycount")
    Integer inventoryCount = 0;

    @SerializedName("unit")
    String unit = "item";

    @SerializedName("warrantymonth")
    Integer warrantyMonth = 12;

    @SerializedName("price")
    Long price;
    
    @SerializedName("datecreated")
    Long dateCreated = 0L;

    @SerializedName("datemodified")
    Long dateModified = 0L;

    @SerializedName("showinpage")
    Boolean showInPage = true;

    @SerializedName("manufacturer")
    ProductManufacturer manufacturer;

    @SerializedName("category")
    ProductCategory category;

    

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
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(Integer inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getWarrantyMonth() {
        return warrantyMonth;
    }

    public void setWarrantyMonth(Integer warrantyMonth) {
        this.warrantyMonth = warrantyMonth;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
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


    public Boolean getShowInPage() {
        return showInPage;
    }

    public void setShowInPage(Boolean showInPage) {
        this.showInPage = showInPage;
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
