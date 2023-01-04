package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.IdName;

import java.util.List;

public interface MpaService {
    List<IdName> getMpa();

    IdName getMpa(Integer id);
}
