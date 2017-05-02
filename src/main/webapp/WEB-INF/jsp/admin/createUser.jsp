<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Active Global Specialised Caregivers | Create New User</title>
<script type="text/javascript">
	function c_user() {
		window.location = "${contextPath}/admin/users";
	}
	
	function create(){
		$(".loading").show();
		$("#userForm").submit();
	}
</script>
</head>
<body>
	<jsp:include page="../header.jsp" />
	<div style="margin-left: 80px; margin-top: 10px">
		<form:form id="userForm" method="POST" commandName="user" class="form-signin">
			<h2 class="form-signin-heading">Create New User</h2>
		    <a href="${contextPath}/admin/users">Users</a> > Create	
			<div style="background-color: lightgray; padding: 20px; width: 50%;">
				<table>
					<tr>
						<td><label>Username</label></td>
						<td><spring:bind path="username">
								<div class="form-group ${status.error ? 'has-error' : ''}"
									style="width: 400px; padding-left: 30px;">
									<form:input type="text" path="username" class="form-control"
										placeholder="Username" autofocus="true"></form:input>
									<form:errors path="username"></form:errors>
								</div>
							</spring:bind></td>
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
						<td><label>Registration Number</label></td>
						<td><spring:bind path="registrationNumber">
								<div class="form-group ${status.error ? 'has-error' : ''}"
									style="width: 400px; padding-left: 30px;">
									<form:input type="text" path="registrationNumber" class="form-control"
										placeholder="registrationNumber" autofocus="true"></form:input>
									<form:errors path="registrationNumber"></form:errors>
								</div>
							</spring:bind></td>
					</tr>
					<tr>
						<td><label>Password</label></td>
						<td><spring:bind path="password">
								<div class="form-group ${status.error ? 'has-error' : ''}"
									style="width: 400px; padding-left: 30px;">
									<form:input type="password" path="password"
										class="form-control" placeholder="Password"></form:input>
									<form:errors path="password"></form:errors>
								</div>
							</spring:bind></td>
					</tr>
					<tr>
						<td><label>Confirm</label></td>
						<td><spring:bind path="passwordConfirm">
								<div class="form-group ${status.error ? 'has-error' : ''}"
									style="width: 400px; padding-left: 30px;">
									<form:input type="password" path="passwordConfirm"
										class="form-control" placeholder="Confirm your password"></form:input>
									<form:errors path="passwordConfirm"></form:errors>
								</div>
							</spring:bind></td>
					</tr>
					<tr>
						<td><label>Role</label></td>
						<td>
							<div class="form-group ${status.error ? 'has-error' : ''}"
								style="width: 400px; padding-left: 30px;">
								<select id="role" name="role">
									<option value="ROLE_ADMIN" label="ADMIN">ADMIN</option>
									<option value="ROLE_SALES" label="SALES">SALES</option>
									<option value="ROLE_RECRUITER" label="RECRUITER">RECRUITER</option>
									<option value="ROLE_PH_RECRUITING_PARTNER" label="PH RECRUITING PARTNER">PH RECRUITING PARTNER</option>
									<option value="ROLE_HOSPITAL" label="HOSPITAL">HOSPITAL</option>
									<option value="ROLE_SALES_SG" label="SALES_SG">SALES_SG</option>
									<option value="ROLE_SALES_HK" label="SALES_HK">SALES_HK</option>
									<option value="ROLE_SALES_TW" label="SALES_TW">SALES_TW</option>
								</select>
							</div>
						</td>
					</tr>
					<%-- <tr>
						<td><label>Location</label></td>
						<td>
							<div class="form-group ${status.error ? 'has-error' : ''}"
								style="width: 400px; padding-left: 30px;">
								<select id="location" name="location">
									<option value="HongKong" label="HongKong" />
									<option value="Singapore" label="Singapore" />
									<option value="Taiwan" label="Taiwan" />
								</select>
							</div>
						</td>
					</tr> --%>
					<%-- <tr>
					<td><label>Role</label></td>
					<td><spring:bind path="role">
							<div class="form-group ${status.error ? 'has-error' : ''}"
								style="width: 400px; padding-left: 30px;">
								<form:errors path="role"></form:errors>
								<form:select path="role">
									<form:option value="admin" label="ADMIN" />
									<form:option value="user" label="USER" />
									<form:option value="recruiter" label="RECRUITER" />
									<form:option value="philRecruiter"
										label="PH RECRUITING PARTNER" />
									<form:option value="hospital" label="HOSPITAL" />
								</form:select>
							</div>
						</spring:bind></td>
				</tr> --%>
				</table>
			</div>
			<div align="right" style="width: 50%; margin-top: 5px;">
			<button class="btn btn-default" type="button" onclick="c_user();">
				CANCEL</button>
			<button class="btn btn-success" type="button" onclick="create();">
				CREATE
			</button> 
			</div>
		</form:form>

	</div>
	
	<!-- /container -->
</body>
</html>