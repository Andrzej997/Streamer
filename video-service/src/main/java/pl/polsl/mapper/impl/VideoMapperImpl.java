package pl.polsl.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.polsl.dto.*;
import pl.polsl.mapper.VideoMapper;
import pl.polsl.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mateusz on 27.11.2016.
 */
@Component
public class VideoMapperImpl implements VideoMapper {

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public DirectorDTO toDirectorDTO(Directors directors) {
        if (directors == null) {
            return null;
        }
        return modelMapper.map(directors, DirectorDTO.class);
    }

    @Override
    public List<DirectorDTO> toDirectorDTOList(List<Directors> directorsList) {
        if (directorsList == null) {
            return null;
        }
        List<DirectorDTO> result = new ArrayList<>();
        directorsList.forEach(directors -> result.add(toDirectorDTO(directors)));
        return result;
    }

    @Override
    public Directors toDirectors(DirectorDTO directorDTO) {
        if (directorDTO == null) {
            return null;
        }
        return modelMapper.map(directorDTO, Directors.class);
    }

    @Override
    public List<Directors> toDirectorsList(List<DirectorDTO> directorDTOList) {
        if (directorDTOList == null) {
            return null;
        }
        List<Directors> result = new ArrayList<>();
        directorDTOList.forEach(directorDTO -> result.add(toDirectors(directorDTO)));
        return result;
    }

    @Override
    public FilmGenreDTO toFilmGenreDTO(FilmGenres filmGenres) {
        if (filmGenres == null) {
            return null;
        }
        return modelMapper.map(filmGenres, FilmGenreDTO.class);
    }

    @Override
    public List<FilmGenreDTO> toFilmGenreDTOList(List<FilmGenres> filmGenres) {
        if (filmGenres == null) {
            return null;
        }
        List<FilmGenreDTO> result = new ArrayList<>();
        filmGenres.forEach(filmGenres1 -> result.add(toFilmGenreDTO(filmGenres1)));
        return result;
    }

    @Override
    public FilmGenres toFilmGenres(FilmGenreDTO filmGenreDTO) {
        if (filmGenreDTO == null) {
            return null;
        }
        return modelMapper.map(filmGenreDTO, FilmGenres.class);
    }

    @Override
    public List<FilmGenres> toFilmGenresList(List<FilmGenreDTO> filmGenreDTOList) {
        if (filmGenreDTOList == null) {
            return null;
        }
        List<FilmGenres> result = new ArrayList<>();
        filmGenreDTOList.forEach(filmGenres -> result.add(toFilmGenres(filmGenres)));
        return result;
    }

    @Override
    public VideoFileMetadataDTO toVideoFileMetadataDTO(VideoFiles videoFiles) {
        if (videoFiles == null) {
            return null;
        }
        Set<VideoFiles> filesSet = videoFiles.getChildVideoFiles();
        List<VideoQualityDTO> qualities = null;
        if (filesSet != null && filesSet.size() > 0) {
            qualities = filesSet.stream().map(vf -> new VideoQualityDTO(vf.getVideoFileId(), vf.getResolution())).collect(Collectors.toList());
        }
        VideoFileMetadataDTO fileMetadataDTO = modelMapper.map(videoFiles, VideoFileMetadataDTO.class);
        fileMetadataDTO.setQualities(qualities);
        return fileMetadataDTO;
    }

    @Override
    public List<VideoFileMetadataDTO> toVideoFileMetadataDTOList(List<VideoFiles> videoFilesList) {
        if (videoFilesList == null) {
            return null;
        }
        List<VideoFileMetadataDTO> result = new ArrayList<>();
        videoFilesList.forEach(videoFiles -> result.add(toVideoFileMetadataDTO(videoFiles)));
        return result;
    }

    @Override
    public VideoFiles toVideoFiles(VideoFileMetadataDTO videoFileMetadataDTO) {
        if (videoFileMetadataDTO == null) {
            return null;
        }
        return modelMapper.map(videoFileMetadataDTO, VideoFiles.class);
    }

    @Override
    public List<VideoFiles> toVideoFilesList(List<VideoFileMetadataDTO> videoFileMetadataDTOList) {
        if (videoFileMetadataDTOList == null) {
            return null;
        }
        List<VideoFiles> result = new ArrayList<>();
        videoFileMetadataDTOList.forEach(videoFileMetadataDTO -> result.add(toVideoFiles(videoFileMetadataDTO)));
        return result;
    }

    @Override
    public VideoSerieDTO toVideoSerieDTO(VideoSeries videoSeries) {
        if (videoSeries == null) {
            return null;
        }
        return modelMapper.map(videoSeries, VideoSerieDTO.class);
    }

