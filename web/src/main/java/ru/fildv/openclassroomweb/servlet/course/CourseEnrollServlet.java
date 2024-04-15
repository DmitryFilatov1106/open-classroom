package ru.fildv.openclassroomweb.servlet.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomservice.service.CourseStudentService;
import ru.fildv.openclassroomweb.webutil.SessionUserHelper;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.COURSE_ENROLL;
import static ru.fildv.openclassroomutil.util.UrlPath.LOGIN;

@WebServlet(COURSE_ENROLL)
public class CourseEnrollServlet extends HttpServlet {
    private final CourseStudentService courseStudentService
            = CourseStudentService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        var idCourse = Integer.parseInt(req.getParameter("id"));
        var user = SessionUserHelper.getUser(req);

        courseStudentService.enroll(idCourse, user.id());

        var prevPage = req.getHeader("referer");
        var page = prevPage != null ? prevPage : LOGIN;
        resp.sendRedirect(page);
    }
}
