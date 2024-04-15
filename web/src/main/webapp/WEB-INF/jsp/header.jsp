<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="lang.jsp" %>
<div id="head">
    <div id="locale">
        <form action="${pageContext.request.contextPath}/locale" method="post">
            <button type="submit" name="language" value="ru_RU">RU</button>
            <button type="submit" name="language" value="en_US">EN</button>
        </form>
    </div>

    <c:if test="${not empty sessionScope.user}">
        <div id="logout">
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <span>
                    <c:if test="${sessionScope.user.role()=='ADMIN'}">
                        <h4><fmt:message key="page.main.admin"/> ${sessionScope.user.username()}</h4>
                    </c:if>
                    <c:if test="${sessionScope.user.role()=='PROFESSOR'}">
                        <h4><fmt:message key="page.main.professor"/> ${sessionScope.user.username()}</h4>
                    </c:if>
                    <c:if test="${sessionScope.user.role()=='STUDENT'}">
                        <h4><fmt:message key="page.main.student"/> ${sessionScope.user.username()}</h4>
                    </c:if>
                </span>
                <button type="submit"><fmt:message key="page.main.logout"/></button>
            </form>
            <form action="${pageContext.request.contextPath}/users">
                <input id="userId" type="number" name="userId" value="${sessionScope.user.id()}" hidden>
                <button type="submit"><fmt:message key="page.users.ref"/></button>
            </form>
        </div>
    </c:if>

    <h1><fmt:message key="page.main.header"/></h1>
</div>
