package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.ImageAuthors;
import pl.polsl.model.ImageAuthorsPK;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@Transactional
public interface ImageAuthorsRepository extends CrudRepository<ImageAuthors, ImageAuthorsPK> {

    List<ImageAuthors> findByImageId(Long imageId);
}
