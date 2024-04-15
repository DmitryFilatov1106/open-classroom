package ru.fildv.openclassroomservice.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.fildv.openclassroomdb.dao.CourseDao;
import ru.fildv.openclassroomdb.dao.CourseStudentDao;
import ru.fildv.openclassroomdb.dto.course.CourseDto;
import ru.fildv.openclassroomdb.dto.course.CreateCourseDto;
import ru.fildv.openclassroomdb.entity.Course;
import ru.fildv.openclassroomdb.entity.Status;
import ru.fildv.openclassroomdb.mapper.course.CourseMapper;
import ru.fildv.openclassroomdb.mapper.course.CreateCourseMapper;
import ru.fildv.openclassroomservice.exception.ValidationException;
import ru.fildv.openclassroomservice.validator.CreateCourseValidator;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseService {
    private static final CourseService INSTANCE = new CourseService();
    private static final CourseMapper COURSE_MAPPER
            = CourseMapper.getInstance();
    private final CreateCourseValidator createCourseValidator
            = CreateCourseValidator.getInstance();
    private final CourseDao courseDao = CourseDao.getInstance();
    private final CourseStudentDao courseStudentDao
            = CourseStudentDao.getInstance();
    private final CreateCourseMapper createCourseMapper
            = CreateCourseMapper.getInstance();

    public static CourseService getInstance() {
        return INSTANCE;
    }

    public Integer create(final CreateCourseDto createCourseDto) {
        var validationResult = createCourseValidator.isValid(createCourseDto);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        var courseEntity = createCourseMapper.mapFrom(createCourseDto);
        courseDao.save(courseEntity);
        return courseEntity.getId();
    }

    public List<CourseDto> findByProfessorIdAndStatus(
            final Integer idProfessor, final Status status) {
        return courseDao
                .findByProfessorIdAndStatus(idProfessor, status).stream()
                .map(COURSE_MAPPER::mapFrom)
                .toList();
    }

    public List<CourseDto> findByStudentIDAndStatus(
            final Integer idStudent, final Status status) {
        return courseDao.findByStudentIDAndStatus(idStudent, status).stream()
                .map(COURSE_MAPPER::mapFrom)
                .toList();
    }

    public String delete(final Integer idCourse) {
        var oCourse = courseDao.findById(idCourse);
        if (oCourse.isPresent()) {
            courseDao.delete(idCourse);
            return oCourse.get().getStatus().toString().toLowerCase();
        }
        return "";
    }

    public String nextStatus(final Integer idCourse) {
        var oCourse = courseDao.findById(idCourse);
        if (oCourse.isPresent()) {
            Status oldStatus = oCourse.get().getStatus();
            Status newStatus = null;
            boolean doReturn = false;
            for (Status status : Status.values()) {
                if (doReturn) {
                    newStatus = status;
                    break;
                }
                if (status == oldStatus) {
                    doReturn = true;
                }
            }
            courseDao.nextStatus(idCourse, newStatus.name());
            if (newStatus == null) {
                return oldStatus.name().toLowerCase();
            }

            return newStatus.name().toLowerCase();
        }
        return "";
    }

    public List<CourseDto> findByStatusWithInlist(
            final Status status, final Integer idStudent) {
        var coursesId = courseDao.findByStatus(status).stream()
                .map(Course::getId)
                .toList();

        List<Integer> courses = courseStudentDao.findStudentInlist(
                        idStudent, coursesId.toArray(new Integer[0])).stream()
                .map(entity -> entity.getCourse().getId())
                .toList();

        return courseDao.findByStatus(status).stream()
                .map(entity -> COURSE_MAPPER.mapFromInlist(
                        entity, courses.contains(entity.getId())))
                .toList();
    }

    public CourseDto getDescription(final Integer idCourse) {
        return courseDao.findById(idCourse)
                .map(COURSE_MAPPER::mapFrom)
                .orElse(null);
    }

    public List<CourseDto> findAll() {
        return courseDao.findAll().stream()
                .map(COURSE_MAPPER::mapFrom)
                .toList();
    }
}
