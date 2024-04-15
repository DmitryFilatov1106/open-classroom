package ru.fildv.openclassroomdb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    private Integer id;
    private String name;
    private Status status;
    private Integer capacity;
    private User professor;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String description;
}
