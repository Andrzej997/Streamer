package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.VideosAuthors;
import pl.polsl.model.VideosAuthorsPK;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
@Transactional
public interface VideoAuthorsRepository extends CrudRepository<VideosAuthors, VideosAuthorsPK> {

    List<VideosAuthors> findByVideoId(Long videoId);
}
