package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    void setup() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertNull(userMapper.toModel(null));
        assertNull(userMapper.toDTO(null));
        assertNull(userMapper.toListModels(null));
        assertNull(userMapper.toListDTO(null));
    }

    @Test
    void shouldMapUserToUserDTO() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        UserDto userDTO = userMapper.toDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void shouldMapUserDTOToUser() {
        UserDto userDTO = UserDto.builder()
                .id(1L)
                .email("jane.doe@example.com")
                .name("Jane Doe")
                .build();

        User user = userMapper.toModel(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    void shouldMapListUserToListUserDTO() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");
        user2.setEmail("jane.doe@example.com");

        List<User> users = Arrays.asList(user1, user2);

        List<UserDto> userDTOS = userMapper.toListDTO(users);

        assertNotNull(userDTOS);
        assertEquals(2, userDTOS.size());
        assertEquals(users.get(0).getId(), userDTOS.get(0).getId());
        assertEquals(users.get(1).getId(), userDTOS.get(1).getId());
    }

    @Test
    void shouldMapListUserDTOToListUser() {
        List<UserDto> userDTOS = Arrays.asList(
                new UserDto(1L, "John Doe", "john.doe@example.com"),
                new UserDto(2L, "Jane Doe", "jane.doe@example.com")
        );

        List<User> users = userMapper.toListModels(userDTOS);

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(userDTOS.get(0).getId(), users.get(0).getId());
        assertEquals(userDTOS.get(1).getId(), users.get(1).getId());
    }
}