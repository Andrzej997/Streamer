package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.dto.ChangePasswordDTO;
import pl.polsl.dto.UsersDTO;
import pl.polsl.encryption.ShaEncrypter;
import pl.polsl.mapper.UsersMapper;
import pl.polsl.model.Authority;
import pl.polsl.model.Users;
import pl.polsl.repository.AuthorityRepository;
import pl.polsl.repository.UsersRepository;
import pl.polsl.service.UsersService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Created by Mateusz on 27.10.2016.
 */
@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UsersMapper mapper;

    @Override
    public Boolean userExists(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return false;
        }
        String sha256 = ShaEncrypter.sha256(password);
        Users user = usersRepository.findByUserNameAndPassword(username, sha256);
        return user != null;
    }

    @Override
    public Boolean userExistsByEmail(String email, String password) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return false;
        }
        String sha256 = ShaEncrypter.sha256(password);
        Users user = usersRepository.findByEmailAndPassword(email, sha256);
        return user != null;
    }

    @Override
    public UsersDTO getUserData(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        Users user = usersRepository.findByUserName(username);
        return user != null ? mapper.toUsersDTO(user) : null;
    }

    @Override
    public Boolean registerUser(String username, String password, String email) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(email)) {
            return false;
        }
        Users user = new Users();
        user.setUserName(username);
        user.setPassword(ShaEncrypter.sha256(password));
        user.setEmail(email);
        user.setAccountLocked(false);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 120);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        user.setPasswordExpirationDate(calendar.getTime());
        user.setAccountExpirationDate(calendar.getTime());

        if (usersRepository.findByUserName(username) != null) {
            return false;
        }
        if (usersRepository.findByEmail(email) != null) {
            return false;
        }
        user = usersRepository.save(user);
        if (user != null) {
            Authority authority = new Authority();
            authority.setUserId(user.getUserId());
            authority.setAuthority("ROLE_USER");
            authorityRepository.save(authority);
        }
        return user != null;
    }

    @Override
    public Users updateUserInformations(UsersDTO dto) {
        if (dto == null || dto.getUserId() == null) {
            return null;
        }
        Users user = mapper.toUsers(dto);
        if (user != null) {
            Optional<Users> oldO = usersRepository.findById(user.getUserId());
            if (oldO.isPresent()) {
                Users old = oldO.get();
                user.setPassword(old.getPassword());
                user.setAccountExpirationDate(old.getAccountExpirationDate());
                user.setAccountLocked(old.getAccountLocked());
                user.setPasswordExpirationDate(old.getPasswordExpirationDate());
                return usersRepository.save(user);
            }
        }
        return null;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        Optional<Users> userO = usersRepository.findById(userId);
        if (userId != null && userO.isPresent()) {
            List<Authority> authorityList = authorityRepository.findByUserId(userId);
            if (authorityList != null && authorityList.isEmpty()) {
                authorityList.forEach(authority -> authorityRepository.delete(authority));
            }
            usersRepository.delete(userO.get());
            return true;
        }
        return false;
    }

    @Override
    public Boolean usernameExists(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        Long usernames = usersRepository.countByUserName(username);
        return usernames != null && usernames > 0;
    }

    @Override
    public Boolean checkEmailExists(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        Long emails = usersRepository.countByEmail(email);
        return emails != null && emails > 0;
    }

    @Override
    public Boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        if (changePasswordDTO == null) {
            return false;
        }
        if (userExists(changePasswordDTO.getUsername(), changePasswordDTO.getOldPassword())) {
            if (StringUtils.isEmpty(changePasswordDTO.getUsername())
                    || StringUtils.isEmpty(changePasswordDTO.getNewPassword())) {
                return false;
            }
            Users user = usersRepository.findByUserName(changePasswordDTO.getUsername());
            if (user == null) {
                return false;
            }
            user.setPassword(ShaEncrypter.sha256(changePasswordDTO.getNewPassword()));
            return usersRepository.save(user) != null;
        }
        return false;
    }

    @Override
    public List<UsersDTO> getAllUsers() {
        Iterable<Users> usersIterable = usersRepository.findAll();
        if (usersIterable == null) {
            return null;
        }
        List<UsersDTO> result = new ArrayList<>();
        usersIterable.forEach(user -> result.add(mapper.toUsersDTO(user)));
        return result;
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

    public AuthorityRepository getAuthorityRepository() {
        return authorityRepository;
    }

    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }
}
