package pl.polsl.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.polsl.dto.VideoDTO;
import pl.polsl.service.VideoManagementService;
import pl.polsl.service.VideoMetadataService;

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
public class VideoAdminControllerTests {

    @InjectMocks
    private VideoAdminController videoAdminController = new VideoAdminController();

    @Mock
    private VideoManagementService videoManagementService;

    @Mock
    private VideoMetadataService videoMetadataService;


    @Test
    public void testGetVideos_WhenUsernameIsNotEmpty() {
        String username = "test";
        List<VideoDTO> videoDTOList = new ArrayList<>();
        when(videoMetadataService.getAllUserVideos(username)).thenReturn(videoDTOList);

        ResponseEntity<List<VideoDTO>> videos = videoAdminController.getVideos(username);

        assertThat(videos).isNotNull();
        assertThat(videos.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(videos.getBody()).isNotNull();
    }

    @Test
    public void testGetVideos_WhenUsernameIsNull() {
        String username = "";
        List<VideoDTO> videoDTOList = new ArrayList<>();
        when(videoMetadataService.getAllVideos()).thenReturn(videoDTOList);

        ResponseEntity<List<VideoDTO>> videos = videoAdminController.getVideos(username);

        assertThat(videos).isNotNull();
        assertThat(videos.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(videos.getBody()).isNotNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenIdIsNull() {
        String username = "test";
        Long id = null;
        when(videoManagementService.removeFileAndMetadata(id, username)).thenReturn(false);

        ResponseEntity<Boolean> responseEntity = videoAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameIsNull() {
        String username = null;
        Long id = 1L;
        when(videoManagementService.removeFileAndMetadata(id, username)).thenReturn(false);

        ResponseEntity<Boolean> responseEntity = videoAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameAndIdExists() {
        String username = "test";
        Long id = 1L;
        when(videoManagementService.removeFileAndMetadata(id, username)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = videoAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isTrue();
    }
}
