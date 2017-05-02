<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Active Global Specialised Caregivers Selector</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico" type="image/vnd.microsoft.icon" />
    <link rel="apple-touch-icon" sizes="192x192" href="http://selector.activeglobalcaregiver.com/logo-active-global-caregiver-dashboard-app.png">

    <link href="${pageContext.request.contextPath}/resources/css/styles.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/header-blue.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/sidebar-green.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/fancybox/jquery.fancybox.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600' rel='stylesheet' type='text/css'>
	<link href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css" >
	<link href="${pageContext.request.contextPath}/resources/plugins/form-daterangepicker/daterangepicker-bs3.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/plugins/form-markdown/css/bootstrap-markdown.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/plugins/codeprettifier/prettify.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/plugins/form-toggle/toggles.css" rel="stylesheet">

    <script type="text/javascript" src="https://code.jquery.com/jquery-1.12.3.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jqueryui-1.10.3.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/less.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/plugins/bootbox/bootbox.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/enquire.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.cookie.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/plugins/codeprettifier/prettify.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/plugins/form-toggle/toggle.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/application.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.nicescroll.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/dataTables-1.10.12.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/date-eu.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/date-euro.js"></script>
<script>
    var site_url = 'agr-selector.cloudaxis.io';
    
    function cancel(){
    	$("#currentPassword").val("");
    	$("#newPassword").val("");
    	$("#newPasswordConfirmation").val("");
    	$("#changePassword").modal('hide');
    }
    
    function changePassword(){
    	var currentPassword = $("#currentPassword").val();
    	var newPassword = $("#newPassword").val();
    	var passwordConfirmation = $("#newPasswordConfirmation").val();
    	if(!(newPassword.length >= 8 && newPassword.length <= 30)){
    		alert("New password should be between 8 and 30 characters long");
    		return;
    	}
    	if(passwordConfirmation != newPassword){
    		alert("New password is not the same as the new password confirmation! Please check again");
    	}else{
    		$.ajax({
     			type : "POST",
     			url : "${pageContext.request.contextPath}/user/changePassword",
     			async : true,
     			cache : false,
     			data : {"userid" : "${sessionScope.userId}", "currentPassword" : currentPassword, "newPassword" : newPassword},
     			dataType : "json",
     			success : function(data){
     				var isTheCorrectPassword = data;
     				if(isTheCorrectPassword){
     	    			alert("Successful");
     	    			cancel();
     	    		}else{
     	    			alert("The current password is wrong!");
     	    		}
     			}
     		});   	
    	}
    }
    
    function doLogout(){	    	
    	$("#logoutForm").submit();
    }
</script>  
</head>

<body class="collapse-leftbar">
    <header class="navbar navbar-inverse navbar-fixed-top" role="banner">
    	<!-- /#Spinner div -->        
		<div class="loading" style="display: none;"><i class="fa fa-refresh fa-5x fa-spin"></i></div>
		
        <a id="leftmenu-trigger" class="tooltips" data-toggle="tooltip" data-placement="right" title="Toggle Sidebar"></a>

        <div class="navbar-header pull-left">
            <a class="navbar-brand" href="#">Active Global Specialised Caregivers Selector</a>
        </div>

        <ul class="nav navbar-nav pull-right toolbar">
        	<li class="dropdown">
        		<a href="#" class="dropdown-toggle username" data-toggle="dropdown"><span class="hidden-xs">${sessionScope.firstName} ${sessionScope.lastName} <i class="fa fa-caret-down"></i></span></a>
        		<ul class="dropdown-menu userinfo arrow">
        			<li class="username">
                        <a href="#">
        				    <h5>Howdy, ${sessionScope.firstName}!</h5><small>Logged in as <span>${sessionScope.userName}</span></small>
                        </a>
        			</li>
        			<li class="userlinks">
                        <ul class="dropdown-menu">
                            <li><a class="text-right"  data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#changePassword">Change Password</a></li>
        					<li><a href="javascript:doLogout()" class="text-right">Sign Out</a></li>
        				</ul>
        			</li>
        		</ul>
        	</li>
		</ul>
		<form id="logoutForm" action="${pageContext.request.contextPath}/logout"
			method="post">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>
	</header>

	<div class="modal fade" id="changePassword" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 400px;margin-top:250px;">
			<div class="modal-content">
				<div class="modal-header" style="background-color: currentColor;">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" style="color: white;">x</button>
					<h4 class="modal-title" style="margin-left: 10px; color: white">CHANGE PASSWORD</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<label class="control-label">Current Password </label>
						<span style="color:red">*</span>
					</div>
					<div class="form-group" style="margin-top:-20px;">
						<input id="currentPassword" type="password" class="form-control"></input>
					</div>
					<div class="form-group">
						<label class="control-label">New Password (between 8 and 30 characters long) </label>
						<span style="color:red">*</span>
					</div>
					<div class="form-group" style="margin-top:-20px;">
						<input id="newPassword" type="password" class="form-control"></input>
					</div>
					<div class="form-group">
						<label class="control-label">New Password Confirmation </label>
						<span style="color:red">*</span>
					</div>
					<div class="form-group" style="margin-top:-20px;">
						<input id="newPasswordConfirmation" type="password" class="form-control"></input>
					</div>
					<div style="text-align:center">
						<button type="button" class="btn btn-primary" onclick="cancel();">CANCEL</button> &nbsp; &nbsp;
						<button type="button" class="btn btn-default"
							onclick="changePassword();">CHANGE PASSWORD</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- BEGIN SIDEBAR -->
		<jsp:include page="sidebar.jsp" />
