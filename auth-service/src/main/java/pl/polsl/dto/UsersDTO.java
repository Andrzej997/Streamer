package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mateusz on 05.11.2016.
 */
public class UsersDTO {

    @JsonProperty(value = "_userId")
    private Long userId;

    @JsonProperty(value = "_userName")
    private String userName;

    @JsonProperty(value = "_email")
    private String email;

    @JsonProperty(value = "_name")
    private String name;

    @JsonProperty(value = "_surname")
    private String surname;

    @JsonProperty(value = "_nationality")
    private String nationality;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
