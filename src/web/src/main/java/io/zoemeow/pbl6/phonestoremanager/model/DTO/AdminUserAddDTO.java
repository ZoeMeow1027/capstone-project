package io.zoemeow.pbl6.phonestoremanager.model.DTO;

import java.util.Arrays;

public class AdminUserAddDTO {
    private Integer id;
    private String type;
    private String username;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String usertype;

    public String getUsertype() {
        return usertype;
    }
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
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
    public void validate(String action) throws Exception {
        if (type != null && type.length() == 0) type = null;
        if (username != null && username.length() == 0) username = null;
        if (password != null && password.length() == 0) password = null;
        if (name != null && name.length() == 0) name = null;
        if (phone != null && phone.length() == 0) phone = null;
        if (email != null && email.length() == 0) email = null;
        if (usertype != null && usertype.length() == 0) usertype = null;

        if (action == "add") {
            if (type == null || username == null || password == null || name == null || usertype == null) {
                throw new Exception("Missing parameters!");
            }
            if (password.length() < 6) {
                throw new Exception("Password must be 6 characters or more!");
            }
            if (username.length() < 5) {
                throw new Exception("Username must be 5 characters or more!");
            }
        } else if (action == "update") {
            if (type == null || username == null || id == null || name == null || usertype == null) {
                throw new Exception("Missing parameters!");
            }
        } else
            throw new Exception("Invalid 'action' value (from server!)");
        if (!type.toLowerCase().equals("user")) {
            throw new Exception("Invalid 'type' value!");
        }
        if (name.trim().length() < 3) {
            throw new Exception("Name must be 6 characters or more!");
        }
        if (phone != null) {
            if (!phone.startsWith("0") || !(phone.length() >= 10 && phone.length() <= 11)) {
                throw new Exception("Invalid 'phone' value!");
            }
        }
        if (!Arrays.asList("0", "1", "2").contains(usertype)) {
            throw new Exception("Invalid 'usertype' value!");
        }
    }
}
