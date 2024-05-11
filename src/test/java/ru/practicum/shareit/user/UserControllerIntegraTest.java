package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserControllerIntegraTest {

    @Autowired
    private MockMvc mvc;

    private UserDto userDto;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Long userId1 = 1L;
    private final Long userId2 = 3L;
    private final Long invalidId = 999L;

    public void setUp() {

        userDto = UserDto.builder()
                .id(1L)
                .email("RuRu@yandex.ru")
                .name("RuRu")
                .build();
    }

    @Test
    @Order(0)
    @SneakyThrows
    public void testCreateUser_ResulStatusCreated() {
        setUp();
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(userDto)));
    }


    @Test
    @Order(1)
    @SneakyThrows
    public void testCreateUser_WithDuplicateEmail_ResulStatusConflict() {
        setUp();
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(2)
    @SneakyThrows
    public void testGetUserById_ResulStatusOk() {
        setUp();
        mvc.perform(get("/users/{userId}", userId1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDto)))
                .andReturn();
    }

    @Test
    @Order(3)
    @SneakyThrows
    public void testGetUserById_WithInvalidId_ResulStatusNotFound() {
        mvc.perform(get("/users/{invalidUserId}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(4)
    @SneakyThrows
    public void testUpdateUser_OnlyEmail_ResulStatusOk() {
        setUp();
        UserDto userDto1 = UserDto.builder().email("updateRuRu@yandex.ru").build();

        mvc.perform(patch("/users/{userId}", userId1)
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(5)
    @SneakyThrows
    public void testUpdateUser_WithInvalidId_ResulStatusNotFound() {
        setUp();
        userDto = userDto.toBuilder().name("updateRuRu").build();

        mvc.perform(patch("/users/{userId}", invalidId)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(6)
    @SneakyThrows
    public void testCreateUser_WithOldEmail_ResulStatusCreated() {
        setUp();
        userDto = userDto.toBuilder().id(3L).build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(7)
    @SneakyThrows
    public void testUpdateUser_OnlyName_ResulStatusOk() {
        setUp();
        UserDto userDto1 = UserDto.builder().name("updateRuRu").build();

        mvc.perform(patch("/users/{userId}", userId2)
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(8)
    @SneakyThrows
    public void testUpdateUser_WithDuplicateEmail_ResulStatusConflict() {
        setUp();
        userDto = userDto.toBuilder().email("updateRuRu@yandex.ru").build();

        mvc.perform(patch("/users/{userId}", userId2)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }


    @Test
    @Order(9)
    @SneakyThrows
    public void testGetAllUser_ResulStatusOk() {
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }

    @Test
    @Order(10)
    @SneakyThrows
    public void testDeleteUser_ResulStatusOk() {
        mvc.perform(delete("/users/{userId}", userId1))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(11)
    @SneakyThrows
    public void testGetAllUser_AfterDeleteUser_ResulStatusOk() {
        UserDto userOutputDTO2 = UserDto.builder()
                .id(3L)
                .email("RuRu@yandex.ru")
                .name("updateRuRu")
                .build();

        List<UserDto> users = Collections.singletonList(userOutputDTO2);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}