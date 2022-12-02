package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {
    User getUser(Integer userId);

    User addUser(User user);

    void deleteUser(Integer userId);

    void updateUser(User user);

    List<User> getAllUsers();
}
