package pl.polsl.service;

import pl.polsl.dto.*;

import java.util.List;

/**
 * Created by Mateusz on 28.11.2016.
 */
public interface EbookMetadataService {
    List<WriterDTO> getWritersByPrediction(String name, String name2, String surname);

    List<LiteraryGenreDTO> getLiteraryGenresByPrediction(String name);

    UploadEbookMetadataDTO saveMetadata(UploadEbookMetadataDTO uploadEbookMetadataDTO);

    List<EbookDTO> getTop10Ebooks(String username, String title);

    List<EbookDTO> getAllUserEbooks(String username);

    List<EbookDTO> searchEbooksByCriteria(SearchEbookCriteriaDTO searchEbookCriteriaDTO);

    EbookDTO updateEbookMetadata(EbookDTO ebookDTO);

    List<EbookDTO> getEbooksTop50();

    void rateEbook(RateEbookDTO rateEbookDTO);

    List<EbookDTO> getAllEbooks();
}
