package pl.polsl.service;

import pl.polsl.dto.LiteraryGenreDTO;
import pl.polsl.dto.UploadEbookMetadataDTO;
import pl.polsl.dto.WriterDTO;

import java.util.List;

/**
 * Created by Mateusz on 28.11.2016.
 */
public interface EbookMetadataService {
    List<WriterDTO> getWritersByPrediction(String name, String name2, String surname);

    List<LiteraryGenreDTO> getLiteraryGenresByPrediction(String name);

    UploadEbookMetadataDTO saveMetadata(UploadEbookMetadataDTO uploadEbookMetadataDTO);
}
