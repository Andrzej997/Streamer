package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.polsl.dto.EbookDTO;
import pl.polsl.dto.UploadEbookMetadataDTO;
import pl.polsl.model.EbookFiles;
import pl.polsl.service.EbookManagementService;
import pl.polsl.service.EbookMetadataService;
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

    @GetMapping(value = "/download")
    public StreamingResponseBody
    downloadImageFile(@RequestParam("id") Long id,
                      @RequestParam("username") String username) {

        EbookFiles ebookFiles = storageService.downloadEbookFile(id, username);
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

    @PostMapping("/file/metadata")
    public
    @ResponseBody
    ResponseEntity<UploadEbookMetadataDTO>
    saveEbookFileMetadata(@RequestBody UploadEbookMetadataDTO uploadEbookMetadataDTO) {

        UploadEbookMetadataDTO result = ebookMetadataService.saveMetadata(uploadEbookMetadataDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top10/ebooks")
    public
    @ResponseBody
    ResponseEntity<List<EbookDTO>>
    getTop10EbooksOnlyPrivates(@RequestParam(value = "title", required = false) String title,
                               @RequestParam("username") String username) {

        List<EbookDTO> top10Ebooks = ebookMetadataService.getTop10Ebooks(username, title);
        return ResponseEntity.ok(top10Ebooks);
    }

    @GetMapping("/user/ebooks")
    public
    @ResponseBody
    ResponseEntity<List<EbookDTO>>
    getAllUserEbooks(@RequestParam("username") String username) {

        List<EbookDTO> allUserEbooks = ebookMetadataService.getAllUserImages(username);
        return ResponseEntity.ok(allUserEbooks);
    }

    @DeleteMapping("/delete/ebook")
    public
    @ResponseBody
    ResponseEntity<Boolean>
    deleteFileAndMetadata(@RequestParam("id") Long id,
                          @RequestParam("username") String username) {

        Boolean success = ebookManagementService.removeFileAndMetadata(id, username);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/ebook")
    public
    @ResponseBody
    ResponseEntity<EbookDTO>
    updateEbookMetadata(@RequestBody EbookDTO ebookDTO) {

        return ResponseEntity.ok(ebookMetadataService.updateEbookMetadata(ebookDTO));
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

    public EbookManagementService getEbookManagementService() {
        return ebookManagementService;
    }

    public void setEbookManagementService(EbookManagementService ebookManagementService) {
        this.ebookManagementService = ebookManagementService;
    }
}
