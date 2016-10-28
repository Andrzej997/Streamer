package pl.polsl.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Mateusz on 18.10.2016.
 */
public class PlaylistsVideosPK implements Serializable {

    @Id
    @Column(name = "playlist_id", nullable = false)
    private Long playlistId;

    @Id
    @Column(name = "video_id", nullable = false)
    private Long videoId;

    public PlaylistsVideosPK() {
    }

    public PlaylistsVideosPK(Long playlistId, Long videoId) {
        this.playlistId = playlistId;
        this.videoId = videoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistsVideosPK that = (PlaylistsVideosPK) o;

        if (!playlistId.equals(that.playlistId)) return false;
        return videoId.equals(that.videoId);

    }

    @Override
    public int hashCode() {
        int result = playlistId.hashCode();
        result = 31 * result + videoId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PlaylistsVideosPK{" +
                "playlistId=" + playlistId +
                ", videoId=" + videoId +
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
}
