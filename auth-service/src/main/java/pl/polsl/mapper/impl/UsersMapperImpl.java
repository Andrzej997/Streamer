package pl.polsl.mapper.impl;

import org.springframework.stereotype.Component;
import pl.polsl.dto.UsersDTO;
import pl.polsl.mapper.UsersMapper;
import pl.polsl.model.Users;

/**
 * Created by Mateusz on 05.11.2016.
 */
@Component
public class UsersMapperImpl implements UsersMapper {

    //private ModelMapper modelMapper = new ModelMapper();

    @Override
    public UsersDTO toUsersDTO(Users users) {
        if (users == null) {
            return null;
        }
        UsersDTO result = new UsersDTO();
        result.setUserId(users.getUserId());
        result.setName(users.getName());
        result.setSurname(users.getSurname());
        result.setNationality(users.getNationality());
        result.setEmail(users.getEmail());
        result.setUserName(users.getUserName());
        return result;
    }

    @Override
    public Users toUsers(UsersDTO users) {
        if (users == null) {
            return null;
        }
        Users result = new Users();
        result.setUserId(users.getUserId());
        result.setName(users.getName());
        result.setSurname(users.getSurname());
        result.setNationality(users.getNationality());
        result.setEmail(users.getEmail());
        result.setUserName(users.getUserName());
        return result;
    }
}
