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
import pl.polsl.model.ImageFiles;
import pl.polsl.model.Images;
import pl.polsl.model.UsersView;
import pl.polsl.repository.ImageFilesRepository;
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
    private ImageFilesRepository imageFilesRepository;

    @Mock
    private UsersRepositoryCustom usersRepository;

    @Test(expected = StorageException.class)
    public void testStoreFile_whenFileIsEmpty() {
        byte[] bytes = new byte[0];
        MultipartFile file = new MockMultipartFile("test", bytes);

        storageService.store(file);
    }

    @Test
    public void testDownloadImageFile_whenIdIsNull() {
        Long id = null;
        when(imageFilesRepository.findOne(id)).thenReturn(null);

        ImageFiles result = storageService.downloadImageFile(id);

        assertThat(result).isNull();
    }

    @Test
    public void testDownloadImageFile_whenFileExists() {
        Long id = 1L;
        ImageFiles imageFiles = new ImageFiles();
        imageFiles.setImageFileId(1L);
        imageFiles.setPublic(true);
        when(imageFilesRepository.findOne(id)).thenReturn(imageFiles);

        ImageFiles result = storageService.downloadImageFile(id);

        assertThat(result).isNotNull();
        assertThat(result.getImageFileId()).isEqualTo(id);
    }

    @Test
    public void testDownloadImageFile_whenUsernameIsNull() {
        Long id = 1L;
        String username = null;

        ImageFiles result = storageService.downloadImageFile(id, username);

        assertThat(result).isNull();
        ;
    }

    @Test
    public void testDownloadImageFile_whenUsernameExists() {
        Long id = 1L;
        String username = "test";
        ImageFiles imageFiles = new ImageFiles();
        imageFiles.setImageFileId(1L);
        imageFiles.setPublic(true);
        Collection<Images> images = new ArrayList<>();
        Images image = new Images();
        image.setOwnerId(1L);
        images.add(image);
        imageFiles.setImagesByImageFileId(images);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        usersView.setUserName(username);
        when(imageFilesRepository.findOne(id)).thenReturn(imageFiles);
        when(usersRepository.findUsersByUserName("test")).thenReturn(usersView);

        ImageFiles result = storageService.downloadImageFile(id, username);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(imageFiles);
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
        MultipartFile file = new MockMultipartFile("test", "test.png", null, bytes);

        String extension = storageService.getExtension(file);

        assertThat(extension).isNotNull();
        assertThat(extension).isEqualTo("png");
    }
}
