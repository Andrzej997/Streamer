package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.MusicArtists;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@Transactional
public interface MusicArtistsRepository extends CrudRepository<MusicArtists, Long> {

    List<MusicArtists> findByNameLike(String name);

    List<MusicArtists> findByName2Like(String name2);

    List<MusicArtists> findBySurnameLike(String surname);

    List<MusicArtists> findByNameLikeAndSurnameLike(String name, String surname);

    List<MusicArtists> findByNameLikeAndName2Like(String name, String name2);

    List<MusicArtists> findByNameLikeAndName2LikeAndSurnameLike(String name, String name2, String surname);

}
