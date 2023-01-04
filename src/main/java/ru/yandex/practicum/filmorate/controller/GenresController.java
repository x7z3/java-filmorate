package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.IdName;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Profile("db_h2")
@RestController
public class GenresController {
    private final GenreService genreService;

    @Autowired
    public GenresController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public List<IdName> getGenres() {
        return genreService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public IdName getGenre(@PathVariable("id") Integer id) {
        return genreService.getGenre(id);
    }
}
