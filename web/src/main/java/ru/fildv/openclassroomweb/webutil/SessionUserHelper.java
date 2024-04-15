package ru.fildv.openclassroomweb.webutil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import ru.fildv.openclassroomdb.dto.user.UserDto;

@UtilityClass
public class SessionUserHelper {
    public static UserDto getUser(final HttpServletRequest req) {
        return (UserDto) req.getSession().getAttribute("user");
    }

    public static String getRole(final HttpServletRequest req) {
        var user = getUser(req);
        if (user != null) {
            return user.role().name();
        }
        return "";
    }
}
