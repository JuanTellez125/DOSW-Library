package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.controller.dto.request.UserRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.UserResponseDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import edu.eci.dosw.tdd.core.service.UserService;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequestDTO requestDTO;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("1")
                .name("Juan")
                .build();

        requestDTO = UserRequestDTO.builder()
                .name("Juan")
                .build();

        responseDTO = UserResponseDTO.builder()
                .id("1")
                .name("Juan")
                .build();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(userMapper.toEntity(requestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(requestDTO);

        assertNotNull(result);
        assertEquals("Juan", result.getName());
        verify(userValidator).validate(requestDTO.getName());
        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.updateUser("1", requestDTO);

        assertNotNull(result);
        assertEquals("Juan", result.getName());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser("1", requestDTO));

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        userService.deleteUser("1");

        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnDelete() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser("1"));

        verify(userRepository, never()).delete(any());
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.findUserById("1");

        assertNotNull(result);
        assertEquals("Juan", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findUserById("1"));
    }

    @Test
    void shouldFindAllUsersSuccessfully() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(responseDTO);

        List<UserResponseDTO> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getName());
    }

}