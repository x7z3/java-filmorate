package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.RecordAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class InMemoryUserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

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
        userRepository.addUser(user1); //id 1
        userRepository.addUser(user2); //id 2
        userRepository.addUser(user3); //id 3
    }

    @Test
    void getUser() {
        User user = userRepository.getUser(1);
        assertEquals(user1, user);

        user = userRepository.getUser(2);
        assertEquals(user2, user);

        user = userRepository.getUser(3);
        assertEquals(user3, user);
    }

    @Test
    void addUser() {
        assertEquals(3, userRepository.getAllUsers().size());
    }

    @Test
    void addUserWithSameUserData() {
        User sameUserAsUser1 = User.builder()
                .email("email1@mail.ru")
                .login("login1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertThrows(RecordAlreadyExistException.class, () -> userRepository.addUser(sameUserAsUser1));
        assertEquals(3, userRepository.getAllUsers().size());
    }

    @Test
    void updateFilmWithWrongId() {
        User newUser2 = User.builder()
                .id(10)
                .email("email4@mail.ru")
                .login("login4")
                .birthday(LocalDate.of(1990, 4, 4))
                .build();

        assertThrows(RecordNotFoundException.class, () -> userRepository.updateUser(newUser2));
    }

    @Test
    void deleteUser() {
        userRepository.deleteUser(1);
        assertEquals(2, userRepository.getAllUsers().size());

        userRepository.deleteUser(2);
        assertEquals(1, userRepository.getAllUsers().size());

        userRepository.deleteUser(3);
        assertEquals(0, userRepository.getAllUsers().size());
    }

    @Test
    void updateUser() {
        User newUser2 = User.builder()
                .id(2)
                .email("email4@mail.ru")
                .login("login4")
                .birthday(LocalDate.of(1990, 4, 4))
                .build();

        assertNotEquals(userRepository.getUser(2), newUser2);

        userRepository.updateUser(newUser2);

        assertEquals(newUser2, userRepository.getUser(2));
        assertNotEquals(user2, userRepository.getUser(2));
    }

    @Test
    void updateUserWithWrongId() {
        User newUser2 = User.builder()
                .id(10)
                .email("email4@mail.ru")
                .login("login4")
                .birthday(LocalDate.of(1990, 4, 4))
                .build();

        assertThrows(RecordNotFoundException.class, () -> userRepository.updateUser(newUser2));
    }

    @Test
    void getAllUsers() {
        List<User> all = userRepository.getAllUsers();
        assertNotNull(all);
        assertEquals(3, all.size());
        assertTrue(all.contains(user1));
        assertTrue(all.contains(user2));
        assertTrue(all.contains(user3));
    }
}