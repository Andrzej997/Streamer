package pl.polsl.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.dto.LiteraryGenreDTO;
import pl.polsl.dto.UploadEbookMetadataDTO;
import pl.polsl.dto.WriterDTO;
import pl.polsl.model.EbookFiles;
import pl.polsl.service.EbookMetadataService;
import pl.polsl.service.StorageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mateusz on 28.11.2016.
 */

@SuppressWarnings("ALL")
@RestController
public class EbookController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private EbookMetadataService ebookMetadataService;

    @PostMapping("/auth/upload")
    public ResponseEntity<Long> handleFileUpload(@RequestParam("file") MultipartFile file) {

        EbookFiles ebookFile = storageService.store(file);
        return ResponseEntity.ok(ebookFile.getEbookFileId());
    }

    @GetMapping("/noauth/download")
    public void downloadEbookFile(@RequestParam("id") Long id, HttpServletResponse response) {
        EbookFiles ebookFile = storageService.downloadEbookFile(id);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" + ebookFile.getFileName() + "\"");
            response.setContentType("audio/" + ebookFile.getExtension());
            IOUtils.copy(ebookFile.getFile().getBinaryStream(), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return;
    }

    @GetMapping("/noauth/writers/prediction")
    public
    @ResponseBody
    ResponseEntity<List<WriterDTO>> getWritersPredictionList(@RequestParam("name") String name,
                                                             @RequestParam("name2") String name2,
                                                             @RequestParam("surname") String surname) {
        List<WriterDTO> writersByPrediction = ebookMetadataService.getWritersByPrediction(name, name2, surname);
        return new ResponseEntity<List<WriterDTO>>(writersByPrediction, HttpStatus.OK);
    }

    @GetMapping("/noauth/literarygenre/prediction")
    public
    @ResponseBody
    ResponseEntity<List<LiteraryGenreDTO>> getLiteraryGenresPredictionList(@RequestParam("name") String name) {
        List<LiteraryGenreDTO> literaryGenresByPrediction = ebookMetadataService.getLiteraryGenresByPrediction(name);
        return new ResponseEntity<List<LiteraryGenreDTO>>(literaryGenresByPrediction, HttpStatus.OK);
    }

    @PostMapping("/auth/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadEbookMetadataDTO> saveEbookFileMetadata(@RequestBody UploadEbookMetadataDTO uploadEbookMetadataDTO) {
        UploadEbookMetadataDTO result = ebookMetadataService.saveMetadata(uploadEbookMetadataDTO);
        return ResponseEntity.ok(result);
    }

}