package pl.polsl.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.dto.MusicAlbumDTO;
import pl.polsl.dto.MusicArtistDTO;
import pl.polsl.dto.MusicGenreDTO;
import pl.polsl.dto.UploadSongMetadataDTO;
import pl.polsl.model.MusicFiles;
import pl.polsl.service.MusicMetadataService;
import pl.polsl.service.StorageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mateusz on 20.11.2016.
 */
@SuppressWarnings("ALL")
@RestController
public class MusicController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private MusicMetadataService musicMetadataService;

    @PostMapping("/auth/upload")
    public ResponseEntity<Long> handleFileUpload(@RequestParam("file") MultipartFile file) {

        MusicFiles musicFile = storageService.store(file);
        return ResponseEntity.ok(musicFile.getMusicFileId());
    }

    @GetMapping(value = "/noauth/download")
    public void downloadMusicFile(@RequestParam("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        MusicFiles musicFile = storageService.downloadMusicFile(id);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" + musicFile.getFileName() + "\"");
            response.setContentType("audio/" + musicFile.getExtension());
            IOUtils.copy(musicFile.getFile().getBinaryStream(), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return;
    }

    @GetMapping("/noauth/authors/prediction")
    public
    @ResponseBody
    ResponseEntity<List<MusicArtistDTO>> getArtistsPredictionList(@RequestParam("name") String name,
                                                                  @RequestParam("name2") String name2,
                                                                  @RequestParam("surname") String surname) {
        List<MusicArtistDTO> artistsByPrediction = musicMetadataService.getArtistsByPrediction(name, name2, surname);
        return new ResponseEntity<List<MusicArtistDTO>>(artistsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/noauth/albums/prediction")
    public
    @ResponseBody
    ResponseEntity<List<MusicAlbumDTO>> getAlbumsPredictionList(@RequestParam("albumTitle") String albumTitle,
                                                                @RequestParam("songTitle") String songTitle) {
        List<MusicAlbumDTO> albumsByPrediction = musicMetadataService.getAlbumsByPrediction(albumTitle, songTitle);
        return new ResponseEntity<List<MusicAlbumDTO>>(albumsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/noauth/genres/prediction")
    public
    @ResponseBody
    ResponseEntity<List<MusicGenreDTO>> getGenresPredictionList(@RequestParam("genreName") String genreName) {
        List<MusicGenreDTO> genresByPrediction = musicMetadataService.getGenresByPrediction(genreName);
        return new ResponseEntity<List<MusicGenreDTO>>(genresByPrediction, HttpStatus.OK);
    }

    @PostMapping("/auth/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadSongMetadataDTO> saveMusicFileMetadata(@RequestBody UploadSongMetadataDTO uploadSongMetadataDTO) {
        UploadSongMetadataDTO result = musicMetadataService.saveMetadata(uploadSongMetadataDTO);
        return ResponseEntity.ok(result);
    }
}