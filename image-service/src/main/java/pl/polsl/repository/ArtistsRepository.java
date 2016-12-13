package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.Artists;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@Transactional
public interface ArtistsRepository extends CrudRepository<Artists, Long> {

    List<Artists> findByNameLike(String name);

    List<Artists> findByName2Like(String name2);

    List<Artists> findBySurnameLike(String surname);

    List<Artists> findByNameLikeAndSurnameLike(String name, String surname);

    List<Artists> findByNameLikeAndName2Like(String name, String name2);

    List<Artists> findByNameLikeAndName2LikeAndSurnameLike(String name, String name2, String surname);
}
