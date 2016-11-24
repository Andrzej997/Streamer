package pl.polsl.mapper;

import pl.polsl.dto.MusicAlbumDTO;
import pl.polsl.dto.MusicArtistDTO;
import pl.polsl.dto.MusicFileMetadataDTO;
import pl.polsl.dto.MusicGenreDTO;
import pl.polsl.model.MusicAlbums;
import pl.polsl.model.MusicArtists;
import pl.polsl.model.MusicFiles;
import pl.polsl.model.MusicGenres;

import java.util.List;

/**
 * Created by Mateusz on 24.11.2016.
 */
public interface MusicMapper {

    MusicAlbumDTO toMusicAlbumDTO(MusicAlbums musicAlbum);

    List<MusicAlbumDTO> toMusicAlbumDTOList(List<MusicAlbums> musicAlbumList);

    MusicAlbums toMusicAlbums(MusicAlbumDTO musicAlbumDTO);

    List<MusicAlbums> toMusicAlbumsList(List<MusicAlbumDTO> musicAlbumDTOList);

    MusicArtistDTO toMusicArtistDTO(MusicArtists musicArtist);

    List<MusicArtistDTO> toMusicArtistDTOList(List<MusicArtists> musicArtistsList);

    MusicArtists toMusicArtists(MusicArtistDTO musicArtistDTO);

    List<MusicArtists> toMusicArtistsList(List<MusicArtistDTO> musicArtistDTOList);

    MusicFileMetadataDTO toMusicFileMetadataDTO(MusicFiles musicFile);

    List<MusicFileMetadataDTO> musicFileMetadataDTOList(List<MusicFiles> musicFilesList);

    MusicFiles toMusicFiles(MusicFileMetadataDTO musicFileMetadataDTO);

    List<MusicFiles> toMusicFilesList(List<MusicFileMetadataDTO> musicFileMetadataDTOList);

    MusicGenreDTO toMusicGenreDTO(MusicGenres musicGenres);

    List<MusicGenreDTO> toMusicGenreDTOList(List<MusicGenres> musicGenresList);

    MusicGenres toMusicGenres(MusicGenreDTO musicGenreDTO);

    List<MusicGenres> toMusicGenresList(List<MusicGenreDTO> musicGenreDTOList);
}
