package pl.polsl.repository.custom;

import pl.polsl.model.Users;

/**
 * Created by Mateusz on 29.10.2016.
 */
public interface UsersRepositoryCustom {
    Users findUsersByUserId(Long userId);
}
