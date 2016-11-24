package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * Created by Mateusz on 23.11.2016.
 */
@JsonSerialize()
public class MusicFileMetadataDTO {

    @JsonProperty(value = "_musicFileId")
    private Long musicFileId;
    @JsonProperty(value = "_fileName")
    private String fileName;
    @JsonProperty(value = "_fileSize")
    private Integer fileSize;
    @JsonProperty(value = "_extension")
    private String extension;
    @JsonProperty(value = "_creationDate")
    private Date creationDate;
    @JsonProperty(value = "_isPublic")
    private Boolean isPublic;

    public Long getMusicFileId() {
        return musicFileId;
    }

    public void setMusicFileId(Long musicFileId) {
        this.musicFileId = musicFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
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
