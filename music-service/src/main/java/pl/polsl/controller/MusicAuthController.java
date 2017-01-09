package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.dto.SongDTO;
import pl.polsl.dto.UploadSongMetadataDTO;
import pl.polsl.model.MusicFiles;
import pl.polsl.service.MusicManagementService;
import pl.polsl.service.MusicMetadataService;
import pl.polsl.service.StorageService;

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
public class MusicAuthController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private MusicMetadataService musicMetadataService;

    @Autowired
    private MusicManagementService musicManagementService;

    @PostMapping("/upload")
    public ResponseEntity<Long>
    handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            return new ResponseEntity<Long>(HttpStatus.NOT_FOUND);
        }
        MusicFiles musicFile = storageService.store(file);
        return ResponseEntity.ok(musicFile.getMusicFileId());
    }

    @GetMapping(value = "/download")
    public StreamingResponseBody
    downloadMusicFile(@RequestParam("id") Long id,
                      @RequestParam("username") String username) {
        if (id == null || username == null) {
            return null;
        }
        MusicFiles musicFile = storageService.downloadMusicFile(id, username);
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

    @PostMapping("/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadSongMetadataDTO>
    saveMusicFileMetadata(@RequestBody UploadSongMetadataDTO uploadSongMetadataDTO) {
        if (uploadSongMetadataDTO == null) {
            return new ResponseEntity<UploadSongMetadataDTO>(HttpStatus.NOT_FOUND);
        }
        UploadSongMetadataDTO result = musicMetadataService.saveMetadata(uploadSongMetadataDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top10/songs")
    public
    @ResponseBody
    ResponseEntity<List<SongDTO>>
    getTop10SongsOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                              @RequestParam("username") String username) {
        if (StringUtils.isEmpty(username)) {
            return new ResponseEntity<List<SongDTO>>(HttpStatus.NOT_FOUND);
        }
        List<SongDTO> top10Songs = musicMetadataService.getTop10Songs(username, title);
        return ResponseEntity.ok(top10Songs);
    }

    @GetMapping("/user/songs")
    public
    @ResponseBody
    ResponseEntity<List<SongDTO>>
    getAllUserSongs(@RequestParam("username") String username) {
        if (StringUtils.isEmpty(username)) {
            return new ResponseEntity<List<SongDTO>>(HttpStatus.NOT_FOUND);
        }
        List<SongDTO> allUserSongs = musicMetadataService.getAllUserSongs(username);
        return ResponseEntity.ok(allUserSongs);
    }

    @DeleteMapping("/delete/song")
    public
    @ResponseBody
    ResponseEntity<Boolean>
    deleteFileAndMetadata(@RequestParam("id") Long id,
                          @RequestParam("username") String username) {
        if (StringUtils.isEmpty(username) || id == null) {
            return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
        }
        Boolean success = musicManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/song")
    public
    @ResponseBody
    ResponseEntity<SongDTO>
    updateSongMetadata(@RequestBody SongDTO songDTO) {
        if (songDTO == null) {
            return new ResponseEntity<SongDTO>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(musicMetadataService.updateSongMetadata(songDTO));
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

    public MusicManagementService getMusicManagementService() {
        return musicManagementService;
    }

    public void setMusicManagementService(MusicManagementService musicManagementService) {
        this.musicManagementService = musicManagementService;
    }
}
