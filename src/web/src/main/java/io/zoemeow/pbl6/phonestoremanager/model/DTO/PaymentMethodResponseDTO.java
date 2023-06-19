package io.zoemeow.pbl6.phonestoremanager.model.dto;

import com.google.gson.annotations.SerializedName;

public class PaymentMethodResponseDTO {
    @SerializedName("orderid")
    Integer orderid;

    @SerializedName("data")
    PayPalResponseDataDTO data;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public PayPalResponseDataDTO getData() {
        return data;
    }

    public void setData(PayPalResponseDataDTO data) {
        this.data = data;
    }
}
