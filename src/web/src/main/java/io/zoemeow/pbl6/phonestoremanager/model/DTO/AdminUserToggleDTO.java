package io.zoemeow.pbl6.phonestoremanager.model.dto;

public class AdminUserToggleDTO {
    private String enabled;
    private String id;
    public String getEnabled() {
        return enabled;
    }
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void validate() throws Exception {
        if (id == null || enabled == null) {
            throw new Exception("Missing parameters!");
        }
        if (!(enabled == "1" || enabled == "0")) {
            throw new Exception("Invalid 'enabled' value!");
        }
    }
}
