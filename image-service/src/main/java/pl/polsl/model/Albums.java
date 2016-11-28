package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
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
    @Column(name = "user_id")
    private Long userId;

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Transient
    private UsersView usersViewByUserId;

    @OneToMany(mappedBy = "albumsByAlbumId", cascade = CascadeType.ALL)
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

    public UsersView getUsersViewByUserId() {
        return usersViewByUserId;
    }

    public void setUsersViewByUserId(UsersView usersViewByUserId) {
        this.usersViewByUserId = usersViewByUserId;
    }

    public Collection<AlbumsImages> getAlbumsImagesByAlbumId() {
        return albumsImagesByAlbumId;
    }

    public void setAlbumsImagesByAlbumId(Collection<AlbumsImages> albumsImagesByAlbumId) {
        this.albumsImagesByAlbumId = albumsImagesByAlbumId;
    }
}
