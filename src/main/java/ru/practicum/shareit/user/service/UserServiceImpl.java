package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto findById(Long id) {
        return userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя не существует по id: " + id)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toListDTO(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDTO) {
        try {
            return userMapper.toDTO(userRepository.save(userMapper.toModel(userDTO)));
        } catch (DataIntegrityViolationException e) {
            throw new ValidEmailException(userDTO.getEmail() + " данная почта уже используется");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDto updateUser(final Long id, UserDto userDTO) {
        userDTO.setId(id);
        UserDto userDtoDB = userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя не существует по id: " + id)));
        if (userDTO.getName() != null) {
            userDtoDB.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            userDtoDB.setEmail(userDTO.getEmail());
        }
        return userMapper.toDTO(userRepository.save(userMapper.toModel(userDtoDB)));
    }
}
