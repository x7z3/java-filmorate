package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam Optional<Integer> count) {
        return filmService.getPopularFilms(count.orElse(10));
    }
}
