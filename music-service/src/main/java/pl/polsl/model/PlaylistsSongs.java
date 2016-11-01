package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "playlists_songs", schema = "public")
@IdClass(PlaylistsSongsPK.class)
public class PlaylistsSongs extends BaseEntity {

    @Id
    @Column(name = "playlist_id", nullable = false)
    private Long playlistId;

    @Id
    @Column(name = "song_id", nullable = false)
    private Long songId;

    @Basic
    @Column(name = "order_number", nullable = false)
    private Short orderNumber;

    @ManyToOne
    @JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id", nullable = false, insertable = false, updatable = false)
    private MusicPlaylists musicPlaylistsByPlaylistId;

    @ManyToOne
    @JoinColumn(name = "song_id", referencedColumnName = "song_id", nullable = false, insertable = false, updatable = false)
    private Songs songsBySongId;

    public PlaylistsSongs() {
    }

    public PlaylistsSongs(Long playlistId, Long songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistsSongs that = (PlaylistsSongs) o;

        if (!playlistId.equals(that.playlistId)) return false;
        if (!songId.equals(that.songId)) return false;
        return orderNumber.equals(that.orderNumber);

    }

    @Override
    public int hashCode() {
        int result = playlistId.hashCode();
        result = 31 * result + songId.hashCode();
        result = 31 * result + orderNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PlaylistsSongs{" +
                "playlistId=" + playlistId +
                ", songId=" + songId +
                ", orderNumber=" + orderNumber +
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

    public Short getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Short orderNumber) {
        this.orderNumber = orderNumber;
    }

    public MusicPlaylists getMusicPlaylistsByPlaylistId() {
        return musicPlaylistsByPlaylistId;
    }

    public void setMusicPlaylistsByPlaylistId(MusicPlaylists musicPlaylistsByPlaylistId) {
        this.musicPlaylistsByPlaylistId = musicPlaylistsByPlaylistId;
    }

    public Songs getSongsBySongId() {
        return songsBySongId;
    }

    public void setSongsBySongId(Songs songsBySongId) {
        this.songsBySongId = songsBySongId;
    }
}
