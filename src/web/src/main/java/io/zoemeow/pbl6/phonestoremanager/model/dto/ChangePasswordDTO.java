package io.zoemeow.pbl6.phonestoremanager.model.dto;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordDTO {
    @SerializedName("old-pass")
    private String oldPass;

    @SerializedName("new-pass")
    private String newPass;

    @SerializedName("re-new-pass")
    private String reNewPass;

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getReNewPass() {
        return reNewPass;
    }

    public void setReNewPass(String reNewPass) {
        this.reNewPass = reNewPass;
    }

    
}
