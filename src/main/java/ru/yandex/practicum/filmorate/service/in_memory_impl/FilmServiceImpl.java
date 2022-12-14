package ru.yandex.practicum.filmorate.service.in_memory_impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.LikesRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Profile("in_memory")
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    @Autowired
    public FilmServiceImpl(
            FilmRepository filmRepository,
            UserRepository userRepository,
            LikesRepository likesRepository
    ) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.likesRepository = likesRepository;
    }

    @Override
    public Film addFilm(Film film) {
        filmRepository.addFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return filmRepository.updateFilm(film);
    }

    @Override
    public Film getFilm(Integer id) {
        return filmRepository.getFilm(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    @Override
    public void likeFilm(Integer filmId, Integer userId) {
        Film film = filmRepository.getFilm(filmId);
        User user = userRepository.getUser(userId);
        likesRepository.addLike(film, user);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        Film film = filmRepository.getFilm(filmId);
        User user = userRepository.getUser(userId);
        likesRepository.removeLike(film, user);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        List<Film> popular = likesRepository.getPopular(count);
        if (popular.isEmpty()) {
            log.warn("no films have liked before");
            return getAllFilms().stream().limit(count).collect(Collectors.toList());
        }
        return popular;
    }
}