    @Override
    public List<VideoSerieDTO> toVideoSerieDTOList(List<VideoSeries> videoSeriesList) {
        if (videoSeriesList == null) {
            return null;
        }
        List<VideoSerieDTO> result = new ArrayList<>();
        videoSeriesList.forEach(videoSeries -> result.add(toVideoSerieDTO(videoSeries)));
        return result;
    }

    @Override
    public VideoSeries toVideoSeries(VideoSerieDTO videoSerieDTO) {
        if (videoSerieDTO == null) {
            return null;
        }
        return modelMapper.map(videoSerieDTO, VideoSeries.class);
    }

    @Override
    public List<VideoSeries> toVideoSeriesList(List<VideoSerieDTO> videoSerieDTOList) {
        if (videoSerieDTOList == null) {
            return null;
        }
        List<VideoSeries> result = new ArrayList<>();
        videoSerieDTOList.forEach(videoSerieDTO -> result.add(toVideoSeries(videoSerieDTO)));
        return result;
    }

    @Override
    public VideoDTO toVideoDTO(Videos videos) {
        if (videos == null) {
            return null;
        }
        FilmGenres filmGenre = videos.getFilmGenresByFilmGenreId();
        VideoFiles videoFile = videos.getVideoFilesByVideoFileId();
        VideoSeries videoSerie = videos.getVideoSeriesByVideoSerieId();
        Collection<VideosAuthors> authorsCollection = videos.getVideosAuthorsesByVideoId();
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setRatingTimes(videos.getRatingTimes());
        videoDTO.setFilmGenre(this.toFilmGenreDTO(filmGenre));
        videoDTO.setFilmGenreId(videos.getFilmGenreId());
        videoDTO.setOwnerId(videos.getOwnerId());
        videoDTO.setProductionYear(videos.getProductionYear());
        videoDTO.setRating(videos.getRating());
        videoDTO.setTitle(videos.getTitle());
        videoDTO.setVideoFileId(videos.getVideoFileId());
        videoDTO.setVideoFileMetadata(toVideoFileMetadataDTO(videoFile));
        videoDTO.setVideoId(videos.getVideoId());
        videoDTO.setVideoSerie(toVideoSerieDTO(videoSerie));
        videoDTO.setVideoSerieId(videos.getVideoSerieId());
        if (authorsCollection != null) {
            List<DirectorDTO> directorDTOList = new ArrayList<>();
            for (VideosAuthors videosAuthors : authorsCollection) {
                directorDTOList.add(toDirectorDTO(videosAuthors.getDirectorsByAuthorId()));
            }
            videoDTO.setDirectorList(directorDTOList);
        }
        return videoDTO;
    }

    @Override
    public List<VideoDTO> toVideoDTOList(List<Videos> videosList) {
        if (videosList == null) {
            return null;
        }
        List<VideoDTO> result = new ArrayList<>();
        videosList.forEach(videos -> result.add(toVideoDTO(videos)));
        return result;
    }

    @Override
    public Videos toVideos(VideoDTO videoDTO) {
        if (videoDTO == null) {
            return null;
        }
        FilmGenreDTO filmGenre = videoDTO.getFilmGenre();
        VideoFileMetadataDTO videoFileMetadata = videoDTO.getVideoFileMetadata();
        VideoSerieDTO videoSerie = videoDTO.getVideoSerie();

        Videos videos = new Videos();
        videos.setVideoId(videoDTO.getVideoId());
        videos.setVideoSerieId(videoDTO.getVideoSerieId());
        videos.setRating(videoDTO.getRating());
        videos.setRatingTimes(videoDTO.getRatingTimes());
        videos.setFilmGenreId(videoDTO.getFilmGenreId());
        videos.setOwnerId(videoDTO.getOwnerId());
        videos.setTitle(videoDTO.getTitle());
        videos.setVideoFileId(videoDTO.getVideoFileId());
        videos.setFilmGenresByFilmGenreId(toFilmGenres(filmGenre));
        videos.setProductionYear(videoDTO.getProductionYear());
        videos.setVideoFilesByVideoFileId(toVideoFiles(videoFileMetadata));
        videos.setVideoSeriesByVideoSerieId(toVideoSeries(videoSerie));
        return videos;
    }

    @Override
    public List<Videos> toVideosList(List<VideoDTO> videoDTOList) {
        if (videoDTOList == null) {
            return null;
        }
        List<Videos> result = new ArrayList<>();
        videoDTOList.forEach(videoDTO -> result.add(toVideos(videoDTO)));
        return result;
    }
}
