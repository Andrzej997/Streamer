package pl.polsl.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Mateusz on 18.10.2016.
 */
public class VideosAuthorsPK implements Serializable {

    @Id
    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    public VideosAuthorsPK() {
    }

    public VideosAuthorsPK(Long videoId, Long authorId) {
        this.videoId = videoId;
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideosAuthorsPK that = (VideosAuthorsPK) o;

        if (!videoId.equals(that.videoId)) return false;
        return authorId.equals(that.authorId);

    }

    @Override
    public int hashCode() {
        int result = videoId.hashCode();
        result = 31 * result + authorId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "VideosAuthorsPK{" +
                "videoId=" + videoId +
                ", authorId=" + authorId +
                '}';
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

}
