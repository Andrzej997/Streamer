package pl.polsl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.polsl.dto.*;
import pl.polsl.mapper.EbookMapper;
import pl.polsl.model.*;
import pl.polsl.repository.*;
import pl.polsl.repository.custom.UsersRepositoryCustom;

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
        properties = {"bootstrap.yml"})
public class EbookMetadataServiceTests {

    @Autowired
    @InjectMocks
    private EbookMetadataService ebookMetadataService;

    @Autowired
    @Spy
    private EbookMapper ebookMapper;

    @Mock
    private UsersRepositoryCustom usersRepository;

    @Mock
    private WritersRepository writersRepository;

    @Mock
    private LiteraryGenreRepository literaryGenreRepository;

    @Mock
    private EbookAuthorsRepository ebookAuthorsRepository;

    @Mock
    private EbookFilesRepository ebookFilesRepository;

    @Mock
    private EbookRepository ebookRepository;

    @Test
    public void testGetWritersByPrediction_whenNameIsNull() {
        String name = null;
        String name2 = null;
        String surname = null;

        List<WriterDTO> result = ebookMetadataService.getWritersByPrediction(name, name2, surname);

        assertThat(result).isNull();
    }

    @Test
    public void testGetWritersByPrediction_whenNameExists() {
        String name = "test";
        String name2 = null;
        String surname = null;
        ArrayList<Writers> writerses = new ArrayList<>();
        writerses.add(new Writers());

        when(writersRepository.findByNameLike(name)).thenReturn(writerses);
        when(writersRepository.findByName2Like(name)).thenReturn(null);
        when(writersRepository.findBySurnameLike(name)).thenReturn(null);

        List<WriterDTO> result = ebookMetadataService.getWritersByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetWritersByPrediction_whenSurnameExists() {
        String name = "test";
        String name2 = null;
        String surname = "test";
        ArrayList<Writers> writerses = new ArrayList<>();
        writerses.add(new Writers());

        when(writersRepository.findByNameLike(name)).thenReturn(writerses);
        when(writersRepository.findByName2Like(name)).thenReturn(null);
        when(writersRepository.findBySurnameLike(name)).thenReturn(null);

        List<WriterDTO> result = ebookMetadataService.getWritersByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetWritersByPrediction_whenName2Exists() {
        String name = "test";
        String name2 = "test";
        String surname = null;
        ArrayList<Writers> writerses = new ArrayList<>();
        writerses.add(new Writers());

        when(writersRepository.findByNameLike(name)).thenReturn(writerses);
        when(writersRepository.findByName2Like(name)).thenReturn(null);
        when(writersRepository.findBySurnameLike(name)).thenReturn(null);

        List<WriterDTO> result = ebookMetadataService.getWritersByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetWritersByPrediction_whenEverythingIsSet() {
        String name = "test";
        String name2 = "test";
        String surname = "test";
        ArrayList<Writers> writerses = new ArrayList<>();
        writerses.add(new Writers());

        when(writersRepository.findByNameLike(name)).thenReturn(writerses);
        when(writersRepository.findByName2Like(name)).thenReturn(null);
        when(writersRepository.findBySurnameLike(name)).thenReturn(null);

        List<WriterDTO> result = ebookMetadataService.getWritersByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetLiteraryGenresByPrediction_whenNameIsNull() {
        String name = null;

        List<LiteraryGenreDTO> result = ebookMetadataService.getLiteraryGenresByPrediction(name);

        assertThat(result).isNull();
    }

    @Test
    public void testGetLiteraryGenresByPrediction_whenNameExists() {
        String name = "test";
        List<LiteraryGenre> literaryGenreList = new ArrayList<>();
        literaryGenreList.add(new LiteraryGenre());
        when(literaryGenreRepository.findByNameLike(name)).thenReturn(literaryGenreList);

        List<LiteraryGenreDTO> result = ebookMetadataService.getLiteraryGenresByPrediction(name);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testSaveMetadata_whenUploadEbookMetadataDTOIsNull() {
        UploadEbookMetadataDTO uploadEbookMetadataDTO = null;

        UploadEbookMetadataDTO result = ebookMetadataService.saveMetadata(uploadEbookMetadataDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSaveMetadata_whenEbookDTOIsNull() {
        UploadEbookMetadataDTO uploadEbookMetadataDTO = new UploadEbookMetadataDTO();
        uploadEbookMetadataDTO.setUsername("test");
        UsersView usersView = new UsersView();
        usersView.setUserName(uploadEbookMetadataDTO.getUsername());
        usersView.setUserId(1L);
        when(usersRepository.findUsersByUserName(uploadEbookMetadataDTO.getUsername())).thenReturn(usersView);

        UploadEbookMetadataDTO result = ebookMetadataService.saveMetadata(uploadEbookMetadataDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSaveMetadata_whenWritersListIsEmpty() {
        UploadEbookMetadataDTO uploadEbookMetadataDTO = new UploadEbookMetadataDTO();
        uploadEbookMetadataDTO.setUsername("test");
        UsersView usersView = new UsersView();
        usersView.setUserName(uploadEbookMetadataDTO.getUsername());
        usersView.setUserId(1L);
        EbookDTO ebookDTO = new EbookDTO();
        ebookDTO.setOwnerId(usersView.getUserId());
        uploadEbookMetadataDTO.setEbookDTO(ebookDTO);
        Ebook ebook = ebookMapper.toEbook(uploadEbookMetadataDTO.getEbookDTO());
        ebook.setNumOfPages(0);
        when(usersRepository.findUsersByUserName(uploadEbookMetadataDTO.getUsername())).thenReturn(usersView);
        when(ebookRepository.save(ebook)).thenReturn(createEbook(ebook));

        UploadEbookMetadataDTO result = ebookMetadataService.saveMetadata(uploadEbookMetadataDTO);

        assertThat(result).isNull();
    }

    private Ebook createEbook(Ebook ebook) {
        ebook.setEbookId(1L);
        return ebook;
    }

    @Test
    public void testGetTop10Ebooks_whenUsernameAndTitleIsNull() {
        String username = null;
        String title = null;
        Ebook ebook = new Ebook();
        ebook.setEbookId(1L);
        List<Ebook> ebookList = new ArrayList<>();
        ebookList.add(ebook);
        when(ebookRepository.findTop10ByIsPublicOrderByRatingDesc(true)).thenReturn(ebookList);

        List<EbookDTO> result = ebookMetadataService.getTop10Ebooks(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(ebookList.size());
    }

    @Test
    public void testGetTop10Ebooks_whenUsernameIsNull() {
        String username = null;
        String title = "test";
        Ebook ebook = new Ebook();
        ebook.setEbookId(1L);
        List<Ebook> ebookList = new ArrayList<>();
        ebookList.add(ebook);
        when(ebookRepository.findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(true, "%" + title + "%")).thenReturn(ebookList);

        List<EbookDTO> result = ebookMetadataService.getTop10Ebooks(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(ebookList.size());
    }

    @Test
    public void testGetTop10Ebooks_whenTitleIsNull() {
        String username = "test";
        String title = null;
        Ebook ebook = new Ebook();
        ebook.setEbookId(1L);
        List<Ebook> ebookList = new ArrayList<>();
        ebookList.add(ebook);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(ebookRepository.findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(false, usersView.getUserId()))
                .thenReturn(ebookList);

        List<EbookDTO> result = ebookMetadataService.getTop10Ebooks(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(ebookList.size());
    }

    @Test
    public void testGetTop10Ebooks_whenEverythingIsSetted() {
        String username = "test";
        String title = "test";
        Ebook ebook = new Ebook();
        ebook.setEbookId(1L);
        List<Ebook> ebookList = new ArrayList<>();
        ebookList.add(ebook);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);

        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(ebookRepository.findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(false, usersView.getUserId(), "%" + title + "%"))
                .thenReturn(ebookList);

        List<EbookDTO> result = ebookMetadataService.getTop10Ebooks(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(ebookList.size());
    }

    @Test
    public void testGetAllUserEbooks_whenUsernameIsNull() {
        String username = null;

        List<EbookDTO> result = ebookMetadataService.getAllUserEbooks(username);

        assertThat(result).isNull();
    }

    @Test
    public void testGetAllUserEbooks_whenUsernameIsSet() {
        String username = "test";
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        usersView.setUserName(username);
        Ebook ebook = new Ebook();
        ebook.setEbookId(1L);
        List<Ebook> ebookList = new ArrayList<>();
        ebookList.add(ebook);

        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(ebookRepository.findByOwnerId(usersView.getUserId())).thenReturn(ebookList);

        List<EbookDTO> result = ebookMetadataService.getAllUserEbooks(username);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    public void testSearchEbooksByCriteria_whenSearchEbookCriteriaDTOIsNull() {
        SearchEbookCriteriaDTO searchEbookCriteriaDTO = null;

        List<EbookDTO> result = ebookMetadataService.searchEbooksByCriteria(searchEbookCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchEbooksByCriteria_whenCriteriaIsNull() {
        SearchEbookCriteriaDTO searchEbookCriteriaDTO = new SearchEbookCriteriaDTO();

        List<EbookDTO> result = ebookMetadataService.searchEbooksByCriteria(searchEbookCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchEbooksByCriteria_whenTextSearchedIsNull() {
        SearchEbookCriteriaDTO searchEbookCriteriaDTO = new SearchEbookCriteriaDTO();
        searchEbookCriteriaDTO.setCriteria("T");

        List<EbookDTO> result = ebookMetadataService.searchEbooksByCriteria(searchEbookCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchEbooksByCriteria_whenCriteriaIsTitle() {
        SearchEbookCriteriaDTO searchEbookCriteriaDTO = new SearchEbookCriteriaDTO();
        searchEbookCriteriaDTO.setCriteria("T");
        searchEbookCriteriaDTO.setTextSearched("test");
        Ebook ebook = new Ebook();
        ebook.setEbookId(1L);
        List<Ebook> ebookList = new ArrayList<>();
        ebookList.add(ebook);

        when(ebookRepository.findByTitleOrderByRating(searchEbookCriteriaDTO.getTextSearched())).thenReturn(ebookList);
        when(ebookRepository.findByTitleLikeOrderByRating(searchEbookCriteriaDTO.getTextSearched() + "%")).thenReturn(null);

        List<EbookDTO> result = ebookMetadataService.searchEbooksByCriteria(searchEbookCriteriaDTO);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testUpdateEbookMetadata_whenEbookDTOIsNull() {
        EbookDTO ebookDTO = null;

        EbookDTO result = ebookMetadataService.updateEbookMetadata(ebookDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testGetEbooksTop50_whenDatabaseIsEmpty() {
        when(ebookRepository.findAllByOrderByRatingDesc()).thenReturn(null);

        List<EbookDTO> result = ebookMetadataService.getEbooksTop50();

        assertThat(result).isNull();
    }

    @Test
    public void testGetEbooksTop50() {
        Ebook ebook = new Ebook();
        ebook.setEbookId(1L);
        ebook.setEbookFileId(1L);
        EbookFiles ebookFiles = new EbookFiles();
        ebookFiles.setEbookFileId(1L);
        ebookFiles.setPublic(true);
        ebook.setEbookFilesByEbookFileId(ebookFiles);
        List<Ebook> ebookList = new ArrayList<>();
        ebookList.add(ebook);

        when(ebookRepository.findAllByOrderByRatingDesc()).thenReturn(ebookList);

        List<EbookDTO> result = ebookMetadataService.getEbooksTop50();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    public void testRateEbook_whenRateEbookDTO() {
        RateEbookDTO rateEbookDTO = null;

        ebookMetadataService.rateEbook(rateEbookDTO);

        assertThat(true);
    }

    @Test
    public void testRateEbook_whenRateEbookDTOIsNotNull() {
        RateEbookDTO rateEbookDTO = new RateEbookDTO();
        rateEbookDTO.setEbookId(1L);
        rateEbookDTO.setRate(5);
        Ebook ebook = new Ebook();
        ebook.setEbookId(1L);
        ebook.setRatingTimes(1L);
        ebook.setRating(5.0f);

        when(ebookRepository.findOne(1L)).thenReturn(ebook);

        ebookMetadataService.rateEbook(rateEbookDTO);

        assertThat(true);
    }

    @Test
    public void testGetAllEbooks_whenEbooksNotExists() {
        when(ebookRepository.findAll()).thenReturn(null);

        List<EbookDTO> result = ebookMetadataService.getAllEbooks();

        assertThat(result).isNull();
    }

    @Test
    public void testGetAllEbooks_whenEbooksExists() {
        Ebook ebook = new Ebook();
        ebook.setEbookId(1L);
        List<Ebook> ebookList = new ArrayList<>();
        ebookList.add(ebook);
        when(ebookRepository.findAll()).thenReturn(ebookList);

        List<EbookDTO> result = ebookMetadataService.getAllEbooks();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }
}