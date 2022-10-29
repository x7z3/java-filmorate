package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryLikesRepository implements LikesRepository {
    private final Map<Film, Likes> likesBase = new HashMap<>();

    @Override
    public void addLike(Film film, User user) {
        log.info("adding a like from a user({}) to film({})", user, film);
        likesBase.putIfAbsent(film, new Likes(film, new HashSet<>()));
        likesBase.get(film).getUsers().add(user);
    }

    @Override
    public void removeLike(Film film, User user) {
        log.info("removing user's ({}) like from film({})", user, film);
        shouldExist(film);
        Set<User> users = likesBase.get(film).getUsers();
        users.remove(user);
        if (users.isEmpty()) likesBase.remove(film);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        log.info("gathering popular films according to their likes");
        List<Likes> sortedLikes = likesBase.values().stream().sorted(Comparator.comparingInt(o -> o.getUsers().size()))
                .collect(Collectors.toList());
        Collections.reverse(sortedLikes);
        return sortedLikes.stream().limit(count).map(Likes::getFilm).collect(Collectors.toList());
    }

    private void shouldExist(Film film) {
        if (!likesBase.containsKey(film)) {
            log.warn("film({}) was not found", film);
            throw new RecordNotFoundException();
        }
    }
}
