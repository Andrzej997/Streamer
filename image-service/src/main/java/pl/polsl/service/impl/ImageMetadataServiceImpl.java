package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.dto.ArtistDTO;
import pl.polsl.dto.ImageDTO;
import pl.polsl.dto.ImageTypeDTO;
import pl.polsl.dto.UploadImageMetadataDTO;
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
        if (imageDTO.getImageTypeDTO() != null && imageDTO.getImageTypeDTO().getTypeId() == null) {
            ImageTypes imageTypes = imageTypesRepository.save(imageMapper.toImageTypes(imageDTO.getImageTypeDTO()));
            images.setImageTypesByTypeId(imageTypes);
        }
        if (imageDTO.getImageFileDTO() != null) {
            ImageFiles imageFiles = imageFilesRepository.save(imageMapper.toImageFiles(imageDTO.getImageFileDTO()));
            images.setImageFilesByImageFileId(imageFiles);
        }
        images = imagesRepository.save(images);
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
        List<ImageAuthors> imageAuthorsList = imageAuthorsRepository.findByImageId(images.getImageId());
        images.setImageAuthorsesByImageId(imageAuthorsList);

        images = imagesRepository.save(images);

        ImageDTO toImageDTO = imageMapper.toImageDTO(images);
        UploadImageMetadataDTO result = new UploadImageMetadataDTO();
        result.setImageDTO(imageDTO);
        result.setUsername(user.getUserName());
        return result;
    }
}
