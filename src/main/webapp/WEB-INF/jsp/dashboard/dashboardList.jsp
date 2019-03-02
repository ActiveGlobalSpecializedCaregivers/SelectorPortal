<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.cloudaxis.agsc.portal.helpers.ApplicationDict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<head>
<title>Active Global Specialised Caregivers Selector</title>
<meta name="description" content="Avant">
<meta name="author" content="The Red Team">

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.10.2.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/dashboard/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/dashboard/idangerous.swiper.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/dashboard/autosearch.css">

<link rel="apple-touch-icon" sizes="128x128" href="${pageContext.request.contextPath}/resources/img/logo-active-global-caregiver-dashboard.png">
<link rel="apple-touch-icon-precomposed" sizes="128x128" sizes="192x192" href="${pageContext.request.contextPath}/resources/img/logo-active-global-caregiver-dashboard.png">

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/plugins/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/dashboard/autosearch.js"></script>

</head>

<jsp:include page="../header.jsp" />
<body class="collapse-leftbar">
    <div id="page-container">
	    <div id="page-content">
		    <div id='wrap1'>
		        <div id="page-head">
	        		<h1>Active Applicant</h1>
	        		<div id="page-head1">
	        			<div id="page-head1-left" style="margin-left: 100px;width: 493px; margin-top:5px;">
							<div>
								<label style="color: #4a5ad3; font-weight: bold; font-size: x-large;">${caregiver.fullName}</label>
							</div>
							<div style="margin-top: -13px">
								<div class="row">
									<div class="col-md-6">
										<label style="color: #d2d3d6; font-size: small; color: gray; font-style: italic;">
											Applied on <fmt:formatDate value="${caregiver.dateApplied}" type="both" pattern="dd/MM/yyyy" />
										</label>
									</div>
									<div class="col-md-6">
										<a href="/admin/users/view_cv?userId=${caregiver.userId}" class="btn btn-primary btn-s" id="view_cv">View Bio-Data</a>
									</div>
								</div>

								<div class="row">
									<div class="col-md-6">
										<label><strong><i class="fa fa-phone header-color"></i>&nbsp; &nbsp;</strong>${caregiver.mobile}</label>
									</div>
									<div class="col-md-6">
										<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUB_ADMIN', 'ROLE_RECRUITER', 'ROLE_PH_RECRUITING_PARTNER')">
											<input id="registerStatus" style="height:13px;width:13px;" type="checkbox" onclick="changeConcordeStatus();"/>
											<label class="header-color"><strong>Registered with Concorde</strong></label>
										</sec:authorize>
										<sec:authorize access="hasAnyRole('ROLE_SALES', 'ROLE_SUB_ADMIN', 'ROLE_SALES_SG', 'ROLE_SALES_HK', 'ROLE_SALES_TW')">
											<input id="registerStatus" style="height:13px;width:13px;" type="checkbox" disabled/>
											<label class="header-color"><strong>Registered with Concorde</strong></label>
										</sec:authorize>
									</div>

								</div>
								
	        				    <div class="row">
	        				    	<div class="col-md-6">
	        				    		<label><strong><i class="fa fa-envelope header-color"></i>&nbsp; &nbsp;</strong>${caregiver.email}</label>
	        				    	</div>
									<div class="col-md-6">
										<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUB_ADMIN', 'ROLE_RECRUITER')">
											<input id="schemaStatus" style="height:13px;width:13px;" type="checkbox" onclick="changeSchemaStatus();"/>
											<label class="header-color"><strong>Advance Placement Scheme</strong></label>
										</sec:authorize>
										<sec:authorize access="hasRole('ROLE_PH_RECRUITING_PARTNER')">
											<input id="verfiedStatus" style="height:13px;width:13px;" type="checkbox" onclick="changeVerifiedStatus();" />
											<label class="header-color" id="medicalCertValidator"><strong>Medical Cert Verified</strong></label>
										</sec:authorize>
										<sec:authorize access="hasAnyRole('ROLE_SALES', 'ROLE_SALES_SG', 'ROLE_SALES_HK', 'ROLE_SALES_TW')">
											<input id="schemaStatus" style="height:13px;width:13px;" type="checkbox" disabled />
											<label class="header-color" id="medicalCertValidator"><strong>Medical Cert Verified</strong></label>
										</sec:authorize>
									</div>
	        					</div>
								<div class="row">
									<div class="col-md-6">&nbsp;</div>
									<div class="col-md-6">
										<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_RECRUITER')">
											<input id="verfiedStatus" style="height:13px;width:13px;" type="checkbox" onclick="changeVerifiedStatus();" />
											<label class="header-color" id="medicalCertValidator"><strong>Medical Cert Verified</strong></label>
										</sec:authorize>
										<sec:authorize access="hasAnyRole('ROLE_SALES', 'ROLE_SALES_SG', 'ROLE_SALES_HK', 'ROLE_SALES_TW')">
											<input id="verfiedStatus" style="height:13px;width:13px;" type="checkbox" disabled />
											<label class="header-color" id="medicalCertValidator"><strong>Medical Cert Verified</strong></label>
										</sec:authorize>
									</div>
								</div>
							</div>       					        		
	        			</div>
	        		
	        			<div id="page-head1-right">
						    <div class="btn-group">
							 	<c:forEach items="${selectStatusList}" var="status" varStatus="s">
									<c:choose>
										<c:when test="${caregiver.status eq status.id}">
											<button class="btn btn-primary dropdown-toggle" type="button"
												id="dropdownMenu1" data-toggle="dropdown"
												aria-haspopup="true" aria-expanded="true">
												${s.count}-${status.name} <span class="caret"></span>
											</button>
										</c:when>
									</c:choose>
								</c:forEach>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
									<c:forEach items="${selectStatusList}" var="status" varStatus="index">
										<c:choose>
											<c:when test="${caregiver.status eq status.id}">
												<li class="disabled"><a href="#">${index.count}-${status.name}</a></li>
											</c:when>
											<c:otherwise>
												<li><a href="#"
													onclick="changeStatus('${status.id}','${status.name}');">${index.count}-${status.name}</a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</ul>
							</div>
						</div>

						<!-- begin show email -->
						<div class="modal fade" id="email-modal" tabindex="-1"
							role="dialog">
							<div class="modal-dialog" style="width: 900px">
								<div class="modal-content">
									<div class="modal-header"
										style="background-color: currentColor;">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true" style="color: white;" onclick="closeShow();">X</button>
										<h4 class="modal-title"
											style="text-align: center; color: white">EMAIL</h4>
									</div>
									<div class="modal-body">
										<div class="over m_b1">
											<label class="fleft m_r1">FROM:</label> <label
												id="emailFromName"></label>
										</div>
										<div class="over m_b1">
											<label class="fleft m_r1">TO:</label> <label id="emailToName"></label>
											<label id="send_date" style="float: right;"></label>
										</div>
										<div class="over m_b1" id="ccDiv">
											<label class="fleft m_r1">CC:</label> <label id="cc_email"></label>
										</div>
										<div class="over m_b1" id="bccDiv">
											<label class="fleft m_r1">BCC:</label> <label id="bcc_email"></label>
										</div>
										<div class="over" style="margin-bottom: 10px;">
											<label class="fleft m_r1">SUBJECT:</label> <label
												id="emailToSubject"></label>
										</div>
										<textarea id="g_content" name="g_content" disabled="true"></textarea>
										<script type="text/javascript">
						            	var editor = CKEDITOR.replace('g_content');
						            </script>
										<!-- show attachments -->
										<ul id="reply-attachment" class="pagination1 attachment-ul"></ul>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-primary"
											data-dismiss="modal" data-toggle="modal"
											data-target="#reply-modal">REPLY</button>
										<button type="button" class="btn btn-default"
											data-dismiss="modal" onclick="closeShow();">CLOSE</button>
									</div>
								</div>
								<!-- /.modal-content -->
							</div>
							<!-- /.modal-dialog -->
						</div>
						<!-- /.modal -->
						<!-- end show email -->

						<!-- begin reply email -->
						<div class="modal fade" id="reply-modal" tabindex="-1"
							role="dialog">
							<div class="modal-dialog" style="width: 900px">
								<div class="modal-content">
									<div class="modal-header"
										style="background-color: currentColor;">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true" style="color: white;" onclick="closeShow()">X</button>
										<h4 class="modal-title"
											style="text-align: center; color: white">REPLY EMAIL</h4>
									</div>
									<div class="modal-body">
										<input type="hidden" name="candidateId"
											value="${caregiver.userId}"> <input type="hidden"
											name="userId" value="${loginUser.userId}">
										<div class="over m_b1" style="display: none;">
											<input name="senderName" class="form-control" type="text"
												style="width: 50%;"
												value="${sessionScope.firstName} ${sessionScope.lastName}">
										</div>
										<div class="over m_b1">
											<label class="fleft m_r1 input-w1">TO:</label> <input
												name="receiveName" class="form-control" type="text"
												style="width: 50%; float: left;"
												placeholder="comma separated list of recipients"
												value="${caregiver.firstName} ${caregiver.lastName}">
											<input name="receiveEmail" type="hidden"
												value="${caregiver.email}"> <a id="reply-cca"
												href="#" style="margin-left: 15px; text-decoration: none;"
												onclick="cc('1');">CC</a> <a id="reply-bcca" href="#"
												style="margin-left: 10px; text-decoration: none;"
												onclick="bcc('1');">BCC</a>
										</div>
										<div class="over m_b1" id="reply-cc" style="display: none">
											<label class="fleft m_r1 input-w1">CC:</label> <input
												name="ccEmail" class="form-control reply-CC" type="text"
												style="width: 50%;"
												placeholder="comma separated list of recipients">
											<script type="text/javascript">
										         var input = $(".reply-CC");
										         var autosearch = new AutoSearch();
										         autosearch.init({input:input,autoShow:false,data:${userJson},mutil:true});
										    </script>
										</div>
										<div class="over m_b1" id="reply-bcc" style="display: none">
											<label class="fleft m_r1 input-w1">BCC:</label> <input
												name="bccEmail" class="form-control reply-BCC" type="text"
												style="width: 50%;"
												placeholder="comma separated list of recipients">
											<!-- auto search -->
											<script type="text/javascript">
										         var input = $(".reply-BCC");
										         var autosearch = new AutoSearch();
										         autosearch.init({input:input,autoShow:false,data:${userJson},mutil:true});
										    </script>
										</div>
										<div class="over" style="margin-bottom: 10px;">
											<label class="fleft m_r1 input-w1">SUBJECT:</label> <input
												name="subject" class="form-control" type="text"
												style="width: 50%;" placeholder="SUBJECT">
										</div>
										<textarea id="content" name="content"></textarea>
										<script type="text/javascript">
							            	var editor = CKEDITOR.replace('content');
							            </script>
										
										<ul id="reply-attachmentUl" class="pagination1 attachment-ul"></ul>
										<form id="reply-fileForm">
											<input type="file" id="reply-file" name="fileList"
												style="display: none;" multiple="multiple"
												onchange="attachment(this,'reply');" /> <input type="hidden"
												name="userId" value="${caregiver.userId}">
										</form>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											onclick="$('#reply-file').click();">Attachment</button>
										<button type="button" class="btn btn-primary"
											onclick="replySend();">SEND</button>
										<button type="button" class="btn btn-default"
											data-dismiss="modal" onclick="closeShow();">CLOSE</button>
									</div>
								</div>
								<!-- /.modal-content -->
							</div>
							<!-- /.modal-dialog -->
						</div>
						<!-- /.modal -->
						<!-- end reply email -->

						<!-- begin add document -->
						<div class="modal fade" id="a_d" tabindex="-1" role="dialog">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header"
										style="background-color: currentColor;">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true" style="color: white;">X</button>
										<h4 class="modal-title" style="color: white">Add Documents</h4>
									</div>
									<div class="modal-body">
										<form id="add_file"
											action="${pageContext.request.contextPath}/file/saveDocument?active=6"
											method="post" enctype="multipart/form-data">
											<input type="hidden" name="userId"
												value="${caregiver.userId}"> <input type="hidden"
												name="${_csrf.parameterName}" value="${_csrf.token}" />
											<div class="form-group">
												<label><strong>1.Choose document(s) to upload.</strong></label> <input type="file" id="a_file" name="a_file"
													multiple="multiple"> <font color='red'
													id="verify_file"></font>
											</div>
											<div class="form-group">
												<label><strong>2.Choose the document type.</strong></label>
												<br> <select id="s_type" name="s_type">
													<option value="resume">resume</option>
													<option value="my_photo">photo</option>
													<option value="passport">passport</option>
													<option value="degrees">degree</option>
													<option value="other_file" selected="selected">other</option>
												</select>
											</div>
										</form>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">Cancel</button>
										<button type="button" class="btn btn-primary"
											onclick="up_document();">Upload Documents</button>
									</div>
								</div>
								<!-- /.modal-content -->
							</div>
							<!-- /.modal-dialog -->
						</div>
						<!-- /.modal -->
						<!-- end add document -->

						<!-- begin rename document -->
						<div class="modal fade" id="i-modal-rename" tabindex="-1"
							role="dialog">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header"
										style="background-color: currentColor;">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true" style="color: white;">X</button>
										<h4 class="modal-title" style="color: white">Rename Documents</h4>
									</div>
									<div class="modal-body">
										<form id="i-rename"
											action="${pageContext.request.contextPath}/file/rename?active=6"
											method="post">
											<input type="hidden" name="${_csrf.parameterName}"
												value="${_csrf.token}" /> <input type="hidden"
												name="userId" value="${caregiver.userId}"> <input
												type="hidden" id="i-bucketName" name="bucketName">
											<div class="form-group">
												<label><strong>Rename this document.</strong></label> <input
													type="text" id="newName" class="form-control"
													name="newName"> <input type="hidden"
													id="originalName">
											</div>
										</form>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">Cancel</button>
										<button type="button" class="btn btn-primary"
											onclick="file_rename('${caregiver.photoDegrees}','${caregiver.photoPassport}','${caregiver.photo}','${caregiver.otherFiles}','${caregiver.resume}');">RENAME</button>
									</div>
								</div>
								<!-- /.modal-content -->
							</div>
							<!-- /.modal-dialog -->
						</div>
						<!-- /.modal -->
						<!-- end rename document -->

						<!-- begin rename fileType -->
						<div class="modal fade" id="i-modal-renameFileType" tabindex="-1"
							role="dialog">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header"
										style="background-color: currentColor;">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true" style="color: white;">X</button>
										<h4 class="modal-title" style="color: white">Change Filetype</h4>
									</div>
									<div class="modal-body">
										<form id="form-fileType"
											action="${pageContext.request.contextPath}/file/renameFileType?active=6"
											method="post">
											<input type="hidden" name="${_csrf.parameterName}"
												value="${_csrf.token}" /> <input type="hidden"
												name="userId" value="${caregiver.userId}"> 
												<input type="hidden" id="input-path" name="bucketName">
												<input type="hidden" id="input-originalType" name="originalType">
												<input type="hidden" id="input-extension" name="input-extension">
											<div class="form-group">
												<label><strong>Rename this document.</strong></label> 
												<select id="select-type" name="type">
													<option value="resume">resume</option>
													<option value="my_photo">photo</option>
													<option value="passport">passport</option>
													<option value="degrees">degree</option>
													<option value="other_file">other</option>
												</select>
											</div>
										</form>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">Cancel</button>
										<button type="button" class="btn btn-primary"
											onclick="fileType_rename();">Submit</button>
									</div>
								</div>
							</div>
						</div>
						<!-- end rename fileType -->

						<!-- begin send email -->
						<div class="modal fade" id="sendMailModal" tabindex="-1"
							role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-dialog" style="width: 900px">
								<div class="modal-content">
									<div class="modal-header"
										style="background-color: currentColor;">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true" style="color: white;" onclick="closeSendMailModal()">X</button>
										<h4 class="modal-title"
											style="text-align: center; color: white">SEND MESSAGE</h4>
									</div>
									<div class="modal-header"
										style="background-color: black; height: 60px;" id="tList">
										<div class="dropdown" style="float: left; width: 50%"
											id="chooseN">
											<button style="background-color: black; color: #d4c6c6"
												class="btn btn-default dropdown-toggle" type="button"
												id="dropdownMenu1" data-toggle="dropdown"
												aria-haspopup="true" aria-expanded="true">
												<font>${sessionScope.firstName}
													${sessionScope.lastName}</font> <span class="caret"></span>
											</button>
											<input type="hidden" id="chooseE" value="${loginUser.userId}" />
											<ul class="dropdown-menu" style="right: inherit;"
												aria-labelledby="dropdownMenu3">
												<li><a href="#"
													onclick="chooseName('${loginUser.firstName} ${loginUser.lastName}','${loginUser.userId}');">${loginUser.firstName}
														${loginUser.lastName}</a></li>
												<li><a href="#"
													onclick="chooseName('Active Global Specialised Caregivers','Active Global Specialised Caregivers');">Active
														Global Specialised Caregivers</a></li>
											</ul>
										</div>
										<div class="dropdown" style="float: right;">
											<button style="background-color: black; color: #d4c6c6"
												class="btn btn-default dropdown-toggle" type="button"
												id="chooseT" data-toggle="dropdown" aria-haspopup="true"
												aria-expanded="true">
												<font>CHOOSE TEMPLATE</font> <span class="caret"></span>
											</button>
											<ul class="dropdown-menu" aria-labelledby="dropdownMenu4">
												<c:forEach items="${templateList}" var="emailTemplate">
													<li id="emailTemplate-${emailTemplate.id}"><a href="#"
														onclick="chooseTemplate('${emailTemplate.id}','${emailTemplate.name}');">${emailTemplate.name}</a></li>
												</c:forEach>
											</ul>
										</div>
										<div style="clear: both"></div>
									</div>
									<form id="f-file">
										<div class="modal-body">
											<div class="over m_b1" style="display: none;">
												<label class="fleft m_r1 input-w1">FROM:</label> <input
													id="senderName" class="form-control" type="text"
													style="width: 50%;"
													value="${sessionScope.firstName} ${sessionScope.lastName}">
											</div>
											<div class="over m_b1">
												<label class="fleft m_r1 input-w1">TO:</label> <input
													id="toName" class="form-control" type="text"
													style="width: 50%; float: left;"
													placeholder="comma separated list of recipients"
													value="${caregiver.firstName} ${caregiver.lastName}">
												<input id="toEmail_address" type="hidden"
													value="${caregiver.email}"> <a id="cc_a" href="#"
													style="margin-left: 15px; text-decoration: none;"
													onclick="cc('0');">CC</a> <a id="bcc_a" href="#"
													style="margin-left: 10px; text-decoration: none;"
													onclick="bcc('0');">BCC</a>
											</div>
											<div class="over m_b1" id="cc" style="display: none">
												<label class="fleft m_r1 input-w1">CC:</label> <input
													id="ccEmail" class="form-control autosearchCC" type="text"
													style="width: 50%;"
													placeholder="comma separated list of recipients">
												<script type="text/javascript">
										         var input = $(".autosearchCC");
										         var autosearch = new AutoSearch();
										         autosearch.init({input:input,autoShow:false,data:${userJson},mutil:true});
										    </script>
											</div>
											<div class="over m_b1" id="bcc" style="display: none">
												<label class="fleft m_r1 input-w1">BCC:</label> <input
													id="bccEmail" class="form-control autosearchBCC" type="text"
													style="width: 50%;"
													placeholder="comma separated list of recipients">
												<!-- auto search -->
												<script type="text/javascript">
										         var input = $(".autosearchBCC");
										         var autosearch = new AutoSearch();
										         autosearch.init({input:input,autoShow:false,data:${userJson},mutil:true});
										    </script>
											</div>
											<div class="over" style="margin-bottom: 10px;">
												<label class="fleft m_r1 input-w1">SUBJECT:</label> 
												<input class="form-control" type="text" name="toSubject" id="toSubject"
													style="width: 50%;" placeholder="SUBJECT">
											</div>
											<textarea id="toContent" name="to_content"></textarea>
											<script type="text/javascript">
							            	var editor = CKEDITOR.replace('to_content');
							            </script>
											<ul id="attachment-file" class="pagination1 attachment-ul"></ul>
										
												<input type="file" id="h-file" name="fileList"
													style="display: none;" multiple="multiple"
													onchange="attachment(this,'new');" /> <input type="hidden"
													name="userId" value="${caregiver.userId}">
										</div>
									</form>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											onclick="$('#h-file').click();">Attachment</button>
										<button type="button" class="btn btn-default"
											onclick="saveTemplate();">Save as Template</button>
										<button type="button" class="btn btn-primary"
											onclick="send();">SEND</button>
									</div>
								</div>
								<!-- /.modal-content -->
							</div>
							<!-- /.modal-dialog -->
						</div>
						<!-- /.modal -->
						<!-- end send email -->

						<!-- begin save as template -->
						<div class="modal fade" id="saveTemplateModal" tabindex="-1"
							role="dialog" style="top: 300px;" aria-labelledby="myModalLabel"
							aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header" style="background-color: grey;">
										<h4 class="modal-title" id="myModalLabel">Save Template
											as...</h4>
									</div>
									<div class="modal-body">
										<input id="templateName"
											style="width: 100%; height: 30px; margin-top: 5px;"
											placeholder="Template's name">
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">Cancel</button>
										<button type="button" class="btn btn-primary"
											onclick="save();">Save</button>
									</div>
								</div>
								<!-- /.modal-content -->
							</div>
							<!-- /.modal -->
						</div>
						<!-- end save as template -->
				
						<!-- begin show templates -->
						<div class="modal fade" id="templates" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
											&times;
										</button>
										<h4 class="modal-title" id="myModalLabel">
											TEMPLATES
										</h4>
									</div>
									<div class="modal-body">
										<table id="templatesList" class="table table-hover row-border order-column ">
									        <thead>
									            <tr style="background-color:grey;color: white;">
									                <th>Name</th>
									                <th>Subject</th>
									                <th>Operation</th>
									            </tr>
									        </thead>
									        <tbody>
									        	<c:forEach items="${templateList}" var="templates">
									        		<tr>
									        			<td width="30%">${templates.name}</td>
									        			<td width="50%">${templates.subject}</td>
									        			<td width="20%">
									        				<button type="button" class="btn btn-primary" title="Delete template" onclick="deleteTemplate(this,'${templates.id}')">
																<span class="fa fa-trash-o"></span>
															</button>
 															<button type="button" class="btn btn-primary" title="Edit template" onclick="showTemplate('${templates.id}')">
																<span class="fa fa fa-edit"></span>
															</button>
									        			</td>
									        		</tr>
									        	</c:forEach>
									        </tbody>
									    </table>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default" data-dismiss="modal">Close
										</button>
									</div>
								</div><!-- /.modal-content -->
							</div><!-- /.modal -->
						</div>
						<!-- end show templates -->
						
						<!-- begin edit templates -->
						<div class="modal fade" id="editTemplate" tabindex="-1" role="dialog" aria-hidden="true">
							<div class="modal-dialog" style="width: 900px">
								<div class="modal-content">
									<div class="modal-header" style="background-color: currentColor;">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="color: white;" onclick="closeEditTemplate()">
											&times;
										</button>
										<h4 class="modal-title" style="text-align: center; color: white">EMAIL TEMPLATE</h4>
									</div>
									<form id="editTemplateForm">
										<input type="hidden" id="e-t-id" name="id">
										<input type="hidden" id="originalAttachment">
										<div class="modal-body">
											<div class="over" style="margin-bottom: 10px;">
												<label class="fleft m_r1 input-w1">NAME:</label> 
												<input id="e-t-name" class="form-control" type="text" name="name"
													style="width: 50%;" placeholder="TEMPLATE NAME" />
											</div>
											<div class="over" style="margin-bottom: 10px;">
												<label class="fleft m_r1 input-w1">SUBJECT:</label> 
												<input id="e-t-subject" class="form-control" type="text" name="subject"
													style="width: 50%;" placeholder="SUBJECT" />
											</div>
											<textarea id="ETcontent" name="content"></textarea>
											<script type="text/javascript">
								            	var editor = CKEDITOR.replace('ETcontent');
								            </script>
											<ul id="e-t-attachment" class="pagination1 attachment-ul"></ul>
											<input type="file" id="e-t-file" name="fileList" style="display: none;" multiple="multiple"
													onchange="attachment(this,'editTemplate');" /> 
										</div>
									</form>
									<div class="modal-footer">
										<button type="button" class="btn btn-default" onclick="$('#e-t-file').click();">
											<i class="fa fa-plus" aria-hidden="true"></i>Attachment
										</button>
										<button type="button" class="btn btn-default" onclick="editTemplate();">
											Save 
										</button>
									</div>
								</div>
								<!-- /.modal-content -->
							</div>
							<!-- /.modal-dialog -->
						</div>
						<!-- /.modal -->
						<!-- end edit templates -->
						
						
	        		</div>
		        </div>
		    	<!-- tab begin -->
		    	<div class="wrap">
					<div style="background:#DCDCDC;">
						<div class="tabs">
							<a id="0" href="#" hidefocus="true" onclick="clickTab('0');">PROFILE</a>
							<a id="1" href="#" hidefocus="true" onclick="clickTab('1');">QUESTIONNAIRE</a>
							<a id="2" href="#" hidefocus="true" onclick="clickTab('2');">COMMENTS</a>
							<a id="3" href="#" hidefocus="true" onclick="clickTab('3');">ASSESSMENTS</a>
							<a id="4" href="#" hidefocus="true" onclick="clickTab('4');">BIO</a>
							<a id="5" href="#" hidefocus="true" onclick="clickTab('5');">EMAILS</a>
							<a id="6" href="#" hidefocus="true" onclick="clickTab('6');">FILES</a>
							<a id="7" href="#" hidefocus="true" onclick="clickTab('7');">HISTORY</a>
							<input type="hidden" id="tab" name="active" value="${active}"> 
						</div>    
						<div class="swiper-container">
							<div class="swiper-wrapper">
								<div class="swiper-slide swiper-no-swiping">		<!-- 0 -->
									<div class="content-slide">
										<%-- <iframe src="${caregiver.resumeUrl}" height="800px" width="80%" frameborder="0"></iframe>
										 --%>
										 <c:choose>
										 	<c:when test="${caregiver.resume != 'null'}">
												<iframe src="https://docs.google.com/gview?url=http://selector.activeglobalcaregiver.com/resources/${caregiver.userId}/resume/${caregiver.resume}&embedded=true"
												height="800px" width="100%" frameborder="0"></iframe>
										 	</c:when>
										 	<c:otherwise>
										 		<iframe src="https://docs.google.com/gview?url=http://selector.activeglobalcaregiver.com/resources/resume/resume.pdf&embedded=true"
												height="800px" width="100%" frameborder="0"></iframe>
										 	</c:otherwise>
										 </c:choose>
									</div>
								</div>
								<div class="swiper-slide swiper-no-swiping">		<!-- 1 -->
									<jsp:include page="questionnaire.jsp" />
								</div>
								<div class="swiper-slide swiper-no-swiping">		<!-- 2 -->
									<div class="content-slide" style="margin: 15px">	
										<jsp:include page="../candidates/comments.jsp"></jsp:include>
									</div>
								</div>
								<div class="swiper-slide swiper-no-swiping">		<!-- 3 -->
									<div class="content-slide">
										<jsp:include page="../candidates/assessments.jsp" />
									</div>
								</div>
								<div class="swiper-slide swiper-no-swiping">		<!-- 4 -->
									<div class="content-slide">
										<jsp:include page="../candidates/bio.jsp" />
									</div>
								</div>
								<div class="swiper-slide swiper-no-swiping">		<!-- 5 -->
									<div class="content-slide">
										<jsp:include page="emailHistory.jsp" />
									</div>
								</div>
								<div class="swiper-slide swiper-no-swiping">		<!-- 6-->
									<div class="content-slide">
										<jsp:include page="fileList.jsp" />
									</div>
								</div>
								<div class="swiper-slide swiper-no-swiping">		<!-- 7 -->
									<div class="content-slide history">
										<c:choose>
											<c:when test="${fn:length(workflowList) > 0}">
												<c:forEach items="${workflowList}" var="workflow">
													<p class="bg-info" style="text-indent: 1em">
														<c:if test="${workflow.workflowType != 'status'}">
															${workflow.workflowType} as 
														</c:if>
														<font style="font-weight: bold; font-style: italic;">${workflow.statusToVal}</font>  
														on  
														<fmt:formatDate value="${workflow.changeDate}" type="both" pattern="dd/MM/yyyy" /> 
														by ${workflow.userName}
													</p>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<label class="no-history"> There is currently no
													workflow history for this candidate. </label>

											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- tab end -->

			</div>
			<!--wrap1 -->
		</div>
		<!-- page-content -->

		<footer role="contentinfo">
			<div class="clearfix">
				<ul class="list-unstyled list-inline pull-left">
					<li>Active Global Specialised Caregivers Pte Ltd &copy; 2016</li>
				</ul>

				<button class="pull-right btn btn-inverse-alt btn-xs hidden-print"
					id="back-to-top">
					<i class="fa fa-arrow-up"></i>
				</button>
		</footer>
	</div>

	<!-- /#Spinner div -->        
	<div class="loading" style="display: none;"><i class="fa fa-refresh fa-5x fa-spin"></i></div>

	<!-- change password -->
	<div class="modal fade" id="changePassword" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 400px; margin-top: 250px;">
			<div class="modal-content">
				<div class="modal-header" style="background-color: currentColor;">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" style="color: white;">x</button>
					<h4 class="modal-title" style="margin-left: 10px; color: white">CHANGE
						PASSWORD</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<label class="control-label">Current Password </label> <span
							style="color: red">*</span>
					</div>
					<div class="form-group" style="margin-top: -20px;">
						<input id="currentPassword" type="password" class="form-control"></input>
					</div>
					<div class="form-group">
						<label class="control-label">New Password (between 8 and
							30 characters long) </label> <span style="color: red">*</span>
					</div>
					<div class="form-group" style="margin-top: -20px;">
						<input id="newPassword" type="password" class="form-control"></input>
					</div>
					<div class="form-group">
						<label class="control-label">New Password Confirmation </label> <span
							style="color: red">*</span>
					</div>
					<div class="form-group" style="margin-top: -20px;">
						<input id="newPasswordConfirmation" type="password"
							class="form-control"></input>
					</div>
					<div style="text-align: center">
						<button type="button" class="btn btn-primary" onclick="cancel();">Cancel</button>
						&nbsp; &nbsp;
						<button type="button" class="btn btn-default"
							onclick="changePassword();">Change Password</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/dashboard/idangerous.swiper.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/dashboard/questionnaireValidate.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/dashboard/email.js"></script>

