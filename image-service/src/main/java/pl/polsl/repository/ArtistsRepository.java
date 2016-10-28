package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import pl.polsl.model.Artists;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
public interface ArtistsRepository extends CrudRepository<Artists, Long> {
}
