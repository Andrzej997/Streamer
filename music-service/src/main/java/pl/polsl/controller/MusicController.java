package pl.polsl.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.model.MusicFiles;
import pl.polsl.service.StorageService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Mateusz on 20.11.2016.
 */
@SuppressWarnings("ALL")
@RestController
public class MusicController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/auth/upload")
    public ResponseEntity<Boolean> handleFileUpload(@RequestParam("file") MultipartFile file) {

        MusicFiles musicFile = storageService.store(file);
        return ResponseEntity.ok(musicFile != null);
    }

    @GetMapping("/auth/download")
    @ResponseBody
    public String downloadMusicFile(@RequestParam("id") Long id, HttpServletResponse response) {
        MusicFiles musicFile = storageService.downloadMusicFile(id);
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" + musicFile.getFileName() + "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType("audio/mp3");
            IOUtils.copy(musicFile.getFile().getBinaryStream(), out);
            out.flush();
            out.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