<script type="text/javascript">
	//display the tab is be selected
	$("#${active}").addClass('active');

	var tabsSwiper = new Swiper('.swiper-container',{
		speed:1000,
		initialSlide :${active},
		noSwiping:true,
		onSlideChangeStart: function(){
			$(".tabs .active").removeClass('active');
			$(".tabs a").eq(tabsSwiper.activeIndex).addClass('active');
		}
	});


var polling;

$(document).ready(function(){
	

	 /* if("${caregiver.medicalCertValidator}" != ""){
		var message = "Validated by ${caregiver.medicalCertValidator}";
		$('#medicalCertValidator').prop('title', message);
		$('[data-toggle="tooltip"]').tooltip(); 
	}*/
	
    var registerStatus = "${caregiver.registeredConcorde}";
    if(registerStatus == "YES"){
    	$("#registerStatus").attr("checked", true);
    }
    var schemaStatus = "${caregiver.preDeployment}";
    if(schemaStatus == "YES"){
    	$("#schemaStatus").attr("checked", true);
    }
    var verifiedStatus = "${caregiver.medicalCertVerified}";
	if(verifiedStatus == "YES"){
		$("#verfiedStatus").attr("checked", true);
	}

	//templates list
	$('#templatesList').DataTable({
    	bLengthChange: false,		//each page display
  		"searching":false
    });
	
});

