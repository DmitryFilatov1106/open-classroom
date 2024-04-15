package ru.fildv.openclassroomdb.dto.user;

public record StudentDto(Integer id,
                         String username,
                         String email,
                         Integer grade) {
}
