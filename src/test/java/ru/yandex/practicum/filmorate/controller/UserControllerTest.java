package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class, UserService.class, UserRepository.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser() throws Exception {
        User user = User.builder()
                .email("email1@mail.ru")
                .login("login1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(userJson)
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    void createUserWithoutLogin() throws Exception {
        User user = User.builder()
                .email("email1@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(userJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void createUserWithSpaceInTheLogin() throws Exception {
        User user = User.builder()
                .login("some login")
                .email("email1@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(userJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void createTwoUsersWithTheSameData() throws Exception {
        User user = User.builder()
                .email("email1@mail.ru")
                .login("login1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(userJson)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(userJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void createUserWithBirthdateInTheFuture() throws Exception {
        User user = User.builder()
                .email("email1@mail.ru")
                .login("login1")
                .birthday(LocalDate.now().plusDays(10))
                .build();

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(userJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void createUserWithEmptyEmail() throws Exception {
        User user = User.builder()
                .email("")
                .login("login1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(userJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void createUserWithEmptyName() throws Exception {
        User user = User.builder()
                .email("email1@mail.ru")
                .login("login1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(userJson)
        ).andExpect(
                status().isOk()
        );

        String usersJson = mockMvc.perform(get("/users")).andReturn().getResponse().getContentAsString();
        List<User> users = objectMapper.readValue(usersJson, new TypeReference<List<User>>() {
        });

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("login1", users.get(0).getName());
    }

    @Test
    void updateUser() throws Exception {
        User user = User.builder()
                .id(1)
                .email("email1@mail.ru")
                .login("login1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User updatedUser = User.builder()
                .id(1)
                .email("email1@mail.ru")
                .name("new name")
                .login("login1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(userJson)
        ).andExpect(
                status().isOk()
        );

        String updatedUserJson = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(
                put("/users").contentType(APPLICATION_JSON).content(updatedUserJson)
        ).andExpect(
                status().isOk()
        );

        String usersJson = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<User> users = objectMapper.readValue(usersJson, new TypeReference<List<User>>() {
        });

        assertNotNull(users);
        assertEquals(1, users.size());
        assertTrue(users.contains(updatedUser));
        assertEquals("new name", users.get(0).getName());
    }

    @Test
    void getUsers() throws Exception {
        User user1 = User.builder()
                .email("email1@mail.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User user2 = User.builder()
                .email("email2@mail.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(1990, 2, 2))
                .build();

        User user3 = User.builder()
                .email("email3@mail.ru")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.of(1990, 3, 3))
                .build();

        String user1Json = objectMapper.writeValueAsString(user1);
        String user2Json = objectMapper.writeValueAsString(user2);
        String user3Json = objectMapper.writeValueAsString(user3);

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(user1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(user2Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(user3Json)
        ).andExpect(
                status().isOk()
        );

        String usersJson = mockMvc.perform(get("/users")).andReturn().getResponse().getContentAsString();
        List<User> users = objectMapper.readValue(usersJson, new TypeReference<List<User>>() {
        });

        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertTrue(users.contains(user3));
    }
}