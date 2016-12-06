package pl.polsl.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.Ebook;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
@Transactional
public interface EbookRepository extends CrudRepository<Ebook, Long> {

    @Query("SELECT e FROM Ebook e where e.rating is not null and e.ebookFileId in " +
            "(select f.ebookFileId from EbookFiles f where f.isPublic = ?1) order by e.rating desc")
    List<Ebook> findTop10ByIsPublicOrderByRatingDesc(Boolean isPublic);

    @Query("SELECT e FROM Ebook e where e.rating is not null and e.title like ?2 and e.ebookFileId in " +
            "(select f.ebookFileId from EbookFiles f where f.isPublic = ?1) order by e.rating desc")
    List<Ebook> findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(Boolean isPublic, String title);

    @Query("SELECT e FROM Ebook e where e.rating is not null and e.ownerId = ?2 and e.ebookFileId in " +
            "(select f.ebookFileId from EbookFiles f where f.isPublic = ?1) order by e.rating desc")
    List<Ebook> findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(Boolean isPublic, Long ownerId);

    @Query("SELECT e FROM Ebook e where e.rating is not null and e.title like ?3 and e.ownerId = ?2 and e.ebookFileId in " +
            "(select f.ebookFileId from EbookFiles f where f.isPublic = ?1) order by e.rating desc")
    List<Ebook> findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(Boolean isPublic, Long ownerId, String title);

    List<Ebook> findByOwnerId(Long ownerId);

    List<Ebook> findByTitleOrderByRating(String title);

    List<Ebook> findByTitleLikeOrderByRating(String title);

    List<Ebook> findByYearOrderByRating(Short year);

    @Query("SELECT e FROM Ebook e where e.genreId in ( select l.genreId from LiteraryGenre l where l.name = ?1 )" +
            "ORDER BY e.rating")
    List<Ebook> findByTypeNameOrderByRating(String name);

    @Query("SELECT e FROM Ebook e where e.genreId in ( select l.genreId from LiteraryGenre l where l.name like ?1 )" +
            " ORDER BY e.rating")
    List<Ebook> findByTypeNameLikeOrderByRating(String name);

    List<Ebook> findByGenreIdOrderByRating(Long genreId);

    @Query("SELECT e FROM Ebook e where e.ebookId in ( select a.ebookId from EbookAuthors a where a.authorId = ?1 )" +
            " order by e.rating")
    List<Ebook> findByAuthorIdOrderByRating(Long authorId);

    @Query("SELECT e FROM Ebook e where e.ebookId in ( select a.ebookId from EbookAuthors a where a.authorId in " +
            "( SELECT w.writerId from Writers w where w.name like ?1 ) ) order by e.rating")
    List<Ebook> findByWriterNameLikeOrderByRating(String artistName);

    @Query("SELECT e FROM Ebook e where e.ebookId in ( select a.ebookId from EbookAuthors a where a.authorId in " +
            "( SELECT w.writerId from Writers w where w.name like ?1 and w.surname like ?2 ) )" +
            " order by e.rating")
    List<Ebook> findByWriterNameLikeAndSurnameLikeOrderByRating(String name, String surname);

    @Query("SELECT e FROM Ebook e where e.ebookId in ( select a.ebookId from EbookAuthors a where a.authorId in " +
            "( SELECT w.writerId from Writers w where w.name like ?1 and w.surname like ?2 " +
            " and w.name2 like ?3 ) ) order by e.rating")
    List<Ebook> findByWriterNameLikeAndSurnameLikeAndName2LikeOrderByRating(String name, String surname, String name2);

    List<Ebook> findAllByOrderByRatingDesc();

}
