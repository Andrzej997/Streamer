package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import pl.polsl.model.VideoFiles;
import pl.polsl.service.StorageService;
import pl.polsl.service.StoreTaskExecutor;

@Component
public class StoreTaskExecutorImpl extends ThreadPoolTaskExecutor implements StoreTaskExecutor {

    @Autowired
    private StorageService storageService;

    @Override
    public void storeVideoFile(VideoFiles videoFile){
        this.execute(() -> {
            storageService.storeFile(videoFile);
        });
    }

}
