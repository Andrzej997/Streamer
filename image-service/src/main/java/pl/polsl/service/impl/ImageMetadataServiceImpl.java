package pl.polsl.service.impl;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.dto.*;
import pl.polsl.mapper.ImageMapper;
import pl.polsl.model.*;
import pl.polsl.repository.*;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.ImageMetadataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Mateusz on 27.11.2016.
 */
@Service
@Transactional
public class ImageMetadataServiceImpl implements ImageMetadataService {

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Autowired
    private ArtistsRepository artistsRepository;

    @Autowired
    private ImageTypesRepository imageTypesRepository;

    @Autowired
    private ImageAuthorsRepository imageAuthorsRepository;

    @Autowired
    private ImageFilesRepository imageFilesRepository;

    @Autowired
    private ImagesRepository imagesRepository;

    @Override
    public List<ArtistDTO> getArtistsByPrediction(String name, String name2, String surname) {
        List<ArtistDTO> result;
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        name = "%" + name + "%";
        List<Artists> artistsList = artistsRepository.findByNameLike(name);
        if (artistsList == null) {
            artistsList = new ArrayList<>();
        }
        artistsList.addAll(artistsRepository.findByName2Like(name));
        artistsList.addAll(artistsRepository.findBySurnameLike(name));
        if (!StringUtils.isEmpty(surname) && !surname.equals("undefined")) {
            surname = "%" + surname + "%";
            artistsList = artistsRepository.findByNameLikeAndSurnameLike(name, surname);
            if (artistsList == null) {
                artistsList = new ArrayList<>();
            }
            artistsList.addAll(artistsRepository.findByNameLikeAndName2Like(name, surname));
        }
        if (!StringUtils.isEmpty(name2) && !name2.equals("undefined")) {
            name2 = "%" + name2 + "%";
            artistsList = artistsRepository.findByNameLikeAndName2Like(name, name2);
            if (artistsList == null) {
                artistsList = new ArrayList<>();
            }
            artistsList.addAll(artistsRepository.findByNameLikeAndSurnameLike(name, name2));
        }
        result = imageMapper.toArtistDTOList(artistsList);
        return result;
    }

    @Override
    public List<ImageTypeDTO> getImageTypesByPrediction(String name) {
        List<ImageTypeDTO> result;
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        name = "%" + name + "%";
        List<ImageTypes> imageTypesList = imageTypesRepository.findByNameLike(name);
        return imageMapper.toImageTypeDTOList(imageTypesList);
    }

    @Override
    public UploadImageMetadataDTO saveMetadata(UploadImageMetadataDTO uploadImageMetadataDTO) {
        if (uploadImageMetadataDTO == null || StringUtils.isEmpty(uploadImageMetadataDTO.getUsername())) {
            return null;
        }
        UsersView user = usersRepository.findUsersByUserName(uploadImageMetadataDTO.getUsername());
        if (user == null) {
            return null;
        }
        ImageDTO imageDTO = uploadImageMetadataDTO.getImageDTO();
        if (imageDTO == null) {
            return null;
        }
        Images images = imageMapper.toImages(imageDTO);
        List<Artists> artistsList = imageMapper.toArtistsList(imageDTO.getArtistDTOList());
        saveImageTypeForImage(imageDTO, images);
        saveImageFileMetadataForImage(imageDTO, images);
        images.setOwnerId(user.getUserId());
        completeRequiredOnNull(images);
        images = imagesRepository.save(images);
        if (images == null) {
            return null;
        }

        saveImageArtistsForImage(images, artistsList);
        List<ImageAuthors> imageAuthorsList = imageAuthorsRepository.findByImageId(images.getImageId());
        images.setImageAuthorsesByImageId(imageAuthorsList);

        images = imagesRepository.save(images);

        ImageDTO toImageDTO = imageMapper.toImageDTO(images);
        UploadImageMetadataDTO result = new UploadImageMetadataDTO();
        result.setImageDTO(toImageDTO);
        result.setUsername(user.getUserName());
        return result;
    }

