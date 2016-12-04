package pl.polsl.service.impl;

import org.apache.commons.lang.math.NumberUtils;
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
        if (videoDTO.getFilmGenre() != null) {
            genre = filmGenresRepository.save(videoMapper.toFilmGenres(videoDTO.getFilmGenre()));
        }
        if (genre == null) {
            return;
        }
        videos.setFilmGenresByFilmGenreId(genre);
        videos.setFilmGenreId(genre.getFilmGenreId());
    }

    private void saveVideoSerieForVideo(VideoDTO videoDTO, Videos videos) {
        VideoSeries videoSeries = null;
        if (videoDTO.getVideoSerie() != null) {
            videoSeries = videoSeriesRepository.save(videoMapper.toVideoSeries(videoDTO.getVideoSerie()));
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

            director = directorsRepository.save(director);

            VideosAuthors videosAuthors = new VideosAuthors();
            videosAuthors.setAuthorId(director.getDirectorId());
            videosAuthors.setDirectorsByAuthorId(director);
            videosAuthors.setVideoId(videos.getVideoId());
            videosAuthors.setVideosByVideoId(videos);
            videoAuthorsRepository.save(videosAuthors);
        }
    }

    @Override
    public List<VideoDTO> getTop10Videos(String username, String title) {
        List<Videos> videosList = null;
        UsersView user = null;
        if (!StringUtils.isEmpty(username)) {
            user = usersRepository.findUsersByUserName(username);
        }
        if (user == null) {
            if (StringUtils.isEmpty(title)) {
                videosList = videosRepository.findTop10ByIsPublicOrderByRatingDesc(true);
            } else {
                title = "%" + title + "%";
                videosList = videosRepository.findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(true, title);
            }
        } else {
            if (StringUtils.isEmpty(title)) {
                videosList = videosRepository.findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(false, user.getUserId());
            } else {
                title = "%" + title + "%";
                videosList = videosRepository.findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(false, user.getUserId(), title);
            }
        }
        if (videosList == null) {
            return null;
        }
        List<VideoDTO> videoDTOList = videoMapper.toVideoDTOList(videosList);
        if (videoDTOList == null || videoDTOList.isEmpty()) {
            return null;
        } else {
            return videoDTOList.subList(0, videoDTOList.size() > 9 ? 9 : videoDTOList.size());
        }
    }

    @Override
    public List<VideoDTO> getAllUserVideos(String username) {
        UsersView user = null;
        user = usersRepository.findUsersByUserName(username);
        if (user == null) {
            return null;
        }
        List<Videos> videosList = videosRepository.findByOwnerId(user.getUserId());
        return videoMapper.toVideoDTOList(videosList);
    }

    @Override
    public List<VideoDTO> searchVideosByCriteria(SearchVideoCriteriaDTO searchVideoCriteriaDTO) {
        if (searchVideoCriteriaDTO == null || StringUtils.isEmpty(searchVideoCriteriaDTO.getCriteria())) {
            return null;
        }
        String criteria = searchVideoCriteriaDTO.getCriteria();
        String textSearched = searchVideoCriteriaDTO.getTextSearched();
        switch (criteria) {
            case "T":
                return getVideosByTitle(textSearched);
            case "A":
                return getVideosByAuthor(textSearched);
            case "G":
                return getVideosByGenreName(textSearched);
            case "Y":
                return getVideosByYear(textSearched);
            case "ALL":
                return getVideosByAllCriteria(textSearched);
        }
        return null;
    }

    private List<VideoDTO> getVideosByTitle(String title) {
        if (title == null || "undefined".equals(title)) {
            return null;
        }
        List<Videos> videosList = videosRepository.findByTitleOrderByRating(title);
        if (videosList == null) {
            videosList = new ArrayList<>();
        }
        title += "%";
        videosList.addAll(videosRepository.findByTitleLikeOrderByRating(title));
        return videoMapper.toVideoDTOList(videosList);
    }

    private List<VideoDTO> getVideosByAuthor(String authorData) {
        if (authorData == null || "undefined".equals(authorData)) {
            return null;
        }
        String[] data = authorData.split(" ");
        if (data == null || data.length <= 0) {
            return null;
        }
        String name = data[0];
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        List<Videos> result = null;
        switch (data.length) {
            case 1:
                result = videosRepository.findByDirectorNameLikeOrderByRating(name);
                break;
            case 2:
                result = videosRepository.findByDirectorNameLikeAndSurnameLikeOrderByRating(name, data[1]);
                break;
            case 3:
                result = videosRepository.findByDirectorNameLikeAndSurnameLikeAndName2LikeOrderByRating(name, data[1], data[2]);
                break;
            default:
                break;
        }
        return videoMapper.toVideoDTOList(result);
    }

    private List<VideoDTO> getVideosByGenreName(String name) {
        if (name == null || "undefined".equals(name)) {
            return null;
        }
        List<Videos> videosList = videosRepository.findByGenreNameOrderByRating(name);
        if (videosList == null) {
            videosList = new ArrayList<>();
        }
        name += "%";
        videosList.addAll(videosRepository.findByGenreNameLikeOrderByRating(name));
        return videoMapper.toVideoDTOList(videosList);
    }

    private List<VideoDTO> getVideosByYear(String year) {
        if (year == null || "undefined".equals(year) || !NumberUtils.isNumber(year)) {
            return null;
        }
        Short yearNumber = Short.parseShort(year);
        List<Videos> videosList = videosRepository.findByProductionYearOrderByRating(yearNumber);
        return videoMapper.toVideoDTOList(videosList);
    }

    private List<VideoDTO> getVideosByAllCriteria(String searchText) {
        if (StringUtils.isEmpty(searchText)) {
            return null;
        }
        List<VideoDTO> result = new ArrayList<>();
        List<VideoDTO> videosByTitle = getVideosByTitle(searchText);
        List<VideoDTO> videosByYear = getVideosByYear(searchText);
        List<VideoDTO> videosByAuthor = getVideosByAuthor(searchText);
        List<VideoDTO> videosByGenreName = getVideosByGenreName(searchText);
        if (videosByTitle != null && !videosByTitle.isEmpty()) {
            result.addAll(videosByTitle);
        }
        if (videosByYear != null && !videosByYear.isEmpty()) {
            result.addAll(videosByYear);
        }
        if (videosByAuthor != null && !videosByAuthor.isEmpty()) {
            result.addAll(videosByAuthor);
        }
        if (videosByGenreName != null && !videosByGenreName.isEmpty()) {
            result.addAll(videosByGenreName);
        }
        return result;
    }

    @Override
    public VideoDTO updateVideoMetadata(VideoDTO videoDTO) {
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
        VideoDTO result = videoMapper.toVideoDTO(videos);
        return result;
    }

    @Override
    public List<VideoDTO> getVideosTop50() {
        Iterable<Videos> all = videosRepository.findAll();
        if (all == null) {
            return null;
        }
        List<Videos> allVideos = new ArrayList<>();
        all.forEach(allVideos::add);
        List<VideoDTO> result = videoMapper.toVideoDTOList(allVideos);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.subList(0, result.size() > 49 ? 49 : result.size());
        }
    }

}
