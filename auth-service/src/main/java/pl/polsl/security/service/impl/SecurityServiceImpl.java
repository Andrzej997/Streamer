package pl.polsl.security.service.impl;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.polsl.model.Authority;
import pl.polsl.model.Users;
import pl.polsl.repository.AuthorityRepository;
import pl.polsl.repository.UsersRepository;
import pl.polsl.security.service.SecurityService;

import java.util.Collection;
import java.util.List;

/**
 * Created by Mateusz on 16.11.2016.
 */
@Service(value = "securityService")
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Boolean hasProtectedAccess() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return false;
        }
        Boolean result = false;
        for (GrantedAuthority grantedAuthority : authorities) {
            Authority authority = (Authority) grantedAuthority;
            if (authority != null && "ROLE_ADMIN".equals(authority.getAuthority())) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public Users getUserByUsername(String username) {

        Users result = usersRepository.findByUserName(username);
        if (result != null) {
            List<Authority> authorityList = authorityRepository.findByUserId(result.getUserId());
            result.setAuthorities(authorityList);
        }
        return result;
    }

    @Override
    public Users getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmailValidator emailValidator = new EmailValidator();
        if (username != null && emailValidator.isValid(username, null)) {
            return getUserByEmail(username);
        }
        return getUserByUsername(username);
    }

    @Override
    public UserDetails getUserByEmail(String email, String password) {
        return usersRepository.findByEmailAndPassword(email, password);
    }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
