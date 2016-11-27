package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.dto.*;
import pl.polsl.mapper.MusicMapper;
import pl.polsl.model.*;
import pl.polsl.repository.*;
import pl.polsl.repository.custom.UsersRepositoryCustom;
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
    private MusicFilesRepository musicFilesRepository;

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Autowired
    private SongsRepository songsRepository;

    @Autowired
    private MusicAuthorsRepository musicAuthorsRepository;

    @Autowired
    private MusicMapper musicMapper;

    @Override
    public List<MusicArtistDTO> getArtistsByPrediction(String name, String name2, String surname) {
        List<MusicArtistDTO> result;
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        name = "%" + name + "%";
        List<MusicArtists> artists = musicArtistsRepository.findByNameLike(name);
        if (artists == null) {
            artists = new ArrayList<>();
        }
        artists.addAll(musicArtistsRepository.findByName2Like(name));
        artists.addAll(musicArtistsRepository.findBySurnameLike(name));
        if (!StringUtils.isEmpty(surname) && !surname.equals("undefined")) {
            surname = "%" + surname + "%";
            artists = musicArtistsRepository.findByNameLikeAndSurnameLike(name, surname);
            if (artists == null) {
                artists = new ArrayList<>();
            }
            artists.addAll(musicArtistsRepository.findByNameLikeAndName2Like(name, surname));
        }
        if (!StringUtils.isEmpty(name2) && !name2.equals("undefined")) {
            name2 = "%" + name2 + "%";
            artists = musicArtistsRepository.findByNameLikeAndName2Like(name, name2);
            if (artists == null) {
                artists = new ArrayList<>();
            }
            artists.addAll(musicArtistsRepository.findByNameLikeAndSurnameLike(name, name2));
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
        albumTitle = "%" + albumTitle + "%";
        List<MusicAlbums> albumsList = musicAlbumsRepository.findByAlbumTitleLike(albumTitle);
        if (!StringUtils.isEmpty(songTitle)) {
            if (albumsList == null) {
                albumsList = new ArrayList<>();
            }
            songTitle = "%" + songTitle + "%";
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
        genreName = "%" + genreName + "%";
        List<MusicGenres> musicGenresList = musicGenresRepository.findByNameLike(genreName);
        return musicMapper.toMusicGenreDTOList(musicGenresList);
    }

    @Override
    public UploadSongMetadataDTO saveMetadata(UploadSongMetadataDTO uploadSongMetadataDTO) {
        UsersView user = usersRepository.findUsersByUserName(uploadSongMetadataDTO.getUsername());
        if (user == null) {
            return null;
        }
        SongDTO songDTO = uploadSongMetadataDTO.getSongDTO();
        if (songDTO == null) {
            return null;
        }
        Songs songs = musicMapper.toSongs(songDTO);
        List<MusicArtists> MusicArtistsList = musicMapper.toMusicArtistsList(songDTO.getAuthors());
        if (songDTO.getGenre() != null && songDTO.getGenre().getMusicGenreId() == null) {
            MusicGenres genre = musicGenresRepository.save(musicMapper.toMusicGenres(songDTO.getGenre()));
            songs.setMusicGenresByMusicGenreId(genre);
        }
        if (songDTO.getAlbum() != null && songDTO.getAlbum().getAlbumId() == null) {
            MusicAlbums album = musicAlbumsRepository.save(musicMapper.toMusicAlbums(songDTO.getAlbum()));
            songs.setMusicAlbumsByAlbumId(album);
        }
        if (songDTO.getFileMetadata() != null) {
            MusicFiles musicFiles = musicFilesRepository.save(musicMapper.toMusicFiles(songDTO.getFileMetadata()));
            songs.setMusicFilesByFileId(musicFiles);
        }
        songs = songsRepository.save(songs);
        for (MusicArtists musicArtists : MusicArtistsList) {
            if (musicArtists.getAuthorId() == null) {
                musicArtists = musicArtistsRepository.save(musicArtists);
            }
            MusicAuthors musicAuthors = new MusicAuthors();
            musicAuthors.setSongsBySongId(songs);
            musicAuthors.setSongId(songs.getSongId());
            musicAuthors.setMusicArtistsByAuthorId(musicArtists);
            musicAuthors.setAuthorId(musicArtists.getAuthorId());
            musicAuthorsRepository.save(musicAuthors);
        }
        List<MusicAuthors> musicAuthors = musicAuthorsRepository.findBySongId(songs.getSongId());
        songs.setMusicAuthorsesBySongId(musicAuthors);

        songsRepository.save(songs);

        SongDTO toSongDTO = musicMapper.toSongDTO(songs);
        UploadSongMetadataDTO result = new UploadSongMetadataDTO();
        result.setSongDTO(toSongDTO);
        result.setUsername(user.getUserName());
        return result;
    }

}
