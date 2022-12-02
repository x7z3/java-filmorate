package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.IdName;

import java.util.List;

public interface GenreService {
    List<IdName> getGenres();

    IdName getGenre(Integer id);
}
