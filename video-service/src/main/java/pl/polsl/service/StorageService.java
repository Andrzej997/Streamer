package pl.polsl.service;

import org.springframework.web.multipart.MultipartFile;
import pl.polsl.exception.FFMPEGException;
import pl.polsl.helper.Resolution;
import pl.polsl.model.VideoFiles;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Mateusz on 27.11.2016.
 */
public interface StorageService {

    VideoFiles store(MultipartFile file, String quality);

    VideoFiles downloadVideoFile(Long id);

    VideoFiles downloadVideoFile(Long id, String username);

    String getExtension(MultipartFile file);

    VideoFiles transcode(Long id, String username, Resolution resolution) throws IOException, SQLException, FFMPEGException, InterruptedException;
}
