package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface LikesRepository {

    void addLike(Film film, User user);

    void removeLike(Film film, User user);

    List<Film> getPopular(Integer count);
}
