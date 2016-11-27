package pl.polsl.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.dto.DirectorDTO;
import pl.polsl.dto.FilmGenreDTO;
import pl.polsl.dto.UploadVideoMetadataDTO;
import pl.polsl.dto.VideoSerieDTO;
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;
import pl.polsl.service.VideoMetadataService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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

    @PostMapping("/auth/upload")
    public ResponseEntity<Long> handleFileUpload(@RequestParam("file") MultipartFile file) {

        VideoFiles videoFile = storageService.store(file);
        return ResponseEntity.ok(videoFile.getVideoFileId());
    }

    @GetMapping("/auth/download")
    public
    @ResponseBody
    String downloadMusicFile(@RequestParam("id") Long id, HttpServletResponse response) {
        VideoFiles videoFile = storageService.downloadVideoFile(id);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" + videoFile.getFileName() + "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType("audio/" + videoFile.getExtension());
            IOUtils.copy(videoFile.getFile().getBinaryStream(), out);
            out.flush();
            out.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
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
}
