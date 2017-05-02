<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="shortcut icon" href="http://activeglobalcaregiver.sg/misc/favicon.ico" type="image/vnd.microsoft.icon" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Set my password</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.5-dist/css/bootstrap.min.css">
<style type="text/css">
body{background-color: lightgray;}
.main{width: 500px;height: 200px;left: 35%;top: 20%;position: absolute;}
</style>
</head>
<body>
	<form:form method="POST" action="${pageContext.request.contextPath}/user/changePwd" commandName="user" class="form-horizontal">
		<form:input type="hidden" path="username"/>
		<div class="main">
			<div class="form-group">
				<label class="col-sm-2 control-label">Password</label>
				<spring:bind path="password">
					<div class="col-sm-10 ${status.error ? 'has-error' : ''}"
						style="width: 400px; padding-left: 30px;">
						<form:input type="password" path="password" class="form-control" placeholder="Password"/>
						<form:errors path="password"></form:errors>
					</div>
				</spring:bind>
			</div>	
			<div class="form-group">
				<label class="col-sm-2 control-label">Confirm</label>
				<spring:bind path="passwordConfirm">
					<div class="col-sm-10 ${status.error ? 'has-error' : ''}"
						style="width: 400px; padding-left: 30px;">
						<form:input type="password" path="passwordConfirm"
							class="form-control" placeholder="Confirm your password"></form:input>
						<form:errors path="passwordConfirm"></form:errors>
					</div>
				</spring:bind>
			</div>	
				
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-10">
					<button type="submit" class="btn btn-primary">SUBMIT</button>
					<a class="btn btn-default" style="margin-left: 30px;" href="${pageContext.request.contextPath}">CANCEL</a>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>