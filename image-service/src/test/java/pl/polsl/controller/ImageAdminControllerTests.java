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
import pl.polsl.dto.ImageDTO;
import pl.polsl.service.ImageManagementService;
import pl.polsl.service.ImageMetadataService;

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
public class ImageAdminControllerTests {

    @InjectMocks
    private ImageAdminController imageAdminController = new ImageAdminController();

    @Mock
    private ImageManagementService imageManagementService;

    @Mock
    private ImageMetadataService imageMetadataService;

    @Test
    public void testGetImages_WhenUsernameIsNotEmpty() {
        String username = "test";
        List<ImageDTO> imageDTOList = new ArrayList<>();
        when(imageMetadataService.getAllUserImages(username)).thenReturn(imageDTOList);

        ResponseEntity<List<ImageDTO>> images = imageAdminController.getImages(username);

        assertThat(images).isNotNull();
        assertThat(images.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(images.getBody()).isNotNull();
    }

    @Test
    public void testGetEbooks_WhenUsernameIsNull() {
        String username = "";
        List<ImageDTO> ebookDTOList = new ArrayList<>();
        when(imageMetadataService.getAllImages()).thenReturn(ebookDTOList);

        ResponseEntity<List<ImageDTO>> images = imageAdminController.getImages(username);

        assertThat(images).isNotNull();
        assertThat(images.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(images.getBody()).isNotNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenIdIsNull() {
        String username = "test";
        Long id = null;
        when(imageManagementService.removeFileAndMetadata(id, username)).thenReturn(false);

        ResponseEntity<Boolean> responseEntity = imageAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameIsNull() {
        String username = null;
        Long id = 1L;
        when(imageManagementService.removeFileAndMetadata(id, username)).thenReturn(false);

        ResponseEntity<Boolean> responseEntity = imageAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameAndIdExists() {
        String username = "test";
        Long id = 1L;
        when(imageManagementService.removeFileAndMetadata(id, username)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = imageAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isTrue();
    }
}
