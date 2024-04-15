package ru.fildv.openclassroomdb.dao;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.fildv.openclassroomdb.entity.Course;
import ru.fildv.openclassroomdb.entity.CourseStudent;
import ru.fildv.openclassroomdb.entity.Student;
import ru.fildv.openclassroomdb.entity.User;
import ru.fildv.openclassroomutil.util.ConnectionManager;
import ru.fildv.openclassroomutil.util.TuneStatement;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseStudentDao implements Dao<CourseStudent, Integer> {
    private static final String FIND_BY_COURSE_AND_STUDENT_SQL = """
            SELECT id, id_course, id_student, grade
            FROM course_student
            WHERE id_course = ?
              AND id_student = ?;
                        """;

    private static final String FIND_STUDENTS_SQL = """
            SELECT cs.id      id,
                   u.id       id_student,
                   u.username username,
                   u.email    email,
                   cs.grade   grade
            FROM course_student cs
                     LEFT JOIN users u ON cs.id_student = u.id
            WHERE cs.id_course = ?;
                        """;
    private static final String FIND_STUDENTS_INLIST_SQL = """
            SELECT id, id_course, id_student, grade
            FROM course_student
            WHERE id_course = ANY (?)
              AND id_student = ?;
                        """;
    private static final String SAVE_SQL = """
            INSERT INTO course_student
            (id_course, id_student, grade)
            VALUES (?, ?, ?)
            RETURNING id;
            """;

    private static final String UPDATE_SQL = """
            UPDATE course_student
            SET
                id_course = ?,
                id_student = ?,
                grade = ?
            WHERE id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM course_student
            WHERE id = ?
            RETURNING id;
            """;
    private static final CourseStudentDao INSTANCE = new CourseStudentDao();

    public static CourseStudentDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<CourseStudent> findAll() {
        return null;
    }

    @Override
    public Optional<CourseStudent> findById(final Integer id) {
        return Optional.empty();
    }

    @Override
    @SneakyThrows
    public boolean delete(final Integer id) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(DELETE_SQL, RETURN_GENERATED_KEYS);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, id);
        prepareStatement.executeUpdate();

        var resultSet = prepareStatement.getGeneratedKeys();
        resultSet.next();

        return Objects.equals(resultSet.getObject("id", Integer.class), id);
    }

    @Override
    @SneakyThrows
    public void update(final CourseStudent entity) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection.prepareStatement(UPDATE_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, entity.getCourse().getId());
        prepareStatement.setObject(2, entity.getStudent().getId());
        prepareStatement.setObject(3, entity.getGrade());
        prepareStatement.setObject(4, entity.getId());

        prepareStatement.executeUpdate();
    }

    @Override
    @SneakyThrows
    public CourseStudent save(final CourseStudent entity) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, entity.getCourse().getId());
        prepareStatement.setObject(2, entity.getStudent().getId());
        prepareStatement.setObject(3, entity.getGrade());

        prepareStatement.executeUpdate();
        var resultSet = prepareStatement.getGeneratedKeys();
        resultSet.next();
        entity.setId(resultSet.getObject("id", Integer.class));
        return entity;
    }

    @SneakyThrows
    public Integer findGrade(final Integer idCourse, final Integer idStudent) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_BY_COURSE_AND_STUDENT_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, idCourse);
        prepareStatement.setObject(2, idStudent);
        var resultSet = prepareStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getObject("grade", Integer.class);
        }
        return 0;
    }

    @SneakyThrows
    public List<Student> findStudents(final int idCourse) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_STUDENTS_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, idCourse);

        var resultSet = prepareStatement.executeQuery();
        List<Student> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(buildStudent(resultSet));
        }
        return list;
    }

    private Student buildStudent(final ResultSet resultSet)
            throws SQLException {
        return Student.builder()
                .id(resultSet.getObject("id_student", Integer.class))
                .username(resultSet.getObject("username", String.class))
                .email(resultSet.getObject("email", String.class))
                .grade(resultSet.getObject("grade", Integer.class))
                .build();
    }

    @SneakyThrows
    public Optional<CourseStudent> findByCourseIdAndStudentId(
            final int idCourse, final int idStudent) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_BY_COURSE_AND_STUDENT_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, idCourse);
        prepareStatement.setObject(2, idStudent);
        var resultSet = prepareStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(CourseStudent.builder()
                    .id(resultSet.getObject("id", Integer.class))
                    .course(Course.builder()
                            .id(resultSet.getObject("id_course", Integer.class))
                            .build())
                    .student(User.builder()
                            .id(resultSet.getObject(
                                    "id_student", Integer.class))
                            .build())
                    .grade(resultSet.getObject("grade", Integer.class))
                    .build());
        }
        return Optional.empty();
    }

    @SneakyThrows
    public List<CourseStudent> findStudentInlist(
            final Integer idStudent, final Integer[] coursesId) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_STUDENTS_INLIST_SQL);
        TuneStatement.tune(prepareStatement);
        Array array = connection.createArrayOf("INTEGER", coursesId);
        prepareStatement.setArray(1, array);
        prepareStatement.setObject(2, idStudent);

        var resultSet = prepareStatement.executeQuery();
        List<CourseStudent> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(
                    CourseStudent.builder()
                            .id(resultSet.getObject("id_course", Integer.class))
                            .course(Course.builder()
                                    .id(resultSet.getObject(
                                            "id_course", Integer.class))
                                    .build())
                            .student(User.builder()
                                    .id(resultSet.getObject(
                                            "id_student", Integer.class))
                                    .build())
                            .grade(resultSet.getObject("grade", Integer.class))
                            .build());
        }
        return list;
    }

    public void enroll(final int idCourse, final int idStudent) {
        save(CourseStudent.builder()
                .course(Course.builder()
                        .id(idCourse)
                        .build())
                .student(User.builder()
                        .id(idStudent)
                        .build())
                .grade(0)
                .build());
    }

    @SneakyThrows
    public void cansel(final int idCourse, final int idStudent) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_BY_COURSE_AND_STUDENT_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, idCourse);
        prepareStatement.setObject(2, idStudent);
        var resultSet = prepareStatement.executeQuery();
        if (resultSet.next()) {
            var id = resultSet.getObject("id", Integer.class);
            delete(id);
        }
    }
}
