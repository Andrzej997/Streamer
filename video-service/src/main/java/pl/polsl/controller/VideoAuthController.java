package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.annotations.VerifyUsername;
import pl.polsl.dto.TranscodeRequestDTO;
import pl.polsl.dto.UploadVideoMetadataDTO;
import pl.polsl.dto.VideoDTO;
import pl.polsl.exception.FFMPEGException;
import pl.polsl.helper.Resolution;
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;
import pl.polsl.service.VideoManagementService;
import pl.polsl.service.VideoMetadataService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
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
    public ResponseEntity<Long>
    handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("quality") String quality) {
        if (file == null) {
            return new ResponseEntity<Long>(HttpStatus.NOT_FOUND);
        }
        VideoFiles videoFile = storageService.store(file, quality);
        return new ResponseEntity<Long>(videoFile.getVideoFileId(), HttpStatus.OK);
    }

    @GetMapping(value = "/download")
    @VerifyUsername(parameterName = "username")
    public ResponseEntity<StreamingResponseBody>
    downloadVideoFile(@RequestParam("id") Long id,
                      @RequestParam("username") String username,
                      @RequestParam("authToken") String authToken,
                      @RequestHeader(value = "Range", required = false) String startRange) {
        if (id == null || username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long startPosition = 0l;
        if (!StringUtils.isEmpty(startRange)){
            startRange = startRange.replaceAll("bytes=", "");
            String startPositionString = startRange.substring(0, startRange.indexOf("-"));
            startPosition = Long.parseLong(startPositionString);
        }
        VideoFiles videoFile = storageService.downloadVideoFile(id, username);
        if (videoFile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Long fileLength = new Long(videoFile.getFile().length());
            final InputStream binaryStream = videoFile.getFile().getBinaryStream();
            //binaryStream.skip(startPosition > 0 ? startPosition - 1 : 0);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/" + videoFile.getExtension()))
                    .contentLength(videoFile.getFile().length())
                    .header("X-Content-Duration", fileLength.toString())
                    .header("Content-Range", "bytes 0-" +
                            new Long(fileLength - 1).toString() + "/" + fileLength.toString())
                    .header("Accept-Ranges", "bytes")
                    .body(os -> {

                        readAndWrite(binaryStream, os);
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/file/metadata")
    @VerifyUsername(parameterType = UploadVideoMetadataDTO.class)
    public
    @ResponseBody
    ResponseEntity<UploadVideoMetadataDTO>
    saveVideoFileMetadata(@RequestBody UploadVideoMetadataDTO uploadVideoMetadataDTO,
                          @RequestHeader("AuthHeader") String authHeader) {
        if (uploadVideoMetadataDTO == null) {
            return new ResponseEntity<UploadVideoMetadataDTO>(HttpStatus.NOT_FOUND);
        }
        UploadVideoMetadataDTO result = videoMetadataService.saveMetadata(uploadVideoMetadataDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/video/transcode")
    @VerifyUsername(parameterType = TranscodeRequestDTO.class)
    public
    @ResponseBody
    ResponseEntity<Long>
    transcodeVideo(@RequestBody TranscodeRequestDTO request,
                   @RequestHeader("AuthHeader") String authHeader) {
        if (request == null) {
            return new ResponseEntity<Long>(HttpStatus.NOT_FOUND);
        }
        try {
            VideoFiles videoFile = storageService.transcode(request.getVideoId(), request.getUsername(), Resolution.valueOf(request.getResolutionType()));

            return new ResponseEntity<Long>(videoFile.getVideoFileId(), HttpStatus.OK);
        } catch (InterruptedException | IOException | FFMPEGException | SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/top10/videos")
    @VerifyUsername(parameterName = "username")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>>
    getTop10VideosOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                               @RequestParam("username") String username,
                               @RequestHeader("AuthHeader") String authHeader) {
        if (StringUtils.isEmpty(username)) {
            return new ResponseEntity<List<VideoDTO>>(HttpStatus.NOT_FOUND);
        }
        List<VideoDTO> top10Videos = videoMetadataService.getTop10Videos(username, title);
        return ResponseEntity.ok(top10Videos);
    }

    @GetMapping("/user/videos")
    @VerifyUsername(parameterName = "username")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>>
    getAllUserVideos(@RequestParam("username") String username,
                     @RequestHeader("AuthHeader") String authHeader) {
        if (StringUtils.isEmpty(username)) {
            return new ResponseEntity<List<VideoDTO>>(HttpStatus.NOT_FOUND);
        }
        List<VideoDTO> allUserVideos = videoMetadataService.getAllUserVideos(username);
        return ResponseEntity.ok(allUserVideos);
    }

    @DeleteMapping("/delete/video")
    @VerifyUsername(parameterName = "username")
    public
    @ResponseBody
    ResponseEntity<Boolean>
    deleteFileAndMetadata(@RequestParam("id") Long id,
                          @RequestParam("username") String username,
                          @RequestHeader("AuthHeader") String authHeader) {
        if (StringUtils.isEmpty(username) || id == null) {
            return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
        }
        Boolean success = videoManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/video")
    @VerifyUsername(parameterType = VideoDTO.class)
    public
    @ResponseBody
    ResponseEntity<VideoDTO>
    updateVideoMetadata(@RequestBody VideoDTO videoDTO,
                        @RequestHeader("AuthHeader") String authHeader) {
        if (videoDTO == null) {
            return new ResponseEntity<VideoDTO>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(videoMetadataService.updateVideoMetadata(videoDTO));
    }

    private void readAndWrite(final InputStream is, OutputStream os)
            throws IOException {
        byte[] data = new byte[2048];
        int read = 0;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
        }
        os.flush();
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
