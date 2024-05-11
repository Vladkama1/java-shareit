package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.constant.ItemConstants.X_SHARER_USER_ID;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserRepository userRepository;

    private UserDto userDto;
    private UserOutDto userOutDto;

    @BeforeEach
    void onStart() {
        userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("name@mail.ru")
                .build();
    }

    @SneakyThrows
    @Test
    void findByIdTest() {
        long userId = 0L;

        when(userService.findById(userId))
                .thenReturn(userDto);

        String result = mockMvc.perform(get("/users/{userId}", userId)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void getAllUsersTest() {
        when(userService.getAllUsers())
                .thenReturn(List.of(userDto));

        String result = mockMvc.perform(get("/users/")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(userDto)), result);
    }

    @SneakyThrows
    @Test
    void saveUserTest() {
        when(userService.saveUser(userDto))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @SneakyThrows
    @Test
    void deleteTest() {
        mockMvc.perform(delete("/users/{userId}", userDto.getId()))
                .andExpect(status().isNoContent());
    }
}