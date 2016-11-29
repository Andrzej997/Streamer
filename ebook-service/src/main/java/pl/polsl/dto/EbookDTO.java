package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Mateusz on 28.11.2016.
 */
public class EbookDTO {

    @JsonProperty("_ebookId")
    private Long ebookId;
    @JsonProperty("_title")
    private String title;
    @JsonProperty("_numOfPages")
    private Integer numOfPages;
    @JsonProperty("_year")
    private Short year;
    @JsonProperty("_rating")
    private Float rating;
    @JsonProperty("_genreId")
    private Long genreId;
    @JsonProperty("_comments")
    private String comments;
    @JsonProperty("_ratingTimes")
    private Long ratingTimes;
    @JsonProperty("_ownerId")
    private Long ownerId;
    @JsonProperty("_ebookFileId")
    private Long ebookFileId;
    @JsonProperty("_literaryGenreDTO")
    private LiteraryGenreDTO literaryGenreDTO;
    @JsonProperty("_ebookFileMetadataDTO")
    private EbookFileMetadataDTO ebookFileMetadataDTO;
    @JsonProperty("_writerDTOList")
    private List<WriterDTO> writerDTOList;

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(Integer numOfPages) {
        this.numOfPages = numOfPages;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getEbookFileId() {
        return ebookFileId;
    }

    public void setEbookFileId(Long ebookFileId) {
        this.ebookFileId = ebookFileId;
    }

    public LiteraryGenreDTO getLiteraryGenreDTO() {
        return literaryGenreDTO;
    }

    public void setLiteraryGenreDTO(LiteraryGenreDTO literaryGenreDTO) {
        this.literaryGenreDTO = literaryGenreDTO;
    }

    public EbookFileMetadataDTO getEbookFileMetadataDTO() {
        return ebookFileMetadataDTO;
    }

    public void setEbookFileMetadataDTO(EbookFileMetadataDTO ebookFileMetadataDTO) {
        this.ebookFileMetadataDTO = ebookFileMetadataDTO;
    }

    public List<WriterDTO> getWriterDTOList() {
        return writerDTOList;
    }

    public void setWriterDTOList(List<WriterDTO> writerDTOList) {
        this.writerDTOList = writerDTOList;
    }
}
