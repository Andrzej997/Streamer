package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class UploadVideoMetadataDTO {

    @JsonProperty("_video")
    private VideoDTO video;
    @JsonProperty("_username")
    private String username;

    public VideoDTO getVideo() {
        return video;
    }

    public void setVideo(VideoDTO video) {
        this.video = video;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