//change the status
function changeStatus(changeStatus,status){
	bootbox.confirm({  
		buttons: {  
		    confirm: {  
		        label: 'ok'  
		    },  
		    cancel: {  
		        label: 'cancel'
		    }  
		},  
		message: 'Are you sure want to change it?',  
		callback: function(result) {
            if(result){
                var fullName = '${caregiver.fullName}';

            	var href = "${pageContext.request.contextPath}/dashboard/changeStatus?userId="+'${caregiver.userId}' + "&numbersOfPlacement=" + '${caregiver.numbersOfPlacement}' +
            				"&fullName="+ encodeURIComponent(fullName) +"&status="+'${caregiver.status}' +"&email="+'${caregiver.email}'+
            				"&templateType="+changeStatus +"&active="+$("#tab").val();
            	location.href = href;
            }
		}
	});  
	
}

//send the email
function send(){
	var senderName = $("#senderName").val();
	var toName = $("#toName").val();
	var toEmailAddress = $("#toEmail_address").val();
	var subject = $("#toSubject").val();
	var ccEmail = $("#ccEmail").val();
	var bccEmail = $("#bccEmail").val();
	
	//get the content
	var content = CKEDITOR.instances.toContent.getData();
	var tcontent = $.trim(content.replace(/<[^>]+>/g,"").replace(/&nbsp;/ig,""));
	var content = encodeURIComponent(content);			
	
	if(toName != '' && toName.indexOf("@") > 0){
		toEmailAddress = toName;
	}
	
	var attachment = "";
	$(".l-name").each(function(){
		attachment = attachment + $(this).text() + ",";
	});
	if(attachment.length > 0){
		attachment = attachment.substr(0, attachment.length-1);
	}
	
	if(senderName==null || senderName==''){
		alertMsg("The sender field cannot be left blank.");
	}else if(toName==null || toName==''){
		alertMsg("The addressee's mail cannot be left blank.");
	}else if(subject==null || subject==''){
		alertMsg("The subject field cannot be left blank.");
	}else if(tcontent==null || tcontent==''){
		alertMsg("The content field cannot be left blank.");
	}else{
		$(".loading").show();
		var href = "${pageContext.request.contextPath}/emailTemplates/send?candidateId="+'${caregiver.userId}' + "&active="+$("#tab").val() +
				"&senderName="+senderName +"&receiveEmail="+toEmailAddress +"&subject="+subject +"&content="+content +"&userId="+$("#chooseE").val() +
				"&ccEmail="+ccEmail +"&bccEmail="+bccEmail +"&attachment="+attachment +"&receiveName="+toName;
		location.href = href;
	}
}

