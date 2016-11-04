package pl.polsl.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.polsl.model.Users;
import pl.polsl.model.base.BaseEntity;
import pl.polsl.repository.custom.UsersRepositoryCustom;

/**
 * Created by Mateusz on 04.11.2016.
 */
@Aspect
@Component
public class UsersAspect {

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Pointcut(value = "execution(* pl.polsl.repository.*.*(..))")
    public void getEntity() {
    }

    @Pointcut(value = "@annotation(requestMapping)")
    public void setEntity(RequestMapping requestMapping) {
    }

    @AfterReturning(pointcut = "getEntity()", returning = "object")
    public void injectUsers(Object object) {
        if (object instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) object;
            if (!entity.containsUsers()) {
                return;
            }
            Users user = usersRepository.findUsersByUserId(entity.getUsersId());
            if (user != null) {
                entity.injectUser(user);
            }
        }
    }

    public UsersRepositoryCustom getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepositoryCustom usersRepository) {
        this.usersRepository = usersRepository;
    }
}
