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

<h3><fmt:message key="page.menu.users"/></h3>

<table>
    <thead>
    <tr>
        <th><fmt:message key="page.table.id"/></th>
        <th><fmt:message key="page.table.username"/></th>
        <th><fmt:message key="page.table.birthday"/></th>
        <th><fmt:message key="page.table.email"/></th>
        <th><fmt:message key="page.table.role"/></th>
        <th><fmt:message key="page.table.gender"/></th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${requestScope.users}">
        <tr>
            <td>${user.id()}</td>
            <td>${user.username()}</td>
            <td>${user.birthday()}</td>
            <td>${user.email()}</td>
            <td>${user.role()}</td>
            <td>${user.gender()}</td>
            <td>
                <a href="${pageContext.request.contextPath}/users?userId=${user.id()}"><span><fmt:message
                        key="page.table.description"/></span></a>
            </td>
        </tr>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
