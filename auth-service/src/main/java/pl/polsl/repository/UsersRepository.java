package pl.polsl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.model.Users;

/**
 * Created by Mateusz on 25.10.2016.
 */
@Repository
public interface UsersRepository extends CrudRepository<Users, Long>, UsersRepositoryCustom {

    Users findByUserNameAndPassword(String username, String password);

    Users findByEmailAndPassword(String email, String password);

    Users findByUserName(String userName);

    Long countByUserName(String userName);

    Long countByEmail(String email);
}
