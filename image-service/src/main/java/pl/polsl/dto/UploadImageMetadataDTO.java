package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mateusz on 27.11.2016.
 */
public class UploadImageMetadataDTO {

    @JsonProperty("_imageDTO")
    private ImageDTO imageDTO;
    @JsonProperty("_username")
    private String username;

    public ImageDTO getImageDTO() {
        return imageDTO;
    }

    public void setImageDTO(ImageDTO imageDTO) {
        this.imageDTO = imageDTO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
