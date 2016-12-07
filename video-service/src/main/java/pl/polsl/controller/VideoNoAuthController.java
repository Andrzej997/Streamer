package pl.polsl.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RestController("/noauth")
public class VideoNoAuthController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private VideoManagementService videoManagementService;

    @GetMapping("/download")
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

    @GetMapping("/directors/prediction")
    public
    @ResponseBody
    ResponseEntity<List<DirectorDTO>> getDirectorsPredictionList(@RequestParam("name") String name,
                                                                 @RequestParam("name2") String name2,
                                                                 @RequestParam("surname") String surname) {
        List<DirectorDTO> directorsByPrediction = videoMetadataService.getDirectorsByPrediction(name, name2, surname);
        return new ResponseEntity<List<DirectorDTO>>(directorsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/videoseries/prediction")
    public
    @ResponseBody
    ResponseEntity<List<VideoSerieDTO>> getVideoSeriesPredictionList(@RequestParam("serieTitle") String serieTitle,
                                                                     @RequestParam("videoTitle") String videoTitle) {
        List<VideoSerieDTO> videoSeriesByPrediction = videoMetadataService.getVideoSeriesByPrediction(serieTitle, videoTitle);
        return new ResponseEntity<List<VideoSerieDTO>>(videoSeriesByPrediction, HttpStatus.OK);
    }

    @GetMapping("/genres/prediction")
    public
    @ResponseBody
    ResponseEntity<List<FilmGenreDTO>> getGenresPredictionList(@RequestParam("name") String name) {
        List<FilmGenreDTO> genresByPrediction = videoMetadataService.getFilmGenresByPrediction(name);
        return new ResponseEntity<List<FilmGenreDTO>>(genresByPrediction, HttpStatus.OK);
    }

    @GetMapping("/top10/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> getTop10Videos(@RequestParam(value = "title", required = false) String title) {
        List<VideoDTO> top10Videos = videoMetadataService.getTop10Videos(null, title);
        return ResponseEntity.ok(top10Videos);
    }

    @PostMapping("/public/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> searchVideosByCriteria(@RequestBody SearchVideoCriteriaDTO searchVideoCriteriaDTO) {
        List<VideoDTO> videoDTOList = videoMetadataService.searchVideosByCriteria(searchVideoCriteriaDTO);
        return ResponseEntity.ok(videoDTOList);
    }

    @GetMapping("/video/top50")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>> getVideosTop50() {
        return ResponseEntity.ok(videoMetadataService.getVideosTop50());
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
