package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.dto.SongDTO;
import pl.polsl.dto.UploadSongMetadataDTO;
import pl.polsl.model.MusicFiles;
import pl.polsl.service.MusicManagementService;
import pl.polsl.service.MusicMetadataService;
import pl.polsl.service.StorageService;

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

        MusicFiles musicFile = storageService.store(file);
        return ResponseEntity.ok(musicFile.getMusicFileId());
    }

    @PostMapping("/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadSongMetadataDTO>
    saveMusicFileMetadata(@RequestBody UploadSongMetadataDTO uploadSongMetadataDTO) {

        UploadSongMetadataDTO result = musicMetadataService.saveMetadata(uploadSongMetadataDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top10/songs")
    public
    @ResponseBody
    ResponseEntity<List<SongDTO>>
    getTop10SongsOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                              @RequestParam("username") String username) {

        List<SongDTO> top10Songs = musicMetadataService.getTop10Songs(username, title);
        return ResponseEntity.ok(top10Songs);
    }

    @GetMapping("/user/songs")
    public
    @ResponseBody
    ResponseEntity<List<SongDTO>>
    getAllUserSongs(@RequestParam("username") String username) {

        List<SongDTO> allUserSongs = musicMetadataService.getAllUserSongs(username);
        return ResponseEntity.ok(allUserSongs);
    }

    @DeleteMapping("/delete/song")
    public
    @ResponseBody
    ResponseEntity<Boolean>
    deleteFileAndMetadata(@RequestParam("id") Long id,
                          @RequestParam("username") String username) {

        Boolean success = musicManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/song")
    public
    @ResponseBody
    ResponseEntity<SongDTO>
    updateSongMetadata(@RequestBody SongDTO songDTO) {

        return ResponseEntity.ok(musicMetadataService.updateSongMetadata(songDTO));
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
