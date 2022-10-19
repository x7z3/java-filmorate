package ru.yandex.practicum.filmorate.validation.constraint;

import ru.yandex.practicum.filmorate.validation.annotation.DateLowerBoundary;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateLowerBoundaryConstraint implements ConstraintValidator<DateLowerBoundary, LocalDate> {
    private LocalDate boundaryDate;

    @Override
    public void initialize(DateLowerBoundary constraintAnnotation) {
        boundaryDate = LocalDate.of(constraintAnnotation.year(), constraintAnnotation.month(), constraintAnnotation.day());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return boundaryDate.isBefore(localDate) || boundaryDate.isEqual(localDate);
    }
}
