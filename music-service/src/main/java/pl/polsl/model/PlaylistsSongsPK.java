package pl.polsl.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Mateusz on 18.10.2016.
 */
public class PlaylistsSongsPK implements Serializable {

    @Id
    @Column(name = "playlist_id", nullable = false)
    private Long playlistId;

    @Id
    @Column(name = "song_id", nullable = false)
    private Long songId;

    public PlaylistsSongsPK() {
    }

    public PlaylistsSongsPK(Long playlistId, Long songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistsSongsPK that = (PlaylistsSongsPK) o;

        if (!playlistId.equals(that.playlistId)) return false;
        return songId.equals(that.songId);

    }

    @Override
    public int hashCode() {
        int result = playlistId.hashCode();
        result = 31 * result + songId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PlaylistsSongsPK{" +
                "playlistId=" + playlistId +
                ", songId=" + songId +
                '}';
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }
}
