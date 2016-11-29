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
        if (uploadSongMetadataDTO == null || uploadSongMetadataDTO.getSongDTO() == null
                || uploadSongMetadataDTO.getSongDTO().getFileId() == null) {
            return null;
        }
        UsersView user = usersRepository.findUsersByUserName(uploadSongMetadataDTO.getUsername());
        if (user == null) {
            return null;
        }
        SongDTO songDTO = uploadSongMetadataDTO.getSongDTO();
        if (songDTO == null) {
            return null;
        }
        Songs songs = musicMapper.toSongs(songDTO);
        List<MusicArtists> musicArtistsList = musicMapper.toMusicArtistsList(songDTO.getAuthors());
        saveMusicGenreForSong(songDTO, songs);
        saveMusicAlbumForSong(songDTO, songs);
        saveMusicFileMetadataForSong(songDTO, songs);
        songs.setOwnerId(user.getUserId());
        songs = songsRepository.save(songs);
        saveMusicArtistsForSong(songs, musicArtistsList);

        List<MusicAuthors> musicAuthors = musicAuthorsRepository.findBySongId(songs.getSongId());
        songs.setMusicAuthorsesBySongId(musicAuthors);

        songs = songsRepository.save(songs);

        SongDTO toSongDTO = musicMapper.toSongDTO(songs);
        UploadSongMetadataDTO result = new UploadSongMetadataDTO();
        result.setSongDTO(toSongDTO);
        result.setUsername(user.getUserName());
        return result;
    }

    private void saveMusicGenreForSong(SongDTO songDTO, Songs songs) {
        MusicGenres genre = null;
        if (songDTO.getGenre() != null && songDTO.getGenre().getMusicGenreId() == null) {
            genre = musicGenresRepository.save(musicMapper.toMusicGenres(songDTO.getGenre()));
        } else if (songDTO.getGenre() != null) {
            genre = musicMapper.toMusicGenres(songDTO.getGenre());
        }
        if (genre == null) {
            return;
        }
        songs.setMusicGenresByMusicGenreId(genre);
        songs.setMusicGenreId(genre.getMusicGenreId());
    }

    private void saveMusicAlbumForSong(SongDTO songDTO, Songs songs) {
        MusicAlbums album = null;
        if (songDTO.getAlbum() != null && songDTO.getAlbum().getAlbumId() == null) {
            album = musicAlbumsRepository.save(musicMapper.toMusicAlbums(songDTO.getAlbum()));
        } else if (songDTO.getAlbum() != null) {
            album = musicMapper.toMusicAlbums(songDTO.getAlbum());
        }
        if (album == null) {
            return;
        }
        songs.setMusicAlbumsByAlbumId(album);
        songs.setAlbumId(album.getAlbumId());
    }

    private void saveMusicFileMetadataForSong(SongDTO songDTO, Songs songs) {
        MusicFiles musicFiles = null;
        if (songDTO.getFileMetadata() != null) {
            musicFiles = musicFilesRepository.save(musicMapper.toMusicFiles(songDTO.getFileMetadata()));
        }
        if (musicFiles == null) {
            return;
        }
        songs.setMusicFilesByFileId(musicFiles);
        songs.setFileId(musicFiles.getMusicFileId());
    }

    private void saveMusicArtistsForSong(Songs songs, List<MusicArtists> musicArtistsList) {
        if (musicArtistsList == null) {
            return;
        }
        for (MusicArtists musicArtists : musicArtistsList) {
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
    }

}
