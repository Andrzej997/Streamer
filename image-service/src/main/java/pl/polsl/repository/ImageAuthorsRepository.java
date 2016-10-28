package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import pl.polsl.model.ImageAuthors;
import pl.polsl.model.ImageAuthorsPK;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
public interface ImageAuthorsRepository extends CrudRepository<ImageAuthors, ImageAuthorsPK> {
}
