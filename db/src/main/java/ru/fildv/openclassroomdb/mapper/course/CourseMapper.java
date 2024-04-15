package ru.fildv.openclassroomdb.mapper.course;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.fildv.openclassroomdb.dto.course.CourseDto;
import ru.fildv.openclassroomdb.entity.Course;
import ru.fildv.openclassroomdb.mapper.Mapper;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseMapper implements Mapper<Course, CourseDto> {
    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final CourseMapper INSTANCE = new CourseMapper();

    public static CourseMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public CourseDto mapFrom(final Course object) {
        return new CourseDto(
                object.getId(),
                object.getName(),
                object.getProfessor().getUsername(),
                object.getStatus().name(),
                object.getCapacity().toString(),
                object.getFromDate(),
                object.getFromDate().format(FORMATTER),
                object.getToDate(),
                object.getToDate().format(FORMATTER),
                object.getDescription(),
                false
        );
    }

    public CourseDto mapFromInlist(final Course object, final boolean inlist) {
        return new CourseDto(
                object.getId(),
                object.getName(),
                object.getProfessor().getUsername(),
                object.getStatus().name(),
                object.getCapacity().toString(),
                object.getFromDate(),
                object.getFromDate().format(FORMATTER),
                object.getToDate(),
                object.getToDate().format(FORMATTER),
                object.getDescription(),
                inlist
        );
    }
}
