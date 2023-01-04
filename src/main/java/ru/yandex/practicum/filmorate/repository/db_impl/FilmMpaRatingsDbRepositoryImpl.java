package ru.yandex.practicum.filmorate.repository.db_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.IdName;
import ru.yandex.practicum.filmorate.repository.FilmMpaRatingsRepository;
import ru.yandex.practicum.filmorate.repository.MpaRatingsRepository;

import java.util.List;

@Slf4j
@Profile("db_h2")
@Repository
public class FilmMpaRatingsDbRepositoryImpl implements FilmMpaRatingsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingsRepository mpaRatingsRepository;

    @Autowired
    public FilmMpaRatingsDbRepositoryImpl(JdbcTemplate jdbcTemplate, MpaRatingsRepository mpaRatingsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRatingsRepository = mpaRatingsRepository;
    }

    @Override
    public void addFilmMpaRating(Integer filmId, Integer mpaRatingId) {
        IdName mpaRating = mpaRatingsRepository.getMpaRating(mpaRatingId);
        log.info("adding mpa rating with ID={} and name '{}' to a film with ID={}",
                mpaRating.getId(), mpaRating.getName(), filmId);
        List<IdName> filmMpaRatings = getFilmMpaRatings(filmId);
        if (filmMpaRatings.isEmpty()) {
            jdbcTemplate.update("insert into film_mpa_ratings (film_id, mpa_rating_id) values (?, ?)", filmId, mpaRatingId);
        } else {
            jdbcTemplate.update("update film_mpa_ratings set mpa_rating_id = ? where film_id = ?", mpaRatingId, filmId);
        }
    }

    @Override
    public void deleteFilmMpaRating(Integer filmId, Integer mpaRating) {
        log.info("deleting mpa rating with ID={} from a film with ID={}", mpaRating, filmId);
        jdbcTemplate.update("delete from film_mpa_ratings where film_id = ? and mpa_rating_id = ?", filmId, mpaRating);
    }

    @Override
    public void deleteFilmMpaRatings(Integer filmId) {
        log.info("delete all mpa ratings from a film with ID={}", filmId);
        jdbcTemplate.update("delete from film_mpa_ratings where film_id = ?", filmId);
    }

    @Override
    public List<IdName> getFilmMpaRatings(Integer filmId) {
        log.info("getting all mpa ratings from a film with ID={}", filmId);
        RowMapper<IdName> rowMapper = (rs, rowNum) -> mpaRatingsRepository.getMpaRating(rs.getInt("mpa_rating_id"));
        return jdbcTemplate.query("select * from film_mpa_ratings where film_id = ?", rowMapper, filmId);
    }
}
