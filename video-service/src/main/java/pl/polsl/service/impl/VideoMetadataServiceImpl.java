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
        if (videoDTO.getFilmGenre() != null && videoDTO.getFilmGenre().getFilmGenreId() == null) {
            FilmGenres genre = filmGenresRepository.save(videoMapper.toFilmGenres(videoDTO.getFilmGenre()));
            videos.setFilmGenresByFilmGenreId(genre);
        }
        if (videoDTO.getVideoSerie() != null && videoDTO.getVideoSerie().getVideoSerieId() == null) {
            VideoSeries videoSeries = videoSeriesRepository.save(videoMapper.toVideoSeries(videoDTO.getVideoSerie()));
            videos.setVideoSeriesByVideoSerieId(videoSeries);
        }
        if (videoDTO.getVideoFileMetadata() != null) {
            VideoFiles videoFiles = videoFilesRepository.save(videoMapper.toVideoFiles(videoDTO.getVideoFileMetadata()));
            videos.setVideoFilesByVideoFileId(videoFiles);
        }
        videos = videosRepository.save(videos);
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
        List<VideosAuthors> videosAuthorsList = videoAuthorsRepository.findByVideoId(videos.getVideoId());
        videos.setVideosAuthorsesByVideoId(videosAuthorsList);

        videos = videosRepository.save(videos);

        VideoDTO toVideoDTO = videoMapper.toVideoDTO(videos);
        UploadVideoMetadataDTO result = new UploadVideoMetadataDTO();
        result.setVideo(toVideoDTO);
        result.setUsername(user.getUserName());
        return result;
    }
}
