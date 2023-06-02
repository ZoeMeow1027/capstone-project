package io.zoemeow.pbl6.phonestoremanager.model.dto;

import com.google.gson.annotations.SerializedName;

public class AdminUserResetPassDTO {
    private String id;
    
    @SerializedName("newpassword")
    private String password;

    @SerializedName("renewpassword")
    private String repassword;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRepassword() {
        return repassword;
    }
    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public void validate() throws Exception {
        if (password != null && password.length() == 0) password = null;
        if (repassword != null && repassword.length() == 0) repassword = null;
        if (id != null && id.length() == 0) id = null;

        if (password == null || repassword == null || id == null) {
            throw new Exception("Missing parameters!");
        }
        if (password.length() < 6) {
            throw new Exception("Password must be 6 characters or more!");
        }
        if (password.compareTo(repassword) != 0) {
            throw new Exception("Password and Re-enter new password mismatch!");
        }
    }
}
