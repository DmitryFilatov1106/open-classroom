package ru.fildv.openclassroomdb.dto.user;

import ru.fildv.openclassroomdb.entity.Gender;
import ru.fildv.openclassroomdb.entity.Role;

import java.time.LocalDate;

public record UserDto(Integer id,
                      String username,
                      LocalDate birthday,
                      String email,
                      String image,
                      Role role,
                      Gender gender) {
}
