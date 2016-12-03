package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.repository.MusicFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.MusicManagementService;

/**
 * Created by Mateusz on 04.12.2016.
 */
@Service
@Transactional
public class MusicManagementServiceImpl implements MusicManagementService {

    @Autowired
    private MusicFilesRepository musicFilesRepository;

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Override
    public Boolean removeFileAndMetadata(Long fileId, String username) {
        if (usersRepository.findUsersByUserName(username) == null) {
            return false;
        }
        if (musicFilesRepository.findOne(fileId) == null) {
            return false;
        }
        musicFilesRepository.delete(fileId);
        return true;
    }
}
