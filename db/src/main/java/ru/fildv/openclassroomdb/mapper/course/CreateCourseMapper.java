package ru.fildv.openclassroomdb.mapper.course;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.fildv.openclassroomdb.dao.UserDao;
import ru.fildv.openclassroomdb.dto.course.CreateCourseDto;
import ru.fildv.openclassroomdb.entity.Course;
import ru.fildv.openclassroomdb.entity.Status;
import ru.fildv.openclassroomdb.entity.User;
import ru.fildv.openclassroomdb.mapper.Mapper;
import ru.fildv.openclassroomutil.util.LocalDateFormatter;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCourseMapper implements Mapper<CreateCourseDto, Course> {
    private static final CreateCourseMapper INSTANCE = new CreateCourseMapper();
    private static final UserDao USER_DAO = UserDao.getInstance();

    public static CreateCourseMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Course mapFrom(final CreateCourseDto object) {
        return Course.builder()
                .name(object.name())
                .status(Status.valueOf(object.status()))
                .capacity(object.capacity())
                .professor(getProfessor(object.idProfessor()))
                .fromDate(LocalDateFormatter.format(object.fromDate()))
                .toDate(LocalDateFormatter.format(object.toDate()))
                .description(object.description())
                .build();
    }

    private User getProfessor(final Integer id) {
        return Optional.ofNullable(id)
                .flatMap(USER_DAO::findById)
                .orElse(null);
    }
}
