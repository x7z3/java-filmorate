package ru.yandex.practicum.filmorate.repository.in_memory_db;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.RecordAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.RecordNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class InMemoryDataBase<T extends IdElement> {
    private final IdGenerator idGenerator = new IdGenerator();
    private final Map<Integer, T> storage = new HashMap<>();

    public void add(T t) {
        log.info("adding a data entity to the in memory storage: {}", t);
        shouldNotExist(t);
        int nextId = idGenerator.getNextId();
        t.setId(nextId);
        storage.put(nextId, t);
    }

    public T get(Integer id) {
        log.info("getting a data entity from the in memory storage under id {}", id);
        shouldExist(id);
        return storage.get(id);
    }

    public void update(T t) {
        log.info("updating a data entity in the in memory storage to the next entity: {}", t);
        int id = t.getId();
        shouldExist(id);
        storage.put(id, t);
    }

    public void remove(Integer id) {
        log.info("removing a data entity from the in memory storage by the id {}", id);
        shouldExist(id);
        storage.remove(id);
    }

    public List<T> getAll() {
        log.info("getting a list of the all stored entities in the in memory storage");
        return new ArrayList<>(storage.values());
    }

    private void shouldExist(Integer id) {
        if (!storage.containsKey(id)) {
            log.error("couldn't find an entity with the id {}", id);
            throw new RecordNotFoundException();
        }
    }

    private void shouldNotExist(T t) {
        if (storage.containsValue(t)) {
            log.error("the entity is already in the in memory storage: {}", t);
            throw new RecordAlreadyExistException();
        }
    }
}
