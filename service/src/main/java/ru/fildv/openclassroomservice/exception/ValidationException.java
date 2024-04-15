package ru.fildv.openclassroomservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.fildv.openclassroomservice.validator.Error;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ValidationException extends RuntimeException {
    private final List<Error> errors;
}
