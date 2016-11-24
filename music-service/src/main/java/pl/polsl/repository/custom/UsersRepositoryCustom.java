package pl.polsl.repository.custom;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import pl.polsl.model.UsersView;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
@RepositoryRestResource
public interface UsersRepositoryCustom {
    UsersView findUsersByUserId(Long userId);
}
