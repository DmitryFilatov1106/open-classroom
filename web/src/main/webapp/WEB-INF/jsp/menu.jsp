<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="lang.jsp" %>
<html>
<head>
    <title><fmt:message key="page.main.header"/></title>
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

</body>
</html>
