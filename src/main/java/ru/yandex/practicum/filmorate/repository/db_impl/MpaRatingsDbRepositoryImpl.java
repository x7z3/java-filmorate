package ru.yandex.practicum.filmorate.repository.db_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.IdName;
import ru.yandex.practicum.filmorate.repository.MpaRatingsRepository;

import java.util.List;

@Slf4j
@Profile("db_h2")
@Repository
public class MpaRatingsDbRepositoryImpl implements MpaRatingsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaRatingsDbRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addMpaRating(String mpaRatingName) {
        log.info("adding mpa rating with name {}", mpaRatingName);
        jdbcTemplate.update("insert into mpa_ratings (mpa_rating_name) values (?)", mpaRatingName);
    }

    @Override
    public void deleteMpaRating(Integer mpaRatingId) {
        log.info("deleting mpa rating with ID={}", mpaRatingId);
        jdbcTemplate.update("delete from mpa_ratings where mpa_rating_id = ?", mpaRatingId);
    }

    @Override
    public List<IdName> getMpaRatings() {
        log.info("getting list of mpa ratings");
        RowMapper<IdName> rowMapper = (rs, rowNum) -> {
            Integer mpaRatingId = rs.getInt("mpa_rating_id");
            String mpaRatingName = rs.getString("mpa_rating_name");
            return new IdName(mpaRatingId, mpaRatingName);
        };
        return jdbcTemplate.query("select * from mpa_ratings order by mpa_rating_id", rowMapper);
    }

    @Override
    public IdName getMpaRating(Integer mpaRatingId) {
        log.info("getting mpa rating with ID={}", mpaRatingId);
        RowMapper<IdName> rowMapper = (rs, rowNum) -> {
            Integer mpaId = rs.getInt("mpa_rating_id");
            String mpaRatingName = rs.getString("mpa_rating_name");
            return new IdName(mpaId, mpaRatingName);
        };
        List<IdName> ratings = jdbcTemplate.query("select * from mpa_ratings where mpa_rating_id = ?", rowMapper, mpaRatingId);
        if (ratings.isEmpty())
            throw new RecordNotFoundException(String.format("Rating with ID=%s is not found", mpaRatingId));
        return ratings.get(0);
    }
}
