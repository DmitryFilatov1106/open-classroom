package ru.fildv.openclassroomdb.dto.course;

import java.time.LocalDate;

public record CourseDto(Integer id,
                        String name,
                        String professor,
                        String status,
                        String capacity,
                        LocalDate fromDate,
                        String sfromDate,
                        LocalDate toDate,
                        String stoDate,
                        String description,
                        boolean inlist) {
}
