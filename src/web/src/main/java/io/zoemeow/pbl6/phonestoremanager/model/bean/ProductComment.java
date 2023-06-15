package io.zoemeow.pbl6.phonestoremanager.model.bean;

import com.google.gson.annotations.SerializedName;

public class ProductComment {
    @SerializedName("id")
    Integer id;
    @SerializedName("userid")
    Integer userId;
    @SerializedName("productid")
    Integer productId;
    @SerializedName("comment")
    String comment;
    @SerializedName("rating")
    Float rating;
    @SerializedName("disabled")
    Boolean disabled;
    @SerializedName("disabledreason")
    String disabledReason;
    @SerializedName("user")
    User user;

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
    public Integer getProductId() {
        return productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Float getRating() {
        return rating;
    }
    public void setRating(Float rating) {
        this.rating = rating;
    }
    public Boolean getDisabled() {
        return disabled;
    }
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    public String getDisabledReason() {
        return disabledReason;
    }
    public void setDisabledReason(String disabledReason) {
        this.disabledReason = disabledReason;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    
}
