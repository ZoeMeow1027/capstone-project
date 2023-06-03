package io.zoemeow.pbl6.phonestoremanager.model;

public class NoPermissionException extends Exception {
    private String message;
    
    public NoPermissionException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
