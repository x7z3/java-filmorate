package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.repository.in_memory_impl.db.IdElement;
import ru.yandex.practicum.filmorate.validation.annotation.DateLowerBoundary;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
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

    private transient IdName mpa;

    private transient List<IdName> genres;
}
