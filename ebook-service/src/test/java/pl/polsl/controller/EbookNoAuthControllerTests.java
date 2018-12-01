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
import pl.polsl.EbookServiceApplication;
import pl.polsl.dto.*;
import pl.polsl.model.EbookFiles;
import pl.polsl.service.EbookMetadataService;
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
 * Created by Mateusz on 02.01.2017.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"bootstrap.yml"}, classes = {EbookServiceApplication.class})
public class EbookNoAuthControllerTests {

    @InjectMocks
    private EbookNoAuthController ebookNoAuthController;

    @Mock
    private StorageService storageService;

    @Mock
    private EbookMetadataService ebookMetadataService;

    @Test
    public void testDownloadEbookFile_whenIdIsNull() {
        Long id = null;
        when(storageService.downloadEbookFile(id)).thenReturn(null);

        StreamingResponseBody responseBody = ebookNoAuthController.downloadEbookFile(id);

        assertThat(responseBody).isNull();
    }

    @Test
    public void testDownloadEbookFile_whenUsernameAndIdExists() {
        Long id = 1L;
        EbookFiles ebookFiles = new EbookFiles();
        ebookFiles.setEbookFileId(1L);
        java.sql.Blob blob = null;
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        try {
            blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ebookFiles.setFile(blob);
        when(storageService.downloadEbookFile(id)).thenReturn(ebookFiles);


        StreamingResponseBody streamingResponseBody = ebookNoAuthController.downloadEbookFile(id);


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
    public void testGetWritersPredictionList_whenNameIsNull() {
        String name = null;
        String name2 = null;
        String surname = null;

        ResponseEntity<List<WriterDTO>> responseEntity = ebookNoAuthController.getWritersPredictionList(name, name2, surname);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetWritersPredictionList_whenEverythingIsSet() {
        String name = "test";
        String name2 = "test";
        String surname = "test";
        when(ebookMetadataService.getWritersByPrediction(name, name2, surname)).thenReturn(new ArrayList<>());

        ResponseEntity<List<WriterDTO>> responseEntity = ebookNoAuthController.getWritersPredictionList(name, name2, surname);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetLiteraryGenresPredictionList_whenNameIsNull() {
        String name = null;

        ResponseEntity<List<LiteraryGenreDTO>> responseEntity = ebookNoAuthController.getLiteraryGenresPredictionList(name);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetLiteraryGenresPredictionList_whenNameExists() {
        String name = "test";
        when(ebookMetadataService.getLiteraryGenresByPrediction(name)).thenReturn(new ArrayList<>());

        ResponseEntity<List<LiteraryGenreDTO>> responseEntity = ebookNoAuthController.getLiteraryGenresPredictionList(name);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetTop10Ebooks_whenTitleIsNull() {
        String title = null;
        when(ebookMetadataService.getTop10Ebooks(null, title)).thenReturn(new ArrayList<>());

        ResponseEntity<List<EbookDTO>> responseEntity = ebookNoAuthController.getTop10Ebooks(title);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetTop10Ebooks_whenTitleExists() {
        String title = "test";
        when(ebookMetadataService.getTop10Ebooks(null, title)).thenReturn(new ArrayList<>(5));

        ResponseEntity<List<EbookDTO>> responseEntity = ebookNoAuthController.getTop10Ebooks(title);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testSearchEbooksByCriteria_whenCriteriaIsNull() {
        SearchEbookCriteriaDTO searchEbookCriteriaDTO = null;

        ResponseEntity<List<EbookDTO>> responseEntity = ebookNoAuthController.searchEbooksByCriteria(searchEbookCriteriaDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testSearchEbooksByCriteria_whenCriteriaExists() {
        SearchEbookCriteriaDTO searchEbookCriteriaDTO = new SearchEbookCriteriaDTO();
        when(ebookMetadataService.searchEbooksByCriteria(searchEbookCriteriaDTO)).thenReturn(new ArrayList<>(5));

        ResponseEntity<List<EbookDTO>> responseEntity = ebookNoAuthController.searchEbooksByCriteria(searchEbookCriteriaDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetEbooksTop50() {
        when(ebookMetadataService.getEbooksTop50()).thenReturn(new ArrayList<>(10));

        ResponseEntity<List<EbookDTO>> responseEntity = ebookNoAuthController.getEbooksTop50();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test(expected = NullPointerException.class)
    public void testRateEbook_whenRateEbookDTOisNull() {
        RateEbookDTO rateEbookDTO = null;
        Mockito.doNothing().when(ebookMetadataService).rateEbook(rateEbookDTO);

        ebookNoAuthController.rateEbook(rateEbookDTO);
    }

    @Test
    public void testRateEbook_whenRateEbookDTOExists() {
        RateEbookDTO rateEbookDTO = new RateEbookDTO();
        Mockito.doNothing().when(ebookMetadataService).rateEbook(rateEbookDTO);

        ebookNoAuthController.rateEbook(rateEbookDTO);

        assertThat(true);
    }
}
