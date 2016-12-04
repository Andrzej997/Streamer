package pl.polsl.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.dto.*;
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;
import pl.polsl.service.VideoManagementService;
import pl.polsl.service.VideoMetadataService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
@SuppressWarnings("ALL")
@RestController
public class VideoController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private VideoManagementService videoManagementService;

    @PostMapping("/auth/upload")
    public ResponseEntity<Long> handleFileUpload(@RequestParam("file") MultipartFile file) {

        VideoFiles videoFile = storageService.store(file);
        return ResponseEntity.ok(videoFile.getVideoFileId());
    }

    @GetMapping("/noauth/download")
    public void downloadVideoFile(@RequestParam("id") Long id, HttpServletResponse response) {
        VideoFiles videoFile = storageService.downloadVideoFile(id);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" + videoFile.getFileName() + "\"");
            response.setContentType("video/" + videoFile.getExtension());
            IOUtils.copy(videoFile.getFile().getBinaryStream(), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return;
    }

    @GetMapping("/noauth/directors/prediction")
    public
    @ResponseBody
    ResponseEntity<List<DirectorDTO>> getDirectorsPredictionList(@RequestParam("name") String name,
                                                                 @RequestParam("name2") String name2,
                                                                 @RequestParam("surname") String surname) {
        List<DirectorDTO> directorsByPrediction = videoMetadataService.getDirectorsByPrediction(name, name2, surname);
        return new ResponseEntity<List<DirectorDTO>>(directorsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/noauth/videoseries/prediction")
    public
    @ResponseBody
    ResponseEntity<List<VideoSerieDTO>> getVideoSeriesPredictionList(@RequestParam("serieTitle") String serieTitle,
                                                                     @RequestParam("videoTitle") String videoTitle) {
        List<VideoSerieDTO> videoSeriesByPrediction = videoMetadataService.getVideoSeriesByPrediction(serieTitle, videoTitle);
        return new ResponseEntity<List<VideoSerieDTO>>(videoSeriesByPrediction, HttpStatus.OK);
    }

    @GetMapping("/noauth/genres/prediction")
    public
    @ResponseBody
    ResponseEntity<List<FilmGenreDTO>> getGenresPredictionList(@RequestParam("name") String name) {
        List<FilmGenreDTO> genresByPrediction = videoMetadataService.getFilmGenresByPrediction(name);
        return new ResponseEntity<List<FilmGenreDTO>>(genresByPrediction, HttpStatus.OK);
    }

    @PostMapping("/auth/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadVideoMetadataDTO> saveVideoFileMetadata(@RequestBody UploadVideoMetadataDTO uploadVideoMetadataDTO) {
        UploadVideoMetadataDTO result = videoMetadataService.saveMetadata(uploadVideoMetadataDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/noauth/top10/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> getTop10Videos(@RequestParam(value = "title", required = false) String title) {
        List<VideoDTO> top10Videos = videoMetadataService.getTop10Videos(null, title);
        return ResponseEntity.ok(top10Videos);
    }

    @GetMapping("/auth/top10/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> getTop10VideosOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                                                              @RequestParam("username") String username) {
        List<VideoDTO> top10Videos = videoMetadataService.getTop10Videos(username, title);
        return ResponseEntity.ok(top10Videos);
    }

    @GetMapping("/auth/user/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> getAllUserVideos(@RequestParam("username") String username) {
        List<VideoDTO> allUserVideos = videoMetadataService.getAllUserVideos(username);
        return ResponseEntity.ok(allUserVideos);
    }


    @GetMapping("/noauth/public/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> searchVideosByCriteria(@RequestParam("criteria") SearchVideoCriteriaDTO searchVideoCriteriaDTO) {
        List<VideoDTO> videoDTOList = videoMetadataService.searchVideosByCriteria(searchVideoCriteriaDTO);
        return ResponseEntity.ok(videoDTOList);
    }

    @DeleteMapping("/auth/delete/video")
    public
    @ResponseBody
    ResponseEntity<Boolean> deleteFileAndMetadata(@RequestParam("id") Long id, @RequestParam("username") String username) {
        Boolean success = videoManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/auth/update/video")
    public
    @ResponseBody
    ResponseEntity<VideoDTO> updateVideoMetadata(@RequestBody VideoDTO videoDTO) {
        return ResponseEntity.ok(videoMetadataService.updateVideoMetadata(videoDTO));
    }

    @GetMapping("/noauth/video/top50")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> getVideosTop50() {
        return ResponseEntity.ok(videoMetadataService.getVideosTop50());
    }

}
