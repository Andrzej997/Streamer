package pl.polsl.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.polsl.model.Users;
import pl.polsl.repository.UsersRepository;
import pl.polsl.security.model.SecuredUser;
import pl.polsl.security.service.SecurityService;

/**
 * Created by Mateusz on 16.11.2016.
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Boolean hasProtectedAccess() {
        return (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Override
    public SecuredUser getUserByUsername(String username) {
        Users user = usersRepository.findByUserName(username);
        return mapUsersToSecuredUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    @Override
    public UserDetails getUserByEmail(String email, String password) {
        Users user = usersRepository.findByEmailAndPassword(email, password);
        return mapUsersToSecuredUser(user);
    }

    private SecuredUser mapUsersToSecuredUser(Users user) {
        SecuredUser result = null;
        if (user != null) {
            result = new SecuredUser();
            result.setPassword(user.getPassword());
            result.setUsername(user.getUserName());
            result.setEmail(user.getEmail());
            result.setId(user.getUserId());
            String role = null;
            if (user.getUserName().equals("admin")) {
                role = "ROLE_ADMIN";
            } else {
                role = "ROLE_USER";
            }
            result.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(role));
        }
        return result;
    }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
