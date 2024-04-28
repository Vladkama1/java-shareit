package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findById(Long id);

    List<UserDto> getAllUsers();

    UserDto saveUser(UserDto userDTO);

    void delete(Long id);

    boolean isExistUser(Long id);

    UserDto updateUser(Long id, UserDto userDTO);
}
