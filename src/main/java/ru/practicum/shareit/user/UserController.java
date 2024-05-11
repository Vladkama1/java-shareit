package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable Long userId) {
        log.info("Получен запрос GET, на получения пользователя, по userId: {}", userId);
        return userService.findById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос GET, на получения всех пользователей.");
        List<UserDto> userDTOList = userService.getAllUsers();
        log.info("Получен ответ, список пользователей, размер: {}", userDTOList.size());
        return userDTOList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Valid @RequestBody UserDto userDTO) {
        log.info("Получен запрос Post, на обновление данных пользователя.");
        log.info("Добавлен пользователь: {}", userDTO.getName());
        return userService.saveUser(userDTO);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Получен запрос на обновление пользователя по id: {}", userId);
        UserDto userDTO1 = userService.updateUser(userId, userDto);
        log.info("Обновлён пользователь: {}", userDTO1.getEmail());
        return userDTO1;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("Получен запрос DELETE, удаление пользователя по userId: {}", userId);
        userService.delete(userId);
    }
}
