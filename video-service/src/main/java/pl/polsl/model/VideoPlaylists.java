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
@Table(name = "video_playlists", schema = "public")
public class VideoPlaylists extends BaseEntity {

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

    @OneToMany(mappedBy = "videoPlaylistsByPlaylistId")
    private Collection<PlaylistsVideos> playlistsVideosesByPlaylistId;

    @Transient
    private Users usersByUserId;

    public VideoPlaylists() {
    }

    @PostLoad
    @Override
    protected void postLoad() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideoPlaylists that = (VideoPlaylists) o;

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
        return "VideoPlaylists{" +
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

    public Collection<PlaylistsVideos> getPlaylistsVideosesByPlaylistId() {
        return playlistsVideosesByPlaylistId;
    }

    public void setPlaylistsVideosesByPlaylistId(Collection<PlaylistsVideos> playlistsVideosesByPlaylistId) {
        this.playlistsVideosesByPlaylistId = playlistsVideosesByPlaylistId;
    }

    public Users getUsersByUserId() {
        return usersByUserId;
    }

    public void setUsersByUserId(Users usersByUserId) {
        this.usersByUserId = usersByUserId;
    }
}
