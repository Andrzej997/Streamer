package pl.polsl.mapper;

import pl.polsl.dto.UsersDTO;
import pl.polsl.model.Users;

/**
 * Created by Mateusz on 05.11.2016.
 */

public interface UsersMapper {

    UsersDTO toUsersDTO(Users users);

    Users toUsers(UsersDTO users);

}
