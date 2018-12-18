package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.polsl.annotations.VerifyUsername;
import pl.polsl.dto.VideoDTO;
import pl.polsl.service.VideoManagementService;
import pl.polsl.service.VideoMetadataService;

import java.util.List;

/**
 * Created by Mateusz on 12.12.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin")
// dla admina teoretycznie nie trzeba sprawdzać uprawnień - na api-gateway są dużo mocniejsze weryfikacje dla admina
public class VideoAdminController {

    @Autowired
    private VideoManagementService videoManagementService;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @GetMapping(value = "/videos")
    public
    @ResponseBody
    ResponseEntity<List<VideoDTO>>
    getVideos(@RequestParam(value = "username", required = false) String username) {
        if (StringUtils.isEmpty(username)) {
            return ResponseEntity.ok(videoMetadataService.getAllVideos());
        } else {
            return ResponseEntity.ok(videoMetadataService.getAllUserVideos(username));
        }
    }

    @DeleteMapping("/delete/video")
    public
    @ResponseBody
    ResponseEntity<Boolean>
    deleteFileAndMetadata(@RequestParam("id") Long id,
                          @RequestParam("username") String username,
                          @RequestHeader("AuthHeader") String authHeader) {
        Boolean success = false;
        if (id == null || username == null) {
            return ResponseEntity.ok(success);
        }
        success = videoManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }
}
