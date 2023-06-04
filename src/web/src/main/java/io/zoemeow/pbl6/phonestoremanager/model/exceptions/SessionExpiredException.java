package io.zoemeow.pbl6.phonestoremanager.model.exceptions;

public class SessionExpiredException extends Exception {
    private String message;
    
    public SessionExpiredException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
