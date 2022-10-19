package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.in_memory_db.InMemoryDataBase;

import java.util.List;

@Repository
public class InMemoryFilmRepository extends InMemoryDataBase<Film> implements FilmRepository {

    @Override
    public Film getFilm(Integer filmId) {
        return super.get(filmId);
    }

    @Override
    public void addFilm(Film film) {
        super.add(film);
    }

    @Override
    public void deleteFilm(Integer filmId) {
        super.remove(filmId);
    }

    @Override
    public void updateFilm(Film film) {
        super.update(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return super.getAll();
    }
}
