package ru.fildv.openclassroomutil.util;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@UtilityClass
public class ConnectionManager {
    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DRIVER_KEY = "db.driver";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE = 5;
    private static final String SAVE_ADMIN_SQL = """
            INSERT INTO users
            (username, email, password, role, gender)
            VALUES ('admin', 'a@mail.ru', '123', 'ADMIN', 'MALE');
                        """;
    private static final String GET_ANY_ADMIN_SQL = """
            SELECT id, username, birthday, email, image, password, role, gender
            FROM users
            WHERE role = 'ADMIN'
            LIMIT 1;
            """;
    private static final String INIT_DDL = """
            CREATE TABLE IF NOT EXISTS users
            (
                id       SERIAL PRIMARY KEY,
                username VARCHAR(64)  NOT NULL,
                birthday DATE DEFAULT '2000-01-01',
                email    VARCHAR(32)  NOT NULL UNIQUE,
                image    VARCHAR(124) DEFAULT 'users/',
                password VARCHAR(16)  NOT NULL,
                role     VARCHAR(16)  NOT NULL,
                gender   VARCHAR(16)  NOT NULL
            );

            CREATE TABLE IF NOT EXISTS course
            (
                id           SERIAL PRIMARY KEY,
                name         VARCHAR(64) NOT NULL UNIQUE,
                status       VARCHAR(16) NOT NULL,
                capacity     INTEGER     NOT NULL,
                id_professor INTEGER REFERENCES users (id),
                from_date    DATE        not null,
                to_date      DATE        not null,
                description  VARCHAR(256)
            );

            CREATE TABLE IF NOT EXISTS course_student
            (
                id         SERIAL PRIMARY KEY,
                id_course  INTEGER REFERENCES course (id),
                id_student INTEGER REFERENCES users (id),
                grade      INTEGER
            );
                        """;
    private volatile BlockingQueue<Connection> pool;
    private List<Connection> poolSource;
    private volatile DBConnectionProvider dbConnectionProvider;

    @SneakyThrows
    public Connection get() {
        if (pool == null) {
            synchronized (ConnectionManager.class) {
                if (pool == null) {
                    loadDriver();
                    initConnectionPool();
                    initDatabase(get());
                }
            }
        }
        return pool.take();
    }

    @SneakyThrows
    public void initDatabase(final Connection connection) {
        @Cleanup var cs = connection.createStatement();

        var resultDDL = cs.execute(getInitScript());

        System.out.println("Database tables created! resultDDL: " + resultDDL);

        if (!checkAdmin(connection)) {
            createAdmin(connection);
            System.out.println("Admin is created.");
        }
    }

    @SneakyThrows
    public void closePool() {
        for (Connection connection : poolSource) {
            connection.close();
        }
    }

    public void setDbConnectionProvider(
            final DBConnectionProvider pDbConnectionProvider) {
        dbConnectionProvider = pDbConnectionProvider;
    }

    private String getInitScript() {
        return INIT_DDL;
    }

    @SneakyThrows
    private void createAdmin(final Connection connection) {
        @Cleanup var prepareStatement = connection
                .prepareStatement(SAVE_ADMIN_SQL);
        prepareStatement.executeUpdate();
    }

    @SneakyThrows
    private boolean checkAdmin(final Connection connection) {
        @Cleanup var prepareStatement = connection
                .prepareStatement(GET_ANY_ADMIN_SQL);
        TuneStatement.tune(prepareStatement);
        return prepareStatement.executeQuery().next();
    }

    private void initConnectionPool() {
        String poolSize = PropertyUtil.get(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE
                : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        poolSource = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(
                    ConnectionManager.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy)
                            : method.invoke(connection, args)
            );
            pool.add(proxyConnection);
            poolSource.add(connection);
        }
    }

    @SneakyThrows
    private void loadDriver() {
        Class.forName(PropertyUtil.get(DRIVER_KEY));
    }

    @SneakyThrows
    private Connection open() {
        if (dbConnectionProvider == null) {
            synchronized (ConnectionManager.class) {
                if (dbConnectionProvider == null) {
                    dbConnectionProvider = new DBConnectionProvider(
                            PropertyUtil.get(URL_KEY),
                            PropertyUtil.get(USER_KEY),
                            PropertyUtil.get(PASSWORD_KEY));
                }
            }
        }
        return dbConnectionProvider.getConnection();

    }

    @RequiredArgsConstructor
    public class DBConnectionProvider {
        private final String url;
        private final String username;
        private final String password;

        @SneakyThrows
        public Connection getConnection() {
            return DriverManager.getConnection(url, username, password);
        }
    }
}