    private void saveImageTypeForImage(ImageDTO imageDTO, Images images) {
        ImageTypes imageTypes = null;
        if (imageDTO.getImageTypeDTO() != null && !StringUtils.isEmpty(imageDTO.getImageTypeDTO().getName())) {
            imageTypes = imageTypesRepository.save(imageMapper.toImageTypes(imageDTO.getImageTypeDTO()));
        }
        if (imageTypes == null) {
            images.setImageTypesByTypeId(null);
            images.setTypeId(null);
            return;
        }
        images.setImageTypesByTypeId(imageTypes);
        images.setTypeId(imageTypes.getTypeId());
    }

    private void saveImageFileMetadataForImage(ImageDTO imageDTO, Images images) {
        ImageFiles imageFiles = null;
        Optional<ImageFiles> file = imageFilesRepository.findById(imageDTO.getImageFileId());
        if (imageDTO.getImageFileDTO() != null) {
            ImageFiles toImageFiles = imageMapper.toImageFiles(imageDTO.getImageFileDTO());
            file.ifPresent(imageFiles1 -> toImageFiles.setFile(imageFiles1.getFile()));
            imageFiles = imageFilesRepository.save(toImageFiles);
        }
        if (imageFiles == null) {
            images.setImageFilesByImageFileId(null);
            images.setImageFileId(null);
            return;
        }
        images.setImageFilesByImageFileId(imageFiles);
        images.setImageFileId(imageFiles.getImageFileId());
    }

    private void saveImageArtistsForImage(Images images, List<Artists> artistsList) {
        if (artistsList == null) {
            return;
        }
        for (Artists artist : artistsList) {

            artist = artistsRepository.save(artist);

            ImageAuthors imageAuthors = new ImageAuthors();
            imageAuthors.setAuthorId(artist.getArtistId());
            imageAuthors.setArtistsByAuthorId(artist);
            imageAuthors.setImageId(images.getImageId());
            imageAuthors.setImagesByImageId(images);
            imageAuthorsRepository.save(imageAuthors);
        }
    }

    private void completeRequiredOnNull(Images image) {
        if (image.getNativeWidth() == null) {
            image.setNativeWidth(0);
        }
        if (image.getNativeHeight() == null) {
            image.setNativeHeight(0);
        }
        if (image.getResolution() == null) {
            image.setResolution(image.getNativeHeight() * image.getNativeWidth());
        }
    }

    @Override
    public List<ImageDTO> getTop10Images(String username, String title) {
        List<Images> imagesList = null;
        UsersView user = null;
        if (!StringUtils.isEmpty(username)) {
            user = usersRepository.findUsersByUserName(username);
        }
        if (user == null) {
            if (StringUtils.isEmpty(title)) {
                imagesList = imagesRepository.findTop10ByIsPublicOrderByRatingDesc(true);
            } else {
                title = "%" + title + "%";
                imagesList = imagesRepository.findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(true, title);
            }
        } else {
            if (StringUtils.isEmpty(title)) {
                imagesList = imagesRepository.findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(false, user.getUserId());
            } else {
                title = "%" + title + "%";
                imagesList = imagesRepository.findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(false, user.getUserId(), title);
            }
        }
        if (imagesList == null) {
            return null;
        }
        List<ImageDTO> imageDTOList = imageMapper.toImageDTOList(imagesList);
        if (imageDTOList == null || imageDTOList.isEmpty()) {
            return null;
        } else {
            return imageDTOList.subList(0, imageDTOList.size() > 9 ? 9 : imageDTOList.size());
        }
    }

    @Override
    public List<ImageDTO> getAllUserImages(String username) {
        UsersView user = null;
        user = usersRepository.findUsersByUserName(username);
        if (user == null) {
            return null;
        }
        List<Images> imagesList = imagesRepository.findByOwnerId(user.getUserId());
        return imageMapper.toImageDTOList(imagesList);
    }

    @Override
    public List<ImageDTO> searchImagesByCriteria(SearchImageCriteriaDTO searchImageCriteriaDTO) {
        if (searchImageCriteriaDTO == null || StringUtils.isEmpty(searchImageCriteriaDTO.getCriteria())) {
            return null;
        }
        String criteria = searchImageCriteriaDTO.getCriteria();
        String textSearched = searchImageCriteriaDTO.getTextSearched();
        switch (criteria) {
            case "T":
                return getImagesByTitle(textSearched);
            case "A":
                return getImagesByAuthor(textSearched);
            case "G":
                return getImagesByTypeName(textSearched);
            case "Y":
                return getImagesByYear(textSearched);
            case "ALL":
                return getImagesByAllCriteria(textSearched);
        }
        return null;
    }

