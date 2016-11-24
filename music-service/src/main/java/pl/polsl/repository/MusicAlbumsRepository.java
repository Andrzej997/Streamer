package pl.polsl.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.MusicAlbums;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
@Transactional
public interface MusicAlbumsRepository extends CrudRepository<MusicAlbums, Long> {

    List<MusicAlbums> findByAlbumTitleLike(String albumTitle);

    @Query(value = "SELECT a FROM MusicAlbums a WHERE  a.albumId IN " +
            "( SELECT s.albumId FROM Songs s WHERE s.title LIKE %?1) ")
    List<MusicAlbums> findBySongTitle(String songTitle);
}
