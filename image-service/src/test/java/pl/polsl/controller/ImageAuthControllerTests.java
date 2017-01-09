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
import pl.polsl.dto.ImageDTO;
import pl.polsl.dto.UploadImageMetadataDTO;
import pl.polsl.model.ImageFiles;
import pl.polsl.service.ImageManagementService;
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
public class ImageAuthControllerTests {

    @Autowired
    @InjectMocks
    private ImageAuthController imageAuthController;

    @Mock
    private StorageService storageService;

    @Mock
    private ImageMetadataService imageMetadataService;

    @Mock
    private ImageManagementService imageManagementService;

    @Test
    public void testHandleFileUpload_whenFileIsNull() {
        MultipartFile file = null;
        when(storageService.store(file)).thenReturn(null);

        ResponseEntity<Long> responseEntity = imageAuthController.handleFileUpload(file);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testHandleFileUpload_whenFileExists() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        ImageFiles imageFiles = new ImageFiles();
        imageFiles.setImageFileId(1L);
        when(storageService.store(file)).thenReturn(imageFiles);

        ResponseEntity<Long> responseEntity = imageAuthController.handleFileUpload(file);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(1L);
    }

    @Test
    public void testDownloadImageFile_whenIdIsNull() {
        Long id = null;
        String username = "test";
        when(storageService.downloadImageFile(id, username)).thenReturn(null);

        StreamingResponseBody responseBody = imageAuthController.downloadImageFile(id, username);

        assertThat(responseBody).isNull();
    }

    @Test
    public void testDownloadImageFile_whenUsernameIsNull() {
        Long id = 1L;
        String username = null;
        when(storageService.downloadImageFile(id, username)).thenReturn(null);

        StreamingResponseBody responseBody = imageAuthController.downloadImageFile(id, username);

        assertThat(responseBody).isNull();
    }

    @Test
    public void testDownloadImageFile_whenUsernameAndIdExists() {
        Long id = 1L;
        String username = "test";
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
        when(storageService.downloadImageFile(id, username)).thenReturn(imageFiles);


        StreamingResponseBody streamingResponseBody = imageAuthController.downloadImageFile(id, username);


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
    public void testSaveImageFileMetadata_whenMetadataIsNull() {

        ResponseEntity<UploadImageMetadataDTO> responseEntity = imageAuthController.saveImageFileMetadata(null);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testSaveImageFileMetadata_whenMetadataExists() {
        UploadImageMetadataDTO uploadImageMetadataDTO = new UploadImageMetadataDTO();
        when(imageMetadataService.saveMetadata(uploadImageMetadataDTO)).thenReturn(uploadImageMetadataDTO);

        ResponseEntity<UploadImageMetadataDTO> responseEntity = imageAuthController.saveImageFileMetadata(uploadImageMetadataDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(uploadImageMetadataDTO);
    }

    @Test
    public void testGetTop10ImagesOnlyPrivates_whenUsernameIsNull() {
        String username = null;
        String title = null;

        ResponseEntity<List<ImageDTO>> responseEntity = imageAuthController.getTop10ImagesOnlyPrivates(title, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetTop10ImagesOnlyPrivates_whenUsernameExists() {
        String username = "test";
        String title = null;
        when(imageMetadataService.getTop10Images(username, title)).thenReturn(new ArrayList<>());

        ResponseEntity<List<ImageDTO>> responseEntity = imageAuthController.getTop10ImagesOnlyPrivates(title, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetAllUserImages_whenUsernameIsNull() {
        String username = null;

        ResponseEntity<List<ImageDTO>> responseEntity = imageAuthController.getAllUserImages(username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetAllUserImages_whenUsernameExists() {
        String username = "test";
        when(imageMetadataService.getAllUserImages(username)).thenReturn(new ArrayList<>());

        ResponseEntity<List<ImageDTO>> responseEntity = imageAuthController.getAllUserImages(username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenFileIdIsNull() {
        String username = "test";
        Long id = null;

        ResponseEntity<Boolean> responseEntity = imageAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameIsNull() {
        String username = null;
        Long id = 1L;

        ResponseEntity<Boolean> responseEntity = imageAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenFileIdAndUsernameExists() {
        String username = "test";
        Long id = 1L;
        when(imageManagementService.removeFileAndMetadata(id, username)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = imageAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isTrue();
    }

    @Test
    public void testUpdateImageMetadata_whenEbookNotExists() {
        ImageDTO imageDTO = null;

        ResponseEntity<ImageDTO> responseEntity = imageAuthController.updateImageMetadata(imageDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testUpdateImageMetadata_whenEbookExists() {
        ImageDTO imageDTO = new ImageDTO();
        when(imageMetadataService.updateImageMetadata(imageDTO)).thenReturn(imageDTO);

        ResponseEntity<ImageDTO> responseEntity = imageAuthController.updateImageMetadata(imageDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(imageDTO);
    }
}
