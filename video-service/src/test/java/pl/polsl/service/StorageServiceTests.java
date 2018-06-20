package pl.polsl.service;

import org.junit.Ignore;
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
import pl.polsl.model.UsersView;
import pl.polsl.model.VideoFiles;
import pl.polsl.model.Videos;
import pl.polsl.repository.VideoFilesRepository;
import pl.polsl.repository.VideosRepository;
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
    private VideoFilesRepository videoFilesRepository;

    @Mock
    private UsersRepositoryCustom usersRepository;

    @Mock
    private VideosRepository videosRepository;

    @Test(expected = StorageException.class)
    public void testStoreFile_whenFileIsEmpty() {
        byte[] bytes = new byte[0];
        MultipartFile file = new MockMultipartFile("test", bytes);

        storageService.store(file, "H480");
    }

    @Test
    public void testDownloadVideoFile_whenIdIsNull() {
        Long id = null;
        when(videoFilesRepository.findOne(id)).thenReturn(null);

        VideoFiles result = storageService.downloadVideoFile(id);

        assertThat(result).isNull();
    }

    @Test
    public void testDownloadVideoFile_whenFileExists() {
        Long id = 1L;
        VideoFiles videoFiles = new VideoFiles();
        videoFiles.setVideoFileId(1L);
        videoFiles.setPublic(true);
        when(videoFilesRepository.findOne(id)).thenReturn(videoFiles);

        VideoFiles result = storageService.downloadVideoFile(id);

        assertThat(result).isNotNull();
        assertThat(result.getVideoFileId()).isEqualTo(id);
    }

    @Test
    public void testDownloadVideoFile_whenUsernameIsNull() {
        Long id = 1L;
        String username = null;

        VideoFiles result = storageService.downloadVideoFile(id, username);

        assertThat(result).isNull();
        ;
    }

    @Test
    @Ignore
    public void testDownloadVideoFile_whenUsernameExists() {
        Long id = 1L;
        String username = "test";
        VideoFiles videoFiles = new VideoFiles();
        videoFiles.setVideoFileId(1L);
        videoFiles.setPublic(true);
        Collection<Videos> videosList = new ArrayList<>();
        Videos video = new Videos();
        video.setOwnerId(1L);
        videosList.add(video);
        videoFiles.setVideosesByVideoFileId(videosList);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        usersView.setUserName(username);
        when(videoFilesRepository.findOne(id)).thenReturn(videoFiles);
        when(videosRepository.findByVideoFileId(videoFiles.getVideoFileId())).thenReturn(videosList);
        when(usersRepository.findUsersByUserName("test")).thenReturn(usersView);

        VideoFiles result = storageService.downloadVideoFile(id, username);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(videoFiles);
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
        MultipartFile file = new MockMultipartFile("test", "test.avi", null, bytes);

        String extension = storageService.getExtension(file);

        assertThat(extension).isNotNull();
        assertThat(extension).isEqualTo("avi");
    }
}
