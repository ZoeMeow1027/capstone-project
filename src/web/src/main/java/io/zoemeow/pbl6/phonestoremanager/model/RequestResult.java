package io.zoemeow.pbl6.phonestoremanager.model;

import com.google.gson.JsonObject;

public class RequestResult {
    private JsonObject data = null;
    private Integer statusCode = null;
    private Boolean isSuccessfulRequest = false;
    private String message = "";

    public RequestResult() {}
    
    public RequestResult(Boolean isSuccessfulRequest, Integer statusCode, JsonObject data, String message) {
        this.data = data;
        this.statusCode = statusCode;
        this.isSuccessfulRequest = isSuccessfulRequest;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsSuccessfulRequest() {
        return isSuccessfulRequest;
    }

    public void setIsSuccessfulRequest(Boolean isSuccessfulRequest) {
        this.isSuccessfulRequest = isSuccessfulRequest;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
