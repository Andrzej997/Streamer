package pl.polsl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.exception.StorageException;
import pl.polsl.model.MusicFiles;
import pl.polsl.model.Songs;
import pl.polsl.model.UsersView;
import pl.polsl.repository.MusicFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Mateusz on 08.01.2017.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"bootstrap.yml"})
public class StorageServiceTests {

    @Autowired
    @InjectMocks
    private StorageService storageService;

    @Mock
    private MusicFilesRepository musicFilesRepository;

    @Mock
    private UsersRepositoryCustom usersRepository;

    @Test(expected = StorageException.class)
    public void testStoreFile_whenFileIsEmpty() {
        byte[] bytes = new byte[0];
        MultipartFile file = new MockMultipartFile("test", bytes);

        storageService.store(file);
    }

    @Test
    public void testDownloadMusicFile_whenIdIsNull() {
        Long id = null;
        when(musicFilesRepository.findOne(id)).thenReturn(null);

        MusicFiles result = storageService.downloadMusicFile(id);

        assertThat(result).isNull();
    }

    @Test
    public void testDownloadMusicFile_whenFileExists() {
        Long id = 1L;
        MusicFiles musicFiles = new MusicFiles();
        musicFiles.setMusicFileId(1L);
        musicFiles.setPublic(true);
        when(musicFilesRepository.findOne(id)).thenReturn(musicFiles);

        MusicFiles result = storageService.downloadMusicFile(id);

        assertThat(result).isNotNull();
        assertThat(result.getMusicFileId()).isEqualTo(id);
    }

    @Test
    public void testDownloadMusicFile_whenUsernameIsNull() {
        Long id = 1L;
        String username = null;

        MusicFiles result = storageService.downloadMusicFile(id, username);

        assertThat(result).isNull();
        ;
    }

    @Test
    public void testDownloadMusicFile_whenUsernameExists() {
        Long id = 1L;
        String username = "test";
        MusicFiles musicFiles = new MusicFiles();
        musicFiles.setMusicFileId(1L);
        musicFiles.setPublic(true);
        Collection<Songs> songsList = new ArrayList<>();
        Songs song = new Songs();
        song.setOwnerId(1L);
        songsList.add(song);
        musicFiles.setSongsesByMusicFileId(songsList);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        usersView.setUserName(username);
        when(musicFilesRepository.findOne(id)).thenReturn(musicFiles);
        when(usersRepository.findUsersByUserName("test")).thenReturn(usersView);

        MusicFiles result = storageService.downloadMusicFile(id, username);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(musicFiles);
    }

    @Test
    public void testGetExtension_whenFileIsNull() {
        MultipartFile file = null;

        String result = storageService.getExtension(file);

        assertThat(result).isNull();
    }

    @Test
    public void testGetExtension_whenFileExists() {
        byte[] bytes = {1, 1, 1, 1, 1, 2, 2, 2, 2, 2,};
        MultipartFile file = new MockMultipartFile("test", "test.mp3", null, bytes);

        String extension = storageService.getExtension(file);

        assertThat(extension).isNotNull();
        assertThat(extension).isEqualTo("mp3");
    }
}
