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
import java.util.stream.Collectors;

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
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        name = "%" + name + "%";
        List<FilmGenres> musicGenresList = filmGenresRepository.findByNameLike(name);
        return videoMapper.toFilmGenreDTOList(musicGenresList);
    }

    @Override
    public List<VideoSerieDTO> getVideoSeriesByPrediction(String serieTitle, String videoTitle) {
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
            List<VideoSeries> videoSeriesListByVideoTitleLike = videoSeriesRepository.findByVideoTitleLike(videoTitle);
            if (videoSeriesListByVideoTitleLike != null && !videoSeriesListByVideoTitleLike.isEmpty()) {
                videoSeriesList.addAll(videoSeriesListByVideoTitleLike);
            }
        }
        return videoMapper.toVideoSerieDTOList(videoSeriesList);
    }

    @Override
    public UploadVideoMetadataDTO saveMetadata(UploadVideoMetadataDTO uploadVideoMetadataDTO) {
        if (uploadVideoMetadataDTO == null || StringUtils.isEmpty(uploadVideoMetadataDTO.getUsername())) {
            return null;
        }
        if (uploadVideoMetadataDTO.getVideo() == null || uploadVideoMetadataDTO.getVideo().getVideoFileId() == null) {
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
        videos.setOwnerId(user.getUserId());
        videos = videosRepository.save(videos);
        if (videos == null) {
            return null;
        }

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
        if (videoDTO.getFilmGenre() != null && !StringUtils.isEmpty(videoDTO.getFilmGenre().getName())) {
            genre = filmGenresRepository.save(videoMapper.toFilmGenres(videoDTO.getFilmGenre()));
        }
        if (genre == null) {
            videos.setFilmGenresByFilmGenreId(null);
            videos.setFilmGenreId(null);
            return;
        }
        videos.setFilmGenresByFilmGenreId(genre);
        videos.setFilmGenreId(genre.getFilmGenreId());
    }

    private void saveVideoSerieForVideo(VideoDTO videoDTO, Videos videos) {
        VideoSeries videoSeries = null;
        if (videoDTO.getVideoSerie() != null && !StringUtils.isEmpty(videoDTO.getVideoSerie().getTitle())) {
            videoSeries = videoSeriesRepository.save(videoMapper.toVideoSeries(videoDTO.getVideoSerie()));
        }
        if (videoSeries == null) {
            videos.setVideoSeriesByVideoSerieId(null);
            videos.setVideoSerieId(null);
            return;
        }
        videos.setVideoSeriesByVideoSerieId(videoSeries);
        videos.setVideoSerieId(videoSeries.getVideoSerieId());
    }

    private void saveVideoFileMetadataForVideo(VideoDTO videoDTO, Videos videos) {
//        VideoFiles videoFiles = null;
//        VideoFiles file = videoFilesRepository.findOne(videoDTO.getVideoFileId());
//        if (videoDTO.getVideoFileMetadata() != null) {
//            VideoFiles toVideoFiles = videoMapper.toVideoFiles(videoDTO.getVideoFileMetadata());
//            toVideoFiles.setFile(file.getFile());
//            toVideoFiles.setResolution(file.getResolution());
//            toVideoFiles.setThumbnail(file.getThumbnail());
//            toVideoFiles.setChildVideoFiles(file.getChildVideoFiles());
//            videoFiles = videoFilesRepository.save(toVideoFiles);
//        }
//        if (videoFiles == null) {
//            videos.setVideoFilesByVideoFileId(null);
//            videos.setVideoFileId(null);
//            return;
//        }
//        videos.setVideoFilesByVideoFileId(videoFiles);
        videos.setVideoFileId(videoDTO.getVideoFileId());
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
        videosList.forEach(videos -> {
            if (videos.getVideoFileId() != null) {
                videos.setVideoFilesByVideoFileId(videoFilesRepository.findOne(videos.getVideoFileId()));
            }
        });
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
        videosList.forEach(videos -> {
            if (videos.getVideoFileId() != null) {
                videos.setVideoFilesByVideoFileId(videoFilesRepository.findOne(videos.getVideoFileId()));
            }
        });
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
        List<Videos> videosListByTitleLike = videosRepository.findByTitleLikeOrderByRating(title);
        if (videosListByTitleLike != null && videosListByTitleLike.size() > 0) {
            videosList.addAll(videosListByTitleLike);
        }
        videosList = videosList.stream().filter(videos -> {
            if (videos.getVideoFileId() != null) {
                videos.setVideoFilesByVideoFileId(videoFilesRepository.findOne(videos.getVideoFileId()));
            }
            return videos.getVideoFilesByVideoFileId() != null && videos.getVideoFilesByVideoFileId().getPublic();
        }).collect(Collectors.toList());
        return videoMapper.toVideoDTOList(videosList);
    }

    private List<VideoDTO> getVideosByAuthor(String authorData) {
        if (authorData == null || "undefined".equals(authorData)) {
            return null;
        }
        String[] data = authorData.split(" ");
        if (data.length <= 0) {
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
        result = result != null ? result.stream().filter(videos -> {
            if (videos.getVideoFileId() != null) {
                videos.setVideoFilesByVideoFileId(videoFilesRepository.findOne(videos.getVideoFileId()));
            }
            return videos.getVideoFilesByVideoFileId() != null && videos.getVideoFilesByVideoFileId().getPublic();
        }).collect(Collectors.toList()) : null;
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
        videosList = videosList.stream().filter(videos -> {
            if (videos.getVideoFileId() != null) {
                videos.setVideoFilesByVideoFileId(videoFilesRepository.findOne(videos.getVideoFileId()));
            }
            return videos.getVideoFilesByVideoFileId() != null && videos.getVideoFilesByVideoFileId().getPublic();
        }).collect(Collectors.toList());
        return videoMapper.toVideoDTOList(videosList);
    }

    private List<VideoDTO> getVideosByYear(String year) {
        if (year == null || "undefined".equals(year) || !NumberUtils.isNumber(year)) {
            return null;
        }
        Short yearNumber = Short.parseShort(year);
        List<Videos> videosList = videosRepository.findByProductionYearOrderByRating(yearNumber);
        videosList = videosList.stream().filter(videos -> {
            if (videos.getVideoFileId() != null) {
                videos.setVideoFilesByVideoFileId(videoFilesRepository.findOne(videos.getVideoFileId()));
            }
            return videos.getVideoFilesByVideoFileId() != null && videos.getVideoFilesByVideoFileId().getPublic();
        }).collect(Collectors.toList());
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
        if (videos.getVideoFileId() != null) {
            videos.setVideoFilesByVideoFileId(videoFilesRepository.findOne(videos.getVideoFileId()));
        }
        return videoMapper.toVideoDTO(videos);
    }

    @Override
    public List<VideoDTO> getVideosTop50() {
        Iterable<Videos> all = videosRepository.findAllByOrderByRatingDesc();
        if (all == null) {
            return null;
        }
        List<Videos> allVideos = new ArrayList<>();
        all.forEach(videos -> {
            if (videos.getVideoFileId() != null) {
                videos.setVideoFilesByVideoFileId(videoFilesRepository.findOne(videos.getVideoFileId()));
            }
            if (videos.getVideoFilesByVideoFileId() != null && videos.getVideoFilesByVideoFileId().getPublic()) {
                allVideos.add(videos);
            }
        });
        allVideos.sort((o1, o2) -> compareFloat(o1.getRating(), o2.getRating()));
        List<VideoDTO> result = videoMapper.toVideoDTOList(allVideos);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.subList(0, result.size() > 49 ? 49 : result.size());
        }
    }

    private int compareFloat(Float f1, Float f2) {
        if (f1 == null && f2 == null) {
            return 0;
        }
        if (f1 == null) {
            return 1;
        }
        if (f2 == null) {
            return -1;
        }
        return f2.compareTo(f1);
    }

    @Override
    public void rateVideo(RateVideoDTO rateVideoDTO) {
        if (rateVideoDTO == null || rateVideoDTO.getFilmId() == null || rateVideoDTO.getRate() == null) {
            return;
        }
        Videos video = videosRepository.findOne(rateVideoDTO.getFilmId());
        if (video == null) {
            return;
        }
        Float rating = video.getRating();
        Long ratingTimes = video.getRatingTimes();
        Float temp = (rating * ratingTimes) + (rateVideoDTO.getRate() * 10);
        video.setRatingTimes(video.getRatingTimes() + 1);
        video.setRating(temp / video.getRatingTimes());
        videosRepository.save(video);
    }

    @Override
    public List<VideoDTO> getAllVideos() {
        Iterable<Videos> videosIterable = videosRepository.findAll();
        if (videosIterable == null) {
            return null;
        }
        List<VideoDTO> result = new ArrayList<>();
        videosIterable.forEach(video -> {
            if (video.getVideoFileId() != null) {
                video.setVideoFilesByVideoFileId(videoFilesRepository.findOne(video.getVideoFileId()));
            }
            result.add(videoMapper.toVideoDTO(video));
        });
        return result;
    }

    public VideoMapper getVideoMapper() {
        return videoMapper;
    }

    public void setVideoMapper(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    public UsersRepositoryCustom getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepositoryCustom usersRepository) {
        this.usersRepository = usersRepository;
    }

    public DirectorsRepository getDirectorsRepository() {
        return directorsRepository;
    }

    public void setDirectorsRepository(DirectorsRepository directorsRepository) {
        this.directorsRepository = directorsRepository;
    }

    public FilmGenresRepository getFilmGenresRepository() {
        return filmGenresRepository;
    }

    public void setFilmGenresRepository(FilmGenresRepository filmGenresRepository) {
        this.filmGenresRepository = filmGenresRepository;
    }

    public VideoAuthorsRepository getVideoAuthorsRepository() {
        return videoAuthorsRepository;
    }

    public void setVideoAuthorsRepository(VideoAuthorsRepository videoAuthorsRepository) {
        this.videoAuthorsRepository = videoAuthorsRepository;
    }

    public VideoFilesRepository getVideoFilesRepository() {
        return videoFilesRepository;
    }

    public void setVideoFilesRepository(VideoFilesRepository videoFilesRepository) {
        this.videoFilesRepository = videoFilesRepository;
    }

    public VideoSeriesRepository getVideoSeriesRepository() {
        return videoSeriesRepository;
    }

    public void setVideoSeriesRepository(VideoSeriesRepository videoSeriesRepository) {
        this.videoSeriesRepository = videoSeriesRepository;
    }

    public VideosRepository getVideosRepository() {
        return videosRepository;
    }

    public void setVideosRepository(VideosRepository videosRepository) {
        this.videosRepository = videosRepository;
    }
}
