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
import pl.polsl.model.MusicFiles;
import pl.polsl.model.Songs;
import pl.polsl.model.UsersView;
import pl.polsl.repository.MusicFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.StorageService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private UsersRepositoryCustom usersRepository;

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
        if (id == null) {
            return null;
        }
        MusicFiles musicFiles = musicFilesRepository.findOne(id);
        if (musicFiles != null && musicFiles.getPublic() != null && musicFiles.getPublic()) {
            return musicFiles;
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MusicFiles downloadMusicFile(Long id, String username) {
        if (id == null || StringUtils.isEmpty(username)) {
            return null;
        }
        MusicFiles musicFiles = musicFilesRepository.findOne(id);
        if (musicFiles == null) {
            return null;
        }
        Collection<Songs> songses = musicFiles.getSongsesByMusicFileId();
        if (songses == null || songses.size() <= 0) {
            return null;
        }
        List<Object> listObject = Arrays.asList(songses.toArray());
        Songs song = (Songs) listObject.get(0);
        UsersView user = usersRepository.findUsersByUserName(username);
        if (user == null || song == null || user.getUserId() == null || !user.getUserId().equals(song.getOwnerId())) {
            return null;
        }
        return musicFiles;
    }

    private MusicFiles createMusicFile(MultipartFile file) throws IOException {
        MusicFiles musicFile = new MusicFiles();
        musicFile.setFileName(file.getOriginalFilename());
        musicFile.setCreationDate(new Timestamp(new Date().getTime()));
        musicFile.setExtension(getExtension(file));
        musicFile.setPublic(true);
        musicFile.setFileSize(new Long(file.getSize()).intValue());
        Blob blob = Hibernate.getLobCreator(getCurrentSession()).createBlob(file.getInputStream(), file.getSize());
        musicFile.setFile(blob);
        return musicFile;
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

    public MusicFilesRepository getMusicFilesRepository() {
        return musicFilesRepository;
    }

    public void setMusicFilesRepository(MusicFilesRepository musicFilesRepository) {
        this.musicFilesRepository = musicFilesRepository;
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
