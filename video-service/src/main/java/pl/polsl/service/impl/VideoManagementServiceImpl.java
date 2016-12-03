package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.repository.VideoFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.VideoManagementService;

/**
 * Created by Mateusz on 04.12.2016.
 */
@Service
@Transactional
public class VideoManagementServiceImpl implements VideoManagementService {

    @Autowired
    private VideoFilesRepository videoFilesRepository;

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Override
    public Boolean removeFileAndMetadata(Long fileId, String username) {
        if (usersRepository.findUsersByUserName(username) == null) {
            return false;
        }
        if (videoFilesRepository.findOne(fileId) == null) {
            return false;
        }
        videoFilesRepository.delete(fileId);
        return true;
    }
}