    private List<ImageDTO> getImagesByTitle(String title) {
        if (title == null || "undefined".equals(title)) {
            return null;
        }
        List<Images> imagesList = imagesRepository.findByTitleOrderByRating(title);
        if (imagesList == null) {
            imagesList = new ArrayList<>();
        }
        title += "%";
        List<Images> imagesListByTitleLike = imagesRepository.findByTitleLikeOrderByRating(title);
        if (imagesListByTitleLike != null && imagesListByTitleLike.size() > 0) {
            imagesList.addAll(imagesListByTitleLike);
        }
        imagesList = imagesList.stream().filter(images ->
                (images.getImageFilesByImageFileId() != null && images.getImageFilesByImageFileId().getPublic()))
                .collect(Collectors.toList());
        return imageMapper.toImageDTOList(imagesList);
    }

    private List<ImageDTO> getImagesByAuthor(String authorData) {
        if (authorData == null || "undefined".equals(authorData)) {
            return null;
        }
        String[] data = authorData.split(" ");
        if (data.length <= 0) {
            return null;
        }
        String name = data[0];
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        List<Images> result = null;
        switch (data.length) {
            case 1:
                result = imagesRepository.findByArtistNameLikeOrderByRating(name);
                break;
            case 2:
                result = imagesRepository.findByArtistNameLikeAndSurnameLikeOrderByRating(name, data[1]);
                break;
            case 3:
                result = imagesRepository.findByArtistNameLikeAndSurnameLikeAndName2LikeOrderByRating(name, data[1], data[2]);
                break;
            default:
                return null;
        }
        result = result.stream().filter(images ->
                (images.getImageFilesByImageFileId() != null && images.getImageFilesByImageFileId().getPublic()))
                .collect(Collectors.toList());
        return imageMapper.toImageDTOList(result);
    }

    private List<ImageDTO> getImagesByTypeName(String name) {
        if (name == null || "undefined".equals(name)) {
            return null;
        }
        List<Images> imagesList = imagesRepository.findByTypeNameOrderByRating(name);
        if (imagesList == null) {
            imagesList = new ArrayList<>();
        }
        name += "%";
        imagesList.addAll(imagesRepository.findByTypeNameLikeOrderByRating(name));
        imagesList = imagesList.stream().filter(images ->
                (images.getImageFilesByImageFileId() != null && images.getImageFilesByImageFileId().getPublic()))
                .collect(Collectors.toList());
        return imageMapper.toImageDTOList(imagesList);
    }

    private List<ImageDTO> getImagesByYear(String year) {
        if (year == null || "undefined".equals(year) || !NumberUtils.isNumber(year)) {
            return null;
        }
        Short yearNumber = Short.parseShort(year);
        List<Images> imagesList = imagesRepository.findByYearOrderByRating(yearNumber);
        imagesList = imagesList.stream().filter(images ->
                (images.getImageFilesByImageFileId() != null && images.getImageFilesByImageFileId().getPublic()))
                .collect(Collectors.toList());
        return imageMapper.toImageDTOList(imagesList);
    }

    private List<ImageDTO> getImagesByAllCriteria(String searchText) {
        if (StringUtils.isEmpty(searchText)) {
            return null;
        }
        List<ImageDTO> result = new ArrayList<>();
        List<ImageDTO> imagesByTitle = getImagesByTitle(searchText);
        List<ImageDTO> imagesByYear = getImagesByYear(searchText);
        List<ImageDTO> imagesByAuthor = getImagesByAuthor(searchText);
        List<ImageDTO> imagesByTypeName = getImagesByTypeName(searchText);
        if (imagesByTitle != null && !imagesByTitle.isEmpty()) {
            result.addAll(imagesByTitle);
        }
        if (imagesByYear != null && !imagesByYear.isEmpty()) {
            result.addAll(imagesByYear);
        }
        if (imagesByAuthor != null && !imagesByAuthor.isEmpty()) {
            result.addAll(imagesByAuthor);
        }
        if (imagesByTypeName != null && !imagesByTypeName.isEmpty()) {
            result.addAll(imagesByTypeName);
        }
        return result;
    }

