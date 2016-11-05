package pl.polsl.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.polsl.dto.UsersDTO;
import pl.polsl.mapper.UsersMapper;
import pl.polsl.model.Users;

/**
 * Created by Mateusz on 05.11.2016.
 */
@Component
public class UsersMapperImpl implements UsersMapper {

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public UsersDTO toUsersDTO(Users users) {
        return modelMapper.map(users, UsersDTO.class);
    }

    @Override
    public Users toUsers(UsersDTO users) {
        return modelMapper.map(users, Users.class);
    }
}
