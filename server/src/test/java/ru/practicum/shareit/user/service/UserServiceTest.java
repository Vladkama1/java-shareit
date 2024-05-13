package ru.practicum.shareit.user.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.ValidEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        userDto = userDto.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .build();
    }

    @SneakyThrows
    @Test
    void findById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userDto);

        UserDto result = userService.findById(2L);

        assertEquals(userDto, result);
        verify(userMapper).toDTO(user);
    }

    @SneakyThrows
    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.toListDTO(anyList())).thenReturn(Arrays.asList(userDto));

        List<UserDto> result = userService.getAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));
    }

    @SneakyThrows
    @Test
    void saveUser() {
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toModel(any(UserDto.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDto);

        UserDto result = userService.saveUser(userDto);

        assertEquals(userDto, result);
        verify(userRepository).save(user);
    }

    @Test
    void whenSaveUserWithDuplicateEmail_thenValidEmailExceptionIsThrown() {
        UserDto userDTO = new UserDto();
        userDTO.setEmail("test@example.com");
        when(userMapper.toModel(any(UserDto.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ValidEmailException.class, () -> userService.saveUser(userDTO));
    }

    @SneakyThrows
    @Test
    void delete() {
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }

    @SneakyThrows
    @Test
    void updateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any())).thenReturn(userDto);
        when(userMapper.toModel(any(UserDto.class))).thenReturn(user);

        UserDto updatedUser = userService.updateUser(1L, userDto);

        assertNotNull(updatedUser);
        verify(userRepository).save(user);
    }

    @Test
    void userExists_thenNoExceptionThrown() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        Boolean er = userRepository.existsById(userId);
        assertEquals(er, true);
    }
}