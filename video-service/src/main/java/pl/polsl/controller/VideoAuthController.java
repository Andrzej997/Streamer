package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.dto.UploadVideoMetadataDTO;
import pl.polsl.dto.VideoDTO;
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;
import pl.polsl.service.VideoManagementService;
import pl.polsl.service.VideoMetadataService;

import java.util.List;

/**
 * Created by Mateusz on 07.12.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/auth")
public class VideoAuthController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private VideoManagementService videoManagementService;

    @PostMapping("/upload")
    public ResponseEntity<Long> handleFileUpload(@RequestParam("file") MultipartFile file) {

        VideoFiles videoFile = storageService.store(file);
        return ResponseEntity.ok(videoFile.getVideoFileId());
    }

    @PostMapping("/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadVideoMetadataDTO> saveVideoFileMetadata(@RequestBody UploadVideoMetadataDTO uploadVideoMetadataDTO) {
        UploadVideoMetadataDTO result = videoMetadataService.saveMetadata(uploadVideoMetadataDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top10/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> getTop10VideosOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                                                              @RequestParam("username") String username) {
        List<VideoDTO> top10Videos = videoMetadataService.getTop10Videos(username, title);
        return ResponseEntity.ok(top10Videos);
    }

    @GetMapping("/user/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> getAllUserVideos(@RequestParam("username") String username) {
        List<VideoDTO> allUserVideos = videoMetadataService.getAllUserVideos(username);
        return ResponseEntity.ok(allUserVideos);
    }

    @DeleteMapping("/delete/video")
    public
    @ResponseBody
    ResponseEntity<Boolean> deleteFileAndMetadata(@RequestParam("id") Long id, @RequestParam("username") String username) {
        Boolean success = videoManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/video")
    public
    @ResponseBody
    ResponseEntity<VideoDTO> updateVideoMetadata(@RequestBody VideoDTO videoDTO) {
        return ResponseEntity.ok(videoMetadataService.updateVideoMetadata(videoDTO));
    }

    public StorageService getStorageService() {
        return storageService;
    }

    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public VideoMetadataService getVideoMetadataService() {
        return videoMetadataService;
    }

    public void setVideoMetadataService(VideoMetadataService videoMetadataService) {
        this.videoMetadataService = videoMetadataService;
    }

    public VideoManagementService getVideoManagementService() {
        return videoManagementService;
    }

    public void setVideoManagementService(VideoManagementService videoManagementService) {
        this.videoManagementService = videoManagementService;
    }
}
