package io.zoemeow.pbl6.phonestoremanager.model;

public class RequestException extends Exception {
    private String url;
    private Integer statusCode;
    private String message;

    public RequestException(String url, Integer statusCode, String message) {
        this.url = url;
        this.statusCode = statusCode;
        this.message = message;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
