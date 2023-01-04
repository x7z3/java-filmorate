package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.IdName;

import java.util.List;

public interface FilmGenresRepository {
    void addFilmGenre(Integer filmId, Integer genreId);

    void addFilmGenres(Integer filmId, List<IdName> genres);

    void deleteFilmGenre(Integer filmId, Integer genreId);

    void deleteFilmGenres(Integer filmId);

    List<IdName> getFilmGenres(Integer filmId);
}
