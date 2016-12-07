package pl.polsl.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.dto.EbookDTO;
import pl.polsl.dto.LiteraryGenreDTO;
import pl.polsl.dto.SearchEbookCriteriaDTO;
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
@RequestMapping("/noauth")
public class EbookNoAuthController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private EbookMetadataService ebookMetadataService;

    @GetMapping("/download")
    public void
    downloadEbookFile(@RequestParam("id") Long id, HttpServletResponse response) {

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

    @GetMapping("/writers/prediction")
    public
    @ResponseBody
    ResponseEntity<List<WriterDTO>>
    getWritersPredictionList(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "name2", required = false) String name2,
                             @RequestParam(value = "surname", required = false) String surname) {

        List<WriterDTO> writersByPrediction = ebookMetadataService.getWritersByPrediction(name, name2, surname);
        return new ResponseEntity<List<WriterDTO>>(writersByPrediction, HttpStatus.OK);
    }

    @GetMapping("/literarygenre/prediction")
    public
    @ResponseBody
    ResponseEntity<List<LiteraryGenreDTO>>
    getLiteraryGenresPredictionList(@RequestParam(value = "name", required = false) String name) {

        List<LiteraryGenreDTO> literaryGenresByPrediction = ebookMetadataService.getLiteraryGenresByPrediction(name);
        return new ResponseEntity<List<LiteraryGenreDTO>>(literaryGenresByPrediction, HttpStatus.OK);
    }

    @GetMapping("/ebooks/top10")
    public
    @ResponseBody
    ResponseEntity<List<EbookDTO>>
    getTop10Ebooks(@RequestParam(value = "title", required = false) String title) {

        List<EbookDTO> top10Ebooks = ebookMetadataService.getTop10Ebooks(null, title);
        return ResponseEntity.ok(top10Ebooks);
    }

    @PostMapping("/public/ebooks")
    public
    @ResponseBody
    ResponseEntity<List<EbookDTO>>
    searchEbooksByCriteria(@RequestBody SearchEbookCriteriaDTO searchEbookCriteriaDTO) {

        List<EbookDTO> ebookDTOList = ebookMetadataService.searchEbooksByCriteria(searchEbookCriteriaDTO);
        return ResponseEntity.ok(ebookDTOList);
    }

    @GetMapping("/ebook/top50")
    public
    @ResponseBody
    ResponseEntity<List<EbookDTO>>
    getEbooksTop50() {

        return ResponseEntity.ok(ebookMetadataService.getEbooksTop50());
    }

    public StorageService getStorageService() {
        return storageService;
    }

    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public EbookMetadataService getEbookMetadataService() {
        return ebookMetadataService;
    }

    public void setEbookMetadataService(EbookMetadataService ebookMetadataService) {
        this.ebookMetadataService = ebookMetadataService;
    }
}
