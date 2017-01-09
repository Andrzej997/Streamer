package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.dto.ImageDTO;
import pl.polsl.dto.UploadImageMetadataDTO;
import pl.polsl.model.ImageFiles;
import pl.polsl.service.ImageManagementService;
import pl.polsl.service.ImageMetadataService;
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
        if (file == null) {
            return new ResponseEntity<Long>(HttpStatus.NOT_FOUND);
        }
        ImageFiles imageFile = storageService.store(file);
        return ResponseEntity.ok(imageFile.getImageFileId());
    }

    @GetMapping(value = "/download")
    public StreamingResponseBody
    downloadImageFile(@RequestParam("id") Long id,
                      @RequestParam("username") String username) {
        if (id == null || username == null) {
            return null;
        }
        ImageFiles imageFiles = storageService.downloadImageFile(id, username);
        if (imageFiles == null) {
            return null;
        }
        try {
            final InputStream binaryStream = imageFiles.getFile().getBinaryStream();
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
    ResponseEntity<UploadImageMetadataDTO>
    saveImageFileMetadata(@RequestBody UploadImageMetadataDTO uploadImageMetadataDTO) {
        if (uploadImageMetadataDTO == null) {
            return new ResponseEntity<UploadImageMetadataDTO>(HttpStatus.NOT_FOUND);
        }
        UploadImageMetadataDTO result = imageMetadataService.saveMetadata(uploadImageMetadataDTO);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/top10/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>>
    getTop10ImagesOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                               @RequestParam("username") String username) {
        if (StringUtils.isEmpty(username)) {
            return new ResponseEntity<List<ImageDTO>>(HttpStatus.NOT_FOUND);
        }
        List<ImageDTO> top10Images = imageMetadataService.getTop10Images(username, title);
        return ResponseEntity.ok(top10Images);
    }

    @GetMapping("/user/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>>
    getAllUserImages(@RequestParam("username") String username) {
        if (StringUtils.isEmpty(username)) {
            return new ResponseEntity<List<ImageDTO>>(HttpStatus.NOT_FOUND);
        }
        List<ImageDTO> allUserImages = imageMetadataService.getAllUserImages(username);
        return ResponseEntity.ok(allUserImages);
    }

    @DeleteMapping("/delete/image")
    public
    @ResponseBody
    ResponseEntity<Boolean>
    deleteFileAndMetadata(@RequestParam("id") Long id,
                          @RequestParam("username") String username) {
        if (StringUtils.isEmpty(username) || id == null) {
            return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
        }
        Boolean success = imageManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/image")
    public
    @ResponseBody
    ResponseEntity<ImageDTO>
    updateImageMetadata(@RequestBody ImageDTO imageDTO) {
        if (imageDTO == null) {
            return new ResponseEntity<ImageDTO>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(imageMetadataService.updateImageMetadata(imageDTO));
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
