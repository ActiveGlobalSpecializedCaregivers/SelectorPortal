<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>forgotPwd</title>
<link rel="shortcut icon" href="http://activeglobalcaregiver.sg/misc/favicon.ico" type="image/vnd.microsoft.icon" />
<link type="text/css" href="${pageContext.request.contextPath}/resources/css/forgot/css.css" rel="stylesheet" />
<link type="text/css" href="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootbox.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
</head>

<body>

  <div class="content">
   <div class="web-width">
     <div class="for-liucheng">
      <div class="liulist for-cur"></div>
      <div class="liulist for-cur"></div>
      <div class="liulist for-cur"></div>
      <div class="liulist"></div>
      <div class="liutextbox">
       <div class="liutext for-cur"><em>1</em><br /><strong>Fill in the user information</strong></div>
       <div class="liutext"><em>2</em><br /><strong>check identity</strong></div>
       <div class="liutext"><em>3</em><br /><strong>set password</strong></div>
       <div class="liutext"><em>4</em><br /><strong>complete</strong></div>
      </div>
     </div>
     <form:form id="myForm" action="${pageContext.request.contextPath}/user/complete" method="post" modelAttribute="user" class="forget-pwd">
     <input type="hidden" name="username" value="${user.username}"/>
     <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
       <!-- <dl>
        <dt>phone:</dt>
        <dd><input type="text" /></dd>
        <div class="clears"></div>
       </dl>  -->
       <dl style="margin-bottom: 50px;">
        <dt>new password:</dt>
        <dd class="${status.error ? 'has-error' : ''}">
	        <spring:bind path="password">
	        	<form:input type="password" class="form-control" id="password" path="password" placeholder="Password"/>
	        	<form:errors path="password"/>
	        </spring:bind>
        </dd>
        <div class="clears"></div>
       </dl> 
       <dl style="margin-bottom: 30px;">
        <dt>confirm password:</dt>
        <dd>
        	<spring:bind path="password">
        		<form:input type="password" class="form-control" id="confirmPwd" path="passwordConfirm" placeholder="Confirm"/>
	        	<form:errors path="passwordConfirm"/>
       		</spring:bind>
        </dd>
        <div class="clears"></div>
       </dl> 
       <div class="subtijiao">
       	<input type="submit" value="submit"/>
       </div> 
      </form:form><!--forget-pwd/-->
   </div><!--web-width/-->
  </div><!--content/-->
  
</body>
</html>
