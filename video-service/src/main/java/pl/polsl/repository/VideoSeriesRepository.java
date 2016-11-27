package pl.polsl.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import pl.polsl.model.VideoSeries;

import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
public interface VideoSeriesRepository extends CrudRepository<VideoSeries, Long> {

    List<VideoSeries> findByTitleLike(String title);

    @Query("SELECT v from VideoSeries v where v.videoSerieId in " +
            "( SELECT f.videoSerieId from Videos f where f.title like :videoTitle)")
    List<VideoSeries> findByVideoTitleLike(String videoTitle);
}
