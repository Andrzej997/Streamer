package pl.polsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.FilmGenres;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@Transactional
public interface FilmGenresRepository extends JpaRepository<FilmGenres, Long> {

    List<FilmGenres> findByNameLike(String name);
}
