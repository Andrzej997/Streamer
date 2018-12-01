package pl.polsl.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.dto.*;
import pl.polsl.model.MusicFiles;
import pl.polsl.service.MusicMetadataService;
import pl.polsl.service.StorageService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
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
public class MusicNoAuthControllerTests {

    @InjectMocks
    private MusicNoAuthController musicNoAuthController = new MusicNoAuthController();

    @Mock
    private StorageService storageService;

    @Mock
    private MusicMetadataService musicMetadataService;

    @Test
    public void testDownloadMusicFile_whenIdIsNull() {
        Long id = null;
        when(storageService.downloadMusicFile(id)).thenReturn(null);

        StreamingResponseBody responseBody = musicNoAuthController.downloadMusicFile(id);

        assertThat(responseBody).isNull();
    }

    @Test
    public void testDownloadMusicFile_whenUsernameAndIdExists() {
        Long id = 1L;
        MusicFiles musicFiles = new MusicFiles();
        musicFiles.setMusicFileId(1L);
        java.sql.Blob blob = null;
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        try {
            blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        musicFiles.setFile(blob);
        when(storageService.downloadMusicFile(id)).thenReturn(musicFiles);


        StreamingResponseBody streamingResponseBody = musicNoAuthController.downloadMusicFile(id);


        assertThat(streamingResponseBody).isNotNull();
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            streamingResponseBody.writeTo(outputStream);
            assertThat(outputStream).isNotNull();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetArtistPredictionList_whenNameIsNull() {
        String name = null;
        String name2 = null;
        String surname = null;

        ResponseEntity<List<MusicArtistDTO>> responseEntity = musicNoAuthController.getArtistsPredictionList(name, name2, surname);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetArtistPredictionList_whenEverythingIsSet() {
        String name = "test";
        String name2 = "test";
        String surname = "test";
        when(musicMetadataService.getArtistsByPrediction(name, name2, surname)).thenReturn(new ArrayList<>());

        ResponseEntity<List<MusicArtistDTO>> responseEntity = musicNoAuthController.getArtistsPredictionList(name, name2, surname);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetGenresPredictionList_whenNameIsNull() {
        String name = null;

        ResponseEntity<List<MusicGenreDTO>> responseEntity = musicNoAuthController.getGenresPredictionList(name);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetGenresPredictionList_whenNameExists() {
        String name = "test";
        when(musicMetadataService.getGenresByPrediction(name)).thenReturn(new ArrayList<>());

        ResponseEntity<List<MusicGenreDTO>> responseEntity = musicNoAuthController.getGenresPredictionList(name);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetAlbumsPredictionList_whenAlbumTitleIsNull() {
        String albumTitle = null;
        String songTitle = "test";

        ResponseEntity<List<MusicAlbumDTO>> responseEntity
                = musicNoAuthController.getAlbumsPredictionList(albumTitle, songTitle);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetAlbumsPredictionList_whenSongTitleIsNull() {
        String albumTitle = "test";
        String songTitle = null;
        when(musicMetadataService.getAlbumsByPrediction(albumTitle, songTitle)).thenReturn(new ArrayList<>());

        ResponseEntity<List<MusicAlbumDTO>> responseEntity
                = musicNoAuthController.getAlbumsPredictionList(albumTitle, songTitle);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetAlbumsPredictionList_whenEverythingIsSet() {
        String albumTitle = "test";
        String songTitle = "test";
        when(musicMetadataService.getAlbumsByPrediction(albumTitle, songTitle)).thenReturn(new ArrayList<>());

        ResponseEntity<List<MusicAlbumDTO>> responseEntity
                = musicNoAuthController.getAlbumsPredictionList(albumTitle, songTitle);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetTop10Songs_whenTitleIsNull() {
        String title = null;
        when(musicMetadataService.getTop10Songs(null, title)).thenReturn(new ArrayList<>());

        ResponseEntity<List<SongDTO>> responseEntity = musicNoAuthController.getTop10Songs(title);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetTop10Songs_whenTitleExists() {
        String title = "test";
        when(musicMetadataService.getTop10Songs(null, title)).thenReturn(new ArrayList<>(5));

        ResponseEntity<List<SongDTO>> responseEntity = musicNoAuthController.getTop10Songs(title);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testSearchSongsByCriteria_whenCriteriaIsNull() {
        SearchSongCriteriaDTO searchSongCriteriaDTO = null;

        ResponseEntity<List<SongDTO>> responseEntity = musicNoAuthController.searchSongsByCriteria(searchSongCriteriaDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testSearchSongsByCriteria_whenCriteriaExists() {
        SearchSongCriteriaDTO searchSongCriteriaDTO = new SearchSongCriteriaDTO();
        when(musicMetadataService.searchSongsByCriteria(searchSongCriteriaDTO)).thenReturn(new ArrayList<>(5));

        ResponseEntity<List<SongDTO>> responseEntity = musicNoAuthController.searchSongsByCriteria(searchSongCriteriaDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetSongsTop50() {
        when(musicMetadataService.getSongsTop50()).thenReturn(new ArrayList<>(10));

        ResponseEntity<List<SongDTO>> responseEntity = musicNoAuthController.getSongsTop50();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test(expected = NullPointerException.class)
    public void testRateSong_whenRateSongDTOisNull() {
        RateSongDTO rateSongDTO = null;
        Mockito.doNothing().when(musicMetadataService).rateSong(rateSongDTO);

        musicNoAuthController.rateSong(rateSongDTO);
    }

    @Test
    public void testRateSong_whenRateSongDTOExists() {
        RateSongDTO rateSongDTO = new RateSongDTO();
        Mockito.doNothing().when(musicMetadataService).rateSong(rateSongDTO);

        musicNoAuthController.rateSong(rateSongDTO);

        assertThat(true);
    }
}
