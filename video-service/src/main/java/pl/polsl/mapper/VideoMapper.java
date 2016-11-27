package pl.polsl.mapper;

import pl.polsl.dto.*;
import pl.polsl.model.*;

import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
public interface VideoMapper {
    DirectorDTO toDirectorDTO(Directors directors);

    List<DirectorDTO> toDirectorDTOList(List<Directors> directorsList);

    Directors toDirectors(DirectorDTO directorDTO);

    List<Directors> toDirectorsList(List<DirectorDTO> directorDTOList);

    FilmGenreDTO toFilmGenreDTO(FilmGenres filmGenres);

    List<FilmGenreDTO> toFilmGenreDTOList(List<FilmGenres> filmGenres);

    FilmGenres toFilmGenres(FilmGenreDTO filmGenreDTO);

    List<FilmGenres> toFilmGenresList(List<FilmGenreDTO> filmGenreDTOList);

    VideoFileMetadataDTO toVideoFileMetadataDTO(VideoFiles videoFiles);

    List<VideoFileMetadataDTO> toVideoFileMetadataDTOList(List<VideoFiles> videoFilesList);

    VideoFiles toVideoFiles(VideoFileMetadataDTO videoFileMetadataDTO);

    List<VideoFiles> toVideoFilesList(List<VideoFileMetadataDTO> videoFileMetadataDTOList);

    VideoSerieDTO toVideoSerieDTO(VideoSeries videoSeries);

    List<VideoSerieDTO> toVideoSerieDTOList(List<VideoSeries> videoSeriesList);

    VideoSeries toVideoSeries(VideoSerieDTO videoSerieDTO);

    List<VideoSeries> toVideoSeriesList(List<VideoSerieDTO> videoSerieDTOList);

    VideoDTO toVideoDTO(Videos videos);

    List<VideoDTO> toVideoDTOList(List<Videos> videosList);

    Videos toVideos(VideoDTO videoDTO);

    List<Videos> toVideosList(List<VideoDTO> videoDTOList);
}
