package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.model.Authority;

import java.util.List;

/**
 * Created by Mateusz on 08.04.2017.
 */
@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    List<Authority> findByUserId(Long userId);

}
