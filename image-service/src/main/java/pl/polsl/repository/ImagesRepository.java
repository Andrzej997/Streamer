package pl.polsl.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.Images;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
@Transactional
public interface ImagesRepository extends CrudRepository<Images, Long> {

    @Query("SELECT i FROM Images i where i.rating is not null and i.imageFileId in " +
            "(select f.imageFileId from ImageFiles f where f.isPublic = ?1) order by i.rating desc")
    List<Images> findTop10ByIsPublicOrderByRatingDesc(Boolean isPublic);

    @Query("SELECT i FROM Images i where i.rating is not null and i.title like ?2 and i.imageFileId in " +
            "(select f.imageFileId from ImageFiles f where f.isPublic = ?1) order by i.rating desc")
    List<Images> findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(Boolean isPublic, String title);

    @Query("SELECT i FROM Images i where i.rating is not null and i.ownerId = ?2 and i.imageFileId in " +
            "(select f.imageFileId from ImageFiles f where f.isPublic = ?1) order by i.rating desc")
    List<Images> findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(Boolean isPublic, Long ownerId);

    @Query("SELECT i FROM Images i where i.rating is not null and i.title like ?3 and i.ownerId = ?2 and i.imageFileId in " +
            "(select f.imageFileId from ImageFiles f where f.isPublic = ?1) order by i.rating desc")
    List<Images> findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(Boolean isPublic, Long ownerId, String title);

    List<Images> findByOwnerId(Long ownerId);

    List<Images> findByTitleOrderByRating(String title);

    List<Images> findByTitleLikeOrderByRating(String title);

    List<Images> findByYearOrderByRating(Short year);

    @Query("SELECT i FROM Images i where i.typeId in ( select t.typeId from ImageTypes t where t.name = ?1 )" +
            "ORDER BY i.rating")
    List<Images> findByTypeNameOrderByRating(String name);

    @Query("SELECT i FROM Images i where i.typeId in ( select t.typeId from ImageTypes t where t.name like ?1 )" +
            " ORDER BY i.rating")
    List<Images> findByTypeNameLikeOrderByRating(String name);

    List<Images> findByTypeIdOrderByRating(Long typeId);

    @Query("SELECT i FROM Images i where i.imageId in ( select a.imageId from ImageAuthors a where a.authorId = ?1 )" +
            " order by i.rating")
    List<Images> findByAuthorIdOrderByRating(Long authorId);

    @Query("SELECT i FROM Images i where i.imageId in ( select a.imageId from ImageAuthors a where a.authorId in " +
            "( SELECT aa.artistId from Artists aa where aa.name like ?1 ) ) order by i.rating")
    List<Images> findByArtistNameLikeOrderByRating(String artistName);

    @Query("SELECT i FROM Images i where i.imageId in ( select a.imageId from ImageAuthors a where a.authorId in " +
            "( SELECT aa.artistId from Artists aa where aa.name like ?1 and aa.surname like ?2 ) )" +
            " order by i.rating")
    List<Images> findByArtistNameLikeAndSurnameLikeOrderByRating(String name, String surname);

    @Query("SELECT i FROM Images i where i.imageId in ( select a.imageId from ImageAuthors a where a.authorId in " +
            "( SELECT aa.artistId from Artists aa where aa.name like ?1 and aa.surname like ?2 " +
            " and aa.name2 like ?3 ) ) order by i.rating")
    List<Images> findByArtistNameLikeAndSurnameLikeAndName2LikeOrderByRating(String name, String surname, String name2);

    List<Images> findAllByOrderByRatingDesc();

}
