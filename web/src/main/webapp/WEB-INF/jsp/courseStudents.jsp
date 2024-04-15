<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="lang.jsp" %>
<html>
<head>
    <title><fmt:message key="page.course.headeradd"/></title>
    <style rel="stylesheet">
        td {
            border: 1px solid gray;
        }

        th {
            border: 1px solid gray;
            text-align: center;
        }
    </style>
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

<h3><fmt:message key="page.course.headerstudents"/></h3>
<p>
    <fmt:message key="page.course.id"/>: ${course.id()}
</p>
<p>
    <fmt:message key="page.course.name"/>: ${course.name()}
</p>
<c:if test="${requestScope.role == 'ADMIN'}">
    <p>
        <fmt:message key="page.course.professor"/>: ${course.professor()}
    </p>
</c:if>
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

<table>
    <thead>
    <tr>
        <th><fmt:message key="page.table.id"/></th>
        <th><fmt:message key="page.table.username"/></th>
        <th><fmt:message key="page.table.email"/></th>
        <c:if test="${course.status() == 'ARCHIVED' || course.status() == 'ENDED'}">
            <th><fmt:message key="page.table.grade"/></th>
        </c:if>
    </tr>
    </thead>
    <tbody>
    <c:if test="${course.status() != 'ENDED'}">
        <c:forEach var="student" items="${requestScope.students}">
            <tr>
                <td>${student.id()}</td>
                <td>${student.username()}</td>
                <td>${student.email()}</td>
                <c:if test="${course.status() == 'ARCHIVED'}">
                    <td>${student.grade()}</td>
                </c:if>
            </tr>
        </c:forEach>
    </c:if>

    <c:if test="${course.status() == 'ENDED'}">

        <c:forEach var="student" items="${requestScope.students}">
            <tr>
                <td>${student.id()}</td>
                <td>${student.username()}</td>
                <td>${student.email()}</td>
                <td>
                    <c:if test="${requestScope.role == 'ADMIN'}">
                        ${student.grade()}
                    </c:if>
                    <c:if test="${requestScope.role != 'ADMIN'}">
                        <form action="${pageContext.request.contextPath}/coursestudents" method="post">
                            <input type="number" name="course" value="${course.id()}" hidden>
                            <input type="number" name="student" value="${student.id()}" hidden>
                            <input type="number" name="grade" value="${student.grade()}">
                            <button type="submit"><fmt:message key="page.course.submit.button"/></button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    </tbody>
</table>

</body>
</html>
