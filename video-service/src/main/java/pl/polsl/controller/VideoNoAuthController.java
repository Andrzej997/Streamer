package pl.polsl.controller;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.cache.VideoFilesCacheableProvider;
import pl.polsl.configuration.MultipartSender;
import pl.polsl.dto.*;
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;
import pl.polsl.service.VideoManagementService;
import pl.polsl.service.VideoMetadataService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/noauth")
public class VideoNoAuthController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private VideoManagementService videoManagementService;

    @Autowired
    private VideoFilesCacheableProvider cacheableProvider;

    @GetMapping(value = "/download")
    public ResponseEntity<StreamingResponseBody>
    downloadVideoFile(@RequestParam("id") Long id, @RequestHeader(value = "Range", required = false) String range) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        VideoFiles videoFile = storageService.downloadVideoFile(id);
        if (videoFile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Long fileLength = new Long(videoFile.getFile().length());
            final InputStream binaryStream = videoFile.getFile().getBinaryStream();
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
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

    @GetMapping(value = "/download-multipart")
    public void downloadVideoFile(@RequestParam("id") Long id,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        if (id == null) {
            return;
        }
        VideoFiles videoFile = cacheableProvider.downloadVideoFile(id);
        if (videoFile == null) {
            return;
        }
        MultipartSender.fromVideoFiles(videoFile)
                .with(request)
                .with(response)
                .serveVideoResource();
    }

    @GetMapping(value = "/thumbnail")
    public ResponseEntity<StreamingResponseBody>
    downloadThumbnail(@RequestParam("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        VideoFiles videoFile = storageService.downloadVideoFile(id);
        if (videoFile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Long fileLength = new Long(videoFile.getThumbnail().length());
            final InputStream binaryStream = videoFile.getThumbnail().getBinaryStream();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/jpeg"))
                    .contentLength(fileLength)
                    .header("Accept-Ranges", "bytes")
                    .body(os -> {

                        readAndWrite(binaryStream, os);
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/directors/prediction")
    public
    @ResponseBody
    ResponseEntity<List<DirectorDTO>>
    getDirectorsPredictionList(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "name2", required = false) String name2,
                               @RequestParam(value = "surname", required = false) String surname) {
        if (StringUtils.isEmpty(name)) {
            return new ResponseEntity<List<DirectorDTO>>(HttpStatus.NOT_FOUND);
        }
        List<DirectorDTO> directorsByPrediction = videoMetadataService.getDirectorsByPrediction(name, name2, surname);
        return new ResponseEntity<List<DirectorDTO>>(directorsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/videoseries/prediction")
    public
    @ResponseBody
    ResponseEntity<List<VideoSerieDTO>>
    getVideoSeriesPredictionList(@RequestParam(value = "serieTitle", required = false) String serieTitle,
                                 @RequestParam(value = "videoTitle", required = false) String videoTitle) {
        if (StringUtils.isEmpty(serieTitle)) {
            return new ResponseEntity<List<VideoSerieDTO>>(HttpStatus.NOT_FOUND);
        }
        List<VideoSerieDTO> videoSeriesByPrediction = videoMetadataService.getVideoSeriesByPrediction(serieTitle, videoTitle);
        return new ResponseEntity<List<VideoSerieDTO>>(videoSeriesByPrediction, HttpStatus.OK);
    }

    @GetMapping("/genres/prediction")
    public
    @ResponseBody
    ResponseEntity<List<FilmGenreDTO>>
    getGenresPredictionList(@RequestParam(value = "name", required = false) String name) {
        if (StringUtils.isEmpty(name)) {
            return new ResponseEntity<List<FilmGenreDTO>>(HttpStatus.NOT_FOUND);
        }
        List<FilmGenreDTO> genresByPrediction = videoMetadataService.getFilmGenresByPrediction(name);
        return new ResponseEntity<List<FilmGenreDTO>>(genresByPrediction, HttpStatus.OK);
    }

    @GetMapping("/videos/top10")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>>
    getTop10Videos(@RequestParam(value = "title", required = false) String title) {

        List<VideoDTO> top10Videos = videoMetadataService.getTop10Videos(null, title);
        return ResponseEntity.ok(top10Videos);
    }

    @PostMapping("/public/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>>
    searchVideosByCriteria(@RequestBody SearchVideoCriteriaDTO searchVideoCriteriaDTO) {
        if (searchVideoCriteriaDTO == null) {
            return new ResponseEntity<List<VideoDTO>>(HttpStatus.NOT_FOUND);
        }

        List<VideoDTO> videoDTOList = videoMetadataService.searchVideosByCriteria(searchVideoCriteriaDTO);
        return ResponseEntity.ok(videoDTOList);
    }

    @GetMapping("/video/top50")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>>
    getVideosTop50() {
        return ResponseEntity.ok(videoMetadataService.getVideosTop50());
    }

    @PutMapping("/rate")
    public void rateVideo(@RequestBody RateVideoDTO rateVideoDTO) {
        if (rateVideoDTO == null) {
            throw new NullPointerException();
        }

        videoMetadataService.rateVideo(rateVideoDTO);
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
