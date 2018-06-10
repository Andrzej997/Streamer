package pl.polsl.service.impl;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.exception.StorageException;
import pl.polsl.exception.FFMPEGException;
import pl.polsl.helper.FFMPEGHelper;
import pl.polsl.helper.Resolution;
import pl.polsl.model.UsersView;
import pl.polsl.model.VideoFiles;
import pl.polsl.model.Videos;
import pl.polsl.repository.VideoFilesRepository;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.StorageService;
import pl.polsl.service.TranscodeVideoTaskExecutor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    @Autowired
    private VideoFilesRepository videoFilesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Autowired
    private FFMPEGHelper ffmpegHelper;

    @Autowired
    @Lazy
    private TranscodeVideoTaskExecutor transcodeVideoTaskExecutor;

    @Override
    public VideoFiles store(MultipartFile file, String quality) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            VideoFiles videoFile = createVideoFile(file, quality);
            videoFile = videoFilesRepository.save(videoFile);
            getCurrentSession().flush();
            transcodeVideoTaskExecutor.transcodeVideoToLowersQuality(Resolution.valueOf(videoFile.getResolution()), videoFile);
            return videoFile;
        } catch (IOException | InterruptedException | FFMPEGException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VideoFiles downloadVideoFile(Long id) {
        if (id == null) {
            return null;
        }
        VideoFiles videoFiles = videoFilesRepository.findOne(id);
        if (videoFiles != null && videoFiles.getPublic() != null && videoFiles.getPublic()) {
            return videoFiles;
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VideoFiles downloadVideoFile(Long id, String username) {
        if (id == null || StringUtils.isEmpty(username)) {
            return null;
        }
        VideoFiles videoFiles = videoFilesRepository.findOne(id);
        if (videoFiles == null) {
            return null;
        }
        Collection<Videos> videoses = videoFiles.getVideosesByVideoFileId();
        if (videoses == null || videoses.size() <= 0) {
            return null;
        }
        List<Object> listObject = Arrays.asList(videoses.toArray());
        Videos video = (Videos) listObject.get(0);
        UsersView user = usersRepository.findUsersByUserName(username);
        if (user == null || video == null || video.getOwnerId() == null || !video.getOwnerId().equals(user.getUserId())) {
            return null;
        }
        return videoFiles;
    }

    public VideoFiles createVideoFile(MultipartFile file, String quality) throws IOException, InterruptedException, FFMPEGException {
        VideoFiles videoFiles = new VideoFiles();
        videoFiles.setFileName(file.getOriginalFilename());
        videoFiles.setCreationDate(new Timestamp(new Date().getTime()));
        videoFiles.setExtension(getExtension(file.getOriginalFilename()));
        videoFiles.setPublic(true);
        videoFiles.setFileSize(file.getSize());
        videoFiles.setResolution(Resolution.valueOf(quality).name());

        Blob video = Hibernate.getLobCreator(getCurrentSession()).createBlob(file.getInputStream(), file.getSize());
        videoFiles.setFile(video);

        File thumbnailFile = generateThumbnail(file);

        FileInputStream thumbnailStream = new FileInputStream(thumbnailFile);

        Blob thumbnail = Hibernate.getLobCreator(getCurrentSession()).createBlob(thumbnailStream, thumbnailFile.length());
        videoFiles.setThumbnail(thumbnail);

        return videoFiles;
    }

    private String getExtension(String originalFilename) {
        if (StringUtils.isEmpty(originalFilename)) {
            return null;
        }
        int index = originalFilename.lastIndexOf(".");
        if (index <= 0) {
            return null;
        }
        return originalFilename.substring(index + 1);
    }

    @Override
    public String getExtension(MultipartFile file) {
        if (file == null) {
            return null;
        }

        return getExtension(file.getOriginalFilename());
    }

    @Override
    public VideoFiles transcode(Long id, String username, Resolution resolution) throws IOException, SQLException, FFMPEGException, InterruptedException {
        VideoFiles videoFile = downloadVideoFile(id, username);

        if (videoFile == null) {
            return null;
        }
        return transcodeInternal(videoFile, resolution, 0);
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void transcode(VideoFiles videoFile, Resolution resolution)
            throws IOException, SQLException, FFMPEGException, InterruptedException {
        this.transcodeInternal(videoFile, resolution, 0);
    }

    private VideoFiles transcodeInternal(VideoFiles videoFile, Resolution resolution, Integer threadTime)
            throws IOException, SQLException, FFMPEGException, InterruptedException {

        if (videoFile == null){
            return null;
        }
        File video = File.createTempFile("sTr", videoFile.getFileName().replaceAll("\\s", "") + "." + videoFile.getExtension());
        video.deleteOnExit();
        Long id = videoFile.getVideoFileId();
        VideoFiles vf = null;
        try {
            vf = videoFile.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        videoFile = videoFilesRepository.findOne(id);
        if (videoFile == null && threadTime < 180000){
            Thread.sleep(100);
            transcodeInternal(vf, resolution, threadTime+100);
        }

        if (videoFile == null){
            return null;
        }
        FileOutputStream out = new FileOutputStream(video);
        IOUtils.copyLarge(videoFile.getFile().getBinaryStream(), out);
        out.close();

        File transcoded = File.createTempFile("sTO", videoFile.getFileName().replaceAll("\\s", "") + "." + videoFile.getExtension());
        transcoded.deleteOnExit();

        ffmpegHelper.transcode(video, transcoded, resolution);

        VideoFiles transcodedFile = createVideoFile(transcoded, videoFile.getVideoFileId(), videoFile.getFileName());
        transcodedFile.setResolution(resolution.name());

        return videoFilesRepository.save(transcodedFile);
    }

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public VideoFilesRepository getVideoFilesRepository() {
        return videoFilesRepository;
    }

    public void setVideoFilesRepository(VideoFilesRepository videoFilesRepository) {
        this.videoFilesRepository = videoFilesRepository;
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

    private File generateThumbnail(MultipartFile file) throws IOException, InterruptedException, FFMPEGException {
        File thumbnailFile = File.createTempFile("sUT", ".jpeg");
        thumbnailFile.deleteOnExit();

        File videoFile = File.createTempFile("sUp", file.getOriginalFilename().replaceAll("\\s", ""));
        videoFile.deleteOnExit();

        FileOutputStream out = new FileOutputStream(videoFile);
        IOUtils.copyLarge(file.getInputStream(), out);
        out.close();

        ffmpegHelper.generateThumbnail(thumbnailFile, videoFile);

        return thumbnailFile;
    }


    private File generateThumbnail(String name, File file) throws IOException, InterruptedException, FFMPEGException, SQLException {
        File thumbnailFile = File.createTempFile("sUT", ".jpeg");
        thumbnailFile.deleteOnExit();

        File videoFile = File.createTempFile("sUp", name.replaceAll("\\s", ""));
        videoFile.deleteOnExit();

        FileOutputStream out = new FileOutputStream(videoFile);
        IOUtils.copyLarge(new FileInputStream(file), out);
        out.close();

        ffmpegHelper.generateThumbnail(thumbnailFile, videoFile);

        return thumbnailFile;
    }

    private VideoFiles createVideoFile(File file, Long parentVideoId, String name) throws IOException, InterruptedException, SQLException, FFMPEGException {
        VideoFiles videoFiles = new VideoFiles();
        videoFiles.setFileName(name);
        videoFiles.setCreationDate(new Timestamp(new Date().getTime()));
        videoFiles.setExtension(getExtension(file.getName()));
        videoFiles.setPublic(true);
        videoFiles.setFileSize(file.length());
        videoFiles.setParentFileId(parentVideoId);

        FileInputStream stream = new FileInputStream(file);

        Blob video = Hibernate.getLobCreator(getCurrentSession()).createBlob(stream, file.length());
        videoFiles.setFile(video);

        File thumbnailFile = generateThumbnail(videoFiles.getFileName() + "." + videoFiles.getExtension(), file);

        FileInputStream thumbnailStream = new FileInputStream(thumbnailFile);

        Blob thumbnail = Hibernate.getLobCreator(getCurrentSession()).createBlob(thumbnailStream, thumbnailFile.length());
        videoFiles.setThumbnail(thumbnail);

        return videoFiles;
    }
}
