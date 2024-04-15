package ru.fildv.openclassroomdb.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Role {
    ADMIN, PROFESSOR, STUDENT;

    public static Optional<Role> find(final String role) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(role))
                .findFirst();
    }

    public static List<Role> getAllowed() {
        return Arrays.stream(values())
                .filter(it -> it != Role.ADMIN)
                .toList();
    }
}
