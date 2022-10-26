package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsRepository {
    void addFriend(User userId, User friendId);

    void deleteFriend(User userId, User friendId);

    List<User> getFriends(User user);

    List<User> getCommonFriends(User userId1, User userId2);
}
