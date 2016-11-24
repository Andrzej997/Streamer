package pl.polsl.repository.custom;

import pl.polsl.model.UsersView;

/**
 * Created by Mateusz on 29.10.2016.
 */
public interface UsersRepositoryCustom {
    UsersView findUsersByUserId(Long userId);
}
