package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TranscodeRequestDTO {

    @JsonProperty("_videoId")
    private Long videoId;

    @JsonProperty("_resolutionType")
    private String resolutionType;

    @JsonProperty("_username")
    private String username;

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getResolutionType() {
        return resolutionType;
    }

    public void setResolutionType(String resolutionType) {
        this.resolutionType = resolutionType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
