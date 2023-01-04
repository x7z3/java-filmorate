package ru.yandex.practicum.filmorate.service.db_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendsRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@Profile("db_h2")
@Service
public class UserDbServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    @Autowired
    public UserDbServiceImpl(
            UserRepository userRepository,
            FriendsRepository friendsRepository

    ) {
        this.userRepository = userRepository;
        this.friendsRepository = friendsRepository;
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("user's name is blank/empty");
            user.setName(user.getLogin());
        }
        return userRepository.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        throwIfNotExists(user.getId());
        userRepository.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void addToUserFriendList(Integer id, Integer friendId) {
        User user = userRepository.getUser(id);
        User friend = userRepository.getUser(friendId);
        friendsRepository.addFriend(user, friend);
    }

    @Override
    public void deleteUserFriend(Integer id, Integer friendId) {
        User user = userRepository.getUser(id);
        User friend = userRepository.getUser(friendId);
        friendsRepository.deleteFriend(user, friend);
    }

    @Override
    public List<User> getUserCommonFriends(Integer id, Integer otherId) {
        User user = userRepository.getUser(id);
        User otherUser = userRepository.getUser(otherId);
        return friendsRepository.getCommonFriends(user, otherUser);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        User user = userRepository.getUser(id);
        return friendsRepository.getFriends(user);
    }

    @Override
    public User getUser(Integer id) {
        return userRepository.getUser(id);
    }

    private void throwIfNotExists(Integer userId) {
        User user = getUser(userId);
        if (user == null) throw new RecordNotFoundException(String.format("User with ID=%s is not found", userId));
    }
}
