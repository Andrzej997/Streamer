package pl.polsl.service;

import pl.polsl.dto.MusicAlbumDTO;
import pl.polsl.dto.MusicArtistDTO;
import pl.polsl.dto.MusicGenreDTO;
import pl.polsl.dto.UploadSongMetadataDTO;

import java.util.List;

/**
 * Created by Mateusz on 23.11.2016.
 */
public interface MusicMetadataService {

    List<MusicArtistDTO> getArtistsByPrediction(String name, String name2, String surname);

    List<MusicAlbumDTO> getAlbumsByPrediction(String albumTitle, String songTitle);

    List<MusicGenreDTO> getGenresByPrediction(String genreName);

    UploadSongMetadataDTO saveMetadata(UploadSongMetadataDTO uploadSongMetadataDTO);
}
