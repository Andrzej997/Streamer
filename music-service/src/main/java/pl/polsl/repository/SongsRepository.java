package pl.polsl.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.Songs;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
@Transactional
public interface SongsRepository extends CrudRepository<Songs, Long> {

    @Query("SELECT s FROM Songs s where s.rating is not null and s.fileId in " +
            "(select f.musicFileId from MusicFiles f where f.isPublic = ?1) order by s.rating desc")
    List<Songs> findTop10ByIsPublicOrderByRatingDesc(Boolean isPublic);

    @Query("SELECT s FROM Songs s where s.rating is not null and s.title like ?2 and s.fileId in " +
            "(select f.musicFileId from MusicFiles f where f.isPublic = ?1) order by s.rating desc")
    List<Songs> findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(Boolean isPublic, String title);

    @Query("SELECT s FROM Songs s where s.rating is not null and s.ownerId = ?2 and s.fileId in " +
            "(select f.musicFileId from MusicFiles f where f.isPublic = ?1) order by s.rating desc")
    List<Songs> findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(Boolean isPublic, Long ownerId);

    @Query("SELECT s FROM Songs s where s.rating is not null and s.title like ?3 and s.ownerId = ?2 and s.fileId in " +
            "(select f.musicFileId from MusicFiles f where f.isPublic = ?1) order by s.rating desc")
    List<Songs> findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(Boolean isPublic, Long ownerId, String title);

    List<Songs> findByOwnerId(Long ownerId);

    List<Songs> findByTitleOrderByRating(String title);

    List<Songs> findByTitleLikeOrderByRating(String title);

    List<Songs> findByProductionYearOrderByRating(Short year);

    @Query("SELECT s FROM Songs s where s.musicGenreId in ( select g.musicGenreId from MusicGenres g where g.name = ?1 )" +
            "ORDER BY s.rating")
    List<Songs> findByGenreNameOrderByRating(String name);

    @Query("SELECT s FROM Songs s where s.musicGenreId in ( select g.musicGenreId from MusicGenres g where g.name like ?1 )" +
            " ORDER BY s.rating")
    List<Songs> findByGenreNameLikeOrderByRating(String name);

    List<Songs> findByMusicGenreIdOrderByRating(Long musicGenreId);

    @Query("SELECT s FROM Songs s where s.songId in ( select a.songId from MusicAuthors a where a.authorId = ?1 )" +
            " order by s.rating")
    List<Songs> findByArtistIdOrderByRating(Long artistId);

    @Query("SELECT s FROM Songs s where s.songId in ( select a.songId from MusicAuthors a where a.authorId in " +
            "( SELECT aa.authorId from MusicArtists aa where aa.name like ?1 ) ) order by s.rating")
    List<Songs> findByArtistNameLikeOrderByRating(String artistName);

    @Query("SELECT s FROM Songs s where s.songId in ( select a.songId from MusicAuthors a where a.authorId in " +
            "( SELECT aa.authorId from MusicArtists aa where aa.name like ?1 and aa.surname like ?2 ) )" +
            " order by s.rating")
    List<Songs> findByArtistNameLikeAndSurnameLikeOrderByRating(String name, String surname);

    @Query("SELECT s FROM Songs s where s.songId in ( select a.songId from MusicAuthors a where a.authorId in " +
            "( SELECT aa.authorId from MusicArtists aa where aa.name like ?1 and aa.surname like ?2 " +
            " and aa.name2 like ?3 ) ) order by s.rating")
    List<Songs> findByArtistNameLikeAndSurnameLikeAndName2LikeOrderByRating(String name, String surname, String name2);

}
