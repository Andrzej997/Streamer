package pl.polsl.helper;

import org.springframework.stereotype.Component;
import pl.polsl.exception.FFMPEGException;

import java.io.*;

@Component
public class FFMPEGHelper {

    public static final int THUMBNAIL_WIDTH = 200;
    public static final int THUMBNAIL_HEIGHT = 200;

    public void generateThumbnail(File thumbnail, File video) throws IOException, InterruptedException, FFMPEGException {
        Runtime run = Runtime.getRuntime();
        Process process = run.exec(System.getProperty("ffmpeg.exec") +
                " -i " + video.getAbsolutePath().replaceAll("\\s", "") +
                " -loglevel quiet" +
                " -y" +
                " -an" +
                " -r 1" +
                " -ss 00:00:00" +
                " -t 00:00:01" +
                " -filter_complex crop=ih:ih:0:0,scale=200:200" + " " +
                thumbnail.getAbsolutePath().replaceAll("\\s", ""));

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new FFMPEGException("FFMPEG exited with non-zero code: " + exitCode);
        }
    }

    public void transcode(File input, File output, Resolution resolution) throws IOException, InterruptedException, FFMPEGException {
        Runtime run = Runtime.getRuntime();
        Process process = run.exec(System.getProperty("ffmpeg.exec") +
                " -i " + input.getAbsolutePath() +
                " -loglevel quiet" +
                " -y" +
                " -vf \"scale=" + resolution.getWidth() + ":" + resolution.getHeight() + "\" " +
                output.getAbsolutePath());

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new FFMPEGException("FFMPEG exited with non-zero code: " + exitCode);
        }
    }
}
