package pl.polsl.mapper;

import pl.polsl.dto.ArtistDTO;
import pl.polsl.dto.ImageDTO;
import pl.polsl.dto.ImageFileDTO;
import pl.polsl.dto.ImageTypeDTO;
import pl.polsl.model.Artists;
import pl.polsl.model.ImageFiles;
import pl.polsl.model.ImageTypes;
import pl.polsl.model.Images;

import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
public interface ImageMapper {
    ArtistDTO toArtistDTO(Artists artists);

    List<ArtistDTO> toArtistDTOList(List<Artists> artistsList);

    Artists toArtists(ArtistDTO artistDTO);

    List<Artists> toArtistsList(List<ArtistDTO> artistDTOList);

    ImageFileDTO toImageFileDTO(ImageFiles imageFiles);

    List<ImageFileDTO> toImageFileDTOList(List<ImageFiles> imageFiles);

    ImageFiles toImageFiles(ImageFileDTO imageFileDTO);

    List<ImageFiles> toImageFilesList(List<ImageFileDTO> imageFileDTOList);

    ImageTypeDTO toImageTypeDTO(ImageTypes imageTypes);

    List<ImageTypeDTO> toImageTypeDTOList(List<ImageTypes> imageTypesList);

    ImageTypes toImageTypes(ImageTypeDTO imageTypeDTO);

    List<ImageTypes> toImageTypesList(List<ImageTypeDTO> imageTypeDTOList);

    ImageDTO toImageDTO(Images images);

    List<ImageDTO> toImageDTOList(List<Images> imagesList);

    Images toImages(ImageDTO imageDTO);

    List<Images> toImagesList(List<ImageDTO> imageDTOList);
}
