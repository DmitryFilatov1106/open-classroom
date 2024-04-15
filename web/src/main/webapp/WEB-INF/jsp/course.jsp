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
<h3><fmt:message key="page.menu.header"/></h3>

<c:if test="${requestScope.role == 'ADMIN'}">
    <%@ include file="menuAdmin.jsp" %>
</c:if>

<c:if test="${requestScope.role == 'PROFESSOR'}">
    <%@ include file="menuProfessor.jsp" %>
</c:if>

<c:if test="${requestScope.role == 'STUDENT'}">
    <%@ include file="menuStudent.jsp" %>
</c:if>

<h3><fmt:message key="page.course.headeradd"/></h3>

<form action="${pageContext.request.contextPath}/courseadd" method="post">
    <p>
        <label for="name"><fmt:message key="page.course.name"/>:
            <input type="text" name="name" id="name" value="${requestScope.name}">
        </label>
    </p>
    <p>
        <label for="status"><fmt:message key="page.course.status"/>:
            <input type="text" name="status" id="status" value="${requestScope.status}" readonly>
        </label>
    </p>
    <p>
        <label for="capacity"><fmt:message key="page.course.capacity"/>:
            <input type="number" name="capacity" id="capacity" value="${requestScope.capacity}">
        </label>
    </p>
    <p>
        <label for="fromdate"><fmt:message key="page.course.fromdate"/>:
            <input type="date" name="fromdate" id="fromdate" value="${requestScope.fromdate}">
        </label>
    </p>
    <p>
        <label for="todate"><fmt:message key="page.course.todate"/>:
            <input type="date" name="todate" id="todate" value="${requestScope.todate}">
        </label>
    </p>
    <p>
        <label for="description"><fmt:message key="page.course.description"/>:
            <textarea name="description" id="description" maxlength="256">${requestScope.description}</textarea>
        </label>
    </p>

    <p>
        <button type="submit"><fmt:message key="page.course.submit.button"/></button>
    </p>
    <c:if test="${not empty requestScope.errors}">
        <div style="color:red">
            <ul>
                <c:forEach var="err" items="${requestScope.errors}">
                    <li>
                            ${err.message()}
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
</form>
</body>
</html>
