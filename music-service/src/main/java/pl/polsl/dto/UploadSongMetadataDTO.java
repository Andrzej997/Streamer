package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Mateusz on 23.11.2016.
 */
@JsonSerialize()
public class UploadSongMetadataDTO {

    @JsonProperty(value = "_song")
    private SongDTO songDTO;

    @JsonProperty(value = "_userName")
    private String username;

    public SongDTO getSongDTO() {
        return songDTO;
    }

    public void setSongDTO(SongDTO songDTO) {
        this.songDTO = songDTO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
