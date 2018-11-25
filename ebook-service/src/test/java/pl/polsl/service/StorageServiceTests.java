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
import pl.polsl.EbookServiceApplication;
import pl.polsl.exception.StorageException;
import pl.polsl.model.Ebook;
import pl.polsl.model.EbookFiles;
import pl.polsl.model.UsersView;
import pl.polsl.repository.EbookFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.impl.StorageServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Mateusz on 02.01.2017.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"bootstrap.yml"}, classes = {EbookServiceApplication.class})
public class StorageServiceTests {

    @InjectMocks
    private StorageService storageService = new StorageServiceImpl();

    @Mock
    private EbookFilesRepository ebookFilesRepository;

    @Mock
    private UsersRepositoryCustom usersRepository;

    @Test(expected = StorageException.class)
    public void testStoreFile_whenFileIsEmpty() {
        byte[] bytes = new byte[0];
        MultipartFile file = new MockMultipartFile("test", bytes);

        storageService.store(file);
    }

    @Test
    public void testDownloadEbookFile_whenIdIsNull() {
        Long id = null;
        when(ebookFilesRepository.findById(id)).thenReturn(Optional.empty());

        EbookFiles result = storageService.downloadEbookFile(id);

        assertThat(result).isNull();
    }

    @Test
    public void testDownloadEbookFile_whenFileExists() {
        Long id = 1L;
        EbookFiles ebookFiles = new EbookFiles();
        ebookFiles.setEbookFileId(1L);
        ebookFiles.setPublic(true);
        when(ebookFilesRepository.findById(id)).thenReturn(Optional.of(ebookFiles));

        EbookFiles result = storageService.downloadEbookFile(id);

        assertThat(result).isNotNull();
        assertThat(result.getEbookFileId()).isEqualTo(id);
    }

    @Test
    public void testDownloadEbookFile_whenUsernameIsNull() {
        Long id = 1L;
        String username = null;

        EbookFiles result = storageService.downloadEbookFile(id, username);

        assertThat(result).isNull();
        ;
    }

    @Test
    public void testDownloadEbookFile_whenUsernameExists() {
        Long id = 1L;
        String username = "test";
        EbookFiles ebookFiles = new EbookFiles();
        ebookFiles.setEbookFileId(1L);
        ebookFiles.setPublic(true);
        Collection<Ebook> ebooks = new ArrayList<>();
        Ebook ebook = new Ebook();
        ebook.setOwnerId(1L);
        ebooks.add(ebook);
        ebookFiles.setEbooksByEbookFileId(ebooks);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        usersView.setUserName(username);
        when(ebookFilesRepository.findById(id)).thenReturn(Optional.of(ebookFiles));
        when(usersRepository.findUsersByUserName("test")).thenReturn(usersView);

        EbookFiles result = storageService.downloadEbookFile(id, username);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(ebookFiles);
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
        MultipartFile file = new MockMultipartFile("test", "test.pdf", null, bytes);

        String extension = storageService.getExtension(file);

        assertThat(extension).isNotNull();
        assertThat(extension).isEqualTo("pdf");
    }
}
