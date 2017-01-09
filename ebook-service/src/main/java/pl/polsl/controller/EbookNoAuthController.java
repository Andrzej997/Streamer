package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.dto.*;
import pl.polsl.model.EbookFiles;
import pl.polsl.service.EbookMetadataService;
import pl.polsl.service.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    @GetMapping(value = "/download")
    public StreamingResponseBody
    downloadEbookFile(@RequestParam("id") Long id) {

        if (id == null) {
            return null;
        }
        EbookFiles ebookFiles = storageService.downloadEbookFile(id);
        if (ebookFiles == null) {
            return null;
        }
        try {
            final InputStream binaryStream = ebookFiles.getFile().getBinaryStream();
            return (os) -> {
                readAndWrite(binaryStream, os);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/writers/prediction")
    public
    @ResponseBody
    ResponseEntity<List<WriterDTO>>
    getWritersPredictionList(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "name2", required = false) String name2,
                             @RequestParam(value = "surname", required = false) String surname) {
        if (StringUtils.isEmpty(name)) {
            return new ResponseEntity<List<WriterDTO>>(HttpStatus.NOT_FOUND);
        }
        List<WriterDTO> writersByPrediction = ebookMetadataService.getWritersByPrediction(name, name2, surname);
        return new ResponseEntity<List<WriterDTO>>(writersByPrediction, HttpStatus.OK);
    }

    @GetMapping("/literarygenre/prediction")
    public
    @ResponseBody
    ResponseEntity<List<LiteraryGenreDTO>>
    getLiteraryGenresPredictionList(@RequestParam(value = "name", required = false) String name) {
        if (StringUtils.isEmpty(name)) {
            return new ResponseEntity<List<LiteraryGenreDTO>>(HttpStatus.NOT_FOUND);
        }
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

        if (searchEbookCriteriaDTO == null) {
            return new ResponseEntity<List<EbookDTO>>(HttpStatus.NOT_FOUND);
        }

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

    @PutMapping("/rate")
    public void rateEbook(@RequestBody RateEbookDTO rateEbookDTO) {
        if (rateEbookDTO == null) {
            throw new NullPointerException();
        }
        ebookMetadataService.rateEbook(rateEbookDTO);
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

    public EbookMetadataService getEbookMetadataService() {
        return ebookMetadataService;
    }

    public void setEbookMetadataService(EbookMetadataService ebookMetadataService) {
        this.ebookMetadataService = ebookMetadataService;
    }
}
