package pl.polsl.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pl.polsl.model.Users;

/**
 * Created by Mateusz on 25.10.2016.
 */
@org.springframework.stereotype.Repository
@RepositoryRestResource
public interface UsersRepository extends CrudRepository<Users, Long>, UsersRepositoryCustom, QueryDslPredicateExecutor {

    Users findByUserNameAndPassword(String username, String password);

    Users findByUserName(String username);
}
