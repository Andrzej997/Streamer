package pl.polsl.service;

import org.springframework.web.multipart.MultipartFile;
import pl.polsl.model.MusicFiles;

/**
 * Created by Mateusz on 20.11.2016.
 */
public interface StorageService {

    MusicFiles store(MultipartFile file);

    MusicFiles downloadMusicFile(Long id);

    MusicFiles downloadMusicFile(Long id, String username);

    String getExtension(MultipartFile file);
}
