package ru.fildv.openclassroomweb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static ru.fildv.openclassroomutil.util.UrlPath.LOCALE;
import static ru.fildv.openclassroomutil.util.UrlPath.LOGIN;

@WebServlet(LOCALE)
public class LocaleServlet extends HttpServlet {
    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
            throws ServletException, IOException {
        var language = req.getParameter("language");
        req.getSession().setAttribute("lang", language);
        var prevPage = req.getHeader("referer");
        var page = prevPage != null ? prevPage : LOGIN;
        resp.sendRedirect(page);
    }
}
