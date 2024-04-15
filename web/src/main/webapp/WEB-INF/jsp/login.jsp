<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="lang.jsp" %>
<html>
<head>
    <title><fmt:message key="page.login.header"/></title>
</head>
<body>
<%@ include file="header.jsp" %>

<h3><fmt:message key="page.login.header"/></h3>
<form action="${pageContext.request.contextPath}/login" method="post">
    <p>
        <label for="email"><fmt:message key="page.login.Email"/>:
            <input type="text" name="email" id="email" value="${param.email}" required>
        </label>
    </p>
    <p>
        <label for="password"><fmt:message key="page.login.Password"/>:
            <input type="password" name="password" id="password" required>
        </label>
    </p>
    <p>
        <button type="submit"><fmt:message key="page.login.submit.button"/></button>
    </p>
    <c:if test="${param.error != null}">
        <div style="color: red">
            <span><fmt:message key="page.login.error"/></span>
        </div>
    </c:if>
</form>

<a href="${pageContext.request.contextPath}/registration">
    <button type="button"><fmt:message key="page.login.register.button"/></button>
</a>
</body>
</html>
