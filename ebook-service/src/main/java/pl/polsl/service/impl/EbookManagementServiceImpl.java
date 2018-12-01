package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.repository.EbookFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.EbookManagementService;

/**
 * Created by Mateusz on 04.12.2016.
 */
@Service
@Transactional
public class EbookManagementServiceImpl implements EbookManagementService {

    @Autowired
    private EbookFilesRepository ebookFilesRepository;

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
        if (!ebookFilesRepository.findById(fileId).isPresent()) {
            return false;
        }
        ebookFilesRepository.deleteById(fileId);
        return true;
    }

    public EbookFilesRepository getEbookFilesRepository() {
        return ebookFilesRepository;
    }

    public void setEbookFilesRepository(EbookFilesRepository ebookFilesRepository) {
        this.ebookFilesRepository = ebookFilesRepository;
    }

    public UsersRepositoryCustom getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepositoryCustom usersRepository) {
        this.usersRepository = usersRepository;
    }
}
