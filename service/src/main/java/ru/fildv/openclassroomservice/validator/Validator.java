package ru.fildv.openclassroomservice.validator;

public interface Validator<T> {
    ValidationResult isValid(T object);
}
