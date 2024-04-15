package ru.fildv.openclassroomutil.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class PropertyUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (var inputStream = PropertyUtil.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(final String key) {
        return PROPERTIES.getProperty(key);
    }
}
