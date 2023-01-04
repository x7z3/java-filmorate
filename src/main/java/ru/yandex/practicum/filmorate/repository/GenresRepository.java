package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.IdName;

import java.util.List;

public interface GenresRepository {
    void addGenre(String genreName);

    void deleteGenre(Integer genreId);

    List<IdName> getGenres();

    IdName getGenre(Integer genreId);
}
