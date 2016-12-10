package pl.polsl.service;

import org.springframework.web.multipart.MultipartFile;
import pl.polsl.model.VideoFiles;

/**
 * Created by Mateusz on 27.11.2016.
 */
public interface StorageService {

    VideoFiles store(MultipartFile file);

    VideoFiles downloadVideoFile(Long id);

    VideoFiles downloadVideoFile(Long id, String username);

    String getExtension(MultipartFile file);
}
