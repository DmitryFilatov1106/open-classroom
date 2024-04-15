package ru.fildv.openclassroomutil.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspHelper {
    private static final String JSP_FORMAT = "/WEB-INF/jsp/%s.jsp";

    public String getPath(final String jspName) {
        return String.format(JSP_FORMAT, jspName);
    }
}
