package pl.polsl.service;

/**
 * Created by Mateusz on 27.10.2016.
 */
public interface UsersService {
    Boolean userExists(String username, String password);

    Boolean userExistsByEmail(String email, String password);

    String getUserData(String username);
}
