package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable Long userId) {
        log.info("Получен запрос GET, на получения пользователя, по userId: {}", userId);
        return userClient.findById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получен запрос GET, на получения всех пользователей.");
        ResponseEntity<Object> userDTOList = userClient.getAllUsers();
        return userDTOList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserDto userDTO) {
        log.info("Получен запрос Post, на обновление данных пользователя.");
        log.info("Добавлен пользователь: {}", userDTO.getName());
        return userClient.saveUser(userDTO);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Получен запрос на обновление пользователя по id: {}", userId);
        ResponseEntity<Object> userDTO1 = userClient.updateUser(userId, userDto);
        return userDTO1;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("Получен запрос DELETE, удаление пользователя по userId: {}", userId);
        userClient.delete(userId);
    }
}
