package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.repository.in_memory_db.IdElement;
import ru.yandex.practicum.filmorate.validation.annotation.DateLowerBoundary;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film implements IdElement, Serializable {
    private transient int id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @DateLowerBoundary(year = 1895, month = 12, day = 28)
    private LocalDate releaseDate;

    @Positive
    private Integer duration; // duration in minutes
}
