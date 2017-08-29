<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<fmt:setBundle basename="com.cloudaxis.i18n.text" />
<fmt:setLocale value="en" />


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Active Global Specialised Caregivers Selector</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Active Global">

    <!-- <link href="assets/less/styles.less" rel="stylesheet/less" media="all"> -->
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/newfavicon.png" type="image/x-icon" />
    <link rel="apple-touch-icon" sizes="192x192" href="${pageContext.request.contextPath}/logo-active-global-caregiver-dashboard-app.png">
    <!-- <link rel="stylesheet" href="assets/css/styles.min.css?=120"> -->
    <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600' rel='stylesheet' type='text/css'>
    <script>
        var site_url = '${pageContext.request.contextPath}/';
    </script>    

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
    
     
	<script type="text/javascript" src="//code.jquery.com/jquery-1.11.1.min.js"></script>

</head>
<body class="focusedform">
<div class="verticalcenter">
	<a href="#"><img src="${pageContext.request.contextPath}/resources/images/logo-active-global-caregiver.png" alt="Logo" class="brand" /></a>
	<div class="panel panel-primary">
		<form action="${pageContext.request.contextPath}/login" method="post" accept-charset="utf-8" class="form-horizontal" id="login_frm" style="margin-bottom: 0px !important;">
		<div class="panel-body">
			<h4 class="text-center" style="margin-bottom: 25px;">Sign in to continue to Dashboard</h4>
				<h4 class="text-center" style="color: red;">${error}</h4>
				<h4 class="text-center" style="color: red;">${logout}</h4>
				<div class="form-group">
					<div class="col-sm-12">
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-user"></i></span>
							<input type="text" class="form-control" id="username" name="username" placeholder="Username" autofocus>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-lock"></i></span>
							<input type="password" class="form-control" id="password" name="password" placeholder="Password">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						</div>
					</div>
				</div>
				<div class="clearfix">
					<div class="pull-right"><label><input type="checkbox" style="margin-bottom: 20px" checked="" name="remember_me"> Remember Me</label></div>
				</div>
		</div>
		<div class="panel-footer">
			<a href="${pageContext.request.contextPath}/user/forgot_password" class="pull-left btn btn-link" style="padding-left:0;margin-top: 0;">Forgot password?</a>
			
			<div class="pull-right">
				<!-- <a href="#" class="btn btn-default">Reset</a> -->
				<input type="reset" class="btn btn-default" value="Reset">
				<input type="submit" name="login" value="Log In" class="btn btn-primary">
			</div>
			
		</div>
		</form>		
	</div>
 </div>
      
</body>
</html>