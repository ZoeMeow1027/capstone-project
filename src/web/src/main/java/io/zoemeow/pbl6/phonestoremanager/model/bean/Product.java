package io.zoemeow.pbl6.phonestoremanager.model.bean;

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

    @SerializedName("warrantymonth")
    Integer warrantymonth = 12;

    @SerializedName("price")
    Long price;
    
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

    public Integer getWarrantyMonth() {
        return warrantymonth;
    }

    public void setWarrantyMonth(Integer warrantyMonth) {
        this.warrantymonth = warrantyMonth;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
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
