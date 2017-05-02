<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="com.cloudaxis.agsc.portal.helpers.ApplicationDict" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Active Global Specialised Caregivers | Edit User</title>
<style type="text/css">
.has-error{
	color: red;
}
</style>
<script type="text/javascript">
	function c_user(){
		window.location = "${contextPath}/admin/users";
	}

	function create(){
		$(".loading").show();
		$("#editForm").submit();
	}
</script>
</head>
<body>
	<jsp:include page="../header.jsp" />
	<div style="margin-left: 80px; margin-top: 10px;">
		<form:form id="editForm" method="POST" commandName="user" class="form-signin">
			<h2 class="form-signin-heading">Edit User</h2>
			<a href="${contextPath}/admin/users">Users</a> > Edit	
			<div style="background-color: lightgray; padding: 20px; width: 50%;">
			<table>
				<tr>
					<td><label>Username</label></td>
					<td>
						<div class="form-group"
							style="width: 400px; padding-left: 30px;">
							<form:input type="text" path="username" class="form-control" readonly="true" style="background-color: #d3d3d3; font-weight: bolder; padding-top: 14px;"></form:input>
						</div>
					</td>
				</tr>

				<tr>
					<td><label>First name</label></td>
					<td><spring:bind path="firstName">
							<div class="form-group ${status.error ? 'has-error' : ''}"
								style="width: 400px; padding-left: 30px;">
								<form:input type="text" path="firstName" class="form-control"
									placeholder="First name" autofocus="true"></form:input>
								<form:errors path="firstName"></form:errors>
							</div>
						</spring:bind></td>
				</tr>
				<tr>
					<td><label>Last name</label></td>
					<td><spring:bind path="lastName">
							<div class="form-group ${status.error ? 'has-error' : ''}"
								style="width: 400px; padding-left: 30px;">
								<form:input type="text" path="lastName" class="form-control"
									placeholder="Last name" autofocus="true"></form:input>
								<form:errors path="lastName"></form:errors>
							</div>
						</spring:bind></td>
				</tr>
				<tr>
					<td><label>Email</label></td>
					<td><spring:bind path="email">
							<div class="form-group ${status.error ? 'has-error' : ''}"
								style="width: 400px; padding-left: 30px;">
								<form:input type="text" path="email" class="form-control"
									placeholder="Email" autofocus="true"></form:input>
								<form:errors path="email"></form:errors>
							</div>
						</spring:bind></td>
				</tr>
				<tr>
					<td><label>Role</label></td>
					<td>
						<div class="form-group ${status.error ? 'has-error' : ''}"
							style="width: 400px; padding-left: 30px;">
							 <form:select path="role">  
					            <form:options items="<%=ApplicationDict.getRolemap()%>"/>  
					        </form:select>
						</div>
					</td>
				</tr>
				<%-- <tr>
					<td><label>Location</label></td>
					<td>
						<div class="form-group ${status.error ? 'has-error' : ''}"
							style="width: 400px; padding-left: 30px;">
							<form:select path="location">
								<form:option value="Singapore" label="Singapore" />
								<form:option value="HongKong" label="HongKong" />
								<form:option value="Taiwan" label="Taiwan" />
							</form:select>
						</div>
					</td>
				</tr> --%>
			</table>
			</div>
			<div align="right" style="width: 50%; margin-top: 5px;">
				<button class="btn btn-default" type="button" onclick="c_user();">CANCEL</button>
				<button class="btn btn-success" type="button" onclick="create();">SUBMIT</button>
			</div>
		</form:form>

	</div>
	<!-- /container -->
</body>
</html>