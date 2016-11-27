package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class VideoSerieDTO {

    @JsonProperty("_videoSerieId")
    private Long videoSerieId;
    @JsonProperty("_title")
    private String title;
    @JsonProperty("_number")
    private Integer number;
    @JsonProperty("_comments")
    private String comments;
    @JsonProperty("_year")
    private Date year;

    public Long getVideoSerieId() {
        return videoSerieId;
    }

    public void setVideoSerieId(Long videoSerieId) {
        this.videoSerieId = videoSerieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }
}
