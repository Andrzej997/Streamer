package pl.polsl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.polsl.dto.*;
import pl.polsl.mapper.MusicMapper;
import pl.polsl.model.*;
import pl.polsl.repository.*;
import pl.polsl.repository.custom.UsersRepositoryCustom;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Mateusz on 08.01.2017.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"bootstrap.yml"})
public class MusicMetadataServiceTests {

    @Autowired
    @InjectMocks
    private MusicMetadataService musicMetadataService;

    @Autowired
    @Spy
    private MusicMapper musicMapper;

    @Mock
    private UsersRepositoryCustom usersRepository;

    @Mock
    private MusicArtistsRepository musicArtistsRepository;

    @Mock
    private MusicGenresRepository musicGenresRepository;

    @Mock
    private MusicAuthorsRepository musicAuthorsRepository;

    @Mock
    private MusicAlbumsRepository musicAlbumsRepository;

    @Mock
    private MusicFilesRepository musicFilesRepository;

    @Mock
    private SongsRepository songsRepository;

    @Test
    public void testGetArtistsByPrediction_whenNameIsNull() {
        String name = null;
        String name2 = null;
        String surname = null;

        List<MusicArtistDTO> result = musicMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNull();
    }

    @Test
    public void testGetArtistsByPrediction_whenNameExists() {
        String name = "test";
        String name2 = null;
        String surname = null;
        ArrayList<MusicArtists> artistses = new ArrayList<>();
        artistses.add(new MusicArtists());

        when(musicArtistsRepository.findByNameLike(name)).thenReturn(artistses);
        when(musicArtistsRepository.findByName2Like(name)).thenReturn(null);
        when(musicArtistsRepository.findBySurnameLike(name)).thenReturn(null);

        List<MusicArtistDTO> result = musicMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetArtistsByPrediction_whenSurnameExists() {
        String name = "test";
        String name2 = null;
        String surname = "test";
        ArrayList<MusicArtists> artistses = new ArrayList<>();
        artistses.add(new MusicArtists());

        when(musicArtistsRepository.findByNameLike(name)).thenReturn(artistses);
        when(musicArtistsRepository.findByName2Like(name)).thenReturn(null);
        when(musicArtistsRepository.findBySurnameLike(name)).thenReturn(null);

        List<MusicArtistDTO> result = musicMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetArtistsByPrediction_whenName2Exists() {
        String name = "test";
        String name2 = "test";
        String surname = null;
        ArrayList<MusicArtists> artistses = new ArrayList<>();
        artistses.add(new MusicArtists());

        when(musicArtistsRepository.findByNameLike(name)).thenReturn(artistses);
        when(musicArtistsRepository.findByName2Like(name)).thenReturn(null);
        when(musicArtistsRepository.findBySurnameLike(name)).thenReturn(null);

        List<MusicArtistDTO> result = musicMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetArtistsByPrediction_whenEverythingIsSet() {
        String name = "test";
        String name2 = "test";
        String surname = "test";
        ArrayList<MusicArtists> artistses = new ArrayList<>();
        artistses.add(new MusicArtists());

        when(musicArtistsRepository.findByNameLike(name)).thenReturn(artistses);
        when(musicArtistsRepository.findByName2Like(name)).thenReturn(null);
        when(musicArtistsRepository.findBySurnameLike(name)).thenReturn(null);

        List<MusicArtistDTO> result = musicMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetGenresByPrediction_whenNameIsNull() {
        String name = null;

        List<MusicGenreDTO> result = musicMetadataService.getGenresByPrediction(name);

        assertThat(result).isNull();
    }

    @Test
    public void testGetGenresByPrediction_whenNameExists() {
        String name = "test";
        List<MusicGenres> musicGenresList = new ArrayList<>();
        musicGenresList.add(new MusicGenres());
        when(musicGenresRepository.findByNameLike(name)).thenReturn(musicGenresList);

        List<MusicGenreDTO> result = musicMetadataService.getGenresByPrediction(name);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetAlbumsByPrediction_whenAlbumTitleIsNull() {
        String albumTitle = null;
        String songTitle = "test";

        List<MusicAlbumDTO> result = musicMetadataService.getAlbumsByPrediction(albumTitle, songTitle);

        assertThat(result).isNull();
    }

    @Test
    public void testGetAlbumsByPrediction_whenSongTitleIsNull() {
        String albumTitle = "test";
        String songTitle = null;
        MusicAlbums musicAlbums = new MusicAlbums();
        musicAlbums.setAlbumId(1L);
        List<MusicAlbums> albumsList = new ArrayList<>();
        albumsList.add(musicAlbums);
        when(musicAlbumsRepository.findByAlbumTitleLike("%" + albumTitle + "%")).thenReturn(albumsList);

        List<MusicAlbumDTO> result = musicMetadataService.getAlbumsByPrediction(albumTitle, songTitle);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getAlbumId()).isEqualTo(1L);
    }

    @Test
    public void testGetAlbumsByPrediction_whenEverythingIsSet() {
        String albumTitle = "test";
        String songTitle = "test";
        MusicAlbums musicAlbums = new MusicAlbums();
        musicAlbums.setAlbumId(1L);
        List<MusicAlbums> albumsList = new ArrayList<>();
        albumsList.add(musicAlbums);
        when(musicAlbumsRepository.findByAlbumTitleLike("%" + albumTitle + "%")).thenReturn(albumsList);
        when(musicAlbumsRepository.findBySongTitle("%" + songTitle + "%")).thenReturn(null);

        List<MusicAlbumDTO> result = musicMetadataService.getAlbumsByPrediction(albumTitle, songTitle);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getAlbumId()).isEqualTo(1L);
    }

    @Test
    public void testSaveMetadata_whenUploadSongMetadataDTOIsNull() {
        UploadSongMetadataDTO uploadSongMetadataDTO = null;

        UploadSongMetadataDTO result = musicMetadataService.saveMetadata(uploadSongMetadataDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSaveMetadata_whenSongDTOIsNull() {
        UploadSongMetadataDTO uploadSongMetadataDTO = new UploadSongMetadataDTO();
        uploadSongMetadataDTO.setUsername("test");
        UsersView usersView = new UsersView();
        usersView.setUserName(uploadSongMetadataDTO.getUsername());
        usersView.setUserId(1L);
        when(usersRepository.findUsersByUserName(uploadSongMetadataDTO.getUsername())).thenReturn(usersView);

        UploadSongMetadataDTO result = musicMetadataService.saveMetadata(uploadSongMetadataDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSaveMetadata_whenMusicArtistListIsEmpty() {
        UploadSongMetadataDTO uploadSongMetadataDTO = new UploadSongMetadataDTO();
        uploadSongMetadataDTO.setUsername("test");
        UsersView usersView = new UsersView();
        usersView.setUserName(uploadSongMetadataDTO.getUsername());
        usersView.setUserId(1L);
        SongDTO songDTO = new SongDTO();
        songDTO.setOwnerId(usersView.getUserId());
        uploadSongMetadataDTO.setSongDTO(songDTO);
        Songs songs = musicMapper.toSongs(uploadSongMetadataDTO.getSongDTO());
        when(usersRepository.findUsersByUserName(uploadSongMetadataDTO.getUsername())).thenReturn(usersView);
        when(songsRepository.save(songs)).thenReturn(createSong(songs));

        UploadSongMetadataDTO result = musicMetadataService.saveMetadata(uploadSongMetadataDTO);

        assertThat(result).isNull();
    }

    private Songs createSong(Songs song) {
        song.setSongId(1L);
        return song;
    }

    @Test
    public void testGetTop10Songs_whenUsernameAndTitleIsNull() {
        String username = null;
        String title = null;
        Songs songs = new Songs();
        songs.setSongId(1L);
        List<Songs> songsList = new ArrayList<>();
        songsList.add(songs);
        when(songsRepository.findByIsPublicOrderByRatingDesc(true)).thenReturn(songsList);

        List<SongDTO> result = musicMetadataService.getTop10Songs(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(songsList.size());
    }

    @Test
    public void testGetTop10Songs_whenUsernameIsNull() {
        String username = null;
        String title = "test";
        Songs songs = new Songs();
        songs.setSongId(1L);
        List<Songs> songsList = new ArrayList<>();
        songsList.add(songs);
        when(songsRepository.findByIsPublicAndTitleLikeOrderByRatingDesc(true, "%" + title + "%")).thenReturn(songsList);

        List<SongDTO> result = musicMetadataService.getTop10Songs(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(songsList.size());
    }

    @Test
    public void testGetTop10Songs_whenTitleIsNull() {
        String username = "test";
        String title = null;
        Songs songs = new Songs();
        songs.setSongId(1L);
        List<Songs> songsList = new ArrayList<>();
        songsList.add(songs);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(songsRepository.findByIsPublicAndOwnerIdOrderByRatingDesc(false, usersView.getUserId()))
                .thenReturn(songsList);

        List<SongDTO> result = musicMetadataService.getTop10Songs(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(songsList.size());
    }

    @Test
    public void testGetTop10Songs_whenEverythingIsSetted() {
        String username = "test";
        String title = "test";
        Songs songs = new Songs();
        songs.setSongId(1L);
        List<Songs> songsList = new ArrayList<>();
        songsList.add(songs);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);

        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(songsRepository.findByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(false, usersView.getUserId(), "%" + title + "%"))
                .thenReturn(songsList);

        List<SongDTO> result = musicMetadataService.getTop10Songs(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(songsList.size());
    }

    @Test
    public void testGetAllUserSongs_whenUsernameIsNull() {
        String username = null;

        List<SongDTO> result = musicMetadataService.getAllUserSongs(username);

        assertThat(result).isNull();
    }

    @Test
    public void testGetAllUserSongs_whenUsernameIsSet() {
        String username = "test";
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        usersView.setUserName(username);
        Songs songs = new Songs();
        songs.setSongId(1L);
        List<Songs> songsList = new ArrayList<>();
        songsList.add(songs);

        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(songsRepository.findByOwnerId(usersView.getUserId())).thenReturn(songsList);

        List<SongDTO> result = musicMetadataService.getAllUserSongs(username);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    public void testSearchSongsByCriteria_whenSearchSongCriteriaDTOIsNull() {
        SearchSongCriteriaDTO searchSongCriteriaDTO = null;

        List<SongDTO> result = musicMetadataService.searchSongsByCriteria(searchSongCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchSongsByCriteria_whenCriteriaIsNull() {
        SearchSongCriteriaDTO searchSongCriteriaDTO = new SearchSongCriteriaDTO();

        List<SongDTO> result = musicMetadataService.searchSongsByCriteria(searchSongCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchSongsByCriteria_whenTextSearchedIsNull() {
        SearchSongCriteriaDTO searchSongCriteriaDTO = new SearchSongCriteriaDTO();
        searchSongCriteriaDTO.setCriteria("T");

        List<SongDTO> result = musicMetadataService.searchSongsByCriteria(searchSongCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchSongsByCriteria_whenCriteriaIsTitle() {
        SearchSongCriteriaDTO searchSongCriteriaDTO = new SearchSongCriteriaDTO();
        searchSongCriteriaDTO.setCriteria("T");
        searchSongCriteriaDTO.setTextSearched("test");
        Songs songs = new Songs();
        songs.setSongId(1L);
        List<Songs> songsList = new ArrayList<>();
        songsList.add(songs);

        when(songsRepository.findByTitleOrderByRating(searchSongCriteriaDTO.getTextSearched())).thenReturn(songsList);
        when(songsRepository.findByTitleLikeOrderByRating(searchSongCriteriaDTO.getTextSearched() + "%")).thenReturn(null);

        List<SongDTO> result = musicMetadataService.searchSongsByCriteria(searchSongCriteriaDTO);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testUpdateSongMetadata_whenSongDTOIsNull() {
        SongDTO songDTO = null;

        SongDTO result = musicMetadataService.updateSongMetadata(songDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testGetSongsTop50_whenDatabaseIsEmpty() {
        when(songsRepository.findAllByOrderByRatingDesc()).thenReturn(null);

        List<SongDTO> result = musicMetadataService.getSongsTop50();

        assertThat(result).isNull();
    }

    @Test
    public void testGetSongsTop50() {
        Songs songs = new Songs();
        songs.setSongId(1L);
        songs.setFileId(1L);
        MusicFiles musicFiles = new MusicFiles();
        musicFiles.setMusicFileId(1L);
        musicFiles.setPublic(true);
        songs.setMusicFilesByFileId(musicFiles);
        List<Songs> songsList = new ArrayList<>();
        songsList.add(songs);

        when(songsRepository.findAllByOrderByRatingDesc()).thenReturn(songsList);

        List<SongDTO> result = musicMetadataService.getSongsTop50();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    public void testRateSong_whenRateSongDTO() {
        RateSongDTO rateSongDTO = null;

        musicMetadataService.rateSong(rateSongDTO);

        assertThat(true);
    }

    @Test
    public void testRateSong_whenRateSongDTOIsNotNull() {
        RateSongDTO rateSongDTO = new RateSongDTO();
        rateSongDTO.setSongId(1L);
        rateSongDTO.setRate(5);
        Songs songs = new Songs();
        songs.setSongId(1L);
        songs.setRatingTimes(1L);
        songs.setRating(5.0f);

        when(songsRepository.findOne(1L)).thenReturn(songs);

        musicMetadataService.rateSong(rateSongDTO);

        assertThat(true);
    }

    @Test
    public void testGetAllSongs_whenSongsNotExists() {
        when(songsRepository.findAll()).thenReturn(null);

        List<SongDTO> result = musicMetadataService.getAllSongs();

        assertThat(result).isNull();
    }

    @Test
    public void testGetAllSongs_whenSongsExists() {
        Songs song = new Songs();
        song.setSongId(1L);
        List<Songs> songsList = new ArrayList<>();
        songsList.add(song);
        when(songsRepository.findAll()).thenReturn(songsList);

        List<SongDTO> result = musicMetadataService.getAllSongs();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }
}
