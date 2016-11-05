package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.dto.UsersDTO;
import pl.polsl.encryption.ShaEncrypter;
import pl.polsl.mapper.UsersMapper;
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

    @Autowired
    private UsersMapper mapper;

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
    public UsersDTO getUserData(String username) {
        Users user = usersRepository.findByUserName(username);
        return user != null ? mapper.toUsersDTO(user) : null;
    }

    @Override
    public Boolean registerUser(String username, String password, String email) {
        Users user = new Users();
        user.setUserName(username);
        user.setPassword(ShaEncrypter.sha256(password));
        user.setEmail(email);

        if (usersRepository.findByUserName(username) != null) {
            return false;
        }
        return usersRepository.save(user) != null;
    }

    @Override
    public Users updateUserInformations(UsersDTO dto) {
        if (dto == null) {
            return null;
        }
        Users user = mapper.toUsers(dto);
        if (user != null) {
            Users old = usersRepository.findOne(user.getUserId());
            if (old != null) {
                return usersRepository.save(user);
            }
        }
        return null;
    }

    @Override
    public Boolean deleteUser(UsersDTO dto) {
        if (dto == null) {
            return false;
        }
        Users user = mapper.toUsers(dto);
        if (user != null && user.getUserId() != null) {
            if (usersRepository.findOne(user.getUserId()) != null) {
                usersRepository.delete(user);
                return true;
            }
        }
        return false;
    }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UsersMapper getMapper() {
        return mapper;
    }

    public void setMapper(UsersMapper mapper) {
        this.mapper = mapper;
    }
}
