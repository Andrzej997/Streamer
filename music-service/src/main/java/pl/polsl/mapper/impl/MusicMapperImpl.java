package pl.polsl.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.polsl.dto.*;
import pl.polsl.mapper.MusicMapper;
import pl.polsl.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mateusz on 24.11.2016.
 */
@Component
public class MusicMapperImpl implements MusicMapper {

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public MusicAlbumDTO toMusicAlbumDTO(MusicAlbums musicAlbum) {
        if (musicAlbum == null) {
            return null;
        }
        return this.modelMapper.map(musicAlbum, MusicAlbumDTO.class);
    }

    @Override
    public List<MusicAlbumDTO> toMusicAlbumDTOList(List<MusicAlbums> musicAlbumList) {
        if (musicAlbumList == null) {
            return null;
        }
        List<MusicAlbumDTO> result = new ArrayList<>();
        musicAlbumList.forEach(musicAlbums -> result.add(this.toMusicAlbumDTO(musicAlbums)));
        return result;
    }

    @Override
    public MusicAlbums toMusicAlbums(MusicAlbumDTO musicAlbumDTO) {
        if (musicAlbumDTO == null) {
            return null;
        }
        return this.modelMapper.map(musicAlbumDTO, MusicAlbums.class);
    }

    @Override
    public List<MusicAlbums> toMusicAlbumsList(List<MusicAlbumDTO> musicAlbumDTOList) {
        if (musicAlbumDTOList == null) {
            return null;
        }
        List<MusicAlbums> result = new ArrayList<>();
        musicAlbumDTOList.forEach(musicAlbumDTO -> result.add(this.toMusicAlbums(musicAlbumDTO)));
        return result;
    }

    @Override
    public MusicArtistDTO toMusicArtistDTO(MusicArtists musicArtist) {
        if (musicArtist == null) {
            return null;
        }
        return this.modelMapper.map(musicArtist, MusicArtistDTO.class);
    }

    @Override
    public List<MusicArtistDTO> toMusicArtistDTOList(List<MusicArtists> musicArtistsList) {
        if (musicArtistsList == null) {
            return null;
        }
        List<MusicArtistDTO> result = new ArrayList<>();
        musicArtistsList.forEach(musicArtists -> result.add(this.toMusicArtistDTO(musicArtists)));
        return result;
    }

    @Override
    public MusicArtists toMusicArtists(MusicArtistDTO musicArtistDTO) {
        if (musicArtistDTO == null) {
            return null;
        }
        return this.modelMapper.map(musicArtistDTO, MusicArtists.class);
    }

    @Override
    public List<MusicArtists> toMusicArtistsList(List<MusicArtistDTO> musicArtistDTOList) {
        if (musicArtistDTOList == null) {
            return null;
        }
        List<MusicArtists> result = new ArrayList<>();
        musicArtistDTOList.forEach(musicArtistDTO -> result.add(this.toMusicArtists(musicArtistDTO)));
        return result;
    }

    @Override
    public MusicFileMetadataDTO toMusicFileMetadataDTO(MusicFiles musicFile) {
        if (musicFile == null) {
            return null;
        }
        return this.modelMapper.map(musicFile, MusicFileMetadataDTO.class);
    }

    @Override
    public List<MusicFileMetadataDTO> musicFileMetadataDTOList(List<MusicFiles> musicFilesList) {
        if (musicFilesList == null) {
            return null;
        }
        List<MusicFileMetadataDTO> result = new ArrayList<>();
        musicFilesList.forEach(musicFiles -> result.add(this.toMusicFileMetadataDTO(musicFiles)));
        return result;
    }

    @Override
    public MusicFiles toMusicFiles(MusicFileMetadataDTO musicFileMetadataDTO) {
        if (musicFileMetadataDTO == null) {
            return null;
        }
        return this.modelMapper.map(musicFileMetadataDTO, MusicFiles.class);
    }

    @Override
    public List<MusicFiles> toMusicFilesList(List<MusicFileMetadataDTO> musicFileMetadataDTOList) {
        if (musicFileMetadataDTOList == null) {
            return null;
        }
        List<MusicFiles> result = new ArrayList<>();
        musicFileMetadataDTOList.forEach(musicFileMetadataDTO -> result.add(this.toMusicFiles(musicFileMetadataDTO)));
        return result;
    }

