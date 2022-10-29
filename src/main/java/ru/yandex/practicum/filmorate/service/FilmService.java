package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void addFilm(Film film);

    void updateFilm(Film film);

    List<Film> getAllFilms();

    void likeFilm(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(Integer count);

    Film getFilm(Integer id);
}
