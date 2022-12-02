package ru.yandex.practicum.filmorate.repository.db_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RecordAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdName;
import ru.yandex.practicum.filmorate.repository.FilmGenresRepository;
import ru.yandex.practicum.filmorate.repository.FilmMpaRatingsRepository;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Profile("db_h2")
@Repository
public class FilmDbRepositoryImpl implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenresRepository filmGenresRepository;
    private final FilmMpaRatingsRepository filmMpaRatingsRepository;

    @Autowired
    public FilmDbRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            FilmGenresRepository filmGenresRepository,
            FilmMpaRatingsRepository filmMpaRatingsRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenresRepository = filmGenresRepository;
        this.filmMpaRatingsRepository = filmMpaRatingsRepository;
    }

    private Film filmRowMapper(ResultSet rs, Integer rowNum) throws SQLException {
        int filmId = rs.getInt("film_id");
        Film.FilmBuilder builder = Film.builder()
                .id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"));

        List<IdName> filmMpaRatings = filmMpaRatingsRepository.getFilmMpaRatings(filmId);
        if (!filmMpaRatings.isEmpty()) builder.mpa(filmMpaRatings.get(0));

        List<IdName> filmGenres = filmGenresRepository.getFilmGenres(filmId);
        if (!filmGenres.isEmpty()) {
            builder.genres(filmGenres);
        } else {
            builder.genres(new ArrayList<>());
        }

        return builder.build();
    }

    @Override
    public Film getFilm(Integer filmId) {
        log.info("requesting film with ID={} from the DB", filmId);
        List<Film> films = jdbcTemplate.query("select * from films where film_id = ?", this::filmRowMapper, filmId);
        if (films.isEmpty()) throw new RecordNotFoundException(String.format("Film with ID=%s is not found", filmId));
        return films.get(0);
    }

    @Override
    public Film addFilm(Film film) {
        log.info("adding film [{}] to the DB", film);
        throwIfFilmExists(film);

        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();

        jdbcTemplate.update("insert into films (name, description, release_date, duration) values (?, ?, ?, ?)",
                name, description, releaseDate, duration
        );

        RowMapper<Integer> filmIdRowMapper = (rs, rowNum) -> rs.getInt("film_id");

        List<Integer> filmIds = jdbcTemplate.query("select film_id from films where name = ? and description = ? and release_date = ? and duration = ?",
                filmIdRowMapper, name, description, releaseDate, duration);

        if (filmIds.isEmpty()) throw new RecordNotFoundException("Couldn't get ID from the database");
        film.setId(filmIds.get(0));

        updateFilmMpa(film);
        updateFilmGenres(film);

        return getFilm(film.getId());
    }

    @Override
    public void deleteFilm(Integer filmId) {
        log.info("deleting film with ID={} from the DB", filmId);
        jdbcTemplate.update("delete from films where film_id = ?", filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("updating film [{}] in the DB", film);
        throwIfFilmNotExists(film.getId());

        updateFilmMpa(film);
        updateFilmGenres(film);

        jdbcTemplate.update(
                "update films set name = ?, description = ?, release_date = ?, duration = ? where film_id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );

        return getFilm(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("getting all films from the DB");
        return jdbcTemplate.query("select * from films", this::filmRowMapper);
    }

    private void updateFilmGenres(Film film) {
        Integer filmId = film.getId();
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<IdName> genres = film.getGenres();
            log.info("adding genres({}) to a film with ID({})", genres, filmId);
            filmGenresRepository.addFilmGenres(filmId, film.getGenres());
        } else {
            filmGenresRepository.deleteFilmGenres(filmId);
        }
    }

    private void updateFilmMpa(Film film) {
        if (film.getMpa() != null) {
            Integer mpaId = film.getMpa().getId();
            Integer filmId = film.getId();
            log.info("adding mpa rating with ID({}) to a film with ID({})", mpaId, filmId);
            filmMpaRatingsRepository.addFilmMpaRating(filmId, mpaId);
        }
    }

    private void throwIfFilmExists(Film film) {
        log.info("checking if film[{}] is already existed in the database", film);
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();

        List<Film> films = jdbcTemplate.query(
                "select * from films where name = ? and description = ? and release_date = ? and duration = ?",
                this::filmRowMapper, name, description, releaseDate, duration
        );
        if (!films.isEmpty()) throw new RecordAlreadyExistException(
                String.format("Film [%s] already exists in the database", film)
        );
    }

    private void throwIfFilmNotExists(Integer filmId) {
        log.info("checking if film with ID={} is not found", filmId);
        getFilm(filmId);
    }
}
