package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "playlists_videos", schema = "public")
@IdClass(PlaylistsVideosPK.class)
public class PlaylistsVideos extends BaseEntity {

    @Id
    @Column(name = "playlist_id", nullable = false)
    private Long playlistId;

    @Id
    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @Basic
    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @ManyToOne
    @JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id", nullable = false, insertable = false, updatable = false)
    private VideoPlaylists videoPlaylistsByPlaylistId;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "video_id", nullable = false, insertable = false, updatable = false)
    private Videos videosByVideoId;

    public PlaylistsVideos() {
    }

    public PlaylistsVideos(Long playlistId, Long videoId) {
        this.playlistId = playlistId;
        this.videoId = videoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistsVideos that = (PlaylistsVideos) o;

        if (!playlistId.equals(that.playlistId)) return false;
        if (!videoId.equals(that.videoId)) return false;
        return orderNumber.equals(that.orderNumber);

    }

    @Override
    public int hashCode() {
        int result = playlistId.hashCode();
        result = 31 * result + videoId.hashCode();
        result = 31 * result + orderNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PlaylistsVideos{" +
                "playlistId=" + playlistId +
                ", videoId=" + videoId +
                ", orderNumber=" + orderNumber +
                '}';
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public VideoPlaylists getVideoPlaylistsByPlaylistId() {
        return videoPlaylistsByPlaylistId;
    }

    public void setVideoPlaylistsByPlaylistId(VideoPlaylists videoPlaylistsByPlaylistId) {
        this.videoPlaylistsByPlaylistId = videoPlaylistsByPlaylistId;
    }

    public Videos getVideosByVideoId() {
        return videosByVideoId;
    }

    public void setVideosByVideoId(Videos videosByVideoId) {
        this.videosByVideoId = videosByVideoId;
    }
}
