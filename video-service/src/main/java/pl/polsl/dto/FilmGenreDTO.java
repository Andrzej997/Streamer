package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class FilmGenreDTO {
    @JsonProperty("_filmGenreId")
    private Long filmGenreId;
    @JsonProperty("_name")
    private String name;
    @JsonProperty("_comments")
    private String comments;

    public Long getFilmGenreId() {
        return filmGenreId;
    }

    public void setFilmGenreId(Long filmGenreId) {
        this.filmGenreId = filmGenreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
