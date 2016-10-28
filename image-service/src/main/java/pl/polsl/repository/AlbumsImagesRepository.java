package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import pl.polsl.model.AlbumsImages;
import pl.polsl.model.AlbumsImagesPK;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
public interface AlbumsImagesRepository extends CrudRepository<AlbumsImages, AlbumsImagesPK> {
}
