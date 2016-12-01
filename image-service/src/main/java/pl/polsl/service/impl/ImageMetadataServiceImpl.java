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
        images = imagesRepository.save(images);

        saveImageArtistsForImage(images, artistsList);
        List<ImageAuthors> imageAuthorsList = imageAuthorsRepository.findByImageId(images.getImageId());
        images.setImageAuthorsesByImageId(imageAuthorsList);

        images = imagesRepository.save(images);

        ImageDTO toImageDTO = imageMapper.toImageDTO(images);
        UploadImageMetadataDTO result = new UploadImageMetadataDTO();
        result.setImageDTO(imageDTO);
        result.setUsername(user.getUserName());
        return result;
    }

    private void saveImageTypeForImage(ImageDTO imageDTO, Images images) {
        ImageTypes imageTypes = null;
        if (imageDTO.getImageTypeDTO() != null && imageDTO.getImageTypeDTO().getTypeId() == null) {
            imageTypes = imageTypesRepository.save(imageMapper.toImageTypes(imageDTO.getImageTypeDTO()));
        } else if (imageDTO.getImageTypeDTO() != null) {
            imageTypes = imageMapper.toImageTypes(imageDTO.getImageTypeDTO());
        }
        if (imageTypes == null) {
            return;
        }
        images.setImageTypesByTypeId(imageTypes);
        images.setTypeId(imageTypes.getTypeId());
    }

    private void saveImageFileMetadataForImage(ImageDTO imageDTO, Images images) {
        ImageFiles imageFiles = null;
        if (imageDTO.getImageFileDTO() != null) {
            imageFiles = imageFilesRepository.save(imageMapper.toImageFiles(imageDTO.getImageFileDTO()));
        }
        if (imageFiles == null) {
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
            if (artist.getArtistId() == null) {
                artist = artistsRepository.save(artist);
            }
            ImageAuthors imageAuthors = new ImageAuthors();
            imageAuthors.setAuthorId(artist.getArtistId());
            imageAuthors.setArtistsByAuthorId(artist);
            imageAuthors.setImageId(images.getImageId());
            imageAuthors.setImagesByImageId(images);
            imageAuthorsRepository.save(imageAuthors);
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
        imagesList.addAll(imagesRepository.findByTitleLikeOrderByRating(title));
        return imageMapper.toImageDTOList(imagesList);
    }

    private List<ImageDTO> getImagesByAuthor(String authorData) {
        if (authorData == null || "undefined".equals(authorData)) {
            return null;
        }
        String[] data = authorData.split(" ");
        if (data == null || data.length <= 0) {
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
                break;
        }
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
        return imageMapper.toImageDTOList(imagesList);
    }

    private List<ImageDTO> getImagesByYear(String year) {
        if (year == null || "undefined".equals(year) || !NumberUtils.isNumber(year)) {
            return null;
        }
        Short yearNumber = Short.parseShort(year);
        List<Images> imagesList = imagesRepository.findByYearOrderByRating(yearNumber);
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
}
