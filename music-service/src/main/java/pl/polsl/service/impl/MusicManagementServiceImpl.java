package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
        if (fileId == null || StringUtils.isEmpty(username)) {
            return false;
        }
        if (usersRepository.findUsersByUserName(username) == null) {
            return false;
        }
        if (!musicFilesRepository.findById(fileId).isPresent()) {
            return false;
        }
        musicFilesRepository.deleteById(fileId);
        return true;
    }

    public MusicFilesRepository getMusicFilesRepository() {
        return musicFilesRepository;
    }

    public void setMusicFilesRepository(MusicFilesRepository musicFilesRepository) {
        this.musicFilesRepository = musicFilesRepository;
    }

    public UsersRepositoryCustom getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepositoryCustom usersRepository) {
        this.usersRepository = usersRepository;
    }
}
