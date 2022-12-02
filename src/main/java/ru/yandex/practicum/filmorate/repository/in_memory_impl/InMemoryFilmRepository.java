package ru.yandex.practicum.filmorate.repository.in_memory_impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.in_memory_impl.db.InMemoryDataBase;

import java.util.List;

@Profile("in_memory")
@Repository
public class InMemoryFilmRepository extends InMemoryDataBase<Film> implements FilmRepository {
    @Override
    public Film getFilm(Integer filmId) {
        return super.get(filmId);
    }

    @Override
    public Film addFilm(Film film) {
        super.add(film);
        return film;
    }

    @Override
    public void deleteFilm(Integer filmId) {
        super.remove(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        return super.update(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return super.getAll();
    }
}
