package io.zoemeow.pbl6.phonestoremanager.model.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BillSummary {
    @SerializedName("id")
    Integer id;

    @SerializedName("userid")
    Integer userId;

    @SerializedName("recipient")
    String recipient;

    @SerializedName("recipientaddress")
    String recipientAddress;

    @SerializedName("recipientphone")
    String recipientPhone;

    @SerializedName("datecreated")
    Long dateCreated;

    @SerializedName("datecompleted")
    Long dateCompleted;

    @SerializedName("shippingprice")
    Double shippingPrice;

    @SerializedName("vat")
    Double vat;

    @SerializedName("discount")
    Double discount;

    @SerializedName("totalprice")
    Double totalPrice;

    @SerializedName("status")
    Integer deliverStatus;

    @SerializedName("statusaddress")
    String statusAddress;

    @SerializedName("statusadditional")
    String statusAdditional;

    @SerializedName("paymentmethod")
    Integer paymentMethod;

    @SerializedName("paymentmethodname")
    String paymentMethodName;

    @SerializedName("paymentid")
    String paymentId;

    @SerializedName("paymentcompleted")
    Boolean paymentCompleted;

    @SerializedName("usermessage")
    String userMessage;

    @SerializedName("user")
    User user;

    @SerializedName("billdetails")
    List<BillDetails> billDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Long dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(Double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(Integer deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getStatusAddress() {
        return statusAddress;
    }

    public void setStatusAddress(String statusAddress) {
        this.statusAddress = statusAddress;
    }

    public String getStatusAdditional() {
        return statusAdditional;
    }

    public void setStatusAdditional(String statusAdditional) {
        this.statusAdditional = statusAdditional;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Boolean getPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(Boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<BillDetails> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetails> billDetails) {
        this.billDetails = billDetails;
    }

    
}
