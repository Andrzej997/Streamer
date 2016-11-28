package pl.polsl.service;

import org.springframework.web.multipart.MultipartFile;
import pl.polsl.model.EbookFiles;

/**
 * Created by Mateusz on 20.11.2016.
 */
public interface StorageService {

    EbookFiles store(MultipartFile file);

    EbookFiles downloadEbookFile(Long id);

    String getExtension(MultipartFile file);
}
