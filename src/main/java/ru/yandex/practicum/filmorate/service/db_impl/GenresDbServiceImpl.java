package ru.yandex.practicum.filmorate.service.db_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.IdName;
import ru.yandex.practicum.filmorate.repository.GenresRepository;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Service
public class GenresDbServiceImpl implements GenreService {
    private final GenresRepository genresRepository;

    @Autowired
    public GenresDbServiceImpl(GenresRepository genresRepository) {
        this.genresRepository = genresRepository;
    }

    @Override
    public List<IdName> getGenres() {
        return genresRepository.getGenres();
    }

    @Override
    public IdName getGenre(Integer id) {
        return genresRepository.getGenre(id);
    }
}
