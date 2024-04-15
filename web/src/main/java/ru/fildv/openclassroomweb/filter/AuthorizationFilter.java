package ru.fildv.openclassroomweb.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomdb.dto.user.UserDto;

import java.io.IOException;
import java.util.Set;

import static ru.fildv.openclassroomutil.util.UrlPath.ALL;
import static ru.fildv.openclassroomutil.util.UrlPath.IMAGES;
import static ru.fildv.openclassroomutil.util.UrlPath.LOCALE;
import static ru.fildv.openclassroomutil.util.UrlPath.LOGIN;
import static ru.fildv.openclassroomutil.util.UrlPath.REGISTRATION;


@WebFilter(ALL)
public class AuthorizationFilter implements Filter {
    private static final Set<String> PUBLIC_PATH
            = Set.of(LOGIN, REGISTRATION, IMAGES, LOCALE);

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
            throws IOException, ServletException {
        String uri = ((HttpServletRequest) servletRequest).getRequestURI();
        if (isPublicPath(uri) || isUserLoggedIn(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            var prevPage = ((HttpServletRequest) servletRequest)
                    .getHeader("referer");
            ((HttpServletResponse) servletResponse)
                    .sendRedirect(prevPage != null ? prevPage : LOGIN);
        }
    }

    private boolean isUserLoggedIn(final ServletRequest servletRequest) {
        // можно проверить на роли! userDto.getRole() = Role.ADMIN
        var userDto = (UserDto) (
                (HttpServletRequest) servletRequest)
                .getSession()
                .getAttribute("user");
        return userDto != null;
    }

    private boolean isPublicPath(final String uri) {
        return PUBLIC_PATH.stream()
                .anyMatch(uri::startsWith);
    }
}
