package io.zoemeow.pbl6.phonestoremanager.model;

public class RequestResult<T> {
    private T data = null;
    private Integer statusCode = null;
    private Boolean isSuccessfulRequest = false;
    private String message = "";

    public RequestResult() {}
    
    public RequestResult(Boolean isSuccessfulRequest, Integer statusCode, T data, String message) {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
