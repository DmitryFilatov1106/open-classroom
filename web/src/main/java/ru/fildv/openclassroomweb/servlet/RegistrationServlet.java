package ru.fildv.openclassroomweb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomdb.dto.user.CreateUserDto;
import ru.fildv.openclassroomdb.entity.Gender;
import ru.fildv.openclassroomdb.entity.Role;
import ru.fildv.openclassroomservice.exception.ValidationException;
import ru.fildv.openclassroomservice.service.UserService;
import ru.fildv.openclassroomutil.util.JspHelper;
import ru.fildv.openclassroomutil.util.UrlPath;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.REGISTRATION;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(REGISTRATION)
public class RegistrationServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("roles", Role.getAllowed());
        req.setAttribute("genders", Gender.values());
        req.setAttribute("username", req.getParameter("username"));
        req.setAttribute("birthday", req.getParameter("birthday"));
        req.setAttribute("email", req.getParameter("email"));
        req.setAttribute("password", req.getParameter("password"));
        req.setAttribute("role", req.getParameter("role"));
        req.setAttribute("gender", req.getParameter("gender"));

        req.getRequestDispatcher(
                JspHelper.getPath("registration")).forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
            throws ServletException, IOException {
        var createUserDto = new CreateUserDto(
                req.getParameter("username"),
                req.getParameter("birthday"),
                req.getPart("image"),
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("role"),
                req.getParameter("gender")
        );
        try {
            userService.create(createUserDto);
            resp.sendRedirect(UrlPath.LOGIN);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            this.doGet(req, resp);
        }
    }
}
