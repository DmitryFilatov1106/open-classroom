package ru.fildv.openclassroomdb.dao;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.fildv.openclassroomdb.entity.Gender;
import ru.fildv.openclassroomdb.entity.Role;
import ru.fildv.openclassroomdb.entity.User;
import ru.fildv.openclassroomutil.util.ConnectionManager;
import ru.fildv.openclassroomutil.util.TuneStatement;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao implements Dao<User, Integer> {
    private static final String IMAGE_FOLDER = "users/";
    private static final String SAVE_SQL = """
            INSERT INTO users
            (username, birthday, email, image, password, role, gender)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
            """;
    private static final String GET_BY_EMAIL_AND_PASSWORD_SQL = """
            SELECT id,
                   username,
                   birthday,
                   email,
                   image,
                   password,
                   role,
                   gender
            FROM users
            WHERE email = ?
              AND password = ?;
                        """;
    private static final String GET_BY_ID_SQL = """
            SELECT id,
                   username,
                   birthday,
                   email,
                   image,
                   password,
                   role,
                   gender
            FROM users
            WHERE id = ?;
                        """;
    private static final String UPDATE_SQL = """
            UPDATE users
            SET username = ?,
                birthday = ?,
                email = ?,
                password = ?,
                role = ?,
                gender = ?
            WHERE id = ?;
            """;
    private static final String UPDATE_WITH_IMAGE_SQL = """
            UPDATE users
            SET username = ?,
                birthday = ?,
                email = ?,
                password = ?,
                role = ?,
                gender = ?,
                image = ?
            WHERE id = ?;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                   username,
                   birthday,
                   email,
                   image,
                   password,
                   role,
                   gender
            FROM users
            ORDER BY id;
            """;
    private static final UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public Optional<User> findByEmailAndPassword(
            final String email, final String password) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(GET_BY_EMAIL_AND_PASSWORD_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, email);
        prepareStatement.setObject(2, password);
        var resultSet = prepareStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = buildUser(resultSet);
        }
        return Optional.ofNullable(user);
    }

    @Override
    @SneakyThrows
    public List<User> findAll() {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(FIND_ALL_SQL);
        TuneStatement.tune(prepareStatement);

        var resultSet = prepareStatement.executeQuery();
        List<User> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(buildUser(resultSet));
        }
        return list;
    }

    @Override
    @SneakyThrows
    public Optional<User> findById(final Integer id) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(GET_BY_ID_SQL);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, id);
        var resultSet = prepareStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = buildUser(resultSet);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean delete(final Integer id) {
        return false;
    }

    @Override
    @SneakyThrows
    public void update(final User entity) {
        String sql = entity.getImage()
                .equals(IMAGE_FOLDER) ? UPDATE_SQL : UPDATE_WITH_IMAGE_SQL;

        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection.prepareStatement(sql);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, entity.getUsername());
        prepareStatement.setObject(2, entity.getBirthday());
        prepareStatement.setObject(3, entity.getEmail());
        prepareStatement.setObject(4, entity.getPassword());
        prepareStatement.setObject(5, entity.getRole().name());
        prepareStatement.setObject(6, entity.getGender().name());

        if (sql.equals(UPDATE_WITH_IMAGE_SQL)) {
            prepareStatement.setObject(7, entity.getImage());
            prepareStatement.setObject(8, entity.getId());
        } else {
            prepareStatement.setObject(7, entity.getId());
        }
        prepareStatement.executeUpdate();
    }

    @Override
    @SneakyThrows
    public User save(final User entity) {
        @Cleanup var connection = ConnectionManager.get();
        @Cleanup var prepareStatement = connection
                .prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS);
        TuneStatement.tune(prepareStatement);
        prepareStatement.setObject(1, entity.getUsername());
        prepareStatement.setObject(2, entity.getBirthday());
        prepareStatement.setObject(3, entity.getEmail());
        prepareStatement.setObject(4, entity.getImage());
        prepareStatement.setObject(5, entity.getPassword());
        prepareStatement.setObject(6, entity.getRole().name());
        prepareStatement.setObject(7, entity.getGender().name());

        prepareStatement.executeUpdate();
        var resultSet = prepareStatement.getGeneratedKeys();
        resultSet.next();
        entity.setId(resultSet.getObject("id", Integer.class));
        return entity;
    }

    private User buildUser(final ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getObject("id", Integer.class))
                .username(resultSet.getObject("username", String.class))
                .birthday(resultSet
                        .getObject("birthday", Date.class).toLocalDate())
                .email(resultSet.getObject("email", String.class))
                .image(resultSet.getObject("image", String.class))
                .password(resultSet.getObject("password", String.class))
                .role(Role.find(resultSet
                        .getObject("role", String.class)).orElse(null))
                .gender(Gender.find(resultSet
                        .getObject("gender", String.class)).orElse(null))
                .build();
    }
}
