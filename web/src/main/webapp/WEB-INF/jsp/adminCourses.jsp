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

<%@ include file="menuAdmin.jsp" %>

<h3><fmt:message key="page.menu.header"/></h3>

<table>
    <thead>
    <tr>
        <th><fmt:message key="page.table.id"/></th>
        <th><fmt:message key="page.table.name"/></th>
        <th><fmt:message key="page.table.status"/></th>
        <th><fmt:message key="page.table.capacity"/></th>
        <th><fmt:message key="page.table.professor"/></th>
        <th><fmt:message key="page.table.fromdate"/></th>
        <th><fmt:message key="page.table.todate"/></th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="course" items="${requestScope.courses}">
        <tr>
            <td>${course.id()}</td>
            <td>${course.name()}</td>
            <td>${course.status()}</td>
            <td>${course.capacity()}</td>
            <td>${course.professor()}</td>
            <td>${course.sfromDate()}</td>
            <td>${course.stoDate()}</td>
            <td>
                <a href="${pageContext.request.contextPath}/coursestudents?id=${course.id()}"><span><fmt:message
                        key="page.table.description"/></span></a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
