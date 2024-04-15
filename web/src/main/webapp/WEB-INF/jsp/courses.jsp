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

<c:if test="${requestScope.status == 'announced'}">
    <h3><fmt:message key="page.menu.announced"/></h3>
    <c:if test="${requestScope.role == 'PROFESSOR'}">
        <br/>
        <a href="${pageContext.request.contextPath}/courseadd">[<span><fmt:message key="page.menu.add"/></span>]</a>
    </c:if>
</c:if>
<c:if test="${requestScope.status == 'waiting'}">
    <h3><fmt:message key="page.menu.waiting"/></h3>
</c:if>
<c:if test="${requestScope.status == 'learning'}">
    <h3><fmt:message key="page.menu.learning"/></h3>
</c:if>
<c:if test="${requestScope.status == 'ended'}">
    <h3><fmt:message key="page.menu.ended"/></h3>
</c:if>
<c:if test="${requestScope.status == 'archived'}">
    <h3><fmt:message key="page.menu.archived"/></h3>
</c:if>

<table>
    <thead>
    <tr>
        <th><fmt:message key="page.table.id"/></th>
        <c:if test="${requestScope.role == 'PROFESSOR'}">
            <c:if test="${requestScope.status != 'archived'}">
                <th><fmt:message key="page.table.nextstatus"/></th>
            </c:if>
        </c:if>
        <th><fmt:message key="page.table.name"/></th>
        <th><fmt:message key="page.table.capacity"/></th>
        <c:if test="${requestScope.role != 'PROFESSOR'}">
            <th><fmt:message key="page.table.professor"/></th>
        </c:if>
        <th><fmt:message key="page.table.fromdate"/></th>
        <th><fmt:message key="page.table.todate"/></th>
        <c:if test="${requestScope.role == 'STUDENT'}">
            <th colspan="3"></th>
        </c:if>
        <c:if test="${requestScope.role != 'STUDENT'}">
            <th colspan="2"></th>
        </c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="course" items="${requestScope.courses}">
        <tr>
            <td>${course.id()}</td>

            <c:if test="${requestScope.role == 'PROFESSOR'}">
                <c:if test="${requestScope.status != 'archived'}">
                    <td>
                        <a href="${pageContext.request.contextPath}/coursenextstatus?id=${course.id()}">==>></a>
                    </td>
                </c:if>
            </c:if>

            <td>${course.name()}</td>
            <td>${course.capacity()}</td>
            <c:if test="${requestScope.role != 'PROFESSOR'}">
                <td>${course.professor()}</td>
            </c:if>
            <td>${course.sfromDate()}</td>
            <td>${course.stoDate()}</td>

            <c:if test="${requestScope.role == 'PROFESSOR'}">
                <td>
                    <a href="${pageContext.request.contextPath}/coursestudents?id=${course.id()}"><span><fmt:message
                            key="page.table.students"/></span></a>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/coursedelete?id=${course.id()}"><span><fmt:message
                            key="page.table.delete"/></span></a>
                </td>
            </c:if>
            <c:if test="${requestScope.role == 'STUDENT'}">
                <td>
                    <a href="${pageContext.request.contextPath}/coursedescription?id=${course.id()}"><span><fmt:message
                            key="page.table.description"/></span></a>
                </td>
                <c:if test="${requestScope.status == 'announced'}">
                    <td>
                        <c:if test="${course.inlist() == false}">
                            <a href="${pageContext.request.contextPath}/courseenroll?id=${course.id()}"><span><fmt:message
                                    key="page.table.enroll"/></span></a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${course.inlist() == true}">
                            <a href="${pageContext.request.contextPath}/coursecansel?id=${course.id()}"><span><fmt:message
                                    key="page.table.cansel"/></span></a>
                        </c:if>
                    </td>
                </c:if>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
