package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import pl.polsl.model.PlaylistsVideos;
import pl.polsl.model.PlaylistsVideosPK;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
public interface PlaylistsVideosRepository extends CrudRepository<PlaylistsVideos, PlaylistsVideosPK> {
}
