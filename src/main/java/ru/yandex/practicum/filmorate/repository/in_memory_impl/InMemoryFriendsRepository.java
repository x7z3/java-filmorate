package ru.yandex.practicum.filmorate.repository.in_memory_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendsRepository;

import java.util.*;

@Slf4j
@Profile("in_memory")
@Repository
public class InMemoryFriendsRepository implements FriendsRepository {
    private final Map<User, Friends> friendsBase = new HashMap<>();

    @Override
    public void addFriend(User user, User friend) {
        log.info("adding friendship between users '{}' and '{}'", user, friend);
        friendsBase.putIfAbsent(user, new Friends(user, new HashSet<>()));
        friendsBase.get(user).getUserFriends().add(friend);

        friendsBase.putIfAbsent(friend, new Friends(friend, new HashSet<>()));
        friendsBase.get(friend).getUserFriends().add(user);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        log.info("deleting friendship between users '{}' and '{}'", user, friend);
        shouldExist(user);
        shouldExist(friend);

        Set<User> userFriends = friendsBase.get(user).getUserFriends();
        userFriends.remove(friend);
        if (userFriends.isEmpty()) friendsBase.remove(user);

        Set<User> friendFriends = friendsBase.get(friend).getUserFriends();
        friendFriends.remove(user);
        if (friendFriends.isEmpty()) friendsBase.remove(friend);
    }

    @Override
    public List<User> getFriends(User user) {
        log.info("getting friends list of the user {}", user);
        Friends friends = friendsBase.get(user);
        if (friends == null) {
            log.info("no friends found");
            return new ArrayList<>();
        }

        Set<User> friendsSet = friends.getUserFriends();
        if (friendsSet == null) {
            log.info("no friends found");
            return new ArrayList<>();
        }

        return new ArrayList<>(friendsSet);
    }

    @Override
    public List<User> getCommonFriends(User user1, User user2) {
        log.info("getting mutual friends of users '{}' and '{}'", user1, user2);
        List<User> commonFriends = new ArrayList<>();

        Friends user1Friends = friendsBase.get(user1);
        Friends user2Friends = friendsBase.get(user2);

        if (user1Friends == null || user1Friends.getUserFriends().isEmpty()
                || user2Friends == null || user2Friends.getUserFriends().isEmpty()) {
            log.info("no mutual friends found");
            return commonFriends;
        }

        for (User user1Friend : user1Friends.getUserFriends()) {
            for (User user2Friend : user2Friends.getUserFriends()) {
                if (user1Friend.equals(user2Friend)) {
                    commonFriends.add(user1Friend);
                }
            }
        }

        commonFriends.remove(user1);
        commonFriends.remove(user2);

        return commonFriends;
    }

    private void shouldExist(User user) {
        if (!friendsBase.containsKey(user)) {
            log.warn("user {} is not found", user);
            throw new RecordNotFoundException();
        }
    }
}
