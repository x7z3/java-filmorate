package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.IdName;

import java.util.List;

public interface FilmMpaRatingsRepository {
    void addFilmMpaRating(Integer filmId, Integer mpaRatingId);

    void deleteFilmMpaRating(Integer filmId, Integer mpaRating);

    void deleteFilmMpaRatings(Integer filmId);

    List<IdName> getFilmMpaRatings(Integer filmId);
}