    @Override
    public MusicGenreDTO toMusicGenreDTO(MusicGenres musicGenres) {
        if (musicGenres == null) {
            return null;
        }
        return this.modelMapper.map(musicGenres, MusicGenreDTO.class);
    }

    @Override
    public List<MusicGenreDTO> toMusicGenreDTOList(List<MusicGenres> musicGenresList) {
        if (musicGenresList == null) {
            return null;
        }
        List<MusicGenreDTO> result = new ArrayList<>();
        musicGenresList.forEach(musicGenres -> result.add(this.toMusicGenreDTO(musicGenres)));
        return result;
    }

    @Override
    public MusicGenres toMusicGenres(MusicGenreDTO musicGenreDTO) {
        if (musicGenreDTO == null) {
            return null;
        }
        return this.modelMapper.map(musicGenreDTO, MusicGenres.class);
    }

    @Override
    public List<MusicGenres> toMusicGenresList(List<MusicGenreDTO> musicGenreDTOList) {
        if (musicGenreDTOList == null) {
            return null;
        }
        List<MusicGenres> result = new ArrayList<>();
        musicGenreDTOList.forEach(musicGenreDTO -> result.add(this.toMusicGenres(musicGenreDTO)));
        return result;
    }

    @Override
    public Songs toSongs(SongDTO songDTO) {
        if (songDTO == null) {
            return null;
        }
        MusicAlbumDTO musicAlbumDTO = songDTO.getAlbum();
        MusicFileMetadataDTO musicFileMetadataDTO = songDTO.getFileMetadata();
        MusicGenreDTO musicGenreDTO = songDTO.getGenre();
        Songs songs = new Songs();
        songs.setMusicAlbumsByAlbumId(this.toMusicAlbums(musicAlbumDTO));
        songs.setMusicFilesByFileId(this.toMusicFiles(musicFileMetadataDTO));
        songs.setMusicGenresByMusicGenreId(this.toMusicGenres(musicGenreDTO));
        songs.setTitle(songDTO.getTitle());
        songs.setAlbumId(musicAlbumDTO != null ? musicAlbumDTO.getAlbumId() : null);
        songs.setFileId(musicFileMetadataDTO != null ? musicFileMetadataDTO.getMusicFileId() : null);
        songs.setMusicGenreId(musicGenreDTO != null ? musicGenreDTO.getMusicGenreId() : null);
        songs.setOwnerId(songDTO.getOwnerId());
        songs.setProductionYear(songDTO.getProductionYear());
        songs.setRating(songDTO.getRating());
        songs.setSongId(songDTO.getSongId());
        return songs;
    }

    @Override
    public SongDTO toSongDTO(Songs songs) {
        if (songs == null) {
            return null;
        }
        SongDTO songDTO = new SongDTO();
        songDTO.setSongId(songs.getSongId());
        songDTO.setTitle(songs.getTitle());
        songDTO.setFileId(songs.getFileId());
        songDTO.setAuthorId(songs.getAuthorId());
        songDTO.setAlbumId(songs.getAlbumId());
        songDTO.setMusicGenreId(songs.getMusicGenreId());
        songDTO.setRating(songs.getRating());
        songDTO.setProductionYear(songs.getProductionYear());
        songDTO.setOwnerId(songs.getOwnerId());
        Collection<MusicAuthors> musicAuthorsesBySongId = songs.getMusicAuthorsesBySongId();
        List<MusicArtists> artists = new ArrayList<>();
        for (MusicAuthors musicAuthors : musicAuthorsesBySongId) {
            artists.add(musicAuthors.getMusicArtistsByAuthorId());
        }
        songDTO.setAuthors(this.toMusicArtistDTOList(artists));
        songDTO.setFileMetadata(this.toMusicFileMetadataDTO(songs.getMusicFilesByFileId()));
        songDTO.setGenre(this.toMusicGenreDTO(songs.getMusicGenresByMusicGenreId()));
        songDTO.setAlbum(this.toMusicAlbumDTO(songs.getMusicAlbumsByAlbumId()));
        return songDTO;
    }

}
