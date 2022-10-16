package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.repository.in_memory_db.IdElement;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class User implements IdElement, Serializable {
    private transient int id;

    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @Past
    private LocalDate birthday;
}
