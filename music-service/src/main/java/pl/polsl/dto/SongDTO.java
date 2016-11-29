package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Created by Mateusz on 23.11.2016.
 */
@JsonSerialize()
public class SongDTO {

    @JsonProperty(value = "_songId")
    private Long songId;
    @JsonProperty(value = "_title")
    private String title;
    @JsonProperty(value = "_fileId")
    private Long fileId;
    @JsonProperty(value = "_ratingTimes")
    private Long ratingTimes;
    @JsonProperty(value = "_albumId")
    private Long albumId;
    @JsonProperty(value = "_musicGenreId")
    private Long musicGenreId;
    @JsonProperty(value = "_rating")
    private Float rating;
    @JsonProperty(value = "_productionYear")
    private Short productionYear;
    @JsonProperty(value = "_ownerId")
    private Long ownerId;
    @JsonProperty(value = "_authors")
    private List<MusicArtistDTO> authors;
    @JsonProperty(value = "_fileMetadata")
    private MusicFileMetadataDTO fileMetadata;
    @JsonProperty(value = "_genre")
    private MusicGenreDTO genre;
    @JsonProperty(value = "_album")
    private MusicAlbumDTO album;

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

    public Long getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
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

    public List<MusicArtistDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<MusicArtistDTO> authors) {
        this.authors = authors;
    }

    public MusicFileMetadataDTO getFileMetadata() {
        return fileMetadata;
    }

    public void setFileMetadata(MusicFileMetadataDTO fileMetadata) {
        this.fileMetadata = fileMetadata;
    }

    public MusicGenreDTO getGenre() {
        return genre;
    }

    public void setGenre(MusicGenreDTO genre) {
        this.genre = genre;
    }

    public MusicAlbumDTO getAlbum() {
        return album;
    }

    public void setAlbum(MusicAlbumDTO album) {
        this.album = album;
    }
}
