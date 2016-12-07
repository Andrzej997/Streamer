package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mateusz on 07.12.2016.
 */
public class ChangePasswordDTO {

    @JsonProperty("_oldPassword")
    private String oldPassword;

    @JsonProperty("_newPassword")
    private String newPassword;

    @JsonProperty("_username")
    private String username;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
