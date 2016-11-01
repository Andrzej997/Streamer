package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "songs", schema = "public")
public class Songs extends BaseEntity {

    @Id
    @Column(name = "song_id", nullable = false)
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
    private Long songId;

    @Basic
    @Column(name = "title", nullable = false, length = 1024)
    private String title;

    @Basic
    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Basic
    @Column(name = "author_id")
    private Long authorId;

    @Basic
    @Column(name = "album_id")
    private Long albumId;

    @Basic
    @Column(name = "music_genre_id")
    private Long musicGenreId;

    @Basic
    @Column(name = "rating")
    private Float rating;

    @Basic
    @Column(name = "production_year")
    private Short productionYear;

    @Basic
    @Column(name = "owner_id")
    private Long ownerId;

    @OneToMany(mappedBy = "songsBySongId")
    private Collection<PlaylistsSongs> playlistsSongsesBySongId;

    @OneToMany(mappedBy = "songsBySongId")
    private Collection<MusicAuthors> musicAuthorsesBySongId;

    @ManyToOne
    @JoinColumn(name = "file_id", referencedColumnName = "music_file_id", nullable = false, insertable = false, updatable = false)
    private MusicFiles musicFilesByFileId;

    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "album_id", insertable = false, updatable = false)
    private MusicAlbums musicAlbumsByAlbumId;

    @ManyToOne
    @JoinColumn(name = "music_genre_id", referencedColumnName = "music_genre_id", insertable = false, updatable = false)
    private MusicGenres musicGenresByMusicGenreId;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private Users usersByOwnerId;

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getMusicGenreId() {
        return musicGenreId;
    }

    public void setMusicGenreId(Long musicGenreId) {
        this.musicGenreId = musicGenreId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Short getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Short productionYear) {
        this.productionYear = productionYear;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Collection<PlaylistsSongs> getPlaylistsSongsesBySongId() {
        return playlistsSongsesBySongId;
    }

    public void setPlaylistsSongsesBySongId(Collection<PlaylistsSongs> playlistsSongsesBySongId) {
        this.playlistsSongsesBySongId = playlistsSongsesBySongId;
    }

    public MusicFiles getMusicFilesByFileId() {
        return musicFilesByFileId;
    }

    public void setMusicFilesByFileId(MusicFiles musicFilesByFileId) {
        this.musicFilesByFileId = musicFilesByFileId;
    }

    public MusicAlbums getMusicAlbumsByAlbumId() {
        return musicAlbumsByAlbumId;
    }

    public void setMusicAlbumsByAlbumId(MusicAlbums musicAlbumsByAlbumId) {
        this.musicAlbumsByAlbumId = musicAlbumsByAlbumId;
    }

    public MusicGenres getMusicGenresByMusicGenreId() {
        return musicGenresByMusicGenreId;
    }

    public void setMusicGenresByMusicGenreId(MusicGenres musicGenresByMusicGenreId) {
        this.musicGenresByMusicGenreId = musicGenresByMusicGenreId;
    }

    public Users getUsersByOwnerId() {
        return usersByOwnerId;
    }

    public void setUsersByOwnerId(Users usersByOwnerId) {
        this.usersByOwnerId = usersByOwnerId;
    }

    public Collection<MusicAuthors> getMusicAuthorsesBySongId() {
        return musicAuthorsesBySongId;
    }

    public void setMusicAuthorsesBySongId(Collection<MusicAuthors> musicAuthorsesBySongId) {
        this.musicAuthorsesBySongId = musicAuthorsesBySongId;
    }
}
