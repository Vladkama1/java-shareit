package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> findById(Long id);

    List<User> getAllUsers();

    boolean existsById(Long id);

    void deleteEmail(String email);

    User saveUser(User user);

    void delete(Long id);

    User updateUser(User user);

    Long existsByEmail(String email);
}
