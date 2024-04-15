package ru.fildv.openclassroomweb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomdb.dto.user.UserDto;
import ru.fildv.openclassroomservice.service.UserService;
import ru.fildv.openclassroomutil.util.JspHelper;
import ru.fildv.openclassroomutil.util.UrlPath;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.LOGIN;

@WebServlet(LOGIN)
public class LoginServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("email", req.getParameter("email"));
        req.getRequestDispatcher(JspHelper.getPath("login")).forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp) {
        var email = req.getParameter("email");
        var password = req.getParameter("password");
        userService.login(email, password).ifPresentOrElse(
                user -> onLoginSuccess(user, req, resp),
                () -> onLoginFail(req, resp)
        );
    }

    private void onLoginFail(final HttpServletRequest req,
                             final HttpServletResponse resp) {
        try {
            resp.sendRedirect(LOGIN
                    + "?error&email=" + req.getParameter("email"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onLoginSuccess(final UserDto user,
                                final HttpServletRequest req,
                                final HttpServletResponse resp) {
        try {
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(UrlPath.MENU);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
