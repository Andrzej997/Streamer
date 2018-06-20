package pl.polsl.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.polsl.dto.*;
import pl.polsl.mapper.VideoMapper;
import pl.polsl.model.*;
import pl.polsl.repository.*;
import pl.polsl.repository.custom.UsersRepositoryCustom;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Mateusz on 08.01.2017.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"bootstrap.yml"})
public class VideoMetadataServiceTests {

    @Autowired
    @InjectMocks
    private VideoMetadataService videoMetadataService;

    @Autowired
    @Spy
    private VideoMapper videoMapper;

    @Mock
    private UsersRepositoryCustom usersRepository;

    @Mock
    private DirectorsRepository directorsRepository;

    @Mock
    private FilmGenresRepository filmGenresRepository;

    @Mock
    private VideoAuthorsRepository videoAuthorsRepository;

    @Mock
    private VideoSeriesRepository videoSeriesRepository;

    @Mock
    private VideoFilesRepository videoFilesRepository;

    @Mock
    private VideosRepository videosRepository;

    @Test
    public void testGetDirectorsByPrediction_whenNameIsNull() {
        String name = null;
        String name2 = null;
        String surname = null;

        List<DirectorDTO> result = videoMetadataService.getDirectorsByPrediction(name, name2, surname);

        assertThat(result).isNull();
    }

