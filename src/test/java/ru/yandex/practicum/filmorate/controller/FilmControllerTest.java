package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FilmController.class, FilmService.class, FilmRepository.class})
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
                post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson)
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
                post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson)
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
                post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson)
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
                post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson)
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
                post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson)
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
                post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson)
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
                post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson)
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
                post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson)
        ).andExpect(
                status().isOk()
        );

        String updatedFilmJson = objectMapper.writeValueAsString(updatedFilm);

        mockMvc.perform(
                put("/films").contentType(MediaType.APPLICATION_JSON).content(updatedFilmJson)
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
                post("/films").contentType(MediaType.APPLICATION_JSON).content(film1Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(MediaType.APPLICATION_JSON).content(film2Json)
        ).andExpect(
                status().isOk()
        );

        mockMvc.perform(
                post("/films").contentType(MediaType.APPLICATION_JSON).content(film3Json)
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
}