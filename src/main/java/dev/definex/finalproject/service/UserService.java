package dev.definex.finalproject.service;

import dev.definex.finalproject.dto.UserDto;
import dev.definex.finalproject.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto save(UserDto userDto);
    List<UserDto> findAll();
    List<UserDto> findByDeleted();
    UserDto update(UserDto userDto) throws UserNotFoundException;
    void delete(UUID uuid) throws UserNotFoundException;
    UserDto findById(UUID uuid) throws UserNotFoundException;

}
