package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void createUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();
}