//pop up the save as template modal
function saveTemplate(){
	var subject = $("#toSubject").val();
	var content = CKEDITOR.instances.toContent.getData();
	var tcontent = $.trim(content.replace(/<[^>]+>/g,"").replace(/&nbsp;/ig,""));
	var content = encodeURIComponent(content);
	if(subject==null || subject==''){
		alertMsg("The subject field cannot be left blank.");
	}else if(tcontent==null || tcontent==''){
		alertMsg("The content field cannot be left blank.");
	}else{
		$("#saveTemplateModal").modal({backdrop: 'static', keyboard: false});
	}
}

//save email template
function save(){
	var name = $("#templateName").val();
	var content = CKEDITOR.instances.toContent.getData();
	$("#toContent").val(content);
	var files = new FormData($( "#f-file" )[0]);
	
	var attachment = "";
	$(".l-name").each(function(){
		attachment = attachment + $(this).text() + ",";
	});
	if(attachment.length > 0){
		attachment = attachment.substr(0, attachment.length-1);
	}
	
	if(name !=null && name !=''){
		$(".loading").show();
		$.ajax({
			type: "POST",
			async : false,
			cache: false,
			url: "${pageContext.request.contextPath}/emailTemplates/save?name="+name +"&attachment="+attachment +"&userId="+${caregiver.userId},
			data:files,
	        contentType: false,  
	        processData: false,  
	        dataType: "json",
            success: function(data){
				if(data.msg){
					//alert(data.msg);
					location.reload()
				}else if(data.error){
					$(".loading").hide();
					alertMsg(data.error);
				}
				//$('#saveTemplateModal').modal('hide');
			}
		});
	}else{
		alertMsg("The template name cannot be left blank. Please provide one.");
	}
}

