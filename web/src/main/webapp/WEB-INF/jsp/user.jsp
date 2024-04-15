<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="lang.jsp" %>
<html>
<head>
    <title><fmt:message key="page.main.header"/></title>
</head>
<body>
<%@ include file="header.jsp" %>

<c:if test="${requestScope.sessionRole == 'ADMIN'}">
    <%@ include file="menuAdmin.jsp" %>
</c:if>

<c:if test="${requestScope.sessionRole == 'PROFESSOR'}">
    <h3><fmt:message key="page.menu.header"/></h3>
    <%@ include file="menuProfessor.jsp" %>
</c:if>

<c:if test="${requestScope.sessionRole == 'STUDENT'}">
    <h3><fmt:message key="page.menu.header"/></h3>
    <%@ include file="menuStudent.jsp" %>
</c:if>

<h3><fmt:message key="page.users.ref"/></h3>

<form action="${pageContext.request.contextPath}/users" method="post" enctype="multipart/form-data">
    <input type="number" name="userId" id="userId" value="${requestScope.userId}" hidden>
    <p>
        <label for="username"><fmt:message key="page.registration.name"/>:
            <input type="text" name="username" id="username" value="${requestScope.username}">
        </label>
    </p>
    <p>
        <label for="birthday"><fmt:message key="page.registration.birthday"/>:
            <input type="date" name="birthday" id="birthday" value="${requestScope.birthday}">
        </label>
    </p>
    <p>
        <label for="email"><fmt:message key="page.registration.email"/>:
            <input type="text" name="email" id="email" value="${requestScope.email}" readonly>
        </label>
    </p>
    <p>
        <label for="password"><fmt:message key="page.registration.password"/>:
            <input type="password" name="password" id="password" value="${requestScope.password}">
        </label>
    </p>
    <p>
        <img src="${pageContext.request.contextPath}${requestScope.pathImage}" width="200" height="200" alt="foto">
        <br/>
        <label for="image"><fmt:message key="page.registration.image"/>:
            <input type="file" name="image" id="image">
        </label>
    </p>
    <p>
        <label for="role"><fmt:message key="page.registration.role"/>:
            <input type="text" name="role" id="role" value="${requestScope.role}" readonly>
        </label>
    </p>
    <p>
        <label for="gender"><fmt:message key="page.registration.gender"/>:
            <c:forEach var="gender" items="${requestScope.genders}">
                <c:if test="${gender == requestScope.gender}">
                    <input type="radio" name="gender" value="${gender}" checked>${gender}
                </c:if>
                <c:if test="${gender != requestScope.gender}">
                    <input type="radio" name="gender" value="${gender}">${gender}
                </c:if>
            </c:forEach>
        </label>
    </p>
    <p>
        <button type="submit"><fmt:message key="page.registration.submit.button"/></button>
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
