package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mateusz on 28.11.2016.
 */
public class UploadEbookMetadataDTO {

    @JsonProperty("_ebookDTO")
    private EbookDTO ebookDTO;
    @JsonProperty("_username")
    private String username;

    public EbookDTO getEbookDTO() {
        return ebookDTO;
    }

    public void setEbookDTO(EbookDTO ebookDTO) {
        this.ebookDTO = ebookDTO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
