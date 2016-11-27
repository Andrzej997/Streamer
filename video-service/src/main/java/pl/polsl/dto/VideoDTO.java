package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class VideoDTO {

    @JsonProperty("_videoId")
    private Long videoId;
    @JsonProperty("_videoFileId")
    private Long videoFileId;
    @JsonProperty("_title")
    private String title;
    @JsonProperty("_directorId")
    private Long directorId;
    @JsonProperty("_filmGenreId")
    private Long filmGenreId;
    @JsonProperty("_videoSerieId")
    private Long videoSerieId;
    @JsonProperty("_rating")
    private Float rating;
    @JsonProperty("_productionYear")
    private Short productionYear;
    @JsonProperty("_ownerId")
    private Long ownerId;
    @JsonProperty("_directorList")
    private List<DirectorDTO> directorList;
    @JsonProperty("_videoFileMetadata")
    private VideoFileMetadataDTO videoFileMetadata;
    @JsonProperty("_filmGenre")
    private FilmGenreDTO filmGenre;
    @JsonProperty("_videoSerie")
    private VideoSerieDTO videoSerie;

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

    public List<DirectorDTO> getDirectorList() {
        return directorList;
    }

    public void setDirectorList(List<DirectorDTO> directorList) {
        this.directorList = directorList;
    }

    public VideoFileMetadataDTO getVideoFileMetadata() {
        return videoFileMetadata;
    }

    public void setVideoFileMetadata(VideoFileMetadataDTO videoFileMetadata) {
        this.videoFileMetadata = videoFileMetadata;
    }

    public FilmGenreDTO getFilmGenre() {
        return filmGenre;
    }

    public void setFilmGenre(FilmGenreDTO filmGenre) {
        this.filmGenre = filmGenre;
    }

    public VideoSerieDTO getVideoSerie() {
        return videoSerie;
    }

    public void setVideoSerie(VideoSerieDTO videoSerie) {
        this.videoSerie = videoSerie;
    }
}
