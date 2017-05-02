<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Users</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.5-dist/css/bootstrap.min.css">
<link rel="shortcut icon" href="http://activeglobalcaregiver.sg/misc/favicon.ico" type="image/vnd.microsoft.icon" />
<style type="text/css">
body{background: #f5f5f5;color: black;}
div{padding:20px;border: 1px solid #ccc;width: 500px;background: white;left: 35%;top: 15%;position: absolute;}
</style>
</head>
<body>
	<div>
		<h3>Your link has expired</h3>
		<hr/>
		<p>We'll be happy to send you another one when you're ready.</p>
		<a class="btn btn-primary" href="${pageContext.request.contextPath}/user/forgot_password">Reset my password</a>
	</div>
</body>
</html>