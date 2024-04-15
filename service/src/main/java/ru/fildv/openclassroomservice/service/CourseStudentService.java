package ru.fildv.openclassroomservice.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.fildv.openclassroomdb.dao.CourseStudentDao;
import ru.fildv.openclassroomdb.dto.user.StudentDto;
import ru.fildv.openclassroomdb.entity.Course;
import ru.fildv.openclassroomdb.entity.CourseStudent;
import ru.fildv.openclassroomdb.entity.User;
import ru.fildv.openclassroomdb.mapper.user.StudentMapper;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseStudentService {
    private static final CourseStudentService INSTANCE
            = new CourseStudentService();
    private static final StudentMapper STUDENT_MAPPER
            = StudentMapper.getInstance();
    private final CourseStudentDao courseStudentDao
            = CourseStudentDao.getInstance();

    public static CourseStudentService getInstance() {
        return INSTANCE;
    }

    public Integer getGrade(final Integer idCourse, final Integer idStudent) {
        return courseStudentDao.findGrade(idCourse, idStudent);
    }

    public List<StudentDto> getStudents(final int idCourse) {
        return courseStudentDao.findStudents(idCourse).stream()
                .map(STUDENT_MAPPER::mapFrom)
                .collect(toList());
    }

    public void saveGrade(
            final int courseId, final int studentId, final int grade) {
        Optional<CourseStudent> oCourseStudent = courseStudentDao
                .findByCourseIdAndStudentId(courseId, studentId);
        CourseStudent courseStudent;
        if (oCourseStudent.isPresent()) {
            courseStudent = oCourseStudent.get();
            courseStudent.setGrade(grade);
            courseStudentDao.update(courseStudent);
        } else {
            courseStudent = CourseStudent.builder()
                    .course(Course.builder()
                            .id(courseId)
                            .build())
                    .student(User.builder()
                            .id(studentId)
                            .build())
                    .build();
            courseStudentDao.save(courseStudent);
        }
    }

    public void enroll(final int idCourse, final int idStudent) {
        courseStudentDao.enroll(idCourse, idStudent);
    }

    public void cansel(final int idCourse, final int idStudent) {
        courseStudentDao.cansel(idCourse, idStudent);
    }
}
