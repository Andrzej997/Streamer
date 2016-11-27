package pl.polsl.service;

import pl.polsl.dto.DirectorDTO;
import pl.polsl.dto.FilmGenreDTO;
import pl.polsl.dto.UploadVideoMetadataDTO;
import pl.polsl.dto.VideoSerieDTO;

import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
public interface VideoMetadataService {

    List<DirectorDTO> getDirectorsByPrediction(String name, String name2, String surname);

    List<FilmGenreDTO> getFilmGenresByPrediction(String name);

    List<VideoSerieDTO> getVideoSeriesByPrediction(String serieTitle, String videoTitle);

    UploadVideoMetadataDTO saveMetadata(UploadVideoMetadataDTO uploadVideoMetadataDTO);
}
