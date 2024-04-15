package ru.fildv.openclassroomweb.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fildv.openclassroomutil.util.UrlPath;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.LOGOUT;

@WebServlet(LOGOUT)
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
            throws IOException {
        var lang = req.getSession().getAttribute("lang");

        req.getSession().invalidate();

        if (lang != null) {
            req.getSession().setAttribute("lang", lang);
        }
        resp.sendRedirect(UrlPath.LOGIN);
    }
}
