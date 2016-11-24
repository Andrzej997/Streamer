package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Mateusz on 23.11.2016.
 */
@JsonSerialize()
public class MusicGenreDTO {

    @JsonProperty(value = "_musicGenreId")
    private Long musicGenreId;

    @JsonProperty(value = "_name")
    private String name;

    @JsonProperty(value = "_comments")
    private String comments;

    public Long getMusicGenreId() {
        return musicGenreId;
    }

    public void setMusicGenreId(Long musicGenreId) {
        this.musicGenreId = musicGenreId;
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
