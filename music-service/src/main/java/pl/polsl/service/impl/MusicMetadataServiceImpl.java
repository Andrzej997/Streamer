package pl.polsl.service.impl;

import org.apache.commons.lang.math.NumberUtils;
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
import java.util.stream.Collectors;

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
        if (songDTO.getGenre() != null && !StringUtils.isEmpty(songDTO.getGenre().getName())) {
            genre = musicGenresRepository.save(musicMapper.toMusicGenres(songDTO.getGenre()));
        }
        if (genre == null) {
            return;
        }
        songs.setMusicGenresByMusicGenreId(genre);
        songs.setMusicGenreId(genre.getMusicGenreId());
    }

    private void saveMusicAlbumForSong(SongDTO songDTO, Songs songs) {
        MusicAlbums album = null;
        if (songDTO.getAlbum() != null && !StringUtils.isEmpty(songDTO.getAlbum().getAlbumTitle())) {
            album = musicAlbumsRepository.save(musicMapper.toMusicAlbums(songDTO.getAlbum()));
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

            musicArtists = musicArtistsRepository.save(musicArtists);

            MusicAuthors musicAuthors = new MusicAuthors();
            musicAuthors.setSongsBySongId(songs);
            musicAuthors.setSongId(songs.getSongId());
            musicAuthors.setMusicArtistsByAuthorId(musicArtists);
            musicAuthors.setAuthorId(musicArtists.getAuthorId());
            musicAuthorsRepository.save(musicAuthors);
        }
    }

    @Override
    public List<SongDTO> getTop10Songs(String username, String title) {
        List<Songs> songsList = null;
        UsersView user = null;
        if (!StringUtils.isEmpty(username)) {
            user = usersRepository.findUsersByUserName(username);
        }
        if (user == null) {
            if (StringUtils.isEmpty(title)) {
                songsList = songsRepository.findByIsPublicOrderByRatingDesc(true);
            } else {
                title = "%" + title + "%";
                songsList = songsRepository.findByIsPublicAndTitleLikeOrderByRatingDesc(true, title);
            }
        } else {
            if (StringUtils.isEmpty(title)) {
                songsList = songsRepository.findByIsPublicAndOwnerIdOrderByRatingDesc(false, user.getUserId());
            } else {
                title = "%" + title + "%";
                songsList = songsRepository.findByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(false, user.getUserId(), title);
            }
        }
        if (songsList == null) {
            return null;
        }
        List<SongDTO> songDTOList = musicMapper.toSongDTOList(songsList);
        if (songDTOList == null || songDTOList.isEmpty()) {
            return null;
        } else {
            return songDTOList.subList(0, songDTOList.size() > 9 ? 9 : songDTOList.size());
        }
    }

    @Override
    public List<SongDTO> getAllUserSongs(String username) {
        UsersView user = usersRepository.findUsersByUserName(username);
        if (user == null) {
            return null;
        }
        List<Songs> songsList = songsRepository.findByOwnerId(user.getUserId());
        return musicMapper.toSongDTOList(songsList);
    }

    @Override
    public List<SongDTO> searchSongsByCriteria(SearchSongCriteriaDTO searchSongCriteriaDTO) {
        if (searchSongCriteriaDTO == null || StringUtils.isEmpty(searchSongCriteriaDTO.getCriteria())) {
            return null;
        }
        String criteria = searchSongCriteriaDTO.getCriteria();
        String textSearched = searchSongCriteriaDTO.getTextSearched();
        switch (criteria) {
            case "T":
                return getSongsByTitle(textSearched);
            case "A":
                return getSongsByAuthor(textSearched);
            case "G":
                return getSongsByGenreName(textSearched);
            case "Y":
                return getSongsByYear(textSearched);
            case "ALL":
                return getSongsByAllCriteria(textSearched);
        }
        return null;
    }

    private List<SongDTO> getSongsByTitle(String title) {
        if (title == null || "undefined".equals(title)) {
            return null;
        }
        List<Songs> songsList = songsRepository.findByTitleOrderByRating(title);
        if (songsList == null) {
            songsList = new ArrayList<>();
        }
        title += "%";
        songsList.addAll(songsRepository.findByTitleLikeOrderByRating(title));
        songsList = songsList.stream().filter(songs ->
                (songs.getMusicFilesByFileId() != null && songs.getMusicFilesByFileId().getPublic()))
                .collect(Collectors.toList());
        return musicMapper.toSongDTOList(songsList);
    }

    private List<SongDTO> getSongsByAuthor(String authorData) {
        if (authorData == null || "undefined".equals(authorData)) {
            return null;
        }
        String[] data = authorData.split(" ");
        if (data == null || data.length <= 0) {
            return null;
        }
        String name = data[0];
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        List<Songs> result = null;
        switch (data.length) {
            case 1:
                result = songsRepository.findByArtistNameLikeOrderByRating(name);
                break;
            case 2:
                result = songsRepository.findByArtistNameLikeAndSurnameLikeOrderByRating(name, data[1]);
                break;
            case 3:
                result = songsRepository.findByArtistNameLikeAndSurnameLikeAndName2LikeOrderByRating(name, data[1], data[2]);
                break;
            default:
                break;
        }
        result = result.stream().filter(songs ->
                (songs.getMusicFilesByFileId() != null && songs.getMusicFilesByFileId().getPublic()))
                .collect(Collectors.toList());
        ;
        return musicMapper.toSongDTOList(result);
    }

    private List<SongDTO> getSongsByGenreName(String name) {
        if (name == null || "undefined".equals(name)) {
            return null;
        }
        List<Songs> songsList = songsRepository.findByGenreNameOrderByRating(name);
        if (songsList == null) {
            songsList = new ArrayList<>();
        }
        name += "%";
        songsList = songsRepository.findByGenreNameLikeOrderByRating(name);
        songsList = songsList.stream().filter(songs ->
                (songs.getMusicFilesByFileId() != null && songs.getMusicFilesByFileId().getPublic()))
                .collect(Collectors.toList());
        return musicMapper.toSongDTOList(songsList);
    }

    private List<SongDTO> getSongsByYear(String year) {
        if (year == null || "undefined".equals(year) || !NumberUtils.isNumber(year)) {
            return null;
        }
        Short yearNumber = Short.parseShort(year);
        List<Songs> songsList = songsRepository.findByProductionYearOrderByRating(yearNumber);
        songsList = songsList.stream().filter(songs ->
                (songs.getMusicFilesByFileId() != null && songs.getMusicFilesByFileId().getPublic()))
                .collect(Collectors.toList());
        return musicMapper.toSongDTOList(songsList);
    }

    private List<SongDTO> getSongsByAllCriteria(String searchText) {
        if (StringUtils.isEmpty(searchText)) {
            return null;
        }
        List<SongDTO> result = new ArrayList<>();
        List<SongDTO> songsByTitle = getSongsByTitle(searchText);
        List<SongDTO> songsByYear = getSongsByYear(searchText);
        List<SongDTO> songsByAuthor = getSongsByAuthor(searchText);
        List<SongDTO> songsByGenreName = getSongsByGenreName(searchText);
        if (songsByTitle != null && !songsByTitle.isEmpty()) {
            result.addAll(songsByTitle);
        }
        if (songsByYear != null && !songsByYear.isEmpty()) {
            result.addAll(songsByYear);
        }
        if (songsByAuthor != null && !songsByAuthor.isEmpty()) {
            result.addAll(songsByAuthor);
        }
        if (songsByGenreName != null && !songsByGenreName.isEmpty()) {
            result.addAll(songsByGenreName);
        }
        return result;
    }

    @Override
    public SongDTO updateSongMetadata(SongDTO songDTO) {
        Songs songs = musicMapper.toSongs(songDTO);
        List<MusicArtists> musicArtistsList = musicMapper.toMusicArtistsList(songDTO.getAuthors());
        saveMusicGenreForSong(songDTO, songs);
        saveMusicAlbumForSong(songDTO, songs);
        saveMusicFileMetadataForSong(songDTO, songs);
        songs = songsRepository.save(songs);
        saveMusicArtistsForSong(songs, musicArtistsList);

        List<MusicAuthors> musicAuthors = musicAuthorsRepository.findBySongId(songs.getSongId());
        songs.setMusicAuthorsesBySongId(musicAuthors);

        songs = songsRepository.save(songs);

        SongDTO result = musicMapper.toSongDTO(songs);
        return result;
    }

    @Override
    public List<SongDTO> getSongsTop50() {
        Iterable<Songs> all = songsRepository.findAllByOrderByRatingDesc();
        if (all == null) {
            return null;
        }
        List<Songs> allSongs = new ArrayList<>();
        all.forEach(songs -> {
            if (songs.getMusicFilesByFileId() != null && songs.getMusicFilesByFileId().getPublic()) {
                allSongs.add(songs);
            }
        });
        allSongs.sort((o1, o2) -> compareFloat(o1.getRating(), o2.getRating()));
        List<SongDTO> result = musicMapper.toSongDTOList(allSongs);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.subList(0, result.size() > 49 ? 49 : result.size());
        }
    }

    private int compareFloat(Float f1, Float f2) {
        if (f1 == null && f2 == null) {
            return 0;
        }
        if (f1 == null) {
            return 1;
        }
        if (f2 == null) {
            return -1;
        }
        return f2.compareTo(f1);
    }

    public MusicArtistsRepository getMusicArtistsRepository() {
        return musicArtistsRepository;
    }

    public void setMusicArtistsRepository(MusicArtistsRepository musicArtistsRepository) {
        this.musicArtistsRepository = musicArtistsRepository;
    }

    public MusicAlbumsRepository getMusicAlbumsRepository() {
        return musicAlbumsRepository;
    }

    public void setMusicAlbumsRepository(MusicAlbumsRepository musicAlbumsRepository) {
        this.musicAlbumsRepository = musicAlbumsRepository;
    }

    public MusicGenresRepository getMusicGenresRepository() {
        return musicGenresRepository;
    }

    public void setMusicGenresRepository(MusicGenresRepository musicGenresRepository) {
        this.musicGenresRepository = musicGenresRepository;
    }

    public MusicFilesRepository getMusicFilesRepository() {
        return musicFilesRepository;
    }

    public void setMusicFilesRepository(MusicFilesRepository musicFilesRepository) {
        this.musicFilesRepository = musicFilesRepository;
    }

    public UsersRepositoryCustom getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepositoryCustom usersRepository) {
        this.usersRepository = usersRepository;
    }

    public SongsRepository getSongsRepository() {
        return songsRepository;
    }

    public void setSongsRepository(SongsRepository songsRepository) {
        this.songsRepository = songsRepository;
    }

    public MusicAuthorsRepository getMusicAuthorsRepository() {
        return musicAuthorsRepository;
    }

    public void setMusicAuthorsRepository(MusicAuthorsRepository musicAuthorsRepository) {
        this.musicAuthorsRepository = musicAuthorsRepository;
    }

    public MusicMapper getMusicMapper() {
        return musicMapper;
    }

    public void setMusicMapper(MusicMapper musicMapper) {
        this.musicMapper = musicMapper;
    }
}
