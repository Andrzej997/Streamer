package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class DirectorDTO {
    @JsonProperty("_directorId")
    private Long directorId;
    @JsonProperty("_name")
    private String name;
    @JsonProperty("_name2")
    private String name2;
    @JsonProperty("_surname")
    private String surname;
    @JsonProperty("_birthDate")
    private Date birthDate;
    @JsonProperty("_deathDate")
    private Date deathDate;
    @JsonProperty("_comments")
    private String comments;
    @JsonProperty("_ratings")
    private Float ratings;

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Float getRatings() {
        return ratings;
    }

    public void setRatings(Float ratings) {
        this.ratings = ratings;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }
}
