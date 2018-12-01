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
                " -loglevel error" +
                " -y" +
                " -an" +
                " -r 1" +
                " -ss 00:00:00" +
                " -t 00:00:01" +
                " -filter_complex crop=ih:ih:0:0,scale=" + THUMBNAIL_WIDTH + ":" + THUMBNAIL_HEIGHT + " " +
                thumbnail.getAbsolutePath().replaceAll("\\s", ""));

        StreamGobbler erorr = new StreamGobbler(process.getErrorStream());
        StreamGobbler out = new StreamGobbler(process.getInputStream());
        erorr.start();
        out.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new FFMPEGException("FFMPEG exited with non-zero code: " + exitCode);
        }
    }

    public void transcode(File input, File output, Resolution resolution) throws IOException, InterruptedException, FFMPEGException {
        Runtime run = Runtime.getRuntime();
        Process process = run.exec(System.getProperty("ffmpeg.exec") +
                " -i " + input.getAbsolutePath().replaceAll("\\s", "") +
                " -movflags faststart " +
                " -loglevel error" +
                " -preset medium " +
                " -threads 0" +
                " -map_metadata 0:g " +
                " -y" +
                " -g 4" +
                " -vf scale=" + resolution.getWidth() + ":" + resolution.getHeight() + " " +
                output.getAbsolutePath().replaceAll("\\s", ""));

        StreamGobbler erorr = new StreamGobbler(process.getErrorStream());
        StreamGobbler out = new StreamGobbler(process.getInputStream());
        erorr.start();
        out.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new FFMPEGException("FFMPEG exited with non-zero code: " + exitCode);
        } else {
            System.out.println("Successful transcoded to " + resolution.name());
        }
    }

    class StreamGobbler extends Thread {
        InputStream is;

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line=null;
                while ( (line = br.readLine()) != null)
                    System.out.println(line);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
