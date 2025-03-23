package dev.definex.finalproject.service.impl;

import dev.definex.finalproject.dto.UserDto;
import dev.definex.finalproject.entity.User;
import dev.definex.finalproject.exception.UserNotFoundException;
import dev.definex.finalproject.mapper.UserMapper;
import dev.definex.finalproject.repository.UserRepository;
import dev.definex.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }



    @Override
    public UserDto save(UserDto userDto) {
        User newUser = userMapper.toUser(userDto);
        return userMapper.toUserDto(userRepository.save(newUser));
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto> userDtoList;
        List<User> userList = userRepository.findAll();
        userDtoList = userList.stream().map(user -> userMapper.toUserDto(user)).toList();
        return userDtoList;
    }

    @Override
    public List<UserDto> findByDeleted() {
        List<UserDto> userDtoList;
        List<User> userList = userRepository.findByIsDeletedFalse();
        userDtoList = userList.stream().map(user -> userMapper.toUserDto(user)).toList();
        return userDtoList;
    }

    @Override
    public UserDto update(UserDto userDto) throws UserNotFoundException {
        User user = userRepository.findById(userDto.getId()).orElseThrow(UserNotFoundException::new);
        user.setUserRole(userDto.getUserRole());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(user.getEmail());
        userRepository.save(user);
        return userMapper.toUserDto(user);

    }

    @Override
    public void delete(UUID uuid) throws UserNotFoundException{
        if(userRepository.existsById(uuid)){
            userRepository.softDeleteById(uuid);
        }
        else
            throw new UserNotFoundException();


    }

    @Override
    public UserDto findById(UUID uuid) throws UserNotFoundException {
        return userMapper.toUserDto(userRepository.findById(uuid).orElseThrow(UserNotFoundException::new));
    }
}
