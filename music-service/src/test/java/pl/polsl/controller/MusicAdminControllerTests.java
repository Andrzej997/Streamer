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
import pl.polsl.dto.SongDTO;
import pl.polsl.service.MusicManagementService;
import pl.polsl.service.MusicMetadataService;

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
public class MusicAdminControllerTests {

    @Autowired
    @InjectMocks
    private MusicAdminController musicAdminController;

    @Mock
    private MusicManagementService musicManagementService;

    @Mock
    private MusicMetadataService musicMetadataService;

    @Test
    public void testGetSongs_WhenUsernameIsNotEmpty() {
        String username = "test";
        List<SongDTO> songDTOList = new ArrayList<>();
        when(musicMetadataService.getAllUserSongs(username)).thenReturn(songDTOList);

        ResponseEntity<List<SongDTO>> songs = musicAdminController.getSongs(username);

        assertThat(songs).isNotNull();
        assertThat(songs.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(songs.getBody()).isNotNull();
    }

    @Test
    public void testGetSongs_WhenUsernameIsNull() {
        String username = "";
        List<SongDTO> songDTOList = new ArrayList<>();
        when(musicMetadataService.getAllSongs()).thenReturn(songDTOList);

        ResponseEntity<List<SongDTO>> songs = musicAdminController.getSongs(username);

        assertThat(songs).isNotNull();
        assertThat(songs.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(songs.getBody()).isNotNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenIdIsNull() {
        String username = "test";
        Long id = null;
        when(musicManagementService.removeFileAndMetadata(id, username)).thenReturn(false);

        ResponseEntity<Boolean> responseEntity = musicAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameIsNull() {
        String username = null;
        Long id = 1L;
        when(musicManagementService.removeFileAndMetadata(id, username)).thenReturn(false);

        ResponseEntity<Boolean> responseEntity = musicAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isFalse();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameAndIdExists() {
        String username = "test";
        Long id = 1L;
        when(musicManagementService.removeFileAndMetadata(id, username)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = musicAdminController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isTrue();
    }
}
