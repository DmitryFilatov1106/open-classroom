package ru.fildv.openclassroomdb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseStudent {
    private Integer id;
    private Course course;
    private User student;
    private Integer grade;
}
