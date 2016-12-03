package pl.polsl.service;

/**
 * Created by Mateusz on 04.12.2016.
 */
public interface ImageManagementService {
    Boolean removeFileAndMetadata(Long fileId, String username);
}
