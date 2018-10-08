package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoQualityDTO {

    @JsonProperty("_videoFileId")
    private Long videoFileId;
    @JsonProperty("_resolution")
    private String resolution;

    public VideoQualityDTO() {
    }

    public VideoQualityDTO(Long videoFileId, String resolution) {
        this.videoFileId = videoFileId;
        this.resolution = resolution;
    }

    public Long getVideoFileId() {
        return videoFileId;
    }

    public void setVideoFileId(Long videoFileId) {
        this.videoFileId = videoFileId;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
