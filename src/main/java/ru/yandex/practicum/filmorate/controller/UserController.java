package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        userService.createUser(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        userService.updateUser(user);
        return user;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getUserCommonFriends(id, otherId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteUserFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteUserFriend(id, friendId);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addToUserFriendList(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addToUserFriendList(id, friendId);
    }
}
