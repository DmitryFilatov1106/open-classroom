package ru.fildv.openclassroomweb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomdb.dto.user.UpdateUserDto;
import ru.fildv.openclassroomdb.dto.user.UserDto;
import ru.fildv.openclassroomdb.entity.Gender;
import ru.fildv.openclassroomservice.exception.ValidationException;
import ru.fildv.openclassroomservice.service.UserService;
import ru.fildv.openclassroomutil.util.JspHelper;
import ru.fildv.openclassroomutil.util.UrlPath;
import ru.fildv.openclassroomweb.webutil.SessionUserHelper;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.LOGIN;
import static ru.fildv.openclassroomutil.util.UrlPath.USERS;


@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(USERS)
public class UserServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        var sessionUser = SessionUserHelper.getUser(req);

        var userId = Integer.parseInt(req.getParameter("userId"));

        var servletUser = userService.findById(userId);
        if (servletUser.isPresent()) {
            UserDto user = servletUser.get();

            req.setAttribute("sessionRole", sessionUser.role());

            req.setAttribute("genders", Gender.values());

            req.setAttribute("userId", user.id());

            req.setAttribute("username", req.getParameter("username") == null
                    ? user.username() : req.getParameter("username"));
            req.setAttribute("birthday", req.getParameter("birthday") == null
                    ? user.birthday() : req.getParameter("birthday"));
            req.setAttribute("email", req.getParameter("email") == null
                    ? user.email() : req.getParameter("email"));
            req.setAttribute("role", user.role());
            req.setAttribute("gender", req.getParameter("gender") == null
                    ? user.gender() : req.getParameter("gender"));

            req.setAttribute("pathImage", UrlPath.IMAGES + "/" + user.image());

            req.getRequestDispatcher(JspHelper.getPath("user"))
                    .forward(req, resp);
        } else {
            var prevPage = req.getHeader("referer");
            var page = prevPage != null ? prevPage : LOGIN;
            resp.sendRedirect(page);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
            throws ServletException, IOException {
        var updateUserDto = new UpdateUserDto(
                Integer.parseInt(req.getParameter("userId")),
                req.getParameter("username"),
                req.getParameter("birthday"),
                req.getPart("image"),
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("role"),
                req.getParameter("gender")
        );
        try {
            userService.update(updateUserDto);

            var sessionUser = SessionUserHelper.getUser(req);
            if (sessionUser.id().equals(updateUserDto.id())) {
                userService.login(
                                updateUserDto.email(),
                                updateUserDto.password())
                        .ifPresentOrElse(
                                user -> req
                                        .getSession()
                                        .setAttribute("user", user),
                                () -> {
                                    try {
                                        this.doGet(req, resp);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
            }
            resp.sendRedirect(USERS + "?userId=" + req.getParameter("userId"));
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            this.doGet(req, resp);
        }
    }
}
