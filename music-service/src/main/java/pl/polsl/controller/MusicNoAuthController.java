package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.dto.*;
import pl.polsl.model.MusicFiles;
import pl.polsl.service.MusicMetadataService;
import pl.polsl.service.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mateusz on 20.11.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/noauth")
public class MusicNoAuthController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private MusicMetadataService musicMetadataService;

    @GetMapping(value = "/download")
    public StreamingResponseBody
    downloadMusicFile(@RequestParam("id") Long id) {

        MusicFiles musicFile = storageService.downloadMusicFile(id);
        if (musicFile == null) {
            return null;
        }
        try {
            final InputStream binaryStream = musicFile.getFile().getBinaryStream();
            return (os) -> {
                readAndWrite(binaryStream, os);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/authors/prediction")
    public
    @ResponseBody
    ResponseEntity<List<MusicArtistDTO>>
    getArtistsPredictionList(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "name2", required = false) String name2,
                             @RequestParam(value = "surname", required = false) String surname) {

        List<MusicArtistDTO> artistsByPrediction = musicMetadataService.getArtistsByPrediction(name, name2, surname);
        return new ResponseEntity<List<MusicArtistDTO>>(artistsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/albums/prediction")
    public
    @ResponseBody
    ResponseEntity<List<MusicAlbumDTO>>
    getAlbumsPredictionList(@RequestParam(value = "albumTitle", required = false) String albumTitle,
                            @RequestParam(value = "songTitle", required = false) String songTitle) {

        List<MusicAlbumDTO> albumsByPrediction = musicMetadataService.getAlbumsByPrediction(albumTitle, songTitle);
        return new ResponseEntity<List<MusicAlbumDTO>>(albumsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/genres/prediction")
    public
    @ResponseBody
    ResponseEntity<List<MusicGenreDTO>>
    getGenresPredictionList(@RequestParam(value = "genreName", required = false) String genreName) {

        List<MusicGenreDTO> genresByPrediction = musicMetadataService.getGenresByPrediction(genreName);
        return new ResponseEntity<List<MusicGenreDTO>>(genresByPrediction, HttpStatus.OK);
    }

    @GetMapping("/songs/top10")
    public
    @ResponseBody
    ResponseEntity<List<SongDTO>> getTop10Songs(@RequestParam(value = "title", required = false) String title) {

        List<SongDTO> top10Songs = musicMetadataService.getTop10Songs(null, title);
        return ResponseEntity.ok(top10Songs);
    }

    @PostMapping("/public/songs")
    public
    @ResponseBody
    ResponseEntity<List<SongDTO>>
    searchSongsByCriteria(@RequestBody SearchSongCriteriaDTO searchSongCriteriaDTO) {

        List<SongDTO> songDTOList = musicMetadataService.searchSongsByCriteria(searchSongCriteriaDTO);
        return ResponseEntity.ok(songDTOList);
    }

    @GetMapping("/song/top50")
    public
    @ResponseBody
    ResponseEntity<List<SongDTO>>
    getSongsTop50() {

        return ResponseEntity.ok(musicMetadataService.getSongsTop50());
    }

    @PutMapping("/rate")
    public void rateSong(@RequestBody RateSongDTO rateSongDTO) {
        musicMetadataService.rateSong(rateSongDTO);
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

    public MusicMetadataService getMusicMetadataService() {
        return musicMetadataService;
    }

    public void setMusicMetadataService(MusicMetadataService musicMetadataService) {
        this.musicMetadataService = musicMetadataService;
    }
}
