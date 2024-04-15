package ru.fildv.openclassroomweb.servlet.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomdb.entity.Role;
import ru.fildv.openclassroomdb.entity.Status;
import ru.fildv.openclassroomservice.service.CourseService;
import ru.fildv.openclassroomutil.util.JspHelper;
import ru.fildv.openclassroomutil.util.UrlPath;
import ru.fildv.openclassroomweb.webutil.SessionUserHelper;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.COURSES;

@WebServlet(COURSES)
public class CoursesServlet extends HttpServlet {
    private final CourseService courseService = CourseService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        var user = SessionUserHelper.getUser(req);
        if (user != null) {
            req.setAttribute("role", user.role());
        } else {
            resp.sendRedirect(UrlPath.LOGIN);
        }

        var status = req.getParameter("status");
        req.setAttribute("status", status);
        if (user.role() == Role.PROFESSOR) {
            req.setAttribute("courses",
                    courseService.findByProfessorIdAndStatus(
                            user.id(),
                            Status.valueOf(status.toUpperCase())));
        }
        if (user.role() == Role.STUDENT) {
            if (status.toUpperCase().equals(Status.ANNOUNCED.name())) {
                req.setAttribute("courses",
                        courseService.findByStatusWithInlist(
                                Status.valueOf(status.toUpperCase()),
                                user.id()));
            } else {
                req.setAttribute("courses",
                        courseService.findByStudentIDAndStatus(
                                user.id(),
                                Status.valueOf(status.toUpperCase())));
            }
        }

        req.getRequestDispatcher(JspHelper.getPath("courses"))
                .forward(req, resp);
    }
}
