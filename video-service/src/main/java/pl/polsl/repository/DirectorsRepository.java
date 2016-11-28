package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.Directors;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
@Transactional
public interface DirectorsRepository extends CrudRepository<Directors, Long> {

    List<Directors> findByNameLike(String name);

    List<Directors> findByName2Like(String name2);

    List<Directors> findBySurnameLike(String surname);

    List<Directors> findByNameLikeAndSurnameLike(String name, String surname);

    List<Directors> findByNameLikeAndName2Like(String name, String name2);

    List<Directors> findByNameLikeAndName2LikeAndSurnameLike(String name, String name2, String surname);
}
