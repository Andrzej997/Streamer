package pl.polsl.service;

import pl.polsl.dto.*;

import java.util.List;

/**
 * Created by Mateusz on 23.11.2016.
 */
public interface MusicMetadataService {

    List<MusicArtistDTO> getArtistsByPrediction(String name, String name2, String surname);

    List<MusicAlbumDTO> getAlbumsByPrediction(String albumTitle, String songTitle);

    List<MusicGenreDTO> getGenresByPrediction(String genreName);

    UploadSongMetadataDTO saveMetadata(UploadSongMetadataDTO uploadSongMetadataDTO);

    List<SongDTO> getTop10Songs(String username, String title);

    List<SongDTO> getAllUserSongs(String username);

    List<SongDTO> searchSongsByCriteria(SearchSongCriteriaDTO searchSongCriteriaDTO);

    SongDTO updateSongMetadata(SongDTO songDTO);
}
