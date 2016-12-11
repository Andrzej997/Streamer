package pl.polsl.service;

import org.springframework.web.multipart.MultipartFile;
import pl.polsl.model.ImageFiles;

/**
 * Created by Mateusz on 27.11.2016.
 */
public interface StorageService {

    ImageFiles store(MultipartFile file);

    ImageFiles downloadImageFile(Long id);

    ImageFiles downloadImageFile(Long id, String username);

    String getExtension(MultipartFile file);

}
