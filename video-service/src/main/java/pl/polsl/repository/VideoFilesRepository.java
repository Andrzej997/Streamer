package pl.polsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.model.VideoFiles;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@Transactional
public interface VideoFilesRepository extends JpaRepository<VideoFiles, Long> {

    @Query(value = "SELECT nextval('DEFAULTDBSEQ')", nativeQuery = true)
    Long getNextSequenceVal();
}
