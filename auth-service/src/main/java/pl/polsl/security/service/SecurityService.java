package pl.polsl.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.polsl.security.model.SecuredUser;

/**
 * Created by Mateusz on 16.11.2016.
 */
public interface SecurityService extends UserDetailsService {

    Boolean hasProtectedAccess();

    SecuredUser getUserByUsername(String username);

    SecuredUser getUserByEmail(String email);

    UserDetails getUserByEmail(String email, String password);
}
