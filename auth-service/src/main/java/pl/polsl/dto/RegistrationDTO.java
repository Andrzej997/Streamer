package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Mateusz on 12.11.2016.
 */
@JsonSerialize()
public class RegistrationDTO {

    @JsonProperty(value = "_username")
    private String username;

    @JsonProperty(value = "_password")
    private String password;

    @JsonProperty(value = "_email")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
