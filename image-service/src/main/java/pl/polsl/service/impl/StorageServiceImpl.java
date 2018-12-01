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
import pl.polsl.model.ImageFiles;
import pl.polsl.model.Images;
import pl.polsl.model.UsersView;
import pl.polsl.repository.ImageFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.StorageService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Mateusz on 27.11.2016.
 */
@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    @Autowired
    private ImageFilesRepository imageFilesRepositoryy;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Override
    public ImageFiles store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            ImageFiles musicFile = createImageFile(file);
            return imageFilesRepositoryy.save(musicFile);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImageFiles downloadImageFile(Long id) {
        if (id == null) {
            return null;
        }
        Optional<ImageFiles> imageFilesO = imageFilesRepositoryy.findById(id);
        if (!imageFilesO.isPresent()) {
            return null;
        }
        ImageFiles imageFiles = imageFilesO.get();
        if (imageFiles != null && imageFiles.getPublic() != null && imageFiles.getPublic()) {
            return imageFiles;
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImageFiles downloadImageFile(Long id, String username) {
        if (id == null || StringUtils.isEmpty(username)) {
            return null;
        }
        Optional<ImageFiles> imageFilesO = imageFilesRepositoryy.findById(id);
        if (!imageFilesO.isPresent()) {
            return null;
        }
        ImageFiles imageFiles = imageFilesO.get();
        Collection<Images> images = imageFiles.getImagesByImageFileId();
        if (images == null || images.size() <= 0) {
            return null;
        }
        List<Object> listObject = Arrays.asList(images.toArray());
        Images image = (Images) listObject.get(0);
        UsersView user = usersRepository.findUsersByUserName(username);
        if (user == null || image == null || image.getOwnerId() == null || !image.getOwnerId().equals(user.getUserId())) {
            return null;
        }
        return imageFiles;
    }

    public ImageFiles createImageFile(MultipartFile file) throws IOException {
        ImageFiles imageFiles = new ImageFiles();
        imageFiles.setFileName(file.getOriginalFilename());
        imageFiles.setCreationDate(new Timestamp(new Date().getTime()));
        imageFiles.setFileExtension(getExtension(file));
        imageFiles.setPublic(true);
        imageFiles.setFileSize(new Long(file.getSize()));
        Blob blob = Hibernate.getLobCreator(getCurrentSession()).createBlob(file.getInputStream(), file.getSize());
        imageFiles.setFile(blob);
        return imageFiles;
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

    public ImageFilesRepository getImageFilesRepositoryy() {
        return imageFilesRepositoryy;
    }

    public void setImageFilesRepositoryy(ImageFilesRepository imageFilesRepositoryy) {
        this.imageFilesRepositoryy = imageFilesRepositoryy;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public UsersRepositoryCustom getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepositoryCustom usersRepository) {
        this.usersRepository = usersRepository;
    }
}
