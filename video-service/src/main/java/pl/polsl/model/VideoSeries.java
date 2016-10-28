package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "video_series", schema = "public")
public class VideoSeries extends BaseEntity {

    @Id
    @Column(name = "video_serie_id", nullable = false)
    private Long videoSerieId;

    @Basic
    @Column(name = "title", length = -1)
    private String title;

    @Basic
    @Column(name = "number")
    private Integer number;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @Basic
    @Column(name = "year")
    private Timestamp year;

    @OneToMany(mappedBy = "videoSeriesByVideoSerieId")
    private Collection<Videos> videosesByVideoSerieId;

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

    public Timestamp getYear() {
        return year;
    }

    public void setYear(Timestamp year) {
        this.year = year;
    }

    public Collection<Videos> getVideosesByVideoSerieId() {
        return videosesByVideoSerieId;
    }

    public void setVideosesByVideoSerieId(Collection<Videos> videosesByVideoSerieId) {
        this.videosesByVideoSerieId = videosesByVideoSerieId;
    }
}
