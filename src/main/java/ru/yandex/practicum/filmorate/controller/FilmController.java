package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(@Autowired FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        filmService.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }
}
