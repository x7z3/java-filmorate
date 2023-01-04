package ru.yandex.practicum.filmorate.repository.db_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.LikesRepository;

import java.util.List;

@Slf4j
@Profile("db_h2")
@Repository
public class LikesDbRepositoryImpl implements LikesRepository {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRepository filmRepository;

    @Autowired
    public LikesDbRepositoryImpl(JdbcTemplate jdbcTemplate, FilmRepository filmRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmRepository = filmRepository;
    }

    @Override
    public void addLike(Film film, User user) {
        log.info("adding like to film [{}] from user [{}]", film, user);
        jdbcTemplate.update("insert into likes (film_id, user_id) values (?, ?)", film.getId(), user.getId());
    }

    @Override
    public void removeLike(Film film, User user) {
        log.info("deleting like from film [{}] of user [{}]", film, user);
        jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", film.getId(), user.getId());
    }

    @Override
    public List<Film> getPopular(Integer count) {
        log.info("getting list of popular films with count of items = {}", count);
        RowMapper<Film> rowMapper = (rs, rowNum) -> {
            Integer filmId = rs.getInt("film_id");
            return filmRepository.getFilm(filmId);
        };
        return jdbcTemplate.query(
                "select film_id, count(user_id) from likes group by film_id order by count(user_id) desc limit ?",
                rowMapper,
                count
        );
    }
}
