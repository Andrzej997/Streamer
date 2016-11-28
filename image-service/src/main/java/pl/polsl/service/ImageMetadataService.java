package pl.polsl.service;

import pl.polsl.dto.ArtistDTO;
import pl.polsl.dto.ImageTypeDTO;
import pl.polsl.dto.UploadImageMetadataDTO;

import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
public interface ImageMetadataService {

    List<ArtistDTO> getArtistsByPrediction(String name, String name2, String surname);

    List<ImageTypeDTO> getImageTypesByPrediction(String name);

    UploadImageMetadataDTO saveMetadata(UploadImageMetadataDTO uploadImageMetadataDTO);
}
