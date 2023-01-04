package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    private final User user1 = User.builder()
            .email("email1@mail.ru")
            .login("login1")
            .birthday(LocalDate.of(1990, 1, 1))
            .build();

    private final User user2 = User.builder()
            .email("email2@mail.ru")
            .login("login2")
            .birthday(LocalDate.of(1990, 2, 2))
            .build();

    private final User user3 = User.builder()
            .email("email3@mail.ru")
            .login("login3")
            .birthday(LocalDate.of(1990, 2, 2))
            .build();

    @BeforeEach
    void setUp() {
        userService.createUser(user1); //id 1
        userService.createUser(user2); //id 2
        userService.createUser(user3); //id 3
    }

    @Test
    void createUser() {
        assertEquals(3, userService.getAllUsers().size());
    }

    @Test
    void updateUser() {
        User newUser2 = User.builder()
                .id(2)
                .email("email4@mail.ru")
                .login("login4")
                .birthday(LocalDate.of(1990, 4, 4))
                .build();

        assertFalse(userService.getAllUsers().contains(newUser2));

        userService.updateUser(newUser2);

        assertTrue(userService.getAllUsers().contains(newUser2));
    }

    @Test
    void updateUserWithWrongId() {
        User newUser2 = User.builder()
                .id(10)
                .email("email4@mail.ru")
                .login("login4")
                .birthday(LocalDate.of(1990, 4, 4))
                .build();

        assertThrows(RecordNotFoundException.class, () -> userService.updateUser(newUser2));
    }

    @Test
    void getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        assertNotNull(allUsers);
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
        assertTrue(allUsers.contains(user3));
    }
}