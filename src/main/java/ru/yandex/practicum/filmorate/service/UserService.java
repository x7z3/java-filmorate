package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    void createUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();

    void addToUserFriendList(Integer id, Integer friendId);

    void deleteUserFriend(Integer id, Integer friendId);

    List<User> getUserCommonFriends(Integer id, Integer otherId);

    List<User> getUserFriends(Integer id);

    User getUser(Integer id);
}
