package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.polsl.dto.SongDTO;
import pl.polsl.service.MusicManagementService;
import pl.polsl.service.MusicMetadataService;

import java.util.List;

/**
 * Created by Mateusz on 12.12.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin")
public class MusicAdminController {

    @Autowired
    private MusicManagementService musicManagementService;

    @Autowired
    private MusicMetadataService musicMetadataService;

    @GetMapping(value = "/songs")
    public
    @ResponseBody
    ResponseEntity<List<SongDTO>>
    getSongs(@RequestParam(value = "username", required = false) String username) {
        if (StringUtils.isEmpty(username)) {
            return ResponseEntity.ok(musicMetadataService.getAllSongs());
        } else {
            return ResponseEntity.ok(musicMetadataService.getAllUserSongs(username));
        }
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

}
