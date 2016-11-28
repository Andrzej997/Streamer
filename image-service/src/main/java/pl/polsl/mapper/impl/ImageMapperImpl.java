package pl.polsl.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.polsl.dto.ArtistDTO;
import pl.polsl.dto.ImageDTO;
import pl.polsl.dto.ImageFileDTO;
import pl.polsl.dto.ImageTypeDTO;
import pl.polsl.mapper.ImageMapper;
import pl.polsl.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
@Component
public class ImageMapperImpl implements ImageMapper {

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public ArtistDTO toArtistDTO(Artists artists) {
        if (artists == null) {
            return null;
        }
        return modelMapper.map(artists, ArtistDTO.class);
    }

    @Override
    public List<ArtistDTO> toArtistDTOList(List<Artists> artistsList) {
        if (artistsList == null) {
            return null;
        }
        List<ArtistDTO> result = new ArrayList<>();
        artistsList.forEach(artists -> result.add(toArtistDTO(artists)));
        return result;
    }

    @Override
    public Artists toArtists(ArtistDTO artistDTO) {
        if (artistDTO == null) {
            return null;
        }
        return modelMapper.map(artistDTO, Artists.class);
    }

    @Override
    public List<Artists> toArtistsList(List<ArtistDTO> artistDTOList) {
        if (artistDTOList == null) {
            return null;
        }
        List<Artists> result = new ArrayList<>();
        artistDTOList.forEach(artists -> result.add(toArtists(artists)));
        return result;
    }

    @Override
    public ImageFileDTO toImageFileDTO(ImageFiles imageFiles) {
        if (imageFiles == null) {
            return null;
        }
        return modelMapper.map(imageFiles, ImageFileDTO.class);
    }

    @Override
    public List<ImageFileDTO> toImageFileDTOList(List<ImageFiles> imageFiles) {
        if (imageFiles == null) {
            return null;
        }
        List<ImageFileDTO> result = new ArrayList<>();
        imageFiles.forEach(imageFiles1 -> result.add(toImageFileDTO(imageFiles1)));
        return result;
    }

    @Override
    public ImageFiles toImageFiles(ImageFileDTO imageFileDTO) {
        if (imageFileDTO == null) {
            return null;
        }
        return modelMapper.map(imageFileDTO, ImageFiles.class);
    }

    @Override
    public List<ImageFiles> toImageFilesList(List<ImageFileDTO> imageFileDTOList) {
        if (imageFileDTOList == null) {
            return null;
        }
        List<ImageFiles> result = new ArrayList<>();
        imageFileDTOList.forEach(imageFileDTO -> result.add(toImageFiles(imageFileDTO)));
        return result;
    }

    @Override
    public ImageTypeDTO toImageTypeDTO(ImageTypes imageTypes) {
        if (imageTypes == null) {
            return null;
        }
        return modelMapper.map(imageTypes, ImageTypeDTO.class);
    }

    @Override
    public List<ImageTypeDTO> toImageTypeDTOList(List<ImageTypes> imageTypesList) {
        if (imageTypesList == null) {
            return null;
        }
        List<ImageTypeDTO> result = new ArrayList<>();
        imageTypesList.forEach(imageTypes -> result.add(toImageTypeDTO(imageTypes)));
        return result;
    }

    @Override
    public ImageTypes toImageTypes(ImageTypeDTO imageTypeDTO) {
        if (imageTypeDTO == null) {
            return null;
        }
        return modelMapper.map(imageTypeDTO, ImageTypes.class);
    }

    @Override
    public List<ImageTypes> toImageTypesList(List<ImageTypeDTO> imageTypeDTOList) {
        if (imageTypeDTOList == null) {
            return null;
        }
        List<ImageTypes> result = new ArrayList<>();
        imageTypeDTOList.forEach(imageTypeDTO -> result.add(toImageTypes(imageTypeDTO)));
        return result;
    }

    @Override
    public ImageDTO toImageDTO(Images images) {
        if (images == null) {
            return null;
        }
        ImageFiles imageFiles = images.getImageFilesByImageFileId();
        ImageTypes imageTypes = images.getImageTypesByTypeId();
        Collection<ImageAuthors> imageAuthorsList = images.getImageAuthorsesByImageId();
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setImageId(images.getImageId());
        imageDTO.setTitle(images.getTitle());
        imageDTO.setImageFileId(images.getImageFileId());
        imageDTO.setNativeWidth(images.getNativeWidth());
        imageDTO.setNativeHeight(images.getNativeHeight());
        imageDTO.setResolution(images.getResolution());
        imageDTO.setDepth(images.getDepth());
        imageDTO.setComments(images.getComments());
        imageDTO.setRating(images.getRating());
        imageDTO.setTypeId(images.getTypeId());
        imageDTO.setYear(images.getYear());
        imageDTO.setOwnerId(images.getOwnerId());
        imageDTO.setImageFileDTO(toImageFileDTO(imageFiles));
        imageDTO.setImageTypeDTO(toImageTypeDTO(imageTypes));
        if (imageAuthorsList != null) {
            List<ArtistDTO> artistDTOList = new ArrayList<>();
            for (ImageAuthors imageAuthors : imageAuthorsList) {
                artistDTOList.add(toArtistDTO(imageAuthors.getArtistsByAuthorId()));
            }
            imageDTO.setArtistDTOList(artistDTOList);
        }
        return imageDTO;
    }

    @Override
    public List<ImageDTO> toImageDTOList(List<Images> imagesList) {
        if (imagesList == null) {
            return null;
        }
        List<ImageDTO> result = new ArrayList<>();
        imagesList.forEach(images -> result.add(toImageDTO(images)));
        return result;
    }

    @Override
    public Images toImages(ImageDTO imageDTO) {
        if (imageDTO == null) {
            return null;
        }
        ImageFileDTO imageFileDTO = imageDTO.getImageFileDTO();
        ImageTypeDTO imageTypeDTO = imageDTO.getImageTypeDTO();
        Images images = new Images();
        images.setImageId(imageDTO.getImageId());
        images.setTitle(imageDTO.getTitle());
        images.setImageFileId(imageDTO.getImageFileId());
        images.setNativeWidth(imageDTO.getNativeWidth());
        images.setNativeHeight(imageDTO.getNativeHeight());
        images.setResolution(imageDTO.getResolution());
        images.setDepth(imageDTO.getDepth());
        images.setComments(imageDTO.getComments());
        images.setRating(imageDTO.getRating());
        images.setTypeId(imageDTO.getTypeId());
        images.setYear(imageDTO.getYear());
        images.setOwnerId(imageDTO.getOwnerId());
        images.setImageFilesByImageFileId(toImageFiles(imageFileDTO));
        images.setImageTypesByTypeId(toImageTypes(imageTypeDTO));
        return images;
    }

    @Override
    public List<Images> toImagesList(List<ImageDTO> imageDTOList) {
        if (imageDTOList == null) {
            return null;
        }
        List<Images> result = new ArrayList<>();
        imageDTOList.forEach(imageDTO -> result.add(toImages(imageDTO)));
        return result;
    }

}
