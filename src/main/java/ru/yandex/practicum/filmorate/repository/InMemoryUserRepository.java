package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.in_memory_db.InMemoryDataBase;

import java.util.List;

@Repository
public class InMemoryUserRepository extends InMemoryDataBase<User> implements UserRepository {
    @Override
    public User getUser(Integer userId) {
        return super.get(userId);
    }

    @Override
    public void addUser(User user) {
        super.add(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        super.remove(userId);
    }

    @Override
    public void updateUser(User user) {
        super.update(user);
    }

    @Override
    public List<User> getAllUsers() {
        return super.getAll();
    }
}
