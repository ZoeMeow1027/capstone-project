package io.zoemeow.pbl6.phonestoremanager.model.dto;

import com.google.gson.annotations.SerializedName;

public class PayPalResponseDataDTO {
    @SerializedName("billingToken")
    String billingToken;

    @SerializedName("facilitatorAccessToken")
    String facilitatorAccessToken;

    @SerializedName("orderID")
    String orderID;

    @SerializedName("payerID")
    String payerID;

    @SerializedName("paymentID")
    String paymentID;

    @SerializedName("paymentSource")
    String paymentSource;

    public String getBillingToken() {
        return billingToken;
    }

    public void setBillingToken(String billingToken) {
        this.billingToken = billingToken;
    }

    public String getFacilitatorAccessToken() {
        return facilitatorAccessToken;
    }

    public void setFacilitatorAccessToken(String facilitatorAccessToken) {
        this.facilitatorAccessToken = facilitatorAccessToken;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPayerID() {
        return payerID;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }
}
