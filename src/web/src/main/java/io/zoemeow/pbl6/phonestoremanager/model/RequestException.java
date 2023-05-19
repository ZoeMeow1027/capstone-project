package io.zoemeow.pbl6.phonestoremanager.model;

public class RequestException extends Exception {
    private String url;
    private int statusCode;
    private String message;

    public RequestException(String url, int statusCode, String message) {
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
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
