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
import pl.polsl.EbookServiceApplication;
import pl.polsl.dto.EbookDTO;
import pl.polsl.service.EbookManagementService;
import pl.polsl.service.EbookMetadataService;

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
public class EbookAdminControllerTests {

    @InjectMocks
    private EbookAdminController ebookAdminController;

    @Mock
    private EbookManagementService ebookManagementService;

    @Mock
    private EbookMetadataService ebookMetadataService;


    @Test
    public void testGetEbooks_WhenUsernameIsNotEmpty() {
        String username = "test";
        List<EbookDTO> ebookDTOList = new ArrayList<>();
        when(ebookMetadataService.getAllUserEbooks(username)).thenReturn(ebookDTOList);

        ResponseEntity<List<EbookDTO>> ebooks = ebookAdminController.getEbooks(username);

        assertThat(ebooks).isNotNull();
        assertThat(ebooks.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ebooks.getBody()).isNotNull();
    }

    @Test
    public void testGetEbooks_WhenUsernameIsNull() {
        String username = "";
        List<EbookDTO> ebookDTOList = new ArrayList<>();
        when(ebookMetadataService.getAllEbooks()).thenReturn(ebookDTOList);

        ResponseEntity<List<EbookDTO>> ebooks = ebookAdminController.getEbooks(username);

        assertThat(ebooks).isNotNull();
        assertThat(ebooks.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ebooks.getBody()).isNotNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenIdIsNull() {
        String username = "test";
        Long id = null;
        when(ebookManagementService.removeFileAndMetadata(id, username)).thenReturn(false);

        ResponseEntity<Boolean> responseEntity = ebookAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameIsNull() {
        String username = null;
        Long id = 1L;
        when(ebookManagementService.removeFileAndMetadata(id, username)).thenReturn(false);

        ResponseEntity<Boolean> responseEntity = ebookAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameAndIdExists() {
        String username = "test";
        Long id = 1L;
        when(ebookManagementService.removeFileAndMetadata(id, username)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = ebookAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isTrue();
    }
}
