package ru.yandex.practicum.filmorate.repository.db_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RecordAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Profile("db_h2")
@Repository
public class UserDbRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getInt("user_id"),
            rs.getString("email"),
            rs.getString("login"),
            rs.getString("name"),
            rs.getDate("birthday").toLocalDate()
    );

    @Autowired
    public UserDbRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(Integer userId) {
        log.info("getting user with ID={}", userId);
        List<User> users = jdbcTemplate.query("select * from users where user_id = ?", userRowMapper, userId);
        if (users.isEmpty()) throw new RecordNotFoundException(String.format("User with ID=%s is not found", userId));
        return users.get(0);
    }

    @Override
    public User addUser(User user) {
        log.info("adding user [{}] ", user);
        throwIfUserExists(user);
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        LocalDate birthday = user.getBirthday();
        jdbcTemplate.update(
                "insert into users (email, login, name, birthday) values (?, ?, ?, ?)",
                email, login, name, birthday
        );
        List<User> users = jdbcTemplate.query("select * from users where email = ? and login = ?", userRowMapper,
                email, login);
        if (users.isEmpty()) throw new RecordNotFoundException("Couldn't get last inserted row");
        return users.get(0);
    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("deleting user with ID={}", userId);
        jdbcTemplate.update("delete from users where user_id = ?", userId);
    }

    @Override
    public void updateUser(User user) {
        log.info("updating user [{}]", user);
        throwIfUserNotFound(user.getId());
        jdbcTemplate.update(
                "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
    }

    @Override
    public List<User> getAllUsers() {
        log.info("getting a list of all users");
        List<User> users = jdbcTemplate.query("select * from users", userRowMapper);
        log.info("got user list: {}", users);
        return users;
    }

    private void throwIfUserExists(User user) {
        log.info("checking if user[{}] is already existed in the database", user);
        String email = user.getEmail();
        String login = user.getLogin();

        List<User> users = jdbcTemplate.query("select * from users where email = ? and login = ?", userRowMapper, email, login);
        if (!users.isEmpty()) throw new RecordAlreadyExistException(
                String.format("User with email(%s) and login(%s) already exists in the database", email, login)
        );
    }

    private void throwIfUserNotFound(Integer userId) {
        log.info("checking if user with ID={} is not found", userId);
        getUser(userId);
    }
}
