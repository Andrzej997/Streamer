package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.dto.*;
import pl.polsl.model.ImageFiles;
import pl.polsl.service.ImageMetadataService;
import pl.polsl.service.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    @GetMapping(value = "/download")
    public StreamingResponseBody
    downloadImageFile(@RequestParam("id") Long id) {
        if (id == null) {
            return null;
        }
        ImageFiles imageFiles = storageService.downloadImageFile(id);
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

    @GetMapping("/artists/prediction")
    public
    @ResponseBody
    ResponseEntity<List<ArtistDTO>>
    getArtistPredictionList(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "name2", required = false) String name2,
                            @RequestParam(value = "surname", required = false) String surname) {
        if (StringUtils.isEmpty(name)) {
            return new ResponseEntity<List<ArtistDTO>>(HttpStatus.NOT_FOUND);
        }
        List<ArtistDTO> artistsByPrediction = imageMetadataService.getArtistsByPrediction(name, name2, surname);
        return new ResponseEntity<List<ArtistDTO>>(artistsByPrediction, HttpStatus.OK);
    }

    @GetMapping("/type/prediction")
    public
    @ResponseBody
    ResponseEntity<List<ImageTypeDTO>>
    getImageTypesByPrediction(@RequestParam(value = "name", required = false) String name) {
        if (StringUtils.isEmpty(name)) {
            return new ResponseEntity<List<ImageTypeDTO>>(HttpStatus.NOT_FOUND);
        }
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
        if (searchImageCriteriaDTO == null) {
            return new ResponseEntity<List<ImageDTO>>(HttpStatus.NOT_FOUND);
        }

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

    @PutMapping("/rate")
    public void rateImage(@RequestBody RateImageDTO rateImageDTO) {
        if (rateImageDTO == null) {
            throw new NullPointerException();
        }

        imageMetadataService.rateImage(rateImageDTO);
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
}
