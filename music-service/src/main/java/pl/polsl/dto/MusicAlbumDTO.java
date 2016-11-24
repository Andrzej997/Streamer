package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Mateusz on 23.11.2016.
 */
@JsonSerialize()
public class MusicAlbumDTO {

    @JsonProperty(value = "_albumId")
    private Long albumId;

    @JsonProperty(value = "_albumTitle")
    private String albumTitle;

    @JsonProperty(value = "_albumYear")
    private Short albumYear;

    @JsonProperty(value = "_comments")
    private String comments;

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public Short getAlbumYear() {
        return albumYear;
    }

    public void setAlbumYear(Short albumYear) {
        this.albumYear = albumYear;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
