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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.EbookServiceApplication;
import pl.polsl.dto.EbookDTO;
import pl.polsl.dto.UploadEbookMetadataDTO;
import pl.polsl.model.EbookFiles;
import pl.polsl.service.EbookManagementService;
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
public class EbookAuthControllerTests {

    @InjectMocks
    private EbookAuthController ebookAuthController;

    @Mock
    private StorageService storageService;

    @Mock
    private EbookMetadataService ebookMetadataService;

    @Mock
    private EbookManagementService ebookManagementService;

    @Test
    public void testHandleFileUpload_whenFileIsNull() {
        MultipartFile file = null;
        when(storageService.store(file)).thenReturn(null);

        ResponseEntity<Long> responseEntity = ebookAuthController.handleFileUpload(file);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testHandleFileUpload_whenFileExists() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        EbookFiles ebookFiles = new EbookFiles();
        ebookFiles.setEbookFileId(1L);
        when(storageService.store(file)).thenReturn(ebookFiles);

        ResponseEntity<Long> responseEntity = ebookAuthController.handleFileUpload(file);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(1L);
    }

    @Test
    public void testDownloadEbookFile_whenIdIsNull() {
        Long id = null;
        String username = "test";
        when(storageService.downloadEbookFile(id, username)).thenReturn(null);

        StreamingResponseBody responseBody = ebookAuthController.downloadImageFile(id, username);

        assertThat(responseBody).isNull();
    }

    @Test
    public void testDownloadEbookFile_whenUsernameIsNull() {
        Long id = 1L;
        String username = null;
        when(storageService.downloadEbookFile(id, username)).thenReturn(null);

        StreamingResponseBody responseBody = ebookAuthController.downloadImageFile(id, username);

        assertThat(responseBody).isNull();
    }

    @Test
    public void testDownloadEbookFile_whenUsernameAndIdExists() {
        Long id = 1L;
        String username = "test";
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
        when(storageService.downloadEbookFile(id, username)).thenReturn(ebookFiles);


        StreamingResponseBody streamingResponseBody = ebookAuthController.downloadImageFile(id, username);


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
    public void testSaveEbookFileMetadata_whenMetadataIsNull() {

        ResponseEntity<UploadEbookMetadataDTO> responseEntity = ebookAuthController.saveEbookFileMetadata(null);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testSaveEbookFileMetadata_whenMetadataExists() {
        UploadEbookMetadataDTO uploadEbookMetadataDTO = new UploadEbookMetadataDTO();
        when(ebookMetadataService.saveMetadata(uploadEbookMetadataDTO)).thenReturn(uploadEbookMetadataDTO);

        ResponseEntity<UploadEbookMetadataDTO> responseEntity = ebookAuthController.saveEbookFileMetadata(uploadEbookMetadataDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(uploadEbookMetadataDTO);
    }

    @Test
    public void testGetTop10EbooksOnlyPrivates_whenUsernameIsNull() {
        String username = null;
        String title = null;

        ResponseEntity<List<EbookDTO>> responseEntity = ebookAuthController.getTop10EbooksOnlyPrivates(title, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetTop10EbooksOnlyPrivates_whenUsernameExists() {
        String username = "test";
        String title = null;
        when(ebookMetadataService.getTop10Ebooks(username, title)).thenReturn(new ArrayList<>());

        ResponseEntity<List<EbookDTO>> responseEntity = ebookAuthController.getTop10EbooksOnlyPrivates(title, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetAllUserEbooks_whenUsernameIsNull() {
        String username = null;

        ResponseEntity<List<EbookDTO>> responseEntity = ebookAuthController.getAllUserEbooks(username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetAllUserEbooks_whenUsernameExists() {
        String username = "test";
        when(ebookMetadataService.getAllUserEbooks(username)).thenReturn(new ArrayList<>());

        ResponseEntity<List<EbookDTO>> responseEntity = ebookAuthController.getAllUserEbooks(username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenFileIdIsNull() {
        String username = "test";
        Long id = null;

        ResponseEntity<Boolean> responseEntity = ebookAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameIsNull() {
        String username = null;
        Long id = 1L;

        ResponseEntity<Boolean> responseEntity = ebookAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenFileIdAndUsernameExists() {
        String username = "test";
        Long id = 1L;
        when(ebookManagementService.removeFileAndMetadata(id, username)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = ebookAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isTrue();
    }

    @Test
    public void testUpdateEbookMetadata_whenEbookNotExists() {
        EbookDTO ebookDTO = null;

        ResponseEntity<EbookDTO> responseEntity = ebookAuthController.updateEbookMetadata(ebookDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testUpdateEbookMetadata_whenEbookExists() {
        EbookDTO ebookDTO = new EbookDTO();
        when(ebookMetadataService.updateEbookMetadata(ebookDTO)).thenReturn(ebookDTO);

        ResponseEntity<EbookDTO> responseEntity = ebookAuthController.updateEbookMetadata(ebookDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(ebookDTO);
    }
}
