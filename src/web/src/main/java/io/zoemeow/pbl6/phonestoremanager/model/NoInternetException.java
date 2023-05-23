package io.zoemeow.pbl6.phonestoremanager.model;

public class NoInternetException extends Exception {
    private String message;
    
    public NoInternetException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
