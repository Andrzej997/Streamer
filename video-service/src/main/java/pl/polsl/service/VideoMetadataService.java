package pl.polsl.service;

import pl.polsl.dto.*;

import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
public interface VideoMetadataService {

    List<DirectorDTO> getDirectorsByPrediction(String name, String name2, String surname);

    List<FilmGenreDTO> getFilmGenresByPrediction(String name);

    List<VideoSerieDTO> getVideoSeriesByPrediction(String serieTitle, String videoTitle);

    UploadVideoMetadataDTO saveMetadata(UploadVideoMetadataDTO uploadVideoMetadataDTO);

    List<VideoDTO> getTop10Videos(String username, String title);

    List<VideoDTO> getAllUserVideos(String username);

    List<VideoDTO> searchVideosByCriteria(SearchVideoCriteriaDTO searchVideoCriteriaDTO);

    VideoDTO updateVideoMetadata(VideoDTO videoDTO);

    List<VideoDTO> getVideosTop50();

    void rateVideo(RateVideoDTO rateVideoDTO);

    List<VideoDTO> getAllVideos();
}
