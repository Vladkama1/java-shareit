package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto findById(Long id) {
        return userMapper.toDTO(userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя не существует по id: " + id)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toListDTO(userStorage.getAllUsers());
    }

    @Override
    public UserDto saveUser(UserDto userDTO) {
        checkUserIdByEmail(userDTO);
        return userMapper.toDTO(userStorage.saveUser(userMapper.toModel(userDTO)));
    }

    @Override
    public void delete(Long id) {
        userStorage.delete(id);
    }

    @Override
    public UserDto updateUser(final Long id, UserDto userDTO) {
        userDTO.setId(id);
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователя не существует по id: " + id));
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            checkUserIdByEmail(userDTO);
            userStorage.deleteEmail(user.getEmail());
            user.setEmail(userDTO.getEmail());
        }
        userStorage.updateUser(user);
        return userMapper.toDTO(user);
    }

    @Override
    public boolean isExistUser(Long id) {
        boolean exists = userStorage.existsById(id);
        return exists;
    }
    private void checkUserIdByEmail(UserDto userDto) {
        final Long userEmail = userStorage.existsByEmail(userDto.getEmail());
        if (userEmail != null) {
            if (!userEmail.equals(userDto.getId())) {
                log.error("Email отсутствует в базе: {}", userDto.getEmail());
                throw new ValidEmailException("Данный email отсутствует в базе");
            }
        }
    }
}
