package ru.fildv.openclassroomweb.servlet.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomservice.service.CourseService;
import ru.fildv.openclassroomutil.util.UrlPath;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.COURSE_DELETE;

@WebServlet(COURSE_DELETE)
public class CourseDeleteServlet extends HttpServlet {
    private final CourseService courseService = CourseService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            var idCourse = Integer.parseInt(req.getParameter("id"));
            var status = courseService.delete(idCourse);
            resp.sendRedirect(UrlPath.COURSES + "?status=" + status);
        } catch (Exception e) {
            resp.sendRedirect(UrlPath.ERROR);
        }
    }
}
