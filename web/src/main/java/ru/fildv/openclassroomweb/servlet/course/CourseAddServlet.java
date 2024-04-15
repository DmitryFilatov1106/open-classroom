package ru.fildv.openclassroomweb.servlet.course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomdb.dto.course.CreateCourseDto;
import ru.fildv.openclassroomdb.entity.Status;
import ru.fildv.openclassroomservice.exception.ValidationException;
import ru.fildv.openclassroomservice.service.CourseService;
import ru.fildv.openclassroomutil.util.JspHelper;
import ru.fildv.openclassroomutil.util.UrlPath;
import ru.fildv.openclassroomweb.webutil.SessionUserHelper;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.COURSE_ADD;

@WebServlet(COURSE_ADD)
public class CourseAddServlet extends HttpServlet {
    private final CourseService courseService = CourseService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("name", req.getParameter("name"));
        req.setAttribute("capacity", req.getParameter("capacity"));
        req.setAttribute("status", req.getParameter("status"));
        req.setAttribute("fromdate", req.getParameter("fromdate"));
        req.setAttribute("todate", req.getParameter("todate"));
        req.setAttribute("description", req.getParameter("description"));

        req.setAttribute("role", SessionUserHelper.getRole(req));

        req.setAttribute("edit", false);
        req.setAttribute("status", Status.ANNOUNCED);
        req.getRequestDispatcher(JspHelper.getPath("course"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
            throws ServletException, IOException {
        var user = SessionUserHelper.getUser(req);
        Integer capacity;
        try {
            capacity = Integer.parseInt(req.getParameter("capacity"));
        } catch (Exception e) {
            capacity = 0;
        }
        var createCourseDto = new CreateCourseDto(
                req.getParameter("name"),
                req.getParameter("status"),
                capacity,
                user.id(),
                req.getParameter("fromdate"),
                req.getParameter("todate"),
                req.getParameter("description")
        );
        try {
            courseService.create(createCourseDto);
            resp.sendRedirect(UrlPath.COURSES + "?status=announced");
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            this.doGet(req, resp);
        }
    }
}