//the tips
function alertMsg(msg){
	 bootbox.alert({  
         buttons: {  
            ok: {  
                 label: 'ok',  
             }  
         },  
         message: msg,  
         title: "Tips",  
     });
}

function clickTab(tab){
	if(tab != $("#tab").val()){
		$("#tab").val(tab);
		var href = "${pageContext.request.contextPath}/dashboard/getCandidate?active="+tab +"&userId="+${caregiver.userId};
		location.href = href;
	}
}

//choose template
function chooseTemplate(id,templateName){
	$.ajax({
		type: "get",
		async : false,
		cache: false,
		url: "${pageContext.request.contextPath}/emailTemplates/getTemplate",
		data: {id:id,userId:${caregiver.userId}},
		dataType: "json",
		success: function(data){
			$("#toSubject").val(data.subject);
			$("#chooseT font").text(templateName);
			$("#templateName").val(templateName);
			CKEDITOR.instances.toContent.setData(data.content);		//set the content value
			
			//attachment
			var attachment = data.attachments;
			if(attachment != null && attachment.length>0){
				for(var i=0; i<attachment.length; i++){
					var size = attachment[i].size;
					if(size < 1024 && size > 0){					//B
						size = (size).toFixed(2) +"B";
					}else if(size < 1024*1024){			//KB
						size = (size/1024).toFixed(2) +"KB";
					}else if(size < 1024*1024*1024){	//M
						size = (size/1024/1024).toFixed(2) +"M";
					}else if(size < 1024*1024*1024*1024){
						size = (size/1024/1024/1024).toFixed(2) +"G";
					}else if(size > 1024*1024*1024*1024){
						size = (size/1024/1024/1024).toFixed(2) +"G";
					}
					$("#attachment-file").append("<li><i class='fa fa-paperclip'></i><label class='l-name'>"
							+attachment[i].name+"</label><label>("+size+")</label>"
							+"<button type='button' class='b-file' onclick='b_close(this);'>X</button></li>");
				}
			}			
			
			
		}
	});
}

