package ru.fildv.openclassroomdb.dao;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.fildv.openclassroomdb.entity.Course;
import ru.fildv.openclassroomdb.entity.Status;
import ru.fildv.openclassroomdb.entity.User;
import ru.fildv.openclassroomutil.util.ConnectionManager;
import ru.fildv.openclassroomutil.util.TuneStatement;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseDao implements Dao<Course, Integer> {
    private static final String SAVE_SQL = """
            INSERT INTO course(name,
                               status,
                               capacity,
                               id_professor,
                               from_date,
                               to_date,
                               description)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
                        """;
    private static final String DELETE_SQL = """
            DELETE
            FROM course
            WHERE id = ?
            RETURNING id;
                        """;
    private static final String FIND_BY_PROFESSOR_ID_AND_STATUS_SQL = """
            SELECT c.id,
                   c.name,
                   c.status,
                   c.capacity,
                   c.id_professor,
                   u.username username_professor,
                   c.from_date,
                   c.to_date,
                   c.description
            FROM course c
                     LEFT JOIN users u on c.id_professor = u.id
            WHERE c.id_professor = ? AND c.status = ?
            ORDER BY c.from_date;
                        """;

    private static final String FIND_BY_STUDENT_ID_AND_STATUS_SQL = """
            SELECT c.id,
                   c.name,
                   c.status,
                   c.capacity,
                   c.id_professor,
                   u.username username_professor,
                   c.from_date,
                   c.to_date,
                   c.description
            FROM course c
                     LEFT JOIN users u on c.id_professor = u.id
                     JOIN course_student cs on c.id = cs.id_course
            WHERE cs.id_student = ? AND c.status = ?
            ORDER BY c.from_date;
                        """;
    private static final String FIND_BY_STATUS_SQL = """
            SELECT c.id,
                   c.name,
                   c.status,
                   c.capacity,
                   c.id_professor,
                   u.username username_professor,
                   c.from_date,
                   c.to_date,
                   c.description
            FROM course c
                     LEFT JOIN users u on c.id_professor = u.id
            WHERE c.status = ?
            ORDER BY c.from_date;
                        """;
    private static final String FIND_BY_ID_SQL = """
            SELECT c.id,
                   c.name,
                   c.status,
                   c.capacity,
                   c.id_professor,
                   u.username username_professor,
                   c.from_date,
                   c.to_date,
                   c.description
            FROM course c
                     LEFT JOIN users u on c.id_professor = u.id
            WHERE c.id = ?;
            """;
    private static final String UPDATE_STATUS_SQL = """
            UPDATE course
            SET status = ?
            WHERE id = ?;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT c.id,
                   c.name,
                   c.status,
                   c.capacity,
                   c.id_professor,
                   u.username username_professor,
                   c.from_date,
                   c.to_date,
                   c.description
            FROM course c
                     LEFT JOIN users u on c.id_professor = u.id
            ORDER BY c.id;
            """;
    private static final CourseDao INSTANCE = new CourseDao();

    public static CourseDao getInstance() {
        return INSTANCE;
    }

    @Override
    @SneakyThrows
    public List<Course> findAll() {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_ALL_SQL);

        TuneStatement.tune(prepareStatement);
        var resultSet = prepareStatement.executeQuery();
        List<Course> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(buildCourse(resultSet));
        }
        return list;
    }

    @Override
    @SneakyThrows
    public Optional<Course> findById(final Integer id) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_BY_ID_SQL);

        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, id);
        var resultSet = prepareStatement.executeQuery();
        Course course = null;
        if (resultSet.next()) {
            course = buildCourse(resultSet);
        }
        return Optional.ofNullable(course);
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
    public void update(final Course entity) {
    }

    @SneakyThrows
    public void nextStatus(final Integer idCourse, final String status) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(UPDATE_STATUS_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, status);
        prepareStatement.setObject(2, idCourse);

        prepareStatement.executeUpdate();
    }

    @Override
    @SneakyThrows
    public Course save(final Course entity) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, entity.getName());
        prepareStatement.setObject(2, entity.getStatus().name());
        prepareStatement.setObject(3, entity.getCapacity());
        prepareStatement.setObject(4, entity.getProfessor().getId());
        prepareStatement.setObject(5, entity.getFromDate());
        prepareStatement.setObject(6, entity.getToDate());
        prepareStatement.setObject(7, entity.getDescription());

        prepareStatement.executeUpdate();
        var resultSet = prepareStatement.getGeneratedKeys();
        resultSet.next();
        entity.setId(resultSet.getObject("id", Integer.class));
        return entity;
    }

    @SneakyThrows
    public List<Course> findByProfessorIdAndStatus(
            final Integer idProfessor, final Status status) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_BY_PROFESSOR_ID_AND_STATUS_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, idProfessor);
        prepareStatement.setObject(2, status.name());

        var resultSet = prepareStatement.executeQuery();
        List<Course> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(buildCourse(resultSet));
        }
        return list;
    }

    @SneakyThrows
    public List<Course> findByStudentIDAndStatus(
            final Integer idStudent, final Status status) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection.prepareStatement(
                FIND_BY_STUDENT_ID_AND_STATUS_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, idStudent);
        prepareStatement.setObject(2, status.name());

        var resultSet = prepareStatement.executeQuery();
        List<Course> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(buildCourse(resultSet));
        }
        return list;
    }

    @SneakyThrows
    public List<Course> findByStatus(final Status status) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_BY_STATUS_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, status.name());

        var resultSet = prepareStatement.executeQuery();
        List<Course> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(buildCourse(resultSet));
        }
        return list;
    }

    private Course buildCourse(final ResultSet resultSet)
            throws SQLException {
        User professor = User.builder()
                .id(resultSet.getObject("id_professor", Integer.class))
                .username(resultSet.getObject(
                        "username_professor", String.class))
                .build();

        return Course.builder()
                .id(resultSet.getObject("id", Integer.class))
                .name(resultSet.getObject("name", String.class))
                .status(Status.find(resultSet.getObject("status",
                        String.class)).orElse(null))
                .capacity(resultSet.getObject("capacity", Integer.class))
                .professor(professor)
                .fromDate(resultSet.getObject("from_date", Date.class)
                        .toLocalDate())
                .toDate(resultSet.getObject("to_date", Date.class)
                        .toLocalDate())
                .description(resultSet.getObject("description", String.class))
                .build();
    }
}
