package ru.fildv.openclassroomdb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    private Integer id;
    private String username;
    private String email;
    private Integer grade;
}
