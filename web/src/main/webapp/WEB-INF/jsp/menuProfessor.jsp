<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="menuprofessor">
    <a href="${pageContext.request.contextPath}/courses?status=announced"><span><fmt:message
            key="page.menu.announced"/></span>|</a>
    <a href="${pageContext.request.contextPath}/courses?status=waiting"><span><fmt:message
            key="page.menu.waiting"/></span>|</a>
    <a href="${pageContext.request.contextPath}/courses?status=learning"><span><fmt:message
            key="page.menu.learning"/></span>|</a>
    <a href="${pageContext.request.contextPath}/courses?status=ended"><span><fmt:message key="page.menu.ended"/></span>|</a>
    <a href="${pageContext.request.contextPath}/courses?status=archived"><span><fmt:message
            key="page.menu.archived"/></span></a>
</div>
