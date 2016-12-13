package pl.polsl.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.Videos;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@Transactional
public interface VideosRepository extends CrudRepository<Videos, Long> {

    @Query("SELECT v FROM Videos v where v.rating is not null and v.videoFileId in " +
            "(select f.videoFileId from VideoFiles f where f.isPublic = ?1) order by v.rating desc")
    List<Videos> findTop10ByIsPublicOrderByRatingDesc(Boolean isPublic);

    @Query("SELECT v FROM Videos v where v.rating is not null and v.title like ?2 and v.videoFileId in " +
            "(select f.videoFileId from VideoFiles f where f.isPublic = ?1) order by v.rating desc")
    List<Videos> findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(Boolean isPublic, String title);

    @Query("SELECT v FROM Videos v where v.rating is not null and v.ownerId = ?2 and v.videoFileId in " +
            "(select f.videoFileId from VideoFiles f where f.isPublic = ?1) order by v.rating desc")
    List<Videos> findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(Boolean isPublic, Long ownerId);

    @Query("SELECT v FROM Videos v where v.rating is not null and v.title like ?3 and v.ownerId = ?2 and v.videoFileId in " +
            "(select f.videoFileId from VideoFiles f where f.isPublic = ?1) order by v.rating desc")
    List<Videos> findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(Boolean isPublic, Long ownerId, String title);

    List<Videos> findByOwnerId(Long ownerId);

    List<Videos> findByTitleOrderByRating(String title);

    List<Videos> findByTitleLikeOrderByRating(String title);

    List<Videos> findByProductionYearOrderByRating(Short year);

    @Query("SELECT v FROM Videos v where v.filmGenreId in ( select g.filmGenreId from FilmGenres g where g.name = ?1 )" +
            "ORDER BY v.rating")
    List<Videos> findByGenreNameOrderByRating(String name);

    @Query("SELECT v FROM Videos v where v.filmGenreId in ( select g.filmGenreId from FilmGenres g where g.name like ?1 )" +
            " ORDER BY v.rating")
    List<Videos> findByGenreNameLikeOrderByRating(String name);

    List<Videos> findByFilmGenreIdOrderByRating(Long FilmGenreId);

    @Query("SELECT v FROM Videos v where v.videoId in ( select a.videoId from VideosAuthors a where a.authorId = ?1 )" +
            " order by v.rating")
    List<Videos> findByAuthorIdOrderByRating(Long authorId);

    @Query("SELECT v FROM Videos v where v.videoId in ( select a.videoId from VideosAuthors a where a.authorId in " +
            "( SELECT d.directorId from Directors d where d.name like ?1 ) ) order by v.rating")
    List<Videos> findByDirectorNameLikeOrderByRating(String artistName);

    @Query("SELECT v FROM Videos v where v.videoId in ( select a.videoId from VideosAuthors a where a.authorId in " +
            "( SELECT d.directorId from Directors d where d.name like ?1 and d.surname like ?2 ) )" +
            " order by v.rating")
    List<Videos> findByDirectorNameLikeAndSurnameLikeOrderByRating(String name, String surname);

    @Query("SELECT v FROM Videos v where v.videoId in ( select a.videoId from VideosAuthors a where a.authorId in " +
            "( SELECT d.directorId from Directors d where d.name like ?1 and d.surname like ?2 " +
            " and d.name2 like ?3 ) ) order by v.rating")
    List<Videos> findByDirectorNameLikeAndSurnameLikeAndName2LikeOrderByRating(String name, String surname, String name2);

    List<Videos> findAllByOrderByRatingDesc();
}
