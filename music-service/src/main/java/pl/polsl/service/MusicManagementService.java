package pl.polsl.service;

/**
 * Created by Mateusz on 04.12.2016.
 */
public interface MusicManagementService {
    Boolean removeFileAndMetadata(Long fileId, String username);
}
