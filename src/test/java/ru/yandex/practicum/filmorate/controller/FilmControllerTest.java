package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.Configuration;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = Configuration.class)
@Sql({"classpath:schema.sql", "classpath:data.sql"})
@AutoConfigureTestDatabase
@WebMvcTest(controllers = {
        FilmController.class,
        UserController.class,
        FilmService.class,
        UserService.class,
        FriendsRepository.class,
        FilmRepository.class,
        LikesRepository.class,
        UserRepository.class,
        FilmGenresRepository.class,
        FilmMpaRatingsRepository.class,
        GenresRepository.class,
        MpaRatingsRepository.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addFilm() throws Exception {
        Film film = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    void addTheSameFilmTwice() throws Exception {
        Film film = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void addFilmWithEmptyName() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void addFilmWithNullName() throws Exception {
        Film film = Film.builder()
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void addFilmWithWrongReleaseDate() throws Exception {
        Film film = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(150)
                .build();

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void addFilmWithNegativeDuration() throws Exception {
        Film film = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(-100)
                .build();

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void addFilmWithLongDescription() throws Exception {
        Film film = Film.builder()
                .name("film 1")
                .description(
                        // 250 symbols
                        "01234567890123456789012345678901234567890123456789" +
                                "01234567890123456789012345678901234567890123456789" +
                                "01234567890123456789012345678901234567890123456789" +
                                "01234567890123456789012345678901234567890123456789" +
                                "01234567890123456789012345678901234567890123456789"

                )
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    void updateFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        Film updatedFilm = Film.builder()
                .id(1)
                .name("film 1 updated")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().isOk()
        );

        String updatedFilmJson = objectMapper.writeValueAsString(updatedFilm);

        mockMvc.perform(
                put("/films").contentType(APPLICATION_JSON).content(updatedFilmJson)
        ).andExpect(
                status().isOk()
        );

        String filmsJson = mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Film> films = objectMapper.readValue(filmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(films);
        assertEquals(1, films.size());
        assertTrue(films.contains(updatedFilm));
        assertEquals("film 1 updated", films.get(0).getName());
    }

    @Test
    void getAllFilms() throws Exception {
        Film film1 = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        Film film2 = Film.builder()
                .name("film 2")
                .description("description 2")
                .releaseDate(LocalDate.of(1901, 1, 1))
                .duration(160)
                .build();

        Film film3 = Film.builder()
                .name("film 3")
                .description("description 3")
                .releaseDate(LocalDate.of(1903, 1, 1))
                .duration(170)
                .build();

        String film1Json = objectMapper.writeValueAsString(film1);
        String film2Json = objectMapper.writeValueAsString(film2);
        String film3Json = objectMapper.writeValueAsString(film3);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film2Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film3Json)
        ).andExpect(
                status().isOk()
        );

        String filmsJson = mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Film> films = objectMapper.readValue(filmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(films);
        assertEquals(3, films.size());
        assertTrue(films.contains(film1));
        assertTrue(films.contains(film2));
        assertTrue(films.contains(film3));
    }

    @Test
    void getFilm() throws Exception {
        Film film1 = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        Film film2 = Film.builder()
                .name("film 2")
                .description("description 2")
                .releaseDate(LocalDate.of(1901, 1, 1))
                .duration(160)
                .build();

        Film film3 = Film.builder()
                .name("film 3")
                .description("description 3")
                .releaseDate(LocalDate.of(1903, 1, 1))
                .duration(170)
                .build();

        String film1Json = objectMapper.writeValueAsString(film1);
        String film2Json = objectMapper.writeValueAsString(film2);
        String film3Json = objectMapper.writeValueAsString(film3);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film2Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film3Json)
        ).andExpect(
                status().isOk()
        );

        String filmJson = mockMvc.perform(get("/films/2"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Film returnedFilm2 = objectMapper.readValue(filmJson, Film.class);

        assertEquals(film2, returnedFilm2);
    }

    @Test
    void getFilmWrongId() throws Exception {
        Film film1 = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        Film film2 = Film.builder()
                .name("film 2")
                .description("description 2")
                .releaseDate(LocalDate.of(1901, 1, 1))
                .duration(160)
                .build();

        Film film3 = Film.builder()
                .name("film 3")
                .description("description 3")
                .releaseDate(LocalDate.of(1903, 1, 1))
                .duration(170)
                .build();

        String film1Json = objectMapper.writeValueAsString(film1);
        String film2Json = objectMapper.writeValueAsString(film2);
        String film3Json = objectMapper.writeValueAsString(film3);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film2Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film3Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(get("/films/9999"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(get("/films/-1"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(get("/films/0"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void likeFilm() throws Exception {
        Film film1 = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        Film film2 = Film.builder()
                .name("film 2")
                .description("description 2")
                .releaseDate(LocalDate.of(1901, 1, 1))
                .duration(160)
                .build();

        Film film3 = Film.builder()
                .name("film 3")
                .description("description 3")
                .releaseDate(LocalDate.of(1903, 1, 1))
                .duration(170)
                .build();

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

        String film1Json = objectMapper.writeValueAsString(film1);
        String film2Json = objectMapper.writeValueAsString(film2);
        String film3Json = objectMapper.writeValueAsString(film3);

        String user1Json = objectMapper.writeValueAsString(user1);
        String user2Json = objectMapper.writeValueAsString(user2);
        String user3Json = objectMapper.writeValueAsString(user3);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film2Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film3Json)
        ).andExpect(
                status().isOk()
        );

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

        String popularFilmsJson = mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<Film> popularFilms = objectMapper.readValue(popularFilmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(popularFilms);
        assertEquals(3, popularFilms.size());

        mockMvc.perform(put("/films/3/like/1")).andExpect(status().isOk());
        mockMvc.perform(put("/films/3/like/2")).andExpect(status().isOk());
        mockMvc.perform(put("/films/3/like/3")).andExpect(status().isOk());
        mockMvc.perform(put("/films/1/like/1")).andExpect(status().isOk());
        mockMvc.perform(put("/films/1/like/3")).andExpect(status().isOk());
        mockMvc.perform(put("/films/2/like/2")).andExpect(status().isOk());

        popularFilmsJson = mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        popularFilms = objectMapper.readValue(popularFilmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(popularFilms);
        assertEquals(3, popularFilms.size());
        assertTrue(popularFilms.contains(film1));
        assertTrue(popularFilms.contains(film2));
        assertTrue(popularFilms.contains(film3));
    }

    @Test
    void likeWrongFilm() throws Exception {
        Film film1 = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        User user1 = User.builder()
                .email("email1@mail.ru")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        String film1Json = objectMapper.writeValueAsString(film1);
        String user1Json = objectMapper.writeValueAsString(user1);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/users").contentType(APPLICATION_JSON).content(user1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(put("/films/1/like/1")).andExpect(status().isOk());

        String popularFilmsJson = mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<Film> popularFilms = objectMapper.readValue(popularFilmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(popularFilms);
        assertEquals(1, popularFilms.size());
        assertTrue(popularFilms.contains(film1));

        mockMvc.perform(put("/films/9999/like/1")).andExpect(status().is4xxClientError());
        mockMvc.perform(put("/films/-1/like/1")).andExpect(status().is4xxClientError());
        mockMvc.perform(put("/films/0/like/1")).andExpect(status().is4xxClientError());

        popularFilmsJson = mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        popularFilms = objectMapper.readValue(popularFilmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(popularFilms);
        assertEquals(1, popularFilms.size());
        assertTrue(popularFilms.contains(film1));
    }

    @Test
    void removeLike() throws Exception {
        Film film1 = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        Film film2 = Film.builder()
                .name("film 2")
                .description("description 2")
                .releaseDate(LocalDate.of(1901, 1, 1))
                .duration(160)
                .build();

        Film film3 = Film.builder()
                .name("film 3")
                .description("description 3")
                .releaseDate(LocalDate.of(1903, 1, 1))
                .duration(170)
                .build();

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

        String film1Json = objectMapper.writeValueAsString(film1);
        String film2Json = objectMapper.writeValueAsString(film2);
        String film3Json = objectMapper.writeValueAsString(film3);

        String user1Json = objectMapper.writeValueAsString(user1);
        String user2Json = objectMapper.writeValueAsString(user2);
        String user3Json = objectMapper.writeValueAsString(user3);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film2Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film3Json)
        ).andExpect(
                status().isOk()
        );

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

        // Adding likes
        mockMvc.perform(put("/films/1/like/3")).andExpect(status().isOk());
        mockMvc.perform(put("/films/2/like/2")).andExpect(status().isOk());
        mockMvc.perform(put("/films/3/like/1")).andExpect(status().isOk());

        String popularFilmsJson = mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<Film> popularFilms = objectMapper.readValue(popularFilmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(popularFilms);
        assertEquals(3, popularFilms.size());

        // Removing likes
        mockMvc.perform(delete("/films/1/like/3")).andExpect(status().isOk());
        mockMvc.perform(delete("/films/2/like/2")).andExpect(status().isOk());
        mockMvc.perform(delete("/films/3/like/1")).andExpect(status().isOk());

        popularFilmsJson = mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        popularFilms = objectMapper.readValue(popularFilmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(popularFilms);
        assertEquals(3, popularFilms.size());
    }

    @Test
    void removeWrongLike() throws Exception {
        Film film1 = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        Film film2 = Film.builder()
                .name("film 2")
                .description("description 2")
                .releaseDate(LocalDate.of(1901, 1, 1))
                .duration(160)
                .build();

        Film film3 = Film.builder()
                .name("film 3")
                .description("description 3")
                .releaseDate(LocalDate.of(1903, 1, 1))
                .duration(170)
                .build();

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

        String film1Json = objectMapper.writeValueAsString(film1);
        String film2Json = objectMapper.writeValueAsString(film2);
        String film3Json = objectMapper.writeValueAsString(film3);

        String user1Json = objectMapper.writeValueAsString(user1);
        String user2Json = objectMapper.writeValueAsString(user2);
        String user3Json = objectMapper.writeValueAsString(user3);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film2Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film3Json)
        ).andExpect(
                status().isOk()
        );

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

        // Adding likes
        mockMvc.perform(put("/films/1/like/3")).andExpect(status().isOk());
        mockMvc.perform(put("/films/2/like/2")).andExpect(status().isOk());
        mockMvc.perform(put("/films/3/like/1")).andExpect(status().isOk());

        String popularFilmsJson = mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<Film> popularFilms = objectMapper.readValue(popularFilmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(popularFilms);
        assertEquals(3, popularFilms.size());

        // Removing likes
        mockMvc.perform(delete("/films/1/like/999")).andExpect(status().is4xxClientError());
        mockMvc.perform(delete("/films/2/like/-1")).andExpect(status().is4xxClientError());
        mockMvc.perform(delete("/films/3/like/0")).andExpect(status().is4xxClientError());

        popularFilmsJson = mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        popularFilms = objectMapper.readValue(popularFilmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(popularFilms);
        assertEquals(3, popularFilms.size());
    }

    @Test
    void getPopularFilms() throws Exception {
        Film film1 = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        Film film2 = Film.builder()
                .name("film 2")
                .description("description 2")
                .releaseDate(LocalDate.of(1901, 1, 1))
                .duration(160)
                .build();

        Film film3 = Film.builder()
                .name("film 3")
                .description("description 3")
                .releaseDate(LocalDate.of(1903, 1, 1))
                .duration(170)
                .build();

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

        String film1Json = objectMapper.writeValueAsString(film1);
        String film2Json = objectMapper.writeValueAsString(film2);
        String film3Json = objectMapper.writeValueAsString(film3);

        String user1Json = objectMapper.writeValueAsString(user1);
        String user2Json = objectMapper.writeValueAsString(user2);
        String user3Json = objectMapper.writeValueAsString(user3);

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film2Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(APPLICATION_JSON).content(film3Json)
        ).andExpect(
                status().isOk()
        );

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

        mockMvc.perform(put("/films/3/like/1")).andExpect(status().isOk()); // film 3 is the most popular
        mockMvc.perform(put("/films/3/like/2")).andExpect(status().isOk());
        mockMvc.perform(put("/films/3/like/3")).andExpect(status().isOk());

        mockMvc.perform(put("/films/1/like/1")).andExpect(status().isOk()); // film 2 takes the second place of the popularity
        mockMvc.perform(put("/films/1/like/3")).andExpect(status().isOk());

        mockMvc.perform(put("/films/2/like/2")).andExpect(status().isOk()); // film 2 the less popular of the all

        String popularFilmsJson = mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<Film> popularFilms = objectMapper.readValue(popularFilmsJson, new TypeReference<List<Film>>() {
        });

        assertNotNull(popularFilms);
        assertEquals(3, popularFilms.size());

        assertEquals(3, popularFilms.size());
        assertEquals(film1, popularFilms.get(1)); // should be in the 2 place in the list
        assertEquals(film2, popularFilms.get(2)); // should be in the 3 place in the list
        assertEquals(film3, popularFilms.get(0)); // should be in the 1 place in the list
    }
}