package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.dto.EbookDTO;
import pl.polsl.dto.UploadEbookMetadataDTO;
import pl.polsl.model.EbookFiles;
import pl.polsl.service.EbookManagementService;
import pl.polsl.service.EbookMetadataService;
import pl.polsl.service.StorageService;

import java.util.List;

/**
 * Created by Mateusz on 07.12.2016.
 */
@SuppressWarnings("ALL")
@RestController("/auth")
public class EbookAuthController {


    @Autowired
    private StorageService storageService;

    @Autowired
    private EbookMetadataService ebookMetadataService;

    @Autowired
    private EbookManagementService ebookManagementService;

    @PostMapping("/upload")
    public ResponseEntity<Long> handleFileUpload(@RequestParam("file") MultipartFile file) {

        EbookFiles ebookFile = storageService.store(file);
        return ResponseEntity.ok(ebookFile.getEbookFileId());
    }

    @PostMapping("/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadEbookMetadataDTO> saveEbookFileMetadata(@RequestBody UploadEbookMetadataDTO uploadEbookMetadataDTO) {
        UploadEbookMetadataDTO result = ebookMetadataService.saveMetadata(uploadEbookMetadataDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top10/ebooks")
    public
    @ResponseBody
    ResponseEntity<List<EbookDTO>> getTop10EbooksOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                                                              @RequestParam("username") String username) {
        List<EbookDTO> top10Ebooks = ebookMetadataService.getTop10Ebooks(username, title);
        return ResponseEntity.ok(top10Ebooks);
    }

    @GetMapping("/user/ebooks")
    public
    @ResponseBody
    ResponseEntity<List<EbookDTO>> getAllUserEbooks(@RequestParam("username") String username) {
        List<EbookDTO> allUserEbooks = ebookMetadataService.getAllUserImages(username);
        return ResponseEntity.ok(allUserEbooks);
    }

    @DeleteMapping("/delete/ebook")
    public
    @ResponseBody
    ResponseEntity<Boolean> deleteFileAndMetadata(@RequestParam("id") Long id, @RequestParam("username") String username) {
        Boolean success = ebookManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/ebook")
    public
    @ResponseBody
    ResponseEntity<EbookDTO> updateEbookMetadata(@RequestBody EbookDTO ebookDTO) {
        return ResponseEntity.ok(ebookMetadataService.updateEbookMetadata(ebookDTO));
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

    public EbookManagementService getEbookManagementService() {
        return ebookManagementService;
    }

    public void setEbookManagementService(EbookManagementService ebookManagementService) {
        this.ebookManagementService = ebookManagementService;
    }
}
