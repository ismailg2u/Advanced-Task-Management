package dev.definex.finalproject.mapper;

import dev.definex.finalproject.dto.UserDto;
import dev.definex.finalproject.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setUserRole(userDto.getUserRole());
        user.setDeleted(false);


        return user;
    }


    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setEmail(user.getEmail());
        userDto.setUserRole(user.getUserRole());

        return userDto;
    }



}
