package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Mateusz on 23.11.2016.
 */
@JsonSerialize()
public class MusicArtistDTO {

    @JsonProperty(value = "_authorId")
    private Long authorId;

    @JsonProperty(value = "_name")
    private String name;

    @JsonProperty(value = "_name2")
    private String name2;

    @JsonProperty(value = "_surname")
    private String surname;

    @JsonProperty(value = "_birthYear")
    private Short birthYear;

    @JsonProperty(value = "_deathYear")
    private Short deathYear;

    @JsonProperty(value = "_comments")
    private String comments;

    @JsonProperty(value = "_ratings")
    private Float ratings;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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

    public Short getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Short birthYear) {
        this.birthYear = birthYear;
    }

    public Short getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Short deathYear) {
        this.deathYear = deathYear;
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
