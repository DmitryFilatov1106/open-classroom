package ru.fildv.openclassroomweb.servlet.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomdb.dto.course.CourseDto;
import ru.fildv.openclassroomservice.service.CourseService;
import ru.fildv.openclassroomservice.service.CourseStudentService;
import ru.fildv.openclassroomutil.util.JspHelper;
import ru.fildv.openclassroomweb.webutil.SessionUserHelper;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.COURSE_DESCRIPTION;

@WebServlet(COURSE_DESCRIPTION)
public class CourseDescriptionServlet extends HttpServlet {
    private final CourseService courseService = CourseService.getInstance();
    private final CourseStudentService courseStudentService
            = CourseStudentService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        var idCourse = Integer.parseInt(req.getParameter("id"));
        var user = SessionUserHelper.getUser(req);

        CourseDto courseDto = courseService.getDescription(idCourse);
        req.setAttribute("course", courseDto);
        req.setAttribute("role", user.role().name());
        req.setAttribute("grade", courseStudentService
                .getGrade(idCourse, user.id()));
        req.getRequestDispatcher(JspHelper
                .getPath("courseDescription")).forward(req, resp);
    }
}