//choose name
function chooseName(chooseName,chooseE){
	$("#chooseN font").text(chooseName);
	$("#chooseE").val(chooseE);
	$("#senderEmail").val(chooseName);
}

//show the cc
function cc(type){
	if(type=='0'){
		$("#cc").show();
		$("#cc_a").hide();			//a  href
	}else{
		$("#reply-cc").show();
		$("#reply-cca").hide();		//a href
	}

}

//show the bcc
function bcc(type){
	if(type=='0'){
		$("#bcc").show();
		$("#bcc_a").hide();
	}else{
		$("#reply-bcc").show();
		$("#reply-bcca").hide();
	}
}

//upload document
function up_document(){
	//var selectType = $("#s_type option:selected").val();
	var filename = $("#a_file").val();
	
	if($.trim(filename) != ''){
		$('#a_d').modal('hide');
		$('.loading').show();
		$("#add_file").submit();
		/* if(selectType == 'resume'){
			bootbox.confirm({  
				buttons: {  
				    confirm: {  
				        label: 'ok'  
				    },  
				    cancel: {  
				        label: 'cancel'
				    }  
				},  
				message: "Do you want to replace the existing resume? If not, please choose the document type 'other'.",  
				callback: function(result) {
		            if(result){
		            	$("#add_file").submit();
		            }
				}
			});  
		}else{
			$('#a_d').modal('hide');
			$('.loading').show();
			$("#add_file").submit();
		} */
	}else{
		alertMsg('The document field cannot be left blank.');
	}
} 

