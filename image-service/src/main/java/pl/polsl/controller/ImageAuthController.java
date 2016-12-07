package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.dto.ImageDTO;
import pl.polsl.dto.UploadImageMetadataDTO;
import pl.polsl.model.ImageFiles;
import pl.polsl.service.ImageManagementService;
import pl.polsl.service.ImageMetadataService;
import pl.polsl.service.StorageService;

import java.util.List;

/**
 * Created by Mateusz on 07.12.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/auth")
public class ImageAuthController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImageMetadataService imageMetadataService;

    @Autowired
    private ImageManagementService imageManagementService;

    @PostMapping("/upload")
    public ResponseEntity<Long>
    handleFileUpload(@RequestParam("file") MultipartFile file) {

        ImageFiles imageFile = storageService.store(file);
        return ResponseEntity.ok(imageFile.getImageFileId());
    }


    @PostMapping("/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadImageMetadataDTO>
    saveImageFileMetadata(@RequestBody UploadImageMetadataDTO uploadImageMetadataDTO) {

        UploadImageMetadataDTO result = imageMetadataService.saveMetadata(uploadImageMetadataDTO);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/top10/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>>
    getTop10ImagesOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                               @RequestParam("username") String username) {

        List<ImageDTO> top10Images = imageMetadataService.getTop10Images(username, title);
        return ResponseEntity.ok(top10Images);
    }

    @GetMapping("/user/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>>
    getAllUserImages(@RequestParam("username") String username) {

        List<ImageDTO> allUserImages = imageMetadataService.getAllUserImages(username);
        return ResponseEntity.ok(allUserImages);
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

    @PutMapping("/update/image")
    public
    @ResponseBody
    ResponseEntity<ImageDTO>
    updateImageMetadata(@RequestBody ImageDTO imageDTO) {

        return ResponseEntity.ok(imageMetadataService.updateImageMetadata(imageDTO));
    }

    public StorageService getStorageService() {
        return storageService;
    }

    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public ImageMetadataService getImageMetadataService() {
        return imageMetadataService;
    }

    public void setImageMetadataService(ImageMetadataService imageMetadataService) {
        this.imageMetadataService = imageMetadataService;
    }

    public ImageManagementService getImageManagementService() {
        return imageManagementService;
    }

    public void setImageManagementService(ImageManagementService imageManagementService) {
        this.imageManagementService = imageManagementService;
    }
}
