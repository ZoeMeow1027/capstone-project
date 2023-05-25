package io.zoemeow.pbl6.phonestoremanager.model;

import com.google.gson.annotations.SerializedName;

public class ProductManufacturer {
    @SerializedName("id")
    Integer id;
    @SerializedName("name")
    String name;
    @SerializedName("showinpage")
    Boolean showInPage = true;
    
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
    public Boolean getShowInPage() {
        return showInPage;
    }
    public void setShowInPage(Boolean showInPage) {
        this.showInPage = showInPage;
    }
}
