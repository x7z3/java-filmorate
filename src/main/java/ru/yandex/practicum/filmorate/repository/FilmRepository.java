package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {
    Film getFilm(Integer filmId);

    Film addFilm(Film film);

    void deleteFilm(Integer filmId);

    Film updateFilm(Film film);

    List<Film> getAllFilms();
}
