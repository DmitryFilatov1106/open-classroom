package ru.fildv.openclassroomutil.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@UtilityClass
public class LocalDateFormatter {
    private static final String PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern(PATTERN);

    public LocalDate format(final String date) {
        return LocalDate.parse(date, FORMATTER);
    }

    public boolean isNotValid(final String date) {
        try {
            return Optional.ofNullable(date)
                    .map(LocalDateFormatter::format)
                    .isEmpty();
        } catch (DateTimeParseException e) {
            return true;
        }
    }
}