//add attachment
function attachment(obj,type){
	var fileList = obj.files;
	if(fileList.length > 0){
		var files;
		if(type == 'new'){
			files = new FormData($( "#f-file" )[0]);
		}else if(type == 'reply'){
			files = new FormData($( "#reply-fileForm" )[0]);
		}else if(type == 'editTemplate'){
			files = new FormData($("#editTemplateForm")[0]);
		}
		
		$.ajax({
			type: "POST",
			async : false,
			cache: false,
			url: "${pageContext.request.contextPath}/file/uploadAttachment",
			data: files,
	        contentType: false,  
	        processData: false,  
			success: function(data){
				for(var i=0; i<fileList.length; i++){
					var fileSize = fileList[i].size;
					if(fileSize < 1024 && fileSize > 0){					//B
						fileSize = (fileSize).toFixed(2) +"B";
					}else if(fileSize < 1024*1024){			//KB
						fileSize = (fileSize/1024).toFixed(2) +"KB";
					}else if(fileSize < 1024*1024*1024){	//M
						fileSize = (fileSize/1024/1024).toFixed(2) +"M";
					}else if(fileSize < 1024*1024*1024*1024){
						fileSize = (fileSize/1024/1024/1024).toFixed(2) +"G";
					}else if(fileSize > 1024*1024*1024*1024){
						fileSize = (fileSize/1024/1024/1024).toFixed(2) +"G";
					}
					
					if(type == 'new'){
						$("#attachment-file").append("<li><i class='fa fa-paperclip'></i><label class='l-name'>"+
								fileList[i].name+"</label><label>("+fileSize+")</label>"+
								"<button type='button' class='b-file' onclick='b_close(this);'>X</button></li>");
					}else if(type == 'reply'){
						$("#reply-attachmentUl").append("<li><i class='fa fa-paperclip'></i><label class='l-name'>"+
								fileList[i].name+"</label><label>("+fileSize+")</label>"+
								"<button type='button' class='b-file' onclick='b_close(this);'>X</button></li>");
					}else if(type == 'editTemplate'){
						$("#e-t-attachment").append("<li><i class='fa fa-paperclip'></i><label class='l-name'>"+
								fileList[i].name+"</label><label>("+fileSize+")</label>"+
								"<button type='button' class='b-file' onclick='b_close(this);'>&times;</button></li>");
					}
				}
			},error: function (e) {  
				alertMsg("The server encountered an error. Please refresh your browser.");
	        } 
		});
		
	}
	
}

//delete attachment
function b_close(obj){
	$(obj).parent().remove();
	/* var documentName = $(obj).parent().find(".l-name").text();		//get the document name
	var files = new FormData($( "#f-file" )[0]);
	$.ajax({
		type: "POST",
		url: "${pageContext.request.contextPath}/file/deleteAttachment?documentName="+documentName,
		async: false,
		cache: false,
		data: files,
	 	contentType: false,  
	    processData: false, 
		success: function(data){
			$(obj).parent().remove();
		},error: function(e){
			alertMsg(e);
		}
	}); */
}

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
		alert("The new passwords do not match. Please try again");
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
 	    			alert("The current password is incorrect. Please try again.");
 	    		}
 			}
 		});   	
	}
}

