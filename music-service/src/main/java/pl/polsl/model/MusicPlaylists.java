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
@Table(name = "music_playlists", schema = "public")
public class MusicPlaylists extends BaseEntity {

    @Id
    @Column(name = "playlist_id", nullable = false)
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
    private Long playlistId;

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
    private UsersView usersViewByUserId;

    @OneToMany(mappedBy = "musicPlaylistsByPlaylistId", cascade = CascadeType.ALL)
    private Collection<PlaylistsSongs> playlistsSongsesByPlaylistId;

    public MusicPlaylists() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicPlaylists that = (MusicPlaylists) o;

        if (!playlistId.equals(that.playlistId)) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (!title.equals(that.title)) return false;
        return creationDate != null ? creationDate.equals(that.creationDate) : that.creationDate == null;

    }

    @Override
    public int hashCode() {
        int result = playlistId.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + title.hashCode();
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MusicPlaylists{" +
                "playlistId=" + playlistId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
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

    public Collection<PlaylistsSongs> getPlaylistsSongsesByPlaylistId() {
        return playlistsSongsesByPlaylistId;
    }

    public void setPlaylistsSongsesByPlaylistId(Collection<PlaylistsSongs> playlistsSongsesByPlaylistId) {
        this.playlistsSongsesByPlaylistId = playlistsSongsesByPlaylistId;
    }
}
