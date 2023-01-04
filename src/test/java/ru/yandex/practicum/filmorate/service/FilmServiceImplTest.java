package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmServiceImplTest {
    @Autowired
    private FilmService filmService;

    private final Film film1 = Film.builder()
            .name("film 1")
            .description("description 1")
            .releaseDate(LocalDate.of(1900, 1, 1))
            .duration(150)
            .build();

    private final Film film2 = Film.builder()
            .name("film 2")
            .description("description 2")
            .releaseDate(LocalDate.of(1901, 1, 1))
            .duration(160)
            .build();

    private final Film film3 = Film.builder()
            .name("film 3")
            .description("description 3")
            .releaseDate(LocalDate.of(1903, 1, 1))
            .duration(170)
            .build();

    @BeforeEach
    void setUp() {
        filmService.addFilm(film1); //id 1
        filmService.addFilm(film2); //id 2
        filmService.addFilm(film3); //id 3
    }

    @Test
    void addFilm() {
        assertEquals(3, filmService.getAllFilms().size());
    }

    @Test
    void updateFilm() {
        Film newFilm2 = Film.builder()
                .id(2)
                .name("new film 2")
                .description("new description 2")
                .releaseDate(LocalDate.of(1999, 1, 21))
                .duration(80)
                .build();

        assertFalse(filmService.getAllFilms().contains(newFilm2));

        filmService.updateFilm(newFilm2);

        assertTrue(filmService.getAllFilms().contains(newFilm2));
    }

    @Test
    void updateFilmWithWrongId() {
        Film newFilm2 = Film.builder()
                .id(10)
                .name("new film 2")
                .description("new description 2")
                .releaseDate(LocalDate.of(1999, 1, 21))
                .duration(80)
                .build();

        assertThrows(RecordNotFoundException.class, () -> filmService.updateFilm(newFilm2));
    }

    @Test
    void getAllFilms() {
        List<Film> allFilms = filmService.getAllFilms();
        assertNotNull(allFilms);
        assertTrue(allFilms.contains(film1));
        assertTrue(allFilms.contains(film2));
        assertTrue(allFilms.contains(film3));
    }
}