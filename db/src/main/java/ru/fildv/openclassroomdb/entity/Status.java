package ru.fildv.openclassroomdb.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Status {
    ANNOUNCED, WAITING, LEARNING, ENDED, ARCHIVED;

    public static Optional<Status> find(final String status) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(status))
                .findFirst();
    }
}
