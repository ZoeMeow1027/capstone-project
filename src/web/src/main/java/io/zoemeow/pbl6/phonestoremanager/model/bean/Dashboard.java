package io.zoemeow.pbl6.phonestoremanager.model.bean;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class Dashboard {
    @SerializedName("deliveryremaining")
    Integer remainingDeliveries;

    @SerializedName("hotsellingproducts")
    JsonArray hotSellingProducts;

    @SerializedName("registeredmembers")
    Integer registeredMembers;

    public Integer getRemainingDeliveries() {
        return remainingDeliveries;
    }

    public void setRemainingDeliveries(Integer remainingDeliveries) {
        this.remainingDeliveries = remainingDeliveries;
    }

    public JsonArray getHotSellingProducts() {
        return hotSellingProducts;
    }

    public void setHotSellingProducts(JsonArray hotSellingProducts) {
        this.hotSellingProducts = hotSellingProducts;
    }

    public Integer getRegisteredMembers() {
        return registeredMembers;
    }

    public void setRegisteredMembers(Integer registeredMembers) {
        this.registeredMembers = registeredMembers;
    }

    
}
