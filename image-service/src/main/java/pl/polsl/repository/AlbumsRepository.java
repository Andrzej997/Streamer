package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.Albums;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@Transactional
public interface AlbumsRepository extends CrudRepository<Albums, Long> {
}
