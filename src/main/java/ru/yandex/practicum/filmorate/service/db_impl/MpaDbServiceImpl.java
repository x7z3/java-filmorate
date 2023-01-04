package ru.yandex.practicum.filmorate.service.db_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.IdName;
import ru.yandex.practicum.filmorate.repository.MpaRatingsRepository;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Service
public class MpaDbServiceImpl implements MpaService {
    private final MpaRatingsRepository mpaRatingsRepository;

    @Autowired
    public MpaDbServiceImpl(MpaRatingsRepository mpaRatingsRepository) {
        this.mpaRatingsRepository = mpaRatingsRepository;
    }

    @Override
    public List<IdName> getMpa() {
        return mpaRatingsRepository.getMpaRatings();
    }

    @Override
    public IdName getMpa(Integer id) {
        return mpaRatingsRepository.getMpaRating(id);
    }
}
