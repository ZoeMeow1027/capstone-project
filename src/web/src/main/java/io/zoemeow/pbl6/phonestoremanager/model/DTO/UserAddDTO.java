package io.zoemeow.pbl6.phonestoremanager.model.DTO;

public class UserAddDTO {
    private String type;
    private String username;
    private String name;
    private String password;
    private String email;
    private String phone;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void validate() throws Exception {
        if (type == null || username == null || password == null || name == null) {
            throw new Exception("Missing parameters!");
        }
        if (!type.toLowerCase().equals("user") && !type.toLowerCase().equals("useraddress")) {
            throw new Exception("Invalid 'type' value!");
        }
        if (name.trim().length() < 3) {
            throw new Exception("'name' must be 6 characters or more!");
        }
        if (password.length() < 6) {
            throw new Exception("'password' must be 6 characters or more!");
        }
        if (username.length() < 5) {
            throw new Exception("'username' must be 5 characters or more!");
        }
        if (phone.length() == 0) phone = null;
        if (email.length() == 0) email = null;
    }
}
