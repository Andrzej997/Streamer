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
import pl.polsl.model.ImageFiles;
import pl.polsl.service.ImageMetadataService;
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
public class ImageNoAuthControllerTests {

    @InjectMocks
    private ImageNoAuthController imageNoAuthController = new ImageNoAuthController();

    @Mock
    private StorageService storageService;

    @Mock
    private ImageMetadataService imageMetadataService;

    @Test
    public void testDownloadImageFile_whenIdIsNull() {
        Long id = null;
        when(storageService.downloadImageFile(id)).thenReturn(null);

        StreamingResponseBody responseBody = imageNoAuthController.downloadImageFile(id);

        assertThat(responseBody).isNull();
    }

    @Test
    public void testDownloadImageFile_whenUsernameAndIdExists() {
        Long id = 1L;
        ImageFiles imageFiles = new ImageFiles();
        imageFiles.setImageFileId(1L);
        java.sql.Blob blob = null;
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        try {
            blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        imageFiles.setFile(blob);
        when(storageService.downloadImageFile(id)).thenReturn(imageFiles);


        StreamingResponseBody streamingResponseBody = imageNoAuthController.downloadImageFile(id);


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

        ResponseEntity<List<ArtistDTO>> responseEntity = imageNoAuthController.getArtistPredictionList(name, name2, surname);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetArtistPredictionList_whenEverythingIsSet() {
        String name = "test";
        String name2 = "test";
        String surname = "test";
        when(imageMetadataService.getArtistsByPrediction(name, name2, surname)).thenReturn(new ArrayList<>());

        ResponseEntity<List<ArtistDTO>> responseEntity = imageNoAuthController.getArtistPredictionList(name, name2, surname);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetImageTypesByPrediction_whenNameIsNull() {
        String name = null;

        ResponseEntity<List<ImageTypeDTO>> responseEntity = imageNoAuthController.getImageTypesByPrediction(name);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetImageTypesByPrediction_whenNameExists() {
        String name = "test";
        when(imageMetadataService.getImageTypesByPrediction(name)).thenReturn(new ArrayList<>());

        ResponseEntity<List<ImageTypeDTO>> responseEntity = imageNoAuthController.getImageTypesByPrediction(name);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetTop10Images_whenTitleIsNull() {
        String title = null;
        when(imageMetadataService.getTop10Images(null, title)).thenReturn(new ArrayList<>());

        ResponseEntity<List<ImageDTO>> responseEntity = imageNoAuthController.getTop10Images(title);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetTop10Images_whenTitleExists() {
        String title = "test";
        when(imageMetadataService.getTop10Images(null, title)).thenReturn(new ArrayList<>(5));

        ResponseEntity<List<ImageDTO>> responseEntity = imageNoAuthController.getTop10Images(title);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testSearchImagesByCriteria_whenCriteriaIsNull() {
        SearchImageCriteriaDTO searchImageCriteriaDTO = null;

        ResponseEntity<List<ImageDTO>> responseEntity = imageNoAuthController.searchImagesByCriteria(searchImageCriteriaDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testSearchImagesByCriteria_whenCriteriaExists() {
        SearchImageCriteriaDTO searchImageCriteriaDTO = new SearchImageCriteriaDTO();
        when(imageMetadataService.searchImagesByCriteria(searchImageCriteriaDTO)).thenReturn(new ArrayList<>(5));

        ResponseEntity<List<ImageDTO>> responseEntity = imageNoAuthController.searchImagesByCriteria(searchImageCriteriaDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetImagesTop50() {
        when(imageMetadataService.getImagesTop50()).thenReturn(new ArrayList<>(10));

        ResponseEntity<List<ImageDTO>> responseEntity = imageNoAuthController.getImagesTop50();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test(expected = NullPointerException.class)
    public void testRateImage_whenRateImageDTOisNull() {
        RateImageDTO rateImageDTO = null;
        Mockito.doNothing().when(imageMetadataService).rateImage(rateImageDTO);

        imageNoAuthController.rateImage(rateImageDTO);
    }

    @Test
    public void testRateImage_whenRateImageDTOExists() {
        RateImageDTO rateImageDTO = new RateImageDTO();
        Mockito.doNothing().when(imageMetadataService).rateImage(rateImageDTO);

        imageNoAuthController.rateImage(rateImageDTO);

        assertThat(true);
    }
}
