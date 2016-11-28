package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mateusz on 28.11.2016.
 */
public class LiteraryGenreDTO {

    @JsonProperty("_genreId")
    private Long genreId;
    @JsonProperty("_name")
    private String name;
    @JsonProperty("_comments")
    private String comments;

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
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
