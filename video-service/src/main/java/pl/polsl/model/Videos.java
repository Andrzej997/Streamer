package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "videos", schema = "public")
public class Videos extends BaseEntity {

    @Id
    @Column(name = "video_id", nullable = false)
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
    private Long videoId;

    @Basic
    @Column(name = "video_file_id", nullable = false)
    private Long videoFileId;

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "director_id")
    private Long directorId;

    @Basic
    @Column(name = "film_genre_id")
    private Long filmGenreId;

    @Basic
    @Column(name = "video_serie_id")
    private Long videoSerieId;

    @Basic
    @Column(name = "rating")
    private Float rating;

    @Basic
    @Column(name = "production_year")
    private Short productionYear;

    @Basic
    @Column(name = "owner_id")
    private Long ownerId;

    @OneToMany(mappedBy = "videosByVideoId")
    private Collection<PlaylistsVideos> playlistsVideosesByVideoId;

    @ManyToOne
    @JoinColumn(name = "video_file_id", referencedColumnName = "video_file_id", nullable = false, insertable = false, updatable = false)
    private VideoFiles videoFilesByVideoFileId;

    @ManyToOne
    @JoinColumn(name = "film_genre_id", referencedColumnName = "film_genre_id", insertable = false, updatable = false)
    private FilmGenres filmGenresByFilmGenreId;

    @ManyToOne
    @JoinColumn(name = "video_serie_id", referencedColumnName = "video_serie_id", insertable = false, updatable = false)
    private VideoSeries videoSeriesByVideoSerieId;

    @Transient
    private Users usersByOwnerId;

    @OneToMany(mappedBy = "videosByVideoId")
    private Collection<VideosAuthors> videosAuthorsesByVideoId;

    public Videos() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Videos videos = (Videos) o;

        if (!videoId.equals(videos.videoId)) return false;
        if (!videoFileId.equals(videos.videoFileId)) return false;
        if (!title.equals(videos.title)) return false;
        if (directorId != null ? !directorId.equals(videos.directorId) : videos.directorId != null) return false;
        if (filmGenreId != null ? !filmGenreId.equals(videos.filmGenreId) : videos.filmGenreId != null) return false;
        if (videoSerieId != null ? !videoSerieId.equals(videos.videoSerieId) : videos.videoSerieId != null)
            return false;
        if (rating != null ? !rating.equals(videos.rating) : videos.rating != null) return false;
        if (productionYear != null ? !productionYear.equals(videos.productionYear) : videos.productionYear != null)
            return false;
        return ownerId != null ? ownerId.equals(videos.ownerId) : videos.ownerId == null;

    }

    @Override
    public int hashCode() {
        int result = videoId.hashCode();
        result = 31 * result + videoFileId.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (directorId != null ? directorId.hashCode() : 0);
        result = 31 * result + (filmGenreId != null ? filmGenreId.hashCode() : 0);
        result = 31 * result + (videoSerieId != null ? videoSerieId.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (productionYear != null ? productionYear.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Videos{" +
                "videoId=" + videoId +
                ", videoFileId=" + videoFileId +
                ", title='" + title + '\'' +
                ", directorId=" + directorId +
                ", filmGenreId=" + filmGenreId +
                ", videoSerieId=" + videoSerieId +
                ", rating=" + rating +
                ", productionYear=" + productionYear +
                ", ownerId=" + ownerId +
                '}';
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getVideoFileId() {
        return videoFileId;
    }

    public void setVideoFileId(Long videoFileId) {
        this.videoFileId = videoFileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public Long getFilmGenreId() {
        return filmGenreId;
    }

    public void setFilmGenreId(Long filmGenreId) {
        this.filmGenreId = filmGenreId;
    }

    public Long getVideoSerieId() {
        return videoSerieId;
    }

    public void setVideoSerieId(Long videoSerieId) {
        this.videoSerieId = videoSerieId;
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

    public Collection<PlaylistsVideos> getPlaylistsVideosesByVideoId() {
        return playlistsVideosesByVideoId;
    }

    public void setPlaylistsVideosesByVideoId(Collection<PlaylistsVideos> playlistsVideosesByVideoId) {
        this.playlistsVideosesByVideoId = playlistsVideosesByVideoId;
    }

    public VideoFiles getVideoFilesByVideoFileId() {
        return videoFilesByVideoFileId;
    }

    public void setVideoFilesByVideoFileId(VideoFiles videoFilesByVideoFileId) {
        this.videoFilesByVideoFileId = videoFilesByVideoFileId;
    }

    public FilmGenres getFilmGenresByFilmGenreId() {
        return filmGenresByFilmGenreId;
    }

    public void setFilmGenresByFilmGenreId(FilmGenres filmGenresByFilmGenreId) {
        this.filmGenresByFilmGenreId = filmGenresByFilmGenreId;
    }

    public VideoSeries getVideoSeriesByVideoSerieId() {
        return videoSeriesByVideoSerieId;
    }

    public void setVideoSeriesByVideoSerieId(VideoSeries videoSeriesByVideoSerieId) {
        this.videoSeriesByVideoSerieId = videoSeriesByVideoSerieId;
    }

    public Users getUsersByOwnerId() {
        return usersByOwnerId;
    }

    public void setUsersByOwnerId(Users usersByOwnerId) {
        this.usersByOwnerId = usersByOwnerId;
    }

    public Collection<VideosAuthors> getVideosAuthorsesByVideoId() {
        return videosAuthorsesByVideoId;
    }

    public void setVideosAuthorsesByVideoId(Collection<VideosAuthors> videosAuthorsesByVideoId) {
        this.videosAuthorsesByVideoId = videosAuthorsesByVideoId;
    }
}
