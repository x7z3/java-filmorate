package ru.yandex.practicum.filmorate.repository.db_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.IdName;
import ru.yandex.practicum.filmorate.repository.FilmGenresRepository;
import ru.yandex.practicum.filmorate.repository.GenresRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Profile("db_h2")
@Repository
public class FilmGenresDbRepositoryImpl implements FilmGenresRepository {
    private final JdbcTemplate jdbcTemplate;
    private final GenresRepository genresRepository;

    @Autowired
    public FilmGenresDbRepositoryImpl(JdbcTemplate jdbcTemplate, GenresRepository genresRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.genresRepository = genresRepository;
    }

    @Override
    public void addFilmGenres(Integer filmId, List<IdName> genres) {
        List<IdName> distinctGenres = genres.stream().distinct().collect(Collectors.toList());
        // will throw an exception if there is no genre
        for (IdName genre : distinctGenres) {
            genresRepository.getGenre(genre.getId());
        }

        // deletes film's genres only after assurance that all adding genres are really existing in the correspond table
        deleteFilmGenres(filmId);

        // safely adds genres above after all existence checks
        for (IdName genre : distinctGenres) {
            addFilmGenre(filmId, genre.getId());
        }
    }

    @Override
    public void addFilmGenre(Integer filmId, Integer genreId) {
        IdName genre = genresRepository.getGenre(genreId);
        log.info("adding film genre with ID={} and name '{}' to a film with ID={}",
                genre.getId(), genre.getName(), filmId);
        jdbcTemplate.update("insert into film_genres (film_id, genre_id) values (?, ?)", filmId, genreId);
    }

    @Override
    public void deleteFilmGenre(Integer filmId, Integer genreId) {
        log.info("deleting film genre with ID={} from a film with ID={}", genreId, filmId);
        jdbcTemplate.update("delete from film_genres where film_id = ? and genre_id = ?", filmId, genreId);
    }

    @Override
    public void deleteFilmGenres(Integer filmId) {
        log.info("deleting all genres from a film with ID={}", filmId);
        jdbcTemplate.update("delete from film_genres where film_id = ?", filmId);
    }

    @Override
    public List<IdName> getFilmGenres(Integer filmId) {
        log.info("getting all genres of the a with ID={}", filmId);
        RowMapper<IdName> rowMapper = (rs, rowNum) -> {
            Integer genreId = rs.getInt("genre_id");
            return genresRepository.getGenre(genreId);
        };
        return jdbcTemplate.query("select * from film_genres where film_id = ?", rowMapper, filmId);
    }
}
