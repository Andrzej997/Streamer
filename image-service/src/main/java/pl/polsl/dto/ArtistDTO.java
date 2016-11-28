package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class ArtistDTO {
    @JsonProperty("_artistId")
    private Long artistId;
    @JsonProperty("_name")
    private String name;
    @JsonProperty("_name2")
    private String name2;
    @JsonProperty("_surname")
    private String surname;
    @JsonProperty("_birthYear")
    private Date birthYear;
    @JsonProperty("_deathYear")
    private Date deathYear;
    @JsonProperty("_comments")
    private String comments;
    @JsonProperty("_ratings")
    private Float ratings;

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
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

    public Date getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Date birthYear) {
        this.birthYear = birthYear;
    }

    public Date getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Date deathYear) {
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
