package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Mateusz on 28.11.2016.
 */
public class WriterDTO {

    @JsonProperty("_writerId")
    private Long writerId;
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

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
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

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
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

}
