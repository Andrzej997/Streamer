package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "music_albums", schema = "public")
public class MusicAlbums extends BaseEntity {

    @Id
    @Column(name = "album_id", nullable = false)
    @GenericGenerator(
            name = "generator",
            strategy = "sequence-identity",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence",
                            value = "DEFAULTDBSEQ"
                    )

            })
    @GeneratedValue(generator = "generator")
    private Long albumId;

    @Basic
    @Column(name = "album_title", nullable = false, length = 1024)
    private String albumTitle;

    @Basic
    @Column(name = "album_year")
    private Short albumYear;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @OneToMany(mappedBy = "musicAlbumsByAlbumId", cascade = CascadeType.ALL)
    private Collection<Songs> songsesByAlbumId;

    public MusicAlbums() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicAlbums that = (MusicAlbums) o;

        if (!albumId.equals(that.albumId)) return false;
        if (!albumTitle.equals(that.albumTitle)) return false;
        if (albumYear != null ? !albumYear.equals(that.albumYear) : that.albumYear != null) return false;
        return comments != null ? comments.equals(that.comments) : that.comments == null;

    }

    @Override
    public int hashCode() {
        int result = albumId.hashCode();
        result = 31 * result + albumTitle.hashCode();
        result = 31 * result + (albumYear != null ? albumYear.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MusicAlbums{" +
                "albumId=" + albumId +
                ", albumTitle='" + albumTitle + '\'' +
                ", albumYear=" + albumYear +
                ", comments='" + comments + '\'' +
                '}';
    }

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

    public Collection<Songs> getSongsesByAlbumId() {
        return songsesByAlbumId;
    }

    public void setSongsesByAlbumId(Collection<Songs> songsesByAlbumId) {
        this.songsesByAlbumId = songsesByAlbumId;
    }
}
