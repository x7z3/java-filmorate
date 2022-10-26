package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.LikesRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository, UserRepository userRepository, LikesRepository likesRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.likesRepository = likesRepository;
    }

    @Override
    public void addFilm(Film film) {
        filmRepository.addFilm(film);
    }

    @Override
    public void updateFilm(Film film) {
        filmRepository.updateFilm(film);
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
        }
        return popular;
    }
}
