package ru.yandex.practicum.filmorate.validation.annotation;

import ru.yandex.practicum.filmorate.validation.constraint.DateLowerBoundaryConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = DateLowerBoundaryConstraint.class
)
public @interface DateLowerBoundary {
    String message() default "{ru.yandex.practicum.filmorate.validation.constraint.DateLowerBoundaryConstraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int year() default 0;

    int month() default 0;

    int day() default 0;
}
