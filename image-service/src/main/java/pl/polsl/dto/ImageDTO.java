package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class ImageDTO {

    @JsonProperty("_imageId")
    private Long imageId;
    @JsonProperty("_title")
    private String title;
    @JsonProperty("_imageFileId")
    private Long imageFileId;
    @JsonProperty("_nativeWidth")
    private Integer nativeWidth;
    @JsonProperty("_nativeHeight")
    private Integer nativeHeight;
    @JsonProperty("_resolution")
    private Integer resolution;
    @JsonProperty("_depth")
    private Short depth;
    @JsonProperty("_comments")
    private String comments;
    @JsonProperty("_rating")
    private Float rating;
    @JsonProperty("_typeId")
    private Long typeId;
    @JsonProperty("_year")
    private Short year;
    @JsonProperty("_ownerId")
    private Long ownerId;
    @JsonProperty("_ratingTimes")
    private Long ratingTimes;
    @JsonProperty("_artistDTOList")
    private List<ArtistDTO> artistDTOList;
    @JsonProperty("_imageFileDTO")
    private ImageFileDTO imageFileDTO;
    @JsonProperty("_imageTypeDTO")
    private ImageTypeDTO imageTypeDTO;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getImageFileId() {
        return imageFileId;
    }

    public void setImageFileId(Long imageFileId) {
        this.imageFileId = imageFileId;
    }

    public Integer getNativeWidth() {
        return nativeWidth;
    }

    public void setNativeWidth(Integer nativeWidth) {
        this.nativeWidth = nativeWidth;
    }

    public Integer getNativeHeight() {
        return nativeHeight;
    }

    public void setNativeHeight(Integer nativeHeight) {
        this.nativeHeight = nativeHeight;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public Short getDepth() {
        return depth;
    }

    public void setDepth(Short depth) {
        this.depth = depth;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Long getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    public List<ArtistDTO> getArtistDTOList() {
        return artistDTOList;
    }

    public void setArtistDTOList(List<ArtistDTO> artistDTOList) {
        this.artistDTOList = artistDTOList;
    }

    public ImageFileDTO getImageFileDTO() {
        return imageFileDTO;
    }

    public void setImageFileDTO(ImageFileDTO imageFileDTO) {
        this.imageFileDTO = imageFileDTO;
    }

    public ImageTypeDTO getImageTypeDTO() {
        return imageTypeDTO;
    }

    public void setImageTypeDTO(ImageTypeDTO imageTypeDTO) {
        this.imageTypeDTO = imageTypeDTO;
    }
}
