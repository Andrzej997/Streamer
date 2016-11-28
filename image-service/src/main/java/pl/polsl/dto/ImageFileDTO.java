package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class ImageFileDTO {

    @JsonProperty("_imageFileId")
    private Long imageFileId;
    @JsonProperty("_fileName")
    private String fileName;
    @JsonProperty("_fileSize")
    private Long fileSize;
    @JsonProperty("_fileExtension")
    private String fileExtension;
    @JsonProperty("_creationDate")
    private Date creationDate;
    @JsonProperty("_isPublic")
    private Boolean isPublic;

    public Long getImageFileId() {
        return imageFileId;
    }

    public void setImageFileId(Long imageFileId) {
        this.imageFileId = imageFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
