package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import pl.polsl.exception.FFMPEGException;
import pl.polsl.helper.Resolution;
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;
import pl.polsl.service.TranscodeVideoTaskExecutor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TranscodeVideoTaskExecutorImpl extends ThreadPoolTaskExecutor implements TranscodeVideoTaskExecutor {

    @Autowired
    private StorageService storageService;

    @Override
    public void transcodeVideoToLowersQuality(Resolution max, VideoFiles videoFile){
        List<Resolution> resolutionList = generateLowerResolutionList(max);
        this.setKeepAliveSeconds(1800);
        resolutionList.forEach(resolution -> {
            execute(() -> {
                try {
                    storageService.transcode(videoFile, resolution);
                } catch (IOException | SQLException | FFMPEGException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private List<Resolution> generateLowerResolutionList(Resolution max){
        List<Resolution> result = new ArrayList<>();
        for (Resolution resolution : Resolution.values()) {
            if (resolution.equals(max)){
                return result;
            } else {
                result.add(resolution);
            }
        }
        return result;
    }
}
