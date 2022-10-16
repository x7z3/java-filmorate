package ru.yandex.practicum.filmorate.repository.in_memory_db;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IdGenerator {
    private int lastId;

    public int getNextId() {
        return ++lastId;
    }
}
