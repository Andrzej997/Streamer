package pl.polsl.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VideoFilesCacheableProvider {

    private StorageService storageService;

    private ConcurrentHashMap<Long, VideoCached> videoCachedMap;

    @Autowired
    public VideoFilesCacheableProvider(StorageService storageService) {
        this.storageService = storageService;
        this.videoCachedMap = new ConcurrentHashMap<>();
    }

    public VideoFiles downloadVideoFile(Long id) {
        if (id == null) {
            return null;
        }
        VideoFiles video = getVideoFromCache(id);
        if (video == null) {
            video = storageService.downloadVideoFile(id);
            if (video != null) {
                videoCachedMap.put(id, new VideoCached(video));
            }
        }
        return video;
    }

    private VideoFiles getVideoFromCache(Long id) {
        VideoCached videoCached = videoCachedMap.get(id);
        if (videoCached == null) {
            return null;
        }
        if (videoCached.expired()) {
            videoCachedMap.remove(id);
            return null;
        }
        return videoCached.getVideoFile();
    }

    class VideoCached {
        private VideoFiles videoFile;
        private LocalDateTime expirationTimestamp;

        VideoCached(VideoFiles videoFile) {
            this.videoFile = videoFile;
            this.expirationTimestamp = LocalDateTime.now().plusMinutes(5L);
        }

        VideoFiles getVideoFile() {
            return videoFile;
        }

        boolean expired(){
            return LocalDateTime.now().isAfter(expirationTimestamp);
        }
    }
}
