package pl.polsl.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.dto.ArtistDTO;
import pl.polsl.dto.ImageDTO;
import pl.polsl.dto.ImageTypeDTO;
import pl.polsl.dto.SearchImageCriteriaDTO;
import pl.polsl.model.ImageFiles;
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
@RequestMapping("/noauth")
public class ImageNoAuthController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImageMetadataService imageMetadataService;

    @GetMapping("/download")
    public void
    downloadImageFile(@RequestParam("id") Long id, HttpServletResponse response) {

        ImageFiles imageFile = storageService.downloadImageFile(id);
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

    @GetMapping("/artists/prediction")
    public
    @ResponseBody
    ResponseEntity<List<ArtistDTO>>
    getArtistPredictionList(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "name2", required = false) String name2,
                            @RequestParam(value = "surname", required = false) String surname) {

        List<ArtistDTO> artistsByPrediction = imageMetadataService.getArtistsByPrediction(name, name2, surname);
        return new ResponseEntity<List<ArtistDTO>>(artistsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/type/prediction")
    public
    @ResponseBody
    ResponseEntity<List<ImageTypeDTO>>
    getImageTypesByPrediction(@RequestParam(value = "name", required = false) String name) {

        List<ImageTypeDTO> imageTypesByPrediction = imageMetadataService.getImageTypesByPrediction(name);
        return new ResponseEntity<List<ImageTypeDTO>>(imageTypesByPrediction, HttpStatus.OK);
    }

    @GetMapping("/images/top10")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>>
    getTop10Images(@RequestParam(value = "title", required = false) String title) {

        List<ImageDTO> top10Images = imageMetadataService.getTop10Images(null, title);
        return ResponseEntity.ok(top10Images);
    }

    @PostMapping("/public/images")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>>
    searchImagesByCriteria(@RequestBody SearchImageCriteriaDTO searchImageCriteriaDTO) {

        List<ImageDTO> imageDTOList = imageMetadataService.searchImagesByCriteria(searchImageCriteriaDTO);
        return ResponseEntity.ok(imageDTOList);
    }

    @GetMapping("/image/top50")
    public
    @ResponseBody
    ResponseEntity<List<ImageDTO>>
    getImagesTop50() {

        return ResponseEntity.ok(imageMetadataService.getImagesTop50());
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
}
