package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.IdName;

import java.util.List;

public interface MpaRatingsRepository {
    void addMpaRating(String mpaRatingName);

    void deleteMpaRating(Integer mpaRatingId);

    List<IdName> getMpaRatings();

    IdName getMpaRating(Integer mpaRatingId);
}
