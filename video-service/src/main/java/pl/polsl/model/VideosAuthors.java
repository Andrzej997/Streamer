package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "videos_authors", schema = "public")
@IdClass(VideosAuthorsPK.class)
public class VideosAuthors extends BaseEntity {

    @Id
    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "video_id", nullable = false)
    private Videos videosByVideoId;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "director_id", nullable = false)
    private Directors directorsByAuthorId;

    public VideosAuthors() {
    }

    public VideosAuthors(Long videoId, Long authorId) {
        this.videoId = videoId;
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideosAuthors that = (VideosAuthors) o;

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
        return "VideosAuthors{" +
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

    public Videos getVideosByVideoId() {
        return videosByVideoId;
    }

    public void setVideosByVideoId(Videos videosByVideoId) {
        this.videosByVideoId = videosByVideoId;
    }

    public Directors getDirectorsByAuthorId() {
        return directorsByAuthorId;
    }

    public void setDirectorsByAuthorId(Directors directorsByAuthorId) {
        this.directorsByAuthorId = directorsByAuthorId;
    }
}
