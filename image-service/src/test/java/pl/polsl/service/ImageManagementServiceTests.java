package pl.polsl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.polsl.model.ImageFiles;
import pl.polsl.model.UsersView;
import pl.polsl.repository.ImageFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.impl.ImageManagementServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Mateusz on 08.01.2017.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"bootstrap.yml"})
public class ImageManagementServiceTests {

    @InjectMocks
    private ImageManagementService imageManagementService = new ImageManagementServiceImpl();

    @Mock
    private ImageFilesRepository imageFilesRepository;

    @Mock
    private UsersRepositoryCustom usersRepository;

    @Test
    public void testRemoveFileAndMetadata_whenFileIsNull() {
        Long fileId = null;
        String username = "test";

        Boolean result = imageManagementService.removeFileAndMetadata(fileId, username);

        assertThat(result).isFalse();
    }

    @Test
    public void testRemoveFileAndMetadata_whenUsernameIsNull() {
        Long fileId = 1L;
        String username = null;

        Boolean result = imageManagementService.removeFileAndMetadata(fileId, username);

        assertThat(result).isFalse();
    }

    @Test
    public void testRemoveFileAndMetadata_whenUsernameNotExists() {
        Long fileId = 1L;
        String username = "test";
        when(usersRepository.findUsersByUserName(username)).thenReturn(null);

        Boolean result = imageManagementService.removeFileAndMetadata(fileId, username);

        assertThat(result).isFalse();
    }

    @Test
    public void testRemoveFileAndMetadata_whenFileNotExists() {
        Long fileId = 1L;
        String username = "test";
        when(usersRepository.findUsersByUserName(username)).thenReturn(new UsersView());
        when(imageFilesRepository.findById(fileId)).thenReturn(Optional.empty());

        Boolean result = imageManagementService.removeFileAndMetadata(fileId, username);

        assertThat(result).isFalse();
    }

    @Test
    public void testRemoveFileAndMetadata_whenEverythingExists() {
        Long fileId = 1L;
        String username = "test";
        when(usersRepository.findUsersByUserName(username)).thenReturn(new UsersView());
        when(imageFilesRepository.findById(fileId)).thenReturn(Optional.of(new ImageFiles()));
        Mockito.doNothing().when(imageFilesRepository).deleteById(fileId);

        Boolean result = imageManagementService.removeFileAndMetadata(fileId, username);

        assertThat(result).isTrue();
    }
}
