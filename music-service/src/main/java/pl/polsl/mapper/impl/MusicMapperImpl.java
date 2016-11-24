package pl.polsl.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.polsl.dto.MusicAlbumDTO;
import pl.polsl.dto.MusicArtistDTO;
import pl.polsl.dto.MusicFileMetadataDTO;
import pl.polsl.dto.MusicGenreDTO;
import pl.polsl.mapper.MusicMapper;
import pl.polsl.model.MusicAlbums;
import pl.polsl.model.MusicArtists;
import pl.polsl.model.MusicFiles;
import pl.polsl.model.MusicGenres;

import java.util.ArrayList;
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
}
