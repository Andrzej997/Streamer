package pl.polsl.service.impl;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.exception.StorageException;
import pl.polsl.model.EbookFiles;
import pl.polsl.repository.EbookFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.StorageService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Mateusz on 28.11.2016.
 */
@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    @Autowired
    private EbookFilesRepository ebookFilesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Override
    public EbookFiles store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            EbookFiles ebookFile = createVideoFile(file);
            return ebookFilesRepository.save(ebookFile);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public EbookFiles downloadEbookFile(Long id) {
        return ebookFilesRepository.findOne(id);
    }

    public EbookFiles createVideoFile(MultipartFile file) throws IOException {
        EbookFiles ebookFiles = new EbookFiles();
        ebookFiles.setFileName(file.getOriginalFilename());
        ebookFiles.setCreationDate(new Timestamp(new Date().getTime()));
        ebookFiles.setExtension(getExtension(file));
        ebookFiles.setPublic(true);
        ebookFiles.setFileSize(new Long(file.getSize()));
        Blob blob = Hibernate.getLobCreator(getCurrentSession()).createBlob(file.getInputStream(), file.getSize());
        ebookFiles.setFile(blob);
        return ebookFiles;
    }

    @Override
    public String getExtension(MultipartFile file) {
        if (file == null) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            return null;
        }
        int index = originalFilename.lastIndexOf(".");
        if (index <= 0) {
            return null;
        }
        return originalFilename.substring(index + 1);
    }

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
}
