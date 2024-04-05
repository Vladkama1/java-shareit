package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserStorageImpl implements UserStorage {
    private Long globalId = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> userEmails = new HashMap<>();

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User saveUser(User user) {
        Long id = creatId();
        user.setId(id);
        users.put(id, user);
        userEmails.put(user.getEmail(), id);
        return user;
    }

    @Override
    public void delete(Long id) {
        User user = users.remove(id);
        userEmails.remove(user.getEmail());
    }

    @Override
    public User updateUser(User user) {
        userEmails.put(user.getEmail(), user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteEmail(String email) {
        userEmails.remove(email);
    }

    @Override
    public Long existsByEmail(String email) {
        return userEmails.get(email);
    }

    private Long creatId() {
        return ++globalId;
    }
}
