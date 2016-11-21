package pl.polsl.service.impl;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.exception.StorageException;
import pl.polsl.model.MusicFiles;
import pl.polsl.repository.MusicFilesRepository;
import pl.polsl.service.StorageService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Mateusz on 20.11.2016.
 */
@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    @Autowired
    private MusicFilesRepository musicFilesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MusicFiles store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            MusicFiles musicFile = createMusicFile(file);
            return musicFilesRepository.save(musicFile);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MusicFiles downloadMusicFile(Long id) {
        return musicFilesRepository.findOne(id);
    }

    private MusicFiles createMusicFile(MultipartFile file) throws IOException {
        MusicFiles musicFile = new MusicFiles();
        musicFile.setFileName(file.getOriginalFilename());
        musicFile.setCreationDate(new Timestamp(new Date().getTime()));
        musicFile.setExtension(".mp3");
        musicFile.setPublic(true);
        musicFile.setFileSize(new Long(file.getSize()).intValue());
        Blob blob = Hibernate.getLobCreator(getCurrentSession()).createBlob(file.getInputStream(), file.getSize());
        musicFile.setFile(blob);
        return musicFile;
    }

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
}
