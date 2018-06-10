package pl.polsl.service;

import pl.polsl.helper.Resolution;
import pl.polsl.model.VideoFiles;

public interface TranscodeVideoTaskExecutor {
    void transcodeVideoToLowersQuality(Resolution max, VideoFiles videoFile);
}
