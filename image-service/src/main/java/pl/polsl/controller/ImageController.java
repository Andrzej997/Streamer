package pl.polsl.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.dto.*;
import pl.polsl.model.ImageFiles;
import pl.polsl.service.ImageManagementService;
import pl.polsl.service.ImageMetadataService;
import pl.polsl.service.StorageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
@SuppressWarnings("ALL")
@RestController
public class ImageController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImageMetadataService imageMetadataService;

    @Autowired
    private ImageManagementService imageManagementService;

    @PostMapping("/auth/upload")
    public ResponseEntity<Long> handleFileUpload(@RequestParam("file") MultipartFile file) {

        ImageFiles imageFile = storageService.store(file);
        return ResponseEntity.ok(imageFile.getImageFileId());
    }

    @GetMapping("/noauth/download")
    public void downloadImageFile(@RequestParam("id") Long id, HttpServletResponse response) {
        ImageFiles imageFile = storageService.downloadVideoFile(id);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" + imageFile.getFileName() + "\"");
            response.setContentType("audio/" + imageFile.getFileExtension());
            IOUtils.copy(imageFile.getFile().getBinaryStream(), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return;
    }


    @GetMapping("/noauth/artists/prediction")
    public
    @ResponseBody
    ResponseEntity<List<ArtistDTO>> getArtistPredictionList(@RequestParam("name") String name,
                                                            @RequestParam("name2") String name2,
                                                            @RequestParam("surname") String surname) {
        List<ArtistDTO> artistsByPrediction = imageMetadataService.getArtistsByPrediction(name, name2, surname);
        return new ResponseEntity<List<ArtistDTO>>(artistsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/noauth/type/prediction")
    public
    @ResponseBody
    ResponseEntity<List<ImageTypeDTO>> getImageTypesByPrediction(@RequestParam("name") String name) {
        List<ImageTypeDTO> imageTypesByPrediction = imageMetadataService.getImageTypesByPrediction(name);
        return new ResponseEntity<List<ImageTypeDTO>>(imageTypesByPrediction, HttpStatus.OK);
    }

    @PostMapping("/auth/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadImageMetadataDTO> saveImageFileMetadata(@RequestBody UploadImageMetadataDTO uploadImageMetadataDTO) {
        UploadImageMetadataDTO result = imageMetadataService.saveMetadata(uploadImageMetadataDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/noauth/top10/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>> getTop10Images(@RequestParam(value = "title", required = false) String title) {
        List<ImageDTO> top10Images = imageMetadataService.getTop10Images(null, title);
        return ResponseEntity.ok(top10Images);
    }

    @GetMapping("/auth/top10/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>> getTop10ImagesOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                                                              @RequestParam("username") String username) {
        List<ImageDTO> top10Images = imageMetadataService.getTop10Images(username, title);
        return ResponseEntity.ok(top10Images);
    }

    @GetMapping("/auth/user/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>> getAllUserImages(@RequestParam("username") String username) {
        List<ImageDTO> allUserImages = imageMetadataService.getAllUserImages(username);
        return ResponseEntity.ok(allUserImages);
    }


    @GetMapping("/noauth/public/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>> searchImagesByCriteria(@RequestParam("criteria") SearchImageCriteriaDTO searchImageCriteriaDTO) {
        List<ImageDTO> imageDTOList = imageMetadataService.searchImagesByCriteria(searchImageCriteriaDTO);
        return ResponseEntity.ok(imageDTOList);
    }

    @DeleteMapping("/auth/delete/image")
    public
    @ResponseBody
    ResponseEntity<Boolean> deleteFileAndMetadata(@RequestParam("id") Long id, @RequestParam("username") String username) {
        Boolean success = imageManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }
}
