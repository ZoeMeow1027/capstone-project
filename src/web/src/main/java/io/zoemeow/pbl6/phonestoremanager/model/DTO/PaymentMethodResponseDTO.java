package io.zoemeow.pbl6.phonestoremanager.model.dto;

import com.google.gson.annotations.SerializedName;

public class PaymentMethodResponseDTO<T> {
    @SerializedName("orderid")
    Integer orderid;

    @SerializedName("data")
    T data;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
