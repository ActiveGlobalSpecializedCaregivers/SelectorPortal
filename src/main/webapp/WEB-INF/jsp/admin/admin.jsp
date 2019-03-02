<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Active Global Specialised Caregivers | ADMIN</title>
</head>
<body>
	<jsp:include page="../header.jsp" />
	<table align="center"  style="margin-top: 200px; height: 151px; width: 274px; text-align: center; background-color: aliceblue">		
		<tr>
			<td style="font-size: xx-large">
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<a href="${contextPath}/user/create">Create User</a>
				</sec:authorize>
				<sec:authorize access="!hasRole('ROLE_ADMIN')">
					Create User
				</sec:authorize>
			</td>
		</tr>
		<tr>
			<td style="font-size: xx-large">
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<a href="${contextPath}/user/delete">Delete User</a>
				</sec:authorize>
				<sec:authorize access="!hasRole('ROLE_ADMIN')">
					Delete User
				</sec:authorize>
			</td>
		</tr>
	</table>
</body>
</html>