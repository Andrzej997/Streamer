package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class VideoFileMetadataDTO {

    @JsonProperty("_videoFileId")
    private Long videoFileId;
    @JsonProperty("_fileName")
    private String fileName;
    @JsonProperty("_fileSize")
    private Long fileSize;
    @JsonProperty("_extension")
    private String extension;
    @JsonProperty("_creationDate")
    private Date creationDate;
    @JsonProperty("_isPublic")
    private Boolean isPublic;

    public Long getVideoFileId() {
        return videoFileId;
    }

    public void setVideoFileId(Long videoFileId) {
        this.videoFileId = videoFileId;
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
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
