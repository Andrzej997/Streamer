package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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
                          @RequestParam("username") String username) {

        Boolean success = videoManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }
}
