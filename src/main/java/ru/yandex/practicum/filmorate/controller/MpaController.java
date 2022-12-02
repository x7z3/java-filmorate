package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.IdName;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Profile("db_h2")
@RestController
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @Autowired

    @GetMapping("/mpa")
    public List<IdName> getMpa() {
        return mpaService.getMpa();
    }

    @GetMapping("/mpa/{id}")
    public IdName getMpa(@PathVariable("id") Integer id) {
        return mpaService.getMpa(id);
    }
}
