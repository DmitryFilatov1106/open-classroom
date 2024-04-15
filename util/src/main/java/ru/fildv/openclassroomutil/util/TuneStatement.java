package ru.fildv.openclassroomutil.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.PreparedStatement;

@UtilityClass
public class TuneStatement {
    private static final String FETCH_SIZE = "db.fetch-size";
    private static final String TIMEOUT = "db.timeout";
    private static final String MAX_ROWS = "db.max-rows";

    @SneakyThrows
    public void tune(final PreparedStatement preparedStatement) {
        preparedStatement.setFetchSize(
                Integer.parseInt(PropertyUtil.get(FETCH_SIZE)));
        preparedStatement.setQueryTimeout(
                Integer.parseInt(PropertyUtil.get(TIMEOUT)));
        preparedStatement.setMaxRows(
                Integer.parseInt(PropertyUtil.get(MAX_ROWS)));
    }
}