//reply email(send)
function replySend(){
	var receiveName = $("input[name='receiveName']").val();
	var receiveEmail = $("input[name='receiveEmail']").val();
	var subject = $("input[name='subject']").val();
	var bccEmail = $(".reply-BCC").val();
	var ccEmail = $(".reply-CC").val();
	var senderName = $("#senderName").val();
	
	var attachment = "";
	$("#reply-attachmentUl .l-name").each(function(){
		attachment = attachment + $(this).text() + ",";
	});
	if(attachment.length > 0){
		attachment = attachment.substr(0, attachment.length-1);
	}
	
	//get the content
	var content = CKEDITOR.instances.content.getData();
	var tcontent = $.trim(content.replace(/<[^>]+>/g,"").replace(/&nbsp;/ig,""));
	var content = encodeURIComponent(content);	
	
	if(receiveName==null || receiveName==''){
		alertMsg("The addressee's mail cannot be left blank.");
	}else if(subject==null || subject==''){
		alertMsg("The subject cannot be left blank.");
	}else if(content==null || content==''){
		alertMsg("The content cannot be left blank.");
	}else{
		var href = "${pageContext.request.contextPath}/emailTemplates/send?candidateId="+'${caregiver.userId}' + "&active="+$("#tab").val() +
		"&senderName="+senderName +"&receiveEmail="+receiveEmail +"&subject="+subject +"&content="+content +"&userId="+$("#chooseE").val() +
		"&ccEmail="+ccEmail +"&bccEmail="+bccEmail +"&attachment="+attachment +"&receiveName="+receiveName;
		
		location.href = href;
		
	}
}

	function changeConcordeStatus(){
		var status = getChangedStatus("#registerStatus");
		$.ajax({
			type : "post",
			async : false,
			cache : false,
			url : "${pageContext.request.contextPath}/candidate/changeTheStatusOfRegisteredConcorde",
			data : {caregiverId : "${caregiver.userId}", status : status},
			dataType : "json",
			success : function (){
				
			}
		});
	};
	
	function getChangedStatus(checkboxId){
		var status;
		if($(checkboxId).is(":checked")){
			status = "YES";
		}else{
			status = "NO";
		}
		return status;
	}
	
	function changeSchemaStatus(){
		var status = getChangedStatus("#schemaStatus");
		$.ajax({
			type : "post",
			async : false,
			cache : false,
			url : "${pageContext.request.contextPath}/candidate/changeTheStatusOfAdvancePlacementScheme",
			data : {caregiverId : "${caregiver.userId}", status : status},
			dataType : "json",
			success : function (){
				
			}
		});
	};
	
	function changeVerifiedStatus(){
		var status = getChangedStatus("#verfiedStatus");
		/* if(status == "YES"){
			$('#medicalCertValidator').tooltip('enable')
			var message = "Validated by ${sessionScope.firstName} ${sessionScope.lastName}";
			$('#medicalCertValidator').attr('title', message).tooltip('fixTitle').tooltip('show');
		}
		if(status == "NO"){
			$('#medicalCertValidator').tooltip('disable')
		}  */
		$.ajax({
			type : "post",
			async : false,
			cache : false,
			url : "${pageContext.request.contextPath}/candidate/changeTheStatusOfMedicalCertVerified",
			data : {caregiverId : "${caregiver.userId}", status : status},
			dataType : "json",
			success : function (){
				
			}
		});
	};
	
	//delete template
	function deleteTemplate(obj,templateId){
		$.ajax({
			type: "POST",
			async : false,
			cache: false,
			url: "${pageContext.request.contextPath}/emailTemplates/deleteTemplate",
			data:{id:templateId},
	        success: function(data){
	        	$(obj).parent().parent().remove();		//remove this template;
	        	$("#emailTemplate-"+templateId).remove();
			},error: function(XMLHttpRequest, textStatus, errorThrown) {
               /*  alert(XMLHttpRequest.status);
                alert(XMLHttpRequest.readyState);
                alert(textStatus); */
                alertMsg("Server error")
            },
		});
		
	}	
	
	//get the template
	function showTemplate(id){
		$.ajax({
			type: "get",
			async : false,
			cache: false,
			url: "${pageContext.request.contextPath}/emailTemplates/getTemplate",
			data: {id:id,userId:${caregiver.userId}},
			dataType: "json",
			success: function(data){
			    console.log('showTemplate id:'+id+' data:'+JSON.stringify(data));
				$("#e-t-id").val(data.id);
				$("#e-t-subject").val(data.subject);
				$("#e-t-name").val(data.name);
				CKEDITOR.instances.ETcontent.setData(data.content);		//set the content value
				
				//attachment
				var attachment = data.attachments;
				if(attachment != null && attachment.length>0){
					$("#originalAttachment").val(data.attachment);
					for(var i=0; i<attachment.length; i++){
						var size = attachment[i].size;
						if(size < 1024 && size > 0){					//B
							size = (size).toFixed(2) +"B";
						}else if(size < 1024*1024){			//KB
							size = (size/1024).toFixed(2) +"KB";
						}else if(size < 1024*1024*1024){	//M
							size = (size/1024/1024).toFixed(2) +"M";
						}else if(size < 1024*1024*1024*1024){
							size = (size/1024/1024/1024).toFixed(2) +"G";
						}else if(size > 1024*1024*1024*1024){
							size = (size/1024/1024/1024).toFixed(2) +"G";
						}
						
						$("#e-t-attachment").append("<li><i class='fa fa-paperclip'></i><label class='l-name'>"
								+attachment[i].name+"</label><label>("+size+")</label>"
								+"<button type='button' class='b-file' onclick='b_close(this);'>X</button></li>");
					}
				}			
				
				//show the template
				$("#editTemplate").modal();
			}
		});
		
	}

	//submit edit template
	function editTemplate(){
	    console.log("editTemplate called")
		var subject = $("#e-t-subject").val();


		//get the content
		var content = CKEDITOR.instances.ETcontent.getData();
		var tcontent = $.trim(content.replace(/<[^>]+>/g,"").replace(/&nbsp;/ig,""));
		var content = encodeURIComponent(content);

		console.log("subject:"+subject+" content:"+content);

		//get attachment
        var originalAttachment = $("#originalAttachment").val();
        var attachment = "";
        $("#e-t-attachment .l-name").each(function(){
            attachment = attachment + $(this).text() + ",";
        });
        if(attachment.length > 0){
            attachment = attachment.substr(0, attachment.length-1);
        }

        var name = $("#e-t-name").val();
        var id = $("#e-t-id").val();
        if(subject=='' || subject==null){
            alertMsg("The subject cannot be left blank.");
        }else if(content==null || content==''){
            alertMsg("The content cannot be left blank.");
        }else if(name==null || name==''){
            alertMsg("The Template name cannot be left blank.");
        }else if(id==null || id==''){
            alertMsg("The Template did not exists.");
        }else{
            var href = "${pageContext.request.contextPath}/emailTemplates/editTemplate?id="+id +"&attachment="+attachment+"&originalAttachment="+originalAttachment
                +"&subject="+subject +"&content="+content +"&name="+name +"&active=5" +"&userId="+'${caregiver.userId}';

            location.href = href;

            $(".loading").show();
        }
	}
	
</script>

</html>	
