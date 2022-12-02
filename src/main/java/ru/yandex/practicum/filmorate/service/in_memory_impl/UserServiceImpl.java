package ru.yandex.practicum.filmorate.service.in_memory_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendsRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Profile("in_memory")
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            @Qualifier("inMemoryFriendsRepository") FriendsRepository friendsRepository
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
        userRepository.updateUser(user);
    }

    @Override
    public User getUser(Integer id) {
        return userRepository.getUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void addToUserFriendList(Integer userId, Integer friendId) {
        User user = userRepository.getUser(userId);
        User friend = userRepository.getUser(friendId);

        if (user.equals(friend)) throw new ValidationException("You cannot add user itself to its own friends list");

        friendsRepository.addFriend(user, friend);
    }

    @Override
    public void deleteUserFriend(Integer userId, Integer friendId) {
        User user = userRepository.getUser(userId);
        User friend = userRepository.getUser(friendId);
        friendsRepository.deleteFriend(user, friend);
    }

    @Override
    public List<User> getUserCommonFriends(Integer userId, Integer otherUserId) {
        User user = userRepository.getUser(userId);
        User otherUser = userRepository.getUser(otherUserId);
        return friendsRepository.getCommonFriends(user, otherUser);
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        User user = userRepository.getUser(userId);
        return friendsRepository.getFriends(user);
    }
}
