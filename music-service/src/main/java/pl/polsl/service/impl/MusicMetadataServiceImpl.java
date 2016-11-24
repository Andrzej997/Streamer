package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.dto.MusicAlbumDTO;
import pl.polsl.dto.MusicArtistDTO;
import pl.polsl.dto.MusicGenreDTO;
import pl.polsl.mapper.MusicMapper;
import pl.polsl.model.MusicAlbums;
import pl.polsl.model.MusicArtists;
import pl.polsl.model.MusicGenres;
import pl.polsl.repository.MusicAlbumsRepository;
import pl.polsl.repository.MusicArtistsRepository;
import pl.polsl.repository.MusicGenresRepository;
import pl.polsl.service.MusicMetadataService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 23.11.2016.
 */
@Service
@Transactional
public class MusicMetadataServiceImpl implements MusicMetadataService {

    @Autowired
    private MusicArtistsRepository musicArtistsRepository;

    @Autowired
    private MusicAlbumsRepository musicAlbumsRepository;

    @Autowired
    private MusicGenresRepository musicGenresRepository;

    @Autowired
    private MusicMapper musicMapper;

    @Override
    public List<MusicArtistDTO> getArtistsByPrediction(String name, String name2, String surname) {
        List<MusicArtistDTO> result;
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        List<MusicArtists> artists = musicArtistsRepository.findByNameLike(name);
        if (artists == null) {
            artists = new ArrayList<>();
        }
        artists.addAll(musicArtistsRepository.findByName2Like(name));
        artists.addAll(musicArtistsRepository.findBySurnameLike(name));
        if (!StringUtils.isEmpty(name2)) {
            artists = musicArtistsRepository.findByNameLikeAndName2Like(name, name2);
            if (artists == null) {
                artists = new ArrayList<>();
            }
            artists.addAll(musicArtistsRepository.findByNameLikeAndSurnameLike(name, name2));
        }
        if (!StringUtils.isEmpty(surname)) {
            artists = musicArtistsRepository.findByNameLikeAndName2LikeAndSurnameLike(name, name2, surname);
        }
        result = musicMapper.toMusicArtistDTOList(artists);
        return result;
    }

    @Override
    public List<MusicAlbumDTO> getAlbumsByPrediction(String albumTitle, String songTitle) {
        List<MusicAlbumDTO> result;
        if (StringUtils.isEmpty(albumTitle)) {
            return null;
        }
        List<MusicAlbums> albumsList = musicAlbumsRepository.findByAlbumTitleLike(albumTitle);
        if (!StringUtils.isEmpty(songTitle)) {
            if (albumsList == null) {
                albumsList = new ArrayList<>();
            }
            albumsList.addAll(musicAlbumsRepository.findBySongTitle(songTitle));
        }
        return musicMapper.toMusicAlbumDTOList(albumsList);
    }

    @Override
    public List<MusicGenreDTO> getGenresByPrediction(String genreName) {
        List<MusicGenreDTO> result;
        if (StringUtils.isEmpty(genreName)) {
            return null;
        }
        List<MusicGenres> musicGenresList = musicGenresRepository.findByNameLike(genreName);
        return musicMapper.toMusicGenreDTOList(musicGenresList);
    }
}
