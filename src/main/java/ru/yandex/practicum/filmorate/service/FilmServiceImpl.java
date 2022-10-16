package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;

    public FilmServiceImpl(@Autowired FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public void addFilm(Film film) {
        LocalDate lowerDateEdge = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(lowerDateEdge)) {
            log.error("incorrect film's release date");
            throw new ValidationException("Incorrect film's date");
        }
        filmRepository.addFilm(film);
    }

    @Override
    public void updateFilm(Film film) {
        filmRepository.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }
}
