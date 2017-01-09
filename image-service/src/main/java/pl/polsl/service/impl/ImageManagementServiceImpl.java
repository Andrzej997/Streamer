package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.repository.ImageFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.ImageManagementService;

/**
 * Created by Mateusz on 04.12.2016.
 */
@Service
@Transactional
public class ImageManagementServiceImpl implements ImageManagementService {

    @Autowired
    private ImageFilesRepository imageFilesRepository;

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
        if (imageFilesRepository.findOne(fileId) == null) {
            return false;
        }
        imageFilesRepository.delete(fileId);
        return true;
    }

    public ImageFilesRepository getImageFilesRepository() {
        return imageFilesRepository;
    }

    public void setImageFilesRepository(ImageFilesRepository imageFilesRepository) {
        this.imageFilesRepository = imageFilesRepository;
    }

    public UsersRepositoryCustom getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepositoryCustom usersRepository) {
        this.usersRepository = usersRepository;
    }
}
