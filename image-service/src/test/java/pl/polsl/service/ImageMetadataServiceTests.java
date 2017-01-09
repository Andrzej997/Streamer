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
import pl.polsl.mapper.ImageMapper;
import pl.polsl.model.*;
import pl.polsl.repository.*;
import pl.polsl.repository.custom.UsersRepositoryCustom;

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
public class ImageMetadataServiceTests {

    @Autowired
    @InjectMocks
    private ImageMetadataService imageMetadataService;

    @Autowired
    @Spy
    private ImageMapper imageMapper;

    @Mock
    private UsersRepositoryCustom usersRepository;

    @Mock
    private ArtistsRepository artistsRepository;

    @Mock
    private ImageTypesRepository imageTypesRepository;

    @Mock
    private ImageAuthorsRepository imageAuthorsRepository;

    @Mock
    private ImageFilesRepository imageFilesRepository;

    @Mock
    private ImagesRepository imagesRepository;

    @Test
    public void testGetArtistsByPrediction_whenNameIsNull() {
        String name = null;
        String name2 = null;
        String surname = null;

        List<ArtistDTO> result = imageMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNull();
    }

    @Test
    public void testGetArtistsByPrediction_whenNameExists() {
        String name = "test";
        String name2 = null;
        String surname = null;
        ArrayList<Artists> artistses = new ArrayList<>();
        artistses.add(new Artists());

        when(artistsRepository.findByNameLike(name)).thenReturn(artistses);
        when(artistsRepository.findByName2Like(name)).thenReturn(null);
        when(artistsRepository.findBySurnameLike(name)).thenReturn(null);

        List<ArtistDTO> result = imageMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetArtistsByPrediction_whenSurnameExists() {
        String name = "test";
        String name2 = null;
        String surname = "test";
        ArrayList<Artists> artistses = new ArrayList<>();
        artistses.add(new Artists());

        when(artistsRepository.findByNameLike(name)).thenReturn(artistses);
        when(artistsRepository.findByName2Like(name)).thenReturn(null);
        when(artistsRepository.findBySurnameLike(name)).thenReturn(null);

        List<ArtistDTO> result = imageMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetArtistsByPrediction_whenName2Exists() {
        String name = "test";
        String name2 = "test";
        String surname = null;
        ArrayList<Artists> artistses = new ArrayList<>();
        artistses.add(new Artists());

        when(artistsRepository.findByNameLike(name)).thenReturn(artistses);
        when(artistsRepository.findByName2Like(name)).thenReturn(null);
        when(artistsRepository.findBySurnameLike(name)).thenReturn(null);

        List<ArtistDTO> result = imageMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetArtistsByPrediction_whenEverythingIsSet() {
        String name = "test";
        String name2 = "test";
        String surname = "test";
        ArrayList<Artists> artistses = new ArrayList<>();
        artistses.add(new Artists());

        when(artistsRepository.findByNameLike(name)).thenReturn(artistses);
        when(artistsRepository.findByName2Like(name)).thenReturn(null);
        when(artistsRepository.findBySurnameLike(name)).thenReturn(null);

        List<ArtistDTO> result = imageMetadataService.getArtistsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetImageTypesByPrediction_whenNameIsNull() {
        String name = null;

        List<ImageTypeDTO> result = imageMetadataService.getImageTypesByPrediction(name);

        assertThat(result).isNull();
    }

    @Test
    public void testGetImageTypesByPrediction_whenNameExists() {
        String name = "test";
        List<ImageTypes> imageTypesList = new ArrayList<>();
        imageTypesList.add(new ImageTypes());
        when(imageTypesRepository.findByNameLike(name)).thenReturn(imageTypesList);

        List<ImageTypeDTO> result = imageMetadataService.getImageTypesByPrediction(name);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testSaveMetadata_whenUploadImageMetadataDTOIsNull() {
        UploadImageMetadataDTO uploadImageMetadataDTO = null;

        UploadImageMetadataDTO result = imageMetadataService.saveMetadata(uploadImageMetadataDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSaveMetadata_whenImageDTOIsNull() {
        UploadImageMetadataDTO uploadImageMetadataDTO = new UploadImageMetadataDTO();
        uploadImageMetadataDTO.setUsername("test");
        UsersView usersView = new UsersView();
        usersView.setUserName(uploadImageMetadataDTO.getUsername());
        usersView.setUserId(1L);
        when(usersRepository.findUsersByUserName(uploadImageMetadataDTO.getUsername())).thenReturn(usersView);

        UploadImageMetadataDTO result = imageMetadataService.saveMetadata(uploadImageMetadataDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSaveMetadata_whenArtistListIsEmpty() {
        UploadImageMetadataDTO uploadImageMetadataDTO = new UploadImageMetadataDTO();
        uploadImageMetadataDTO.setUsername("test");
        UsersView usersView = new UsersView();
        usersView.setUserName(uploadImageMetadataDTO.getUsername());
        usersView.setUserId(1L);
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setOwnerId(usersView.getUserId());
        uploadImageMetadataDTO.setImageDTO(imageDTO);
        Images image = imageMapper.toImages(uploadImageMetadataDTO.getImageDTO());
        image.setNativeWidth(0);
        image.setNativeHeight(0);
        image.setResolution(image.getNativeHeight() * image.getNativeWidth());
        when(usersRepository.findUsersByUserName(uploadImageMetadataDTO.getUsername())).thenReturn(usersView);
        when(imagesRepository.save(image)).thenReturn(createImage(image));

        UploadImageMetadataDTO result = imageMetadataService.saveMetadata(uploadImageMetadataDTO);

        assertThat(result).isNull();
    }

    private Images createImage(Images image) {
        image.setImageId(1L);
        return image;
    }

    @Test
    public void testGetTop10Images_whenUsernameAndTitleIsNull() {
        String username = null;
        String title = null;
        Images image = new Images();
        image.setImageId(1L);
        List<Images> imagesList = new ArrayList<>();
        imagesList.add(image);
        when(imagesRepository.findTop10ByIsPublicOrderByRatingDesc(true)).thenReturn(imagesList);

        List<ImageDTO> result = imageMetadataService.getTop10Images(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(imagesList.size());
    }

    @Test
    public void testGetTop10Images_whenUsernameIsNull() {
        String username = null;
        String title = "test";
        Images image = new Images();
        image.setImageId(1L);
        List<Images> imagesList = new ArrayList<>();
        imagesList.add(image);
        when(imagesRepository.findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(true, "%" + title + "%")).thenReturn(imagesList);

        List<ImageDTO> result = imageMetadataService.getTop10Images(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(imagesList.size());
    }

    @Test
    public void testGetTop10Images_whenTitleIsNull() {
        String username = "test";
        String title = null;
        Images image = new Images();
        image.setImageId(1L);
        List<Images> imagesList = new ArrayList<>();
        imagesList.add(image);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(imagesRepository.findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(false, usersView.getUserId()))
                .thenReturn(imagesList);

        List<ImageDTO> result = imageMetadataService.getTop10Images(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(imagesList.size());
    }

    @Test
    public void testGetTop10Images_whenEverythingIsSetted() {
        String username = "test";
        String title = "test";
        Images image = new Images();
        image.setImageId(1L);
        List<Images> imagesList = new ArrayList<>();
        imagesList.add(image);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);

        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(imagesRepository.findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(false, usersView.getUserId(), "%" + title + "%"))
                .thenReturn(imagesList);

        List<ImageDTO> result = imageMetadataService.getTop10Images(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(imagesList.size());
    }

    @Test
    public void testGetAllUserImages_whenUsernameIsNull() {
        String username = null;

        List<ImageDTO> result = imageMetadataService.getAllUserImages(username);

        assertThat(result).isNull();
    }

    @Test
    public void testGetAllUserImages_whenUsernameIsSet() {
        String username = "test";
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        usersView.setUserName(username);
        Images image = new Images();
        image.setImageId(1L);
        List<Images> imagesList = new ArrayList<>();
        imagesList.add(image);

        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(imagesRepository.findByOwnerId(usersView.getUserId())).thenReturn(imagesList);

        List<ImageDTO> result = imageMetadataService.getAllUserImages(username);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    public void testSearchImagesByCriteria_whenSearchImageCriteriaDTOIsNull() {
        SearchImageCriteriaDTO searchImageCriteriaDTO = null;

        List<ImageDTO> result = imageMetadataService.searchImagesByCriteria(searchImageCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchImagesByCriteria_whenCriteriaIsNull() {
        SearchImageCriteriaDTO searchImageCriteriaDTO = new SearchImageCriteriaDTO();

        List<ImageDTO> result = imageMetadataService.searchImagesByCriteria(searchImageCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchImagesByCriteria_whenTextSearchedIsNull() {
        SearchImageCriteriaDTO searchImageCriteriaDTO = new SearchImageCriteriaDTO();
        searchImageCriteriaDTO.setCriteria("T");

        List<ImageDTO> result = imageMetadataService.searchImagesByCriteria(searchImageCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchImagesByCriteria_whenCriteriaIsTitle() {
        SearchImageCriteriaDTO searchImageCriteriaDTO = new SearchImageCriteriaDTO();
        searchImageCriteriaDTO.setCriteria("T");
        searchImageCriteriaDTO.setTextSearched("test");
        Images image = new Images();
        image.setImageId(1L);
        List<Images> imagesList = new ArrayList<>();
        imagesList.add(image);

        when(imagesRepository.findByTitleOrderByRating(searchImageCriteriaDTO.getTextSearched())).thenReturn(imagesList);
        when(imagesRepository.findByTitleLikeOrderByRating(searchImageCriteriaDTO.getTextSearched() + "%")).thenReturn(null);

        List<ImageDTO> result = imageMetadataService.searchImagesByCriteria(searchImageCriteriaDTO);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testUpdateImageMetadata_whenImageDTOIsNull() {
        ImageDTO imageDTO = null;

        ImageDTO result = imageMetadataService.updateImageMetadata(imageDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testGetImagesTop50_whenDatabaseIsEmpty() {
        when(imagesRepository.findAllByOrderByRatingDesc()).thenReturn(null);

        List<ImageDTO> result = imageMetadataService.getImagesTop50();

        assertThat(result).isNull();
    }

    @Test
    public void testGetImagesTop50() {
        Images image = new Images();
        image.setImageId(1L);
        image.setImageFileId(1L);
        ImageFiles imageFiles = new ImageFiles();
        imageFiles.setImageFileId(1L);
        imageFiles.setPublic(true);
        image.setImageFilesByImageFileId(imageFiles);
        List<Images> imagesList = new ArrayList<>();
        imagesList.add(image);

        when(imagesRepository.findAllByOrderByRatingDesc()).thenReturn(imagesList);

        List<ImageDTO> result = imageMetadataService.getImagesTop50();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    public void testRateImage_whenRateImageDTO() {
        RateImageDTO rateImageDTO = null;

        imageMetadataService.rateImage(rateImageDTO);

        assertThat(true);
    }

    @Test
    public void testRateImage_whenRateImageDTOIsNotNull() {
        RateImageDTO rateImageDTO = new RateImageDTO();
        rateImageDTO.setImageId(1L);
        rateImageDTO.setRate(5);
        Images images = new Images();
        images.setImageId(1L);
        images.setRatingTimes(1L);
        images.setRating(5.0f);

        when(imagesRepository.findOne(1L)).thenReturn(images);

        imageMetadataService.rateImage(rateImageDTO);

        assertThat(true);
    }

    @Test
    public void testGetAllImages_whenImagesNotExists() {
        when(imagesRepository.findAll()).thenReturn(null);

        List<ImageDTO> result = imageMetadataService.getAllImages();

        assertThat(result).isNull();
    }

    @Test
    public void testGetAllImages_whenImagesExists() {
        Images image = new Images();
        image.setImageId(1L);
        List<Images> imagesList = new ArrayList<>();
        imagesList.add(image);
        when(imagesRepository.findAll()).thenReturn(imagesList);

        List<ImageDTO> result = imageMetadataService.getAllImages();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }
}
