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

import static ru.fildv.openclassroomutil.util.UrlPath.ADMIN;
import static ru.fildv.openclassroomutil.util.UrlPath.ERROR;
import static ru.fildv.openclassroomutil.util.UrlPath.LOGIN;
import static ru.fildv.openclassroomutil.util.UrlPath.MENU;

@WebFilter(ADMIN)
public class AdminFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
            throws IOException, ServletException {
        var user = (UserDto) ((HttpServletRequest) servletRequest)
                .getSession().getAttribute("user");
        if (user != null) {
            var role = user.role();
            switch (role) {
                case ADMIN -> filterChain
                        .doFilter(servletRequest, servletResponse);
                case STUDENT, PROFESSOR -> (
                        (HttpServletResponse) servletResponse)
                        .sendRedirect(MENU);
                default -> ((HttpServletResponse) servletResponse)
                        .sendRedirect(ERROR);
            }
        } else {
            ((HttpServletResponse) servletResponse).sendRedirect(LOGIN);
        }
    }
}
