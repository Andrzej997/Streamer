package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.Writers;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
@Transactional
public interface WritersRepository extends CrudRepository<Writers, Long> {

    List<Writers> findByNameLike(String name);

    List<Writers> findByName2Like(String name2);

    List<Writers> findBySurnameLike(String surname);

    List<Writers> findByNameLikeAndSurnameLike(String name, String surname);

    List<Writers> findByNameLikeAndName2Like(String name, String name2);

    List<Writers> findByNameLikeAndName2LikeAndSurnameLike(String name, String name2, String surname);

}
