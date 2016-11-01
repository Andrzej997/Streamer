package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "albums", schema = "public")
public class Albums extends BaseEntity {

    @Id
    @Column(name = "album_id", nullable = false)
    private Long albumId;

    @Basic
    @Column(name = "user_id")
    private Long userId;

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "creation_date")
    private Timestamp creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private Users usersByUserId;

    @OneToMany(mappedBy = "albumsByAlbumId")
    private Collection<AlbumsImages> albumsImagesByAlbumId;

    public Albums() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Albums albums = (Albums) o;

        if (!albumId.equals(albums.albumId)) return false;
        if (userId != null ? !userId.equals(albums.userId) : albums.userId != null) return false;
        if (!title.equals(albums.title)) return false;
        return creationDate != null ? creationDate.equals(albums.creationDate) : albums.creationDate == null;

    }

    @Override
    public int hashCode() {
        int result = albumId.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + title.hashCode();
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Albums{" +
                "albumId=" + albumId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Users getUsersByUserId() {
        return usersByUserId;
    }

    public void setUsersByUserId(Users usersByUserId) {
        this.usersByUserId = usersByUserId;
    }

    public Collection<AlbumsImages> getAlbumsImagesByAlbumId() {
        return albumsImagesByAlbumId;
    }

    public void setAlbumsImagesByAlbumId(Collection<AlbumsImages> albumsImagesByAlbumId) {
        this.albumsImagesByAlbumId = albumsImagesByAlbumId;
    }
}
