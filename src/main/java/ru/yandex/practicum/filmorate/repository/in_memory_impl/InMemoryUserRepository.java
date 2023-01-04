package ru.yandex.practicum.filmorate.repository.in_memory_impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.in_memory_impl.db.InMemoryDataBase;

import java.util.List;

@Profile("in_memory")
@Repository
public class InMemoryUserRepository extends InMemoryDataBase<User> implements UserRepository {
    @Override
    public User getUser(Integer userId) {
        return super.get(userId);
    }

    @Override
    public User addUser(User user) {
        super.add(user);
        return user;
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
