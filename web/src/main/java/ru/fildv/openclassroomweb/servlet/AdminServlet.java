package ru.fildv.openclassroomweb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomservice.service.CourseService;
import ru.fildv.openclassroomservice.service.UserService;
import ru.fildv.openclassroomutil.util.JspHelper;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.ADMIN;

@WebServlet(ADMIN)
public class AdminServlet extends HttpServlet {
    private final CourseService courseService = CourseService.getInstance();
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        var courses = req.getParameter("courses");
        var users = req.getParameter("users");

        if (courses != null) {
            req.setAttribute("courses", courseService.findAll());
            req.getRequestDispatcher(JspHelper.getPath("adminCourses"))
                    .forward(req, resp);
        }
        if (users != null) {
            req.setAttribute("users", userService.findAll());
            req.getRequestDispatcher(JspHelper.getPath("adminUsers"))
                    .forward(req, resp);
        }
    }
}
