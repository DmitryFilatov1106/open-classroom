package ru.fildv.openclassroomservice.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<Error> errors = new ArrayList<>();

    public List<Error> getErrors() {
        return errors;
    }

    public void add(final Error error) {
        errors.add(error);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }
}
