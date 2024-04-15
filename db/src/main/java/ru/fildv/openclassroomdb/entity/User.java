package ru.fildv.openclassroomdb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer id;
    private String username;
    private LocalDate birthday;
    private String image;
    private String email;
    private String password;
    private Role role;
    private Gender gender;
}
