package ru.fildv.openclassroomdb.dto.user;

import jakarta.servlet.http.Part;

public record UpdateUserDto(Integer id,
                            String username,
                            String birthday,
                            Part image,
                            String email,
                            String password,
                            String role,
                            String gender) {
}
