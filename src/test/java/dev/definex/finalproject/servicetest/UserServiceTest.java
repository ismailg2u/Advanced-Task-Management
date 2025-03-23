package dev.definex.finalproject.servicetest;

import dev.definex.finalproject.dto.UserDto;
import dev.definex.finalproject.entity.User;
import dev.definex.finalproject.enums.UserRole;
import dev.definex.finalproject.exception.UserNotFoundException;
import dev.definex.finalproject.mapper.UserMapper;
import dev.definex.finalproject.repository.UserRepository;
import dev.definex.finalproject.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserDto userDto;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDto = new UserDto(UUID.randomUUID(), "John", "Doe", "john.doe@example.com", UserRole.Team_Member);
        user = new User(userDto.getId(), userDto.getName(), userDto.getSurname(), userDto.getEmail(), "encoded_password", userDto.getUserRole(), false, null, null);
    }

    @Test
    public void testSaveUser() {
        when(userMapper.toUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        UserDto savedUser = userService.save(userDto);
        assertNotNull(savedUser);
        assertEquals(userDto.getName(), savedUser.getName());
        verify(userRepository, times(1)).save(user);
    }
    @Test
    public void testFindAllUsers() {
        List<User> userList = List.of(user);
        List<UserDto> userDtoList = List.of(userDto);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        List<UserDto> result = userService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDto.getName(), result.get(0).getName());
    }
    @Test
    public void testFindByDeleted() {
        List<User> userList = List.of(user);
        List<UserDto> userDtoList = List.of(userDto);

        when(userRepository.findByIsDeletedFalse()).thenReturn(userList);
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        List<UserDto> result = userService.findByDeleted();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDto.getName(), result.get(0).getName());
    }
    @Test
    public void testUpdateUser() throws UserNotFoundException {

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);


        UserDto updatedUser = userService.update(userDto);


        assertNotNull(updatedUser);
        assertEquals(userDto.getName(), updatedUser.getName());
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindById() throws UserNotFoundException {
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        UserDto result = userService.findById(userDto.getId());
        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.update(userDto);
        });
    }

    @Test
    public void testFindByIdUserNotFound() {
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(userDto.getId());
        });
    }

    @Test
    public void testDeleteUserNotFound()  {
        when(userRepository.existsById(userDto.getId())).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> {
            userService.delete(userDto.getId());
        });
    }
    @Test
    public void testDelete() throws UserNotFoundException {
        when(userRepository.existsById(userDto.getId())).thenReturn(true);
        userService.delete(userDto.getId());
        verify(userRepository).softDeleteById(userDto.getId());
    }










}
