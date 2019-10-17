package pl.polsl.service;

import pl.polsl.dto.*;

import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
public interface ImageMetadataService {

    List<ArtistDTO> getArtistsByPrediction(String name, String name2, String surname);

    List<ImageTypeDTO> getImageTypesByPrediction(String name);

    UploadImageMetadataDTO saveMetadata(UploadImageMetadataDTO uploadImageMetadataDTO);

    List<ImageDTO> getTop10Images(String username, String title);

    List<ImageDTO> getTop20Images(String username, String title);

    List<ImageDTO> getAllUserImages(String username);

    List<ImageDTO> searchImagesByCriteria(SearchImageCriteriaDTO searchImageCriteriaDTO);

    ImageDTO updateImageMetadata(ImageDTO imageDTO);

    List<ImageDTO> getImagesTop50();

    void rateImage(RateImageDTO rateImageDTO);

    List<ImageDTO> getAllImages();
}
