package ru.fildv.openclassroomweb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomservice.service.UserService;
import ru.fildv.openclassroomutil.util.JspHelper;
import ru.fildv.openclassroomutil.util.UrlPath;
import ru.fildv.openclassroomweb.webutil.SessionUserHelper;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.MENU;

@WebServlet(MENU)
public class MenuServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        var user = SessionUserHelper.getUser(req);
        if (user != null) {
            req.setAttribute("role", user.role());
            req.getRequestDispatcher(
                    JspHelper.getPath("menu")).forward(req, resp);
        } else {
            resp.sendRedirect(UrlPath.LOGIN);
        }
    }
}
