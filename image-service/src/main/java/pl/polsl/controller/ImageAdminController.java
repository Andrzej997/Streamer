package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.polsl.dto.ImageDTO;
import pl.polsl.service.ImageManagementService;
import pl.polsl.service.ImageMetadataService;

import java.util.List;

/**
 * Created by Mateusz on 12.12.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin")
public class ImageAdminController {

    @Autowired
    private ImageManagementService imageManagementService;

    @Autowired
    private ImageMetadataService imageMetadataService;

    @GetMapping(value = "/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>>
    getImages(@RequestParam(value = "username", required = false) String username) {
        if (StringUtils.isEmpty(username)) {
            return ResponseEntity.ok(imageMetadataService.getAllImages());
        } else {
            return ResponseEntity.ok(imageMetadataService.getAllUserImages(username));
        }
    }

    @DeleteMapping("/delete/image")
    public
    @ResponseBody
    ResponseEntity<Boolean>
    deleteFileAndMetadata(@RequestParam("id") Long id,
                          @RequestParam("username") String username) {

        Boolean success = imageManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }
}
