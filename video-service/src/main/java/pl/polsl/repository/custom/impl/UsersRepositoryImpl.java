package pl.polsl.repository.custom.impl;

import org.springframework.stereotype.Repository;
import pl.polsl.model.UsersView;
import pl.polsl.repository.custom.UsersRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Mateusz on 29.10.2016.
 */
@Repository
public class UsersRepositoryImpl implements UsersRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UsersView findUsersByUserId(Long userId) {
        Query query = entityManager.createNativeQuery("select u.* from USERS where user_id = :userId", UsersView.class);
        query.setParameter("userId", userId);
        List<UsersView> resultList = (List<UsersView>) query.getResultList();
        return resultList != null && !resultList.isEmpty() ? resultList.get(0) : null;
    }


    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
