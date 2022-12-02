package ru.yandex.practicum.filmorate.service.db_impl;

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

@Profile("db_h2")
@Service
public class FilmDbServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;

    @Autowired
    public FilmDbServiceImpl(
            FilmRepository filmRepository,
            LikesRepository likesRepository,
            UserRepository userRepository
    ) {
        this.filmRepository = filmRepository;
        this.likesRepository = likesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Film addFilm(Film film) {
        return filmRepository.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmRepository.updateFilm(film);
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
        if (popular.isEmpty()) return getAllFilms().stream().limit(count).collect(Collectors.toList());
        return popular;
    }

    @Override
    public Film getFilm(Integer id) {
        return filmRepository.getFilm(id);
    }
}
