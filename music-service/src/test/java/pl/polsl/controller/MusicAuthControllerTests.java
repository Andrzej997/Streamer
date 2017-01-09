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
import pl.polsl.dto.SongDTO;
import pl.polsl.dto.UploadSongMetadataDTO;
import pl.polsl.model.MusicFiles;
import pl.polsl.service.MusicManagementService;
import pl.polsl.service.MusicMetadataService;
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
public class MusicAuthControllerTests {

    @Autowired
    @InjectMocks
    private MusicAuthController musicAuthController;

    @Mock
    private StorageService storageService;

    @Mock
    private MusicMetadataService musicMetadataService;

    @Mock
    private MusicManagementService musicManagementService;

    @Test
    public void testHandleFileUpload_whenFileIsNull() {
        MultipartFile file = null;
        when(storageService.store(file)).thenReturn(null);

        ResponseEntity<Long> responseEntity = musicAuthController.handleFileUpload(file);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testHandleFileUpload_whenFileExists() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        MusicFiles musicFiles = new MusicFiles();
        musicFiles.setMusicFileId(1L);
        when(storageService.store(file)).thenReturn(musicFiles);

        ResponseEntity<Long> responseEntity = musicAuthController.handleFileUpload(file);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(1L);
    }

    @Test
    public void testDownloadMusicFile_whenIdIsNull() {
        Long id = null;
        String username = "test";
        when(storageService.downloadMusicFile(id, username)).thenReturn(null);

        StreamingResponseBody responseBody = musicAuthController.downloadMusicFile(id, username);

        assertThat(responseBody).isNull();
    }

    @Test
    public void testDownloadMusicFile_whenUsernameIsNull() {
        Long id = 1L;
        String username = null;
        when(storageService.downloadMusicFile(id, username)).thenReturn(null);

        StreamingResponseBody responseBody = musicAuthController.downloadMusicFile(id, username);

        assertThat(responseBody).isNull();
    }

    @Test
    public void testDownloadMusicFile_whenUsernameAndIdExists() {
        Long id = 1L;
        String username = "test";
        MusicFiles musicFiles = new MusicFiles();
        musicFiles.setMusicFileId(1L);
        java.sql.Blob blob = null;
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        try {
            blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        musicFiles.setFile(blob);
        when(storageService.downloadMusicFile(id, username)).thenReturn(musicFiles);


        StreamingResponseBody streamingResponseBody = musicAuthController.downloadMusicFile(id, username);


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
    public void testSaveMusicFileMetadata_whenMetadataIsNull() {

        ResponseEntity<UploadSongMetadataDTO> responseEntity = musicAuthController.saveMusicFileMetadata(null);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testSaveMusicFileMetadata_whenMetadataExists() {
        UploadSongMetadataDTO uploadSongMetadataDTO = new UploadSongMetadataDTO();
        when(musicMetadataService.saveMetadata(uploadSongMetadataDTO)).thenReturn(uploadSongMetadataDTO);

        ResponseEntity<UploadSongMetadataDTO> responseEntity = musicAuthController.saveMusicFileMetadata(uploadSongMetadataDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(uploadSongMetadataDTO);
    }

    @Test
    public void testGetTop10SongsOnlyPrivates_whenUsernameIsNull() {
        String username = null;
        String title = null;

        ResponseEntity<List<SongDTO>> responseEntity = musicAuthController.getTop10SongsOnlyPrivates(title, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetTop10SongsOnlyPrivates_whenUsernameExists() {
        String username = "test";
        String title = null;
        when(musicMetadataService.getTop10Songs(username, title)).thenReturn(new ArrayList<>());

        ResponseEntity<List<SongDTO>> responseEntity = musicAuthController.getTop10SongsOnlyPrivates(title, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testGetAllUserSongs_whenUsernameIsNull() {
        String username = null;

        ResponseEntity<List<SongDTO>> responseEntity = musicAuthController.getAllUserSongs(username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testGetAllUserSongs_whenUsernameExists() {
        String username = "test";
        when(musicMetadataService.getAllUserSongs(username)).thenReturn(new ArrayList<>());

        ResponseEntity<List<SongDTO>> responseEntity = musicAuthController.getAllUserSongs(username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenFileIdIsNull() {
        String username = "test";
        Long id = null;

        ResponseEntity<Boolean> responseEntity = musicAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenUsernameIsNull() {
        String username = null;
        Long id = 1L;

        ResponseEntity<Boolean> responseEntity = musicAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testDeleteFileAndMetadata_whenFileIdAndUsernameExists() {
        String username = "test";
        Long id = 1L;
        when(musicManagementService.removeFileAndMetadata(id, username)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = musicAuthController.deleteFileAndMetadata(id, username);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isTrue();
    }

    @Test
    public void testUpdateSongMetadata_whenEbookNotExists() {
        SongDTO songDTO = null;

        ResponseEntity<SongDTO> responseEntity = musicAuthController.updateSongMetadata(songDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testUpdateSongMetadata_whenEbookExists() {
        SongDTO songDTO = new SongDTO();
        when(musicMetadataService.updateSongMetadata(songDTO)).thenReturn(songDTO);

        ResponseEntity<SongDTO> responseEntity = musicAuthController.updateSongMetadata(songDTO);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(songDTO);
    }
}
