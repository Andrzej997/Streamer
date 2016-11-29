package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.dto.*;
import pl.polsl.mapper.VideoMapper;
import pl.polsl.model.*;
import pl.polsl.repository.*;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.VideoMetadataService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 27.11.2016.
 */
@Service
@Transactional
public class VideoMetadataServiceImpl implements VideoMetadataService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Autowired
    private DirectorsRepository directorsRepository;

    @Autowired
    private FilmGenresRepository filmGenresRepository;

    @Autowired
    private VideoAuthorsRepository videoAuthorsRepository;

    @Autowired
    private VideoFilesRepository videoFilesRepository;

    @Autowired
    private VideoSeriesRepository videoSeriesRepository;

    @Autowired
    private VideosRepository videosRepository;


    @Override
    public List<DirectorDTO> getDirectorsByPrediction(String name, String name2, String surname) {
        List<DirectorDTO> result;
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        name = "%" + name + "%";
        List<Directors> directorsList = directorsRepository.findByNameLike(name);
        if (directorsList == null) {
            directorsList = new ArrayList<>();
        }
        directorsList.addAll(directorsRepository.findByName2Like(name));
        directorsList.addAll(directorsRepository.findBySurnameLike(name));
        if (!StringUtils.isEmpty(surname) && !surname.equals("undefined")) {
            surname = "%" + surname + "%";
            directorsList = directorsRepository.findByNameLikeAndSurnameLike(name, surname);
            if (directorsList == null) {
                directorsList = new ArrayList<>();
            }
            directorsList.addAll(directorsRepository.findByNameLikeAndName2Like(name, surname));
        }
        if (!StringUtils.isEmpty(name2) && !name2.equals("undefined")) {
            name2 = "%" + name2 + "%";
            directorsList = directorsRepository.findByNameLikeAndName2Like(name, name2);
            if (directorsList == null) {
                directorsList = new ArrayList<>();
            }
            directorsList.addAll(directorsRepository.findByNameLikeAndSurnameLike(name, name2));
        }
        result = videoMapper.toDirectorDTOList(directorsList);
        return result;
    }

    @Override
    public List<FilmGenreDTO> getFilmGenresByPrediction(String name) {
        List<FilmGenreDTO> result;
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        name = "%" + name + "%";
        List<FilmGenres> musicGenresList = filmGenresRepository.findByNameLike(name);
        return videoMapper.toFilmGenreDTOList(musicGenresList);
    }

    @Override
    public List<VideoSerieDTO> getVideoSeriesByPrediction(String serieTitle, String videoTitle) {
        List<VideoSerieDTO> result;
        if (StringUtils.isEmpty(serieTitle)) {
            return null;
        }
        serieTitle = "%" + serieTitle + "%";
        List<VideoSeries> videoSeriesList = videoSeriesRepository.findByTitleLike(serieTitle);
        if (!StringUtils.isEmpty(videoTitle)) {
            if (videoSeriesList == null) {
                videoSeriesList = new ArrayList<>();
            }
            videoTitle = "%" + videoTitle + "%";
            videoSeriesList.addAll(videoSeriesRepository.findByVideoTitleLike(videoTitle));
        }
        return videoMapper.toVideoSerieDTOList(videoSeriesList);
    }

    @Override
    public UploadVideoMetadataDTO saveMetadata(UploadVideoMetadataDTO uploadVideoMetadataDTO) {
        if (uploadVideoMetadataDTO == null || uploadVideoMetadataDTO.getVideo() == null
                || uploadVideoMetadataDTO.getVideo().getVideoFileId() == null) {
            return null;
        }
        UsersView user = usersRepository.findUsersByUserName(uploadVideoMetadataDTO.getUsername());
        if (user == null) {
            return null;
        }
        VideoDTO videoDTO = uploadVideoMetadataDTO.getVideo();
        if (videoDTO == null) {
            return null;
        }
        Videos videos = videoMapper.toVideos(videoDTO);
        List<Directors> directorsList = videoMapper.toDirectorsList(videoDTO.getDirectorList());
        saveFilmGenreForVideo(videoDTO, videos);
        saveVideoSerieForVideo(videoDTO, videos);
        saveVideoFileMetadataForVideo(videoDTO, videos);

        videos = videosRepository.save(videos);

        saveVideoDirectors(videos, directorsList);
        List<VideosAuthors> videosAuthorsList = videoAuthorsRepository.findByVideoId(videos.getVideoId());
        videos.setVideosAuthorsesByVideoId(videosAuthorsList);

        videos = videosRepository.save(videos);

        VideoDTO toVideoDTO = videoMapper.toVideoDTO(videos);
        UploadVideoMetadataDTO result = new UploadVideoMetadataDTO();
        result.setVideo(toVideoDTO);
        result.setUsername(user.getUserName());
        return result;
    }

    private void saveFilmGenreForVideo(VideoDTO videoDTO, Videos videos) {
        FilmGenres genre = null;
        if (videoDTO.getFilmGenre() != null && videoDTO.getFilmGenre().getFilmGenreId() == null) {
            genre = filmGenresRepository.save(videoMapper.toFilmGenres(videoDTO.getFilmGenre()));
        } else if (videoDTO.getFilmGenre() != null) {
            genre = videoMapper.toFilmGenres(videoDTO.getFilmGenre());
        }
        if (genre == null) {
            return;
        }
        videos.setFilmGenresByFilmGenreId(genre);
        videos.setFilmGenreId(genre.getFilmGenreId());
    }

    private void saveVideoSerieForVideo(VideoDTO videoDTO, Videos videos) {
        VideoSeries videoSeries = null;
        if (videoDTO.getVideoSerie() != null && videoDTO.getVideoSerie().getVideoSerieId() == null) {
            videoSeries = videoSeriesRepository.save(videoMapper.toVideoSeries(videoDTO.getVideoSerie()));
        } else if (videoDTO.getVideoSerie() != null) {
            videoSeries = videoMapper.toVideoSeries(videoDTO.getVideoSerie());
        }
        if (videoSeries == null) {
            return;
        }
        videos.setVideoSeriesByVideoSerieId(videoSeries);
        videos.setVideoSerieId(videoSeries.getVideoSerieId());
    }

    private void saveVideoFileMetadataForVideo(VideoDTO videoDTO, Videos videos) {
        VideoFiles videoFiles = null;
        if (videoDTO.getVideoFileMetadata() != null) {
            videoFiles = videoFilesRepository.save(videoMapper.toVideoFiles(videoDTO.getVideoFileMetadata()));
        }
        if (videoFiles == null) {
            return;
        }
        videos.setVideoFilesByVideoFileId(videoFiles);
        videos.setVideoFileId(videoFiles.getVideoFileId());
    }

    private void saveVideoDirectors(Videos videos, List<Directors> directorsList) {
        for (Directors director : directorsList) {
            if (director.getDirectorId() == null) {
                director = directorsRepository.save(director);
            }
            VideosAuthors videosAuthors = new VideosAuthors();
            videosAuthors.setAuthorId(director.getDirectorId());
            videosAuthors.setDirectorsByAuthorId(director);
            videosAuthors.setVideoId(videos.getVideoId());
            videosAuthors.setVideosByVideoId(videos);
            videoAuthorsRepository.save(videosAuthors);
        }
    }
}
