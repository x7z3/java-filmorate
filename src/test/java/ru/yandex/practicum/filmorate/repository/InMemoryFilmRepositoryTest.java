package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.RecordAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class InMemoryFilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

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
        filmRepository.addFilm(film1); //id 1
        filmRepository.addFilm(film2); //id 2
        filmRepository.addFilm(film3); //id 3
    }

    @Test
    void getFilm() {
        Film film = filmRepository.getFilm(1);
        assertEquals(film1, film);

        film = filmRepository.getFilm(2);
        assertEquals(film2, film);

        film = filmRepository.getFilm(3);
        assertEquals(film3, film);
    }

    @Test
    void addFilm() {
        assertEquals(3, filmRepository.getAllFilms().size());
    }

    @Test
    void addFilmWithSameFilmData() {
        Film filmWithId1 = Film.builder()
                .name("film 1")
                .description("description 1")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(150)
                .build();

        assertThrows(RecordAlreadyExistException.class, () -> filmRepository.addFilm(filmWithId1));
        assertEquals(3, filmRepository.getAllFilms().size());
    }

    @Test
    void deleteFilm() {
        filmRepository.deleteFilm(1);
        assertEquals(2, filmRepository.getAllFilms().size());

        filmRepository.deleteFilm(2);
        assertEquals(1, filmRepository.getAllFilms().size());

        filmRepository.deleteFilm(3);
        assertEquals(0, filmRepository.getAllFilms().size());
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

        assertNotEquals(filmRepository.getFilm(2), newFilm2);

        filmRepository.updateFilm(newFilm2);

        assertEquals(filmRepository.getFilm(2), newFilm2);
        assertNotEquals(filmRepository.getFilm(2), film2);
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

        assertThrows(RecordNotFoundException.class, () -> filmRepository.updateFilm(newFilm2));
    }

    @Test
    void getAllFilms() {
        List<Film> all = filmRepository.getAllFilms();
        assertNotNull(all);
        assertEquals(3, all.size());
        assertTrue(all.contains(film1));
        assertTrue(all.contains(film2));
        assertTrue(all.contains(film3));
    }
}