    @Test
    public void testGetDirectorsByPrediction_whenNameExists() {
        String name = "test";
        String name2 = null;
        String surname = null;
        ArrayList<Directors> directorses = new ArrayList<>();
        directorses.add(new Directors());

        when(directorsRepository.findByNameLike(name)).thenReturn(directorses);
        when(directorsRepository.findByName2Like(name)).thenReturn(null);
        when(directorsRepository.findBySurnameLike(name)).thenReturn(null);

        List<DirectorDTO> result = videoMetadataService.getDirectorsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetDirectorsByPrediction_whenSurnameExists() {
        String name = "test";
        String name2 = null;
        String surname = "test";
        ArrayList<Directors> directorses = new ArrayList<>();
        directorses.add(new Directors());

        when(directorsRepository.findByNameLike(name)).thenReturn(directorses);
        when(directorsRepository.findByName2Like(name)).thenReturn(null);
        when(directorsRepository.findBySurnameLike(name)).thenReturn(directorses);

        List<DirectorDTO> result = videoMetadataService.getDirectorsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetDirectorsByPrediction_whenName2Exists() {
        String name = "test";
        String name2 = "test";
        String surname = null;
        ArrayList<Directors> directorses = new ArrayList<>();
        directorses.add(new Directors());

        when(directorsRepository.findByNameLike(name)).thenReturn(directorses);
        when(directorsRepository.findByName2Like(name)).thenReturn(directorses);
        when(directorsRepository.findBySurnameLike(name)).thenReturn(null);

        List<DirectorDTO> result = videoMetadataService.getDirectorsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetDirectorsByPrediction_whenEverythingIsSet() {
        String name = "test";
        String name2 = "test";
        String surname = "test";
        ArrayList<Directors> directorses = new ArrayList<>();
        directorses.add(new Directors());

        when(directorsRepository.findByNameLike(name)).thenReturn(directorses);
        when(directorsRepository.findByName2Like(name)).thenReturn(directorses);
        when(directorsRepository.findBySurnameLike(name)).thenReturn(directorses);

        List<DirectorDTO> result = videoMetadataService.getDirectorsByPrediction(name, name2, surname);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetFilmGenresByPrediction_whenNameIsNull() {
        String name = null;

        List<FilmGenreDTO> result = videoMetadataService.getFilmGenresByPrediction(name);

        assertThat(result).isNull();
    }

    @Test
    public void testGetFilmGenresByPrediction_whenNameExists() {
        String name = "test";
        List<FilmGenres> filmGenresList = new ArrayList<>();
        filmGenresList.add(new FilmGenres());
        when(filmGenresRepository.findByNameLike(name)).thenReturn(filmGenresList);

        List<FilmGenreDTO> result = videoMetadataService.getFilmGenresByPrediction(name);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testGetVideoSeriesByPrediction_whenSerieTitleIsNull() {
        String serieTitle = null;
        String videoTitle = "test";

        List<VideoSerieDTO> result = videoMetadataService.getVideoSeriesByPrediction(serieTitle, videoTitle);

        assertThat(result).isNull();
    }

    @Test
    public void testGetVideoSeriesByPrediction_whenVideoTitleIsNull() {
        String serieTitle = "test";
        String videoTitle = null;
        VideoSeries videoSeries = new VideoSeries();
        videoSeries.setVideoSerieId(1L);
        List<VideoSeries> videoSeriesList = new ArrayList<>();
        videoSeriesList.add(videoSeries);
        when(videoSeriesRepository.findByTitleLike("%" + serieTitle + "%")).thenReturn(videoSeriesList);

        List<VideoSerieDTO> result = videoMetadataService.getVideoSeriesByPrediction(serieTitle, videoTitle);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getVideoSerieId()).isEqualTo(1L);
    }

    @Test
    public void testGetVideoSeriesByPrediction_whenEverythingIsSet() {
        String serieTitle = "test";
        String videoTitle = "test";
        VideoSeries videoSeries = new VideoSeries();
        videoSeries.setVideoSerieId(1L);
        List<VideoSeries> videoSeriesList = new ArrayList<>();
        videoSeriesList.add(videoSeries);
        when(videoSeriesRepository.findByTitleLike("%" + serieTitle + "%")).thenReturn(videoSeriesList);
        when(videoSeriesRepository.findByVideoTitleLike("%" + videoTitle + "%")).thenReturn(null);

        List<VideoSerieDTO> result = videoMetadataService.getVideoSeriesByPrediction(serieTitle, videoTitle);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getVideoSerieId()).isEqualTo(1L);
    }

    @Test
    public void testSaveMetadata_whenUploadVideoMetadataDTOIsNull() {
        UploadVideoMetadataDTO uploadVideoMetadataDTO = null;

        UploadVideoMetadataDTO result = videoMetadataService.saveMetadata(uploadVideoMetadataDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSaveMetadata_whenVideoDTOIsNull() {
        UploadVideoMetadataDTO uploadVideoMetadataDTO = new UploadVideoMetadataDTO();
        uploadVideoMetadataDTO.setUsername("test");
        UsersView usersView = new UsersView();
        usersView.setUserName(uploadVideoMetadataDTO.getUsername());
        usersView.setUserId(1L);
        when(usersRepository.findUsersByUserName(uploadVideoMetadataDTO.getUsername())).thenReturn(usersView);

        UploadVideoMetadataDTO result = videoMetadataService.saveMetadata(uploadVideoMetadataDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSaveMetadata_whenDirectorsListIsEmpty() {
        UploadVideoMetadataDTO uploadVideoMetadataDTO = new UploadVideoMetadataDTO();
        uploadVideoMetadataDTO.setUsername("test");
        UsersView usersView = new UsersView();
        usersView.setUserName(uploadVideoMetadataDTO.getUsername());
        usersView.setUserId(1L);
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setOwnerId(usersView.getUserId());
        uploadVideoMetadataDTO.setVideo(videoDTO);
        Videos video = videoMapper.toVideos(uploadVideoMetadataDTO.getVideo());
        when(usersRepository.findUsersByUserName(uploadVideoMetadataDTO.getUsername())).thenReturn(usersView);
        when(videosRepository.save(video)).thenReturn(createVideo(video));

        UploadVideoMetadataDTO result = videoMetadataService.saveMetadata(uploadVideoMetadataDTO);

        assertThat(result).isNull();
    }

    private Videos createVideo(Videos video) {
        video.setVideoId(1L);
        return video;
    }

    @Test
    public void testGetTop10Videos_whenUsernameAndTitleIsNull() {
        String username = null;
        String title = null;
        Videos video = new Videos();
        video.setVideoId(1L);
        List<Videos> videosList = new ArrayList<>();
        videosList.add(video);
        when(videosRepository.findTop10ByIsPublicOrderByRatingDesc(true)).thenReturn(videosList);

        List<VideoDTO> result = videoMetadataService.getTop10Videos(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(videosList.size());
    }

    @Test
    public void testGetTop10Videos_whenUsernameIsNull() {
        String username = null;
        String title = "test";
        Videos video = new Videos();
        video.setVideoId(1L);
        List<Videos> videosList = new ArrayList<>();
        videosList.add(video);
        when(videosRepository.findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(true, "%" + title + "%")).thenReturn(videosList);

        List<VideoDTO> result = videoMetadataService.getTop10Videos(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(videosList.size());
    }

    @Test
    public void testGetTop10Videos_whenTitleIsNull() {
        String username = "test";
        String title = null;
        Videos video = new Videos();
        video.setVideoId(1L);
        List<Videos> videosList = new ArrayList<>();
        videosList.add(video);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(videosRepository.findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(false, usersView.getUserId()))
                .thenReturn(videosList);

        List<VideoDTO> result = videoMetadataService.getTop10Videos(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(videosList.size());
    }

    @Test
    public void testGetTop10Videos_whenEverythingIsSetted() {
        String username = "test";
        String title = "test";
        Videos video = new Videos();
        video.setVideoId(1L);
        List<Videos> videosList = new ArrayList<>();
        videosList.add(video);
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);

        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(videosRepository.findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(false, usersView.getUserId(), "%" + title + "%"))
                .thenReturn(videosList);

        List<VideoDTO> result = videoMetadataService.getTop10Videos(username, title);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(videosList.size());
    }

    @Test
    public void testGetAllUserVideos_whenUsernameIsNull() {
        String username = null;

        List<VideoDTO> result = videoMetadataService.getAllUserVideos(username);

        assertThat(result).isNull();
    }

    @Test
    public void testGetAllUserVideos_whenUsernameIsSet() {
        String username = "test";
        UsersView usersView = new UsersView();
        usersView.setUserId(1L);
        usersView.setUserName(username);
        Videos video = new Videos();
        video.setVideoId(1L);
        List<Videos> videosList = new ArrayList<>();
        videosList.add(video);

        when(usersRepository.findUsersByUserName(username)).thenReturn(usersView);
        when(videosRepository.findByOwnerId(usersView.getUserId())).thenReturn(videosList);

        List<VideoDTO> result = videoMetadataService.getAllUserVideos(username);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    public void testSearchVideosByCriteria_whenSearchVideoCriteriaDTOIsNull() {
        SearchVideoCriteriaDTO searchVideoCriteriaDTO = null;

        List<VideoDTO> result = videoMetadataService.searchVideosByCriteria(searchVideoCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchVideosByCriteria_whenCriteriaIsNull() {
        SearchVideoCriteriaDTO searchVideoCriteriaDTO = new SearchVideoCriteriaDTO();

        List<VideoDTO> result = videoMetadataService.searchVideosByCriteria(searchVideoCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchVideosByCriteria_whenTextSearchedIsNull() {
        SearchVideoCriteriaDTO searchVideoCriteriaDTO = new SearchVideoCriteriaDTO();
        searchVideoCriteriaDTO.setCriteria("T");

        List<VideoDTO> result = videoMetadataService.searchVideosByCriteria(searchVideoCriteriaDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testSearchVideosByCriteria_whenCriteriaIsTitle() {
        SearchVideoCriteriaDTO searchVideoCriteriaDTO = new SearchVideoCriteriaDTO();
        searchVideoCriteriaDTO.setCriteria("T");
        searchVideoCriteriaDTO.setTextSearched("test");
        Videos video = new Videos();
        video.setVideoId(1L);
        List<Videos> videosList = new ArrayList<>();
        videosList.add(video);

        when(videosRepository.findByTitleOrderByRating(searchVideoCriteriaDTO.getTextSearched())).thenReturn(videosList);
        when(videosRepository.findByTitleLikeOrderByRating(searchVideoCriteriaDTO.getTextSearched() + "%")).thenReturn(null);

        List<VideoDTO> result = videoMetadataService.searchVideosByCriteria(searchVideoCriteriaDTO);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testUpdateVideoMetadata_whenVideoDTOIsNull() {
        VideoDTO videoDTO = null;

        VideoDTO result = videoMetadataService.updateVideoMetadata(videoDTO);

        assertThat(result).isNull();
    }

    @Test
    public void testGetVideosTop50_whenDatabaseIsEmpty() {
        when(videosRepository.findAllByOrderByRatingDesc()).thenReturn(null);

        List<VideoDTO> result = videoMetadataService.getVideosTop50();

        assertThat(result).isNull();
    }

    @Test
    @Ignore
    public void testGetVideosTop50() {
        Videos video = new Videos();
        video.setVideoId(1L);
        video.setVideoFileId(1L);
        VideoFiles videoFiles = new VideoFiles();
        videoFiles.setVideoFileId(1L);
        videoFiles.setPublic(true);
        video.setVideoFilesByVideoFileId(videoFiles);
        List<Videos> videosList = new ArrayList<>();
        videosList.add(video);

        when(videosRepository.findAllByOrderByRatingDesc()).thenReturn(videosList);

        List<VideoDTO> result = videoMetadataService.getVideosTop50();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    public void testRateVideo_whenRateVideoDTO() {
        RateVideoDTO rateVideoDTO = null;

        videoMetadataService.rateVideo(rateVideoDTO);

        assertThat(true);
    }

    @Test
    public void testRateVideo_whenRateVideoDTOIsNotNull() {
        RateVideoDTO rateVideoDTO = new RateVideoDTO();
        rateVideoDTO.setFilmId(1L);
        rateVideoDTO.setRate(5);
        Videos video = new Videos();
        video.setVideoId(1L);
        video.setRatingTimes(1L);
        video.setRating(5.0f);

        when(videosRepository.findOne(1L)).thenReturn(video);

        videoMetadataService.rateVideo(rateVideoDTO);

        assertThat(true);
    }

    @Test
    public void testGetAllVideos_whenVideosNotExists() {
        when(videosRepository.findAll()).thenReturn(null);

        List<VideoDTO> result = videoMetadataService.getAllVideos();

        assertThat(result).isNull();
    }

    @Test
    public void testGetAllVideos_whenVideosExists() {
        Videos video = new Videos();
        video.setVideoId(1L);
        List<Videos> videosList = new ArrayList<>();
        videosList.add(video);
        when(videosRepository.findAll()).thenReturn(videosList);

        List<VideoDTO> result = videoMetadataService.getAllVideos();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }

}
