package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.EbookAuthors;
import pl.polsl.model.EbookAuthorsPK;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@Transactional
public interface EbookAuthorsRepository extends CrudRepository<EbookAuthors, EbookAuthorsPK> {

    List<EbookAuthors> findByEbookId(Long ebookId);

}
