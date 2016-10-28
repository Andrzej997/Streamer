package pl.polsl.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Mateusz on 18.10.2016.
 */
public class MusicAuthorsPK implements Serializable {

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Id
    @Column(name = "song_id", nullable = false)
    private Long songId;

    public MusicAuthorsPK() {
    }

    public MusicAuthorsPK(Long authorId, Long songId) {
        this.authorId = authorId;
        this.songId = songId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicAuthorsPK that = (MusicAuthorsPK) o;

        if (!authorId.equals(that.authorId)) return false;
        return songId.equals(that.songId);

    }

    @Override
    public int hashCode() {
        int result = authorId.hashCode();
        result = 31 * result + songId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MusicAuthorsPK{" +
                "authorId=" + authorId +
                ", songId=" + songId +
                '}';
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

}
