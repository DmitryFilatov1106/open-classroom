<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="menuadmin">
    <a href="${pageContext.request.contextPath}/admin?users"><span><fmt:message key="page.menu.users"/></span>|</a>
    <%--    <a href="${pageContext.request.contextPath}/admin?professors"><span><fmt:message key="page.menu.professors"/></span>|</a>--%>
    <%--    <a href="${pageContext.request.contextPath}/admin?students"><span><fmt:message key="page.menu.students"/></span>|</a>--%>
    <a href="${pageContext.request.contextPath}/admin?courses"><span><fmt:message key="page.menu.courses"/></span></a>
</div>
