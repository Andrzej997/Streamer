package pl.polsl.service;

import pl.polsl.dto.UsersDTO;
import pl.polsl.model.Users;

/**
 * Created by Mateusz on 27.10.2016.
 */
public interface UsersService {
    Boolean userExists(String username, String password);

    Boolean userExistsByEmail(String email, String password);

    UsersDTO getUserData(String username);

    Boolean registerUser(String username, String password, String email);

    Users updateUserInformations(UsersDTO dto);

    Boolean deleteUser(UsersDTO dto);

    Boolean usernameExists(String username);

    Boolean checkEmailExists(String email);
}
