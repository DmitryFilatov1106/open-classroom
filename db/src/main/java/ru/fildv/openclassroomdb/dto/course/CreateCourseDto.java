package ru.fildv.openclassroomdb.dto.course;

public record CreateCourseDto(String name,
                              String status,
                              Integer capacity,
                              Integer idProfessor,
                              String fromDate,
                              String toDate,
                              String description) {
}