    @Override
    public ImageDTO updateImageMetadata(ImageDTO imageDTO) {
        if (imageDTO == null) {
            return null;
        }
        Images images = imageMapper.toImages(imageDTO);
        List<Artists> artistsList = imageMapper.toArtistsList(imageDTO.getArtistDTOList());
        saveImageTypeForImage(imageDTO, images);
        saveImageFileMetadataForImage(imageDTO, images);
        images = imagesRepository.save(images);

        saveImageArtistsForImage(images, artistsList);
        List<ImageAuthors> imageAuthorsList = imageAuthorsRepository.findByImageId(images.getImageId());
        images.setImageAuthorsesByImageId(imageAuthorsList);

        images = imagesRepository.save(images);

        return imageMapper.toImageDTO(images);
    }

    @Override
    public List<ImageDTO> getImagesTop50() {
        Iterable<Images> all = imagesRepository.findAllByOrderByRatingDesc();
        if (all == null) {
            return null;
        }
        List<Images> allImages = new ArrayList<>();
        all.forEach(images -> {
            if (images.getImageFilesByImageFileId() != null && images.getImageFilesByImageFileId().getPublic()) {
                allImages.add(images);
            }
        });
        allImages.sort((o1, o2) -> compareFloat(o1.getRating(), o2.getRating()));
        List<ImageDTO> result = imageMapper.toImageDTOList(allImages);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.subList(0, result.size() > 49 ? 49 : result.size());
        }
    }

    private int compareFloat(Float f1, Float f2) {
        if (f1 == null && f2 == null) {
            return 0;
        }
        if (f1 == null) {
            return 1;
        }
        if (f2 == null) {
            return -1;
        }
        return f2.compareTo(f1);
    }

    @Override
    public void rateImage(RateImageDTO rateImageDTO) {
        if (rateImageDTO == null || rateImageDTO.getImageId() == null || rateImageDTO.getRate() == null) {
            return;
        }
        Optional<Images> imageO = imagesRepository.findById(rateImageDTO.getImageId());
        imageO.ifPresent(image -> {
            Float rating = image.getRating();
            Long ratingTimes = image.getRatingTimes();
            Float temp = (rating * ratingTimes) + (rateImageDTO.getRate() * 10);
            image.setRatingTimes(image.getRatingTimes() + 1);
            image.setRating(temp / image.getRatingTimes());
            imagesRepository.save(image);
        });
    }

    @Override
    public List<ImageDTO> getAllImages() {
        Iterable<Images> imagesIterable = imagesRepository.findAll();
        List<ImageDTO> result = new ArrayList<>();
        imagesIterable.forEach(image -> result.add(imageMapper.toImageDTO(image)));
        return result;
    }

    public ImageMapper getImageMapper() {
        return imageMapper;
    }

    public void setImageMapper(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    public UsersRepositoryCustom getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepositoryCustom usersRepository) {
        this.usersRepository = usersRepository;
    }

    public ArtistsRepository getArtistsRepository() {
        return artistsRepository;
    }

    public void setArtistsRepository(ArtistsRepository artistsRepository) {
        this.artistsRepository = artistsRepository;
    }

    public ImageTypesRepository getImageTypesRepository() {
        return imageTypesRepository;
    }

    public void setImageTypesRepository(ImageTypesRepository imageTypesRepository) {
        this.imageTypesRepository = imageTypesRepository;
    }

    public ImageAuthorsRepository getImageAuthorsRepository() {
        return imageAuthorsRepository;
    }

    public void setImageAuthorsRepository(ImageAuthorsRepository imageAuthorsRepository) {
        this.imageAuthorsRepository = imageAuthorsRepository;
    }

    public ImageFilesRepository getImageFilesRepository() {
        return imageFilesRepository;
    }

    public void setImageFilesRepository(ImageFilesRepository imageFilesRepository) {
        this.imageFilesRepository = imageFilesRepository;
    }

    public ImagesRepository getImagesRepository() {
        return imagesRepository;
    }

    public void setImagesRepository(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }
}
