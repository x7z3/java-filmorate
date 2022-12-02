package ru.yandex.practicum.filmorate.repository.db_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.IdName;
import ru.yandex.practicum.filmorate.repository.GenresRepository;

import java.util.List;

@Slf4j
@Profile("db_h2")
@Repository
public class GenresDbRepositoryImpl implements GenresRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDbRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenre(String genreName) {
        log.info("adding genre with name {}", genreName);
        jdbcTemplate.update("insert into genres (genre_name) values ( ? )", genreName);
    }

    @Override
    public void deleteGenre(Integer genreId) {
        log.info("deleting genre with ID={}", genreId);
        jdbcTemplate.update("delete from genres where genre_id = ?", genreId);
    }

    @Override
    public List<IdName> getGenres() {
        log.info("getting all genres");
        RowMapper<IdName> rowMapper = (rs, rowNum) -> {
            Integer genreId = rs.getInt("genre_id");
            String genreName = rs.getString("genre_name");
            return new IdName(genreId, genreName);
        };
        return jdbcTemplate.query("select * from genres order by genre_id", rowMapper);
    }

    @Override
    public IdName getGenre(Integer genreId) {
        log.info("getting genre with ID={}", genreId);
        RowMapper<IdName> rowMapper = (rs, rowNum) -> {
            Integer gId = rs.getInt("genre_id");
            String genreName = rs.getString("genre_name");
            return new IdName(gId, genreName);
        };
        List<IdName> genres = jdbcTemplate.query("select * from genres where genre_id = ?", rowMapper, genreId);
        if (genres.isEmpty())
            throw new RecordNotFoundException(String.format("Genre with ID=%s is not found", genreId));
        return genres.get(0);
    }
}
