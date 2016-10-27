package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.encryption.ShaEncrypter;
import pl.polsl.model.Users;
import pl.polsl.repository.UsersRepository;
import pl.polsl.service.UsersService;

/**
 * Created by Mateusz on 27.10.2016.
 */
@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Boolean userExists(String username, String password) {
        String sha256 = ShaEncrypter.sha256(password);
        Users user = usersRepository.findByUserNameAndPassword(username, sha256);
        return user != null;
    }

    @Override
    public Boolean userExistsByEmail(String email, String password) {
        String sha256 = ShaEncrypter.sha256(password);
        Users user = usersRepository.findByEmailAndPassword(email, sha256);
        return user != null;
    }

    @Override
    public String getUserData(String username) {
        Users user = usersRepository.findByUserName(username);
        return user.getUserName() + " " + user.getEmail();
    }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
