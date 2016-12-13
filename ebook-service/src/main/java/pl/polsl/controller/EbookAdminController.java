package pl.polsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.polsl.dto.EbookDTO;
import pl.polsl.service.EbookManagementService;
import pl.polsl.service.EbookMetadataService;

import java.util.List;

/**
 * Created by Mateusz on 12.12.2016.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin")
public class EbookAdminController {

    @Autowired
    private EbookManagementService ebookManagementService;

    @Autowired
    private EbookMetadataService ebookMetadataService;

    @GetMapping(value = "/ebooks")
    public
    @ResponseBody
    ResponseEntity<List<EbookDTO>>
    getEbooks(@RequestParam(value = "username", required = false) String username) {
        if (StringUtils.isEmpty(username)) {
            return ResponseEntity.ok(ebookMetadataService.getAllEbooks());
        } else {
            return ResponseEntity.ok(ebookMetadataService.getAllUserEbooks(username));
        }
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

}
