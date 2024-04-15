<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="lang.jsp" %>
<html>
<head>
    <title><fmt:message key="page.course.headeradd"/></title>
</head>
<body>
<%@ include file="header.jsp" %>

<c:if test="${requestScope.role == 'ADMIN'}">
    <%@ include file="menuAdmin.jsp" %>
</c:if>

<c:if test="${requestScope.role == 'PROFESSOR'}">
    <h3><fmt:message key="page.menu.header"/></h3>
    <%@ include file="menuProfessor.jsp" %>
</c:if>

<c:if test="${requestScope.role == 'STUDENT'}">
    <h3><fmt:message key="page.menu.header"/></h3>
    <%@ include file="menuStudent.jsp" %>
</c:if>

<h3><fmt:message key="page.course.headerdescription"/></h3>
<p>
    <fmt:message key="page.course.id"/>: ${course.id()}
</p>
<p>
    <fmt:message key="page.course.name"/>: ${course.name()}
</p>
<p>
    <fmt:message key="page.course.professor"/>: ${course.professor()}
</p>
<p>
    <fmt:message key="page.course.status"/>: ${course.status()}
</p>
<p>
    <fmt:message key="page.course.capacity"/>: ${course.capacity()}
</p>
<p>
    <fmt:message key="page.course.fromdate"/>: ${course.sfromDate()}
</p>
<p>
    <fmt:message key="page.course.todate"/>: ${course.stoDate()}
</p>
<p>
    <fmt:message key="page.course.description"/>: ${course.description()}
</p>
<p>
    <fmt:message key="page.course.grade"/>: ${grade}
</p>
</body>
</html>
