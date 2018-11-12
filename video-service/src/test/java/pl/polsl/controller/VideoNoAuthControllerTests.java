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
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;
import pl.polsl.service.VideoMetadataService;

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
public class VideoNoAuthControllerTests {

    @Autowired
    @InjectMocks
    private VideoNoAuthController videoNoAuthController;

    @Mock
    private StorageService storageService;

    @Mock
    private VideoMetadataService videoMetadataService;

    @Test
    public void testDownloadVideoFile_whenIdIsNull() {
        Long id = null;
        when(storageService.downloadVideoFile(id)).thenReturn(null);

        ResponseEntity<StreamingResponseBody> responseBody = videoNoAuthController.downloadVideoFile(id, null);

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDownloadVideoFile_whenUsernameAndIdExists() {
        Long id = 1L;
        VideoFiles videoFiles = new VideoFiles();
        videoFiles.setVideoFileId(1L);
        java.sql.Blob blob = null;
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        try {
            blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        videoFiles.setFile(blob);
        when(storageService.downloadVideoFile(id)).thenReturn(videoFiles);


        ResponseEntity<StreamingResponseBody> streamingResponseBody = videoNoAuthController.downloadVideoFile(id, null);


        assertThat(streamingResponseBody).isNotNull();
        assertThat(streamingResponseBody.getBody()).isNotNull();
        assertThat(streamingResponseBody.getStatusCode()).isEqualByComparingTo(HttpStatus.PARTIAL_CONTENT);
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            streamingResponseBody.getBody().writeTo(outputStream);
            assertThat(outputStream).isNotNull();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDirectorsPredictionList_whenNameIsNull() {
        String name = null;
        String name2 = null;
        String surname = null;

        ResponseEntity<List<DirectorDTO>> responseEntity = videoNoAuthController.getDirectorsPredictionList(name, name2, surname);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetDirectorsPredictionList_whenEverythingIsSet() {
        String name = "test";
        String name2 = "test";
        String surname = "test";
        when(videoMetadataService.getDirectorsByPrediction(name, name2, surname)).thenReturn(new ArrayList<>());

        ResponseEntity<List<DirectorDTO>> responseEntity = videoNoAuthController.getDirectorsPredictionList(name, name2, surname);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetGenresPredictionList_whenNameIsNull() {
        String name = null;

        ResponseEntity<List<FilmGenreDTO>> responseEntity = videoNoAuthController.getGenresPredictionList(name);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetGenresPredictionList_whenNameExists() {
        String name = "test";
        when(videoMetadataService.getFilmGenresByPrediction(name)).thenReturn(new ArrayList<>());

        ResponseEntity<List<FilmGenreDTO>> responseEntity = videoNoAuthController.getGenresPredictionList(name);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetVideoSeriesPredictionList_whenAlbumTitleIsNull() {
        String serieTitle = null;
        String videoTitle = "test";

        ResponseEntity<List<VideoSerieDTO>> responseEntity
                = videoNoAuthController.getVideoSeriesPredictionList(serieTitle, videoTitle);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetVideoSeriesPredictionList_whenSongTitleIsNull() {
        String serieTitle = "test";
        String videoTitle = null;
        when(videoMetadataService.getVideoSeriesByPrediction(serieTitle, videoTitle)).thenReturn(new ArrayList<>());

        ResponseEntity<List<VideoSerieDTO>> responseEntity
                = videoNoAuthController.getVideoSeriesPredictionList(serieTitle, videoTitle);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetVideoSeriesPredictionList_whenEverythingIsSet() {
        String serieTitle = "test";
        String videoTitle = "test";
        when(videoMetadataService.getVideoSeriesByPrediction(serieTitle, videoTitle)).thenReturn(new ArrayList<>());

        ResponseEntity<List<VideoSerieDTO>> responseEntity
                = videoNoAuthController.getVideoSeriesPredictionList(serieTitle, videoTitle);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetTop10Videos_whenTitleIsNull() {
        String title = null;
        when(videoMetadataService.getTop10Videos(null, title)).thenReturn(new ArrayList<>());

        ResponseEntity<List<VideoDTO>> responseEntity = videoNoAuthController.getTop10Videos(title);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetTop10Videos_whenTitleExists() {
        String title = "test";
        when(videoMetadataService.getTop10Videos(null, title)).thenReturn(new ArrayList<>(5));

        ResponseEntity<List<VideoDTO>> responseEntity = videoNoAuthController.getTop10Videos(title);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testSearchVideosByCriteria_whenCriteriaIsNull() {
        SearchVideoCriteriaDTO searchVideoCriteriaDTO = null;

        ResponseEntity<List<VideoDTO>> responseEntity = videoNoAuthController.searchVideosByCriteria(searchVideoCriteriaDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testSearchVideosByCriteria_whenCriteriaExists() {
        SearchVideoCriteriaDTO searchVideoCriteriaDTO = new SearchVideoCriteriaDTO();
        when(videoMetadataService.searchVideosByCriteria(searchVideoCriteriaDTO)).thenReturn(new ArrayList<>(5));

        ResponseEntity<List<VideoDTO>> responseEntity = videoNoAuthController.searchVideosByCriteria(searchVideoCriteriaDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetVideosTop50() {
        when(videoMetadataService.getVideosTop50()).thenReturn(new ArrayList<>(10));

        ResponseEntity<List<VideoDTO>> responseEntity = videoNoAuthController.getVideosTop50();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test(expected = NullPointerException.class)
    public void testRateVideo_whenRateVideoDTOisNull() {
        RateVideoDTO rateVideoDTO = null;
        Mockito.doNothing().when(videoMetadataService).rateVideo(rateVideoDTO);

        videoNoAuthController.rateVideo(rateVideoDTO);
    }

    @Test
    public void testRateVideo_whenRateVideoDTOExists() {
        RateVideoDTO rateVideoDTO = new RateVideoDTO();
        Mockito.doNothing().when(videoMetadataService).rateVideo(rateVideoDTO);

        videoNoAuthController.rateVideo(rateVideoDTO);

        assertThat(true);
    }
}
