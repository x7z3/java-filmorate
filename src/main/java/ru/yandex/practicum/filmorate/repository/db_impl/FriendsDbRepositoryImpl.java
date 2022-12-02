package ru.yandex.practicum.filmorate.repository.db_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendsRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;

@Slf4j
@Profile("db_h2")
@Repository
public class FriendsDbRepositoryImpl implements FriendsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    @Autowired
    public FriendsDbRepositoryImpl(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public void addFriend(User user, User friend) {
        log.info("adding a friend with ID={} to a user with ID={}", friend, user);
        jdbcTemplate.update("insert into friends (user_id, friend_id) values (?, ?)", user.getId(), friend.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        log.info("deleting a friend with ID={} from a user with ID={}", friend, user);
        jdbcTemplate.update("delete from friends where user_id = ? and friend_id = ?", user.getId(), friend.getId());
    }

    @Override
    public List<User> getFriends(User user) {
        log.info("getting list of friends from user [{}]", user);
        RowMapper<User> rowMapper = (rs, rowNum) -> {
            Integer friendId = rs.getInt("friend_id");
            return userRepository.getUser(friendId);
        };
        return jdbcTemplate.query("select friend_id from friends where user_id = ?", rowMapper, user.getId());
    }

    @Override
    public List<User> getCommonFriends(User user1, User user2) {
        log.info("getting a list of common users between user with ID={} and user with ID={}", user1.getId(), user2.getId());
        RowMapper<User> rowMapper = (rs, rowNum) -> {
            Integer friendId = rs.getInt("friend_id");
            return userRepository.getUser(friendId);
        };

        return jdbcTemplate.query(
                "select friend_id from friends where friend_id in (select friend_id from friends where user_id = ?) and user_id = ?",
                rowMapper,
                user1.getId(),
                user2.getId());
    }
}
