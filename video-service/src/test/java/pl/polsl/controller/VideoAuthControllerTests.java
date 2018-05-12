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
import pl.polsl.dto.UploadVideoMetadataDTO;
import pl.polsl.dto.VideoDTO;
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;
import pl.polsl.service.VideoManagementService;
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
public class VideoAuthControllerTests {

    @Autowired
    @InjectMocks
    private VideoAuthController videoAuthController;

    @Mock
    private StorageService storageService;

    @Mock
    private VideoMetadataService videoMetadataService;

    @Mock
    private VideoManagementService videoManagementService;

    @Test
    public void testHandleFileUpload_whenFileIsNull() {
        MultipartFile file = null;
        when(storageService.store(file)).thenReturn(null);

        ResponseEntity<Long> responseEntity = videoAuthController.handleFileUpload(file);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testHandleFileUpload_whenFileExists() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        VideoFiles videoFiles = new VideoFiles();
        videoFiles.setVideoFileId(1L);
        when(storageService.store(file)).thenReturn(videoFiles);

        ResponseEntity<Long> responseEntity = videoAuthController.handleFileUpload(file);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(1L);
    }

    @Test
    public void testDownloadVideoFile_whenIdIsNull() {
        Long id = null;
        String username = "test";
        when(storageService.downloadVideoFile(id, username)).thenReturn(null);

        ResponseEntity<StreamingResponseBody> responseBody = videoAuthController.downloadVideoFile(id, username);

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDownloadVideoFile_whenUsernameIsNull() {
        Long id = 1L;
        String username = null;
        when(storageService.downloadVideoFile(id, username)).thenReturn(null);

        ResponseEntity<StreamingResponseBody> responseBody = videoAuthController.downloadVideoFile(id, username);

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDownloadVideoFile_whenUsernameAndIdExists() {
        Long id = 1L;
        String username = "test";
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
        when(storageService.downloadVideoFile(id, username)).thenReturn(videoFiles);


        ResponseEntity<StreamingResponseBody> streamingResponseBody = videoAuthController.downloadVideoFile(id, username);


        assertThat(streamingResponseBody).isNotNull();
        assertThat(streamingResponseBody.getBody()).isNotNull();
        assertThat(streamingResponseBody.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            streamingResponseBody.getBody().writeTo(outputStream);
            assertThat(outputStream).isNotNull();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveVideoFileMetadata_whenMetadataIsNull() {

        ResponseEntity<UploadVideoMetadataDTO> responseEntity = videoAuthController.saveVideoFileMetadata(null);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testSaveVideoFileMetadata_whenMetadataExists() {
        UploadVideoMetadataDTO uploadVideoMetadataDTO = new UploadVideoMetadataDTO();
        when(videoMetadataService.saveMetadata(uploadVideoMetadataDTO)).thenReturn(uploadVideoMetadataDTO);

        ResponseEntity<UploadVideoMetadataDTO> responseEntity = videoAuthController.saveVideoFileMetadata(uploadVideoMetadataDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(uploadVideoMetadataDTO);
    }

    @Test
    public void testGetTop10VideosOnlyPrivates_whenUsernameIsNull() {
        String username = null;
        String title = null;

        ResponseEntity<List<VideoDTO>> responseEntity = videoAuthController.getTop10VideosOnlyPrivates(title, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetTop10VideosOnlyPrivates_whenUsernameExists() {
        String username = "test";
        String title = null;
        when(videoMetadataService.getTop10Videos(username, title)).thenReturn(new ArrayList<>());

        ResponseEntity<List<VideoDTO>> responseEntity = videoAuthController.getTop10VideosOnlyPrivates(title, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetAllUserVideos_whenUsernameIsNull() {
        String username = null;

        ResponseEntity<List<VideoDTO>> responseEntity = videoAuthController.getAllUserVideos(username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetAllUserVideos_whenUsernameExists() {
        String username = "test";
        when(videoMetadataService.getAllUserVideos(username)).thenReturn(new ArrayList<>());

        ResponseEntity<List<VideoDTO>> responseEntity = videoAuthController.getAllUserVideos(username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenFileIdIsNull() {
        String username = "test";
        Long id = null;

        ResponseEntity<Boolean> responseEntity = videoAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameIsNull() {
        String username = null;
        Long id = 1L;

        ResponseEntity<Boolean> responseEntity = videoAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenFileIdAndUsernameExists() {
        String username = "test";
        Long id = 1L;
        when(videoManagementService.removeFileAndMetadata(id, username)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = videoAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isTrue();
    }

    @Test
    public void testUpdateEbookMetadata_whenEbookNotExists() {
        VideoDTO videoDTO = null;

        ResponseEntity<VideoDTO> responseEntity = videoAuthController.updateVideoMetadata(videoDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testUpdateEbookMetadata_whenEbookExists() {
        VideoDTO videoDTO = new VideoDTO();
        when(videoMetadataService.updateVideoMetadata(videoDTO)).thenReturn(videoDTO);

        ResponseEntity<VideoDTO> responseEntity = videoAuthController.updateVideoMetadata(videoDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(videoDTO);
    }
}
