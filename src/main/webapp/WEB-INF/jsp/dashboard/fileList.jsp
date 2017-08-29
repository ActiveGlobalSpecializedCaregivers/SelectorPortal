<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/dashboard/fileList.js"></script>
</head>
<body style="overflow: scroll;">
	<div class="row" style="margin-left: 10px;">
		<div class="col-md-8" style="margin-bottom: 20px;margin-top: 30px;">
			<h3 style="float:left;">
				<span class="fa fa-copy"></span> <strong>Documents</strong>
			</h3>
			<button type="button" id="i-add" style="float:right;margin-right: 20px;" class="btn btn-success">
				<span class="glyphicon glyphicon-plus"></span> ADD DOCUMENT
			</button>
		</div>
		<div class="col-md-8">
		<table id="fileList" class="table table-hover">
			<thead>
				<tr>
					<th>Name</th>
					<th>FileType</th>
					<th>LastModified</th>
					<th>Extension</th>
					<th>Operation</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${dfList}" var="file">
					<c:if test="${file.thumbnail eq null}">
						<tr>
							<td>
								<c:choose>
									<c:when test="${file.extension eq fn:toUpperCase('pdf')}">
										<c:set var="found" value="${false}"/>
										<c:forEach items="${dfList}" var="f">
											<c:if test="${file.name eq fn:replace(f.name, '.thumbnail.jpg', '') && fn:contains(f.s3Key, file.s3Key)}">
												<c:set var="found" value="${true}"/>
												<img src="${f.thumbnail}"></img>
											</c:if>
										</c:forEach>
										<c:if test="${found == false}">
											<i class="fa fa-file-pdf-o"></i>
										</c:if>
									</c:when>
									<c:when test="${file.extension eq fn:toUpperCase('doc') || file.extension eq fn:toUpperCase('docx')}">
										<i class="fa fa-file-word-o"></i> 
									</c:when>
									<c:when test="${file.extension eq fn:toUpperCase('jpg') || file.extension eq fn:toUpperCase('jpeg') || file.extension eq fn:toUpperCase('png') || file.extension eq fn:toUpperCase('gif')}">
										<c:forEach items="${dfList}" var="f">
											<c:if test="${file.name eq fn:replace(f.name, '.thumbnail.jpg', '') && fn:contains(f.s3Key, file.s3Key)}">
												<img src="${f.thumbnail}"></img> 
											</c:if>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<i class="fa fa-picture-o"></i>
									</c:otherwise>
								</c:choose>
								<a title="${file.name}" target="_blank" href="${file.path}">${file.name}</a>
							</td>
							<td>${file.type}</td>
							<td><fmt:formatDate value="${file.lastModified}" type="both" pattern="dd/MM/yyyy HH:mm:ss"/></td>
							<td>${file.extension}</td>
							<td>
								<c:choose>
									<c:when test="${file.type eq 'resume'}">
										<%-- <c:choose>
											<c:when test="${file.name eq 'resume.pdf'}">
												<a class="fa fa-download" title="Download Document" download href="${file.path}"></a>
												<a class="fa fa-trash-o btn-pad" title="Delete Document" href="#" onclick="file_del('${caregiver.userId}','${file.s3Key}','${pageContext.request.contextPath}');"></a>
											</c:when>
											<c:otherwise>
												<a class="fa fa-download" title="Download Document" download href="${file.path}"></a>
												<a class="fa fa-trash-o btn-pad" title="Delete Document" href="#" onclick="file_del('${caregiver.userId}','${file.s3Key}','${pageContext.request.contextPath}');"></a>
												<a class="fa fa-edit" title="Rename Document" href="#" onclick="rename('${caregiver.userId}','${file.s3Key}','${file.name}');"></a>
												<a class="fa fa-file btn-pad" title="Change FileType" href="#" onclick="renameType('${caregiver.userId}','${file.s3Key}','${file.type}','${file.extension}');"></a>
											</c:otherwise>
										</c:choose> --%>
										<a class="fa fa-download" title="Download Document" download href="${file.path}"></a>
										<a class="fa fa-trash-o btn-pad" title="Delete Document" href="#" onclick="file_del('${caregiver.userId}','${file.s3Key}','${pageContext.request.contextPath}');"></a>
									</c:when>
									<c:when test="${file.type eq 'my_photo'}">
										<a class="fa fa-download" title="Download Document" download href="${file.path}"></a>
										<a class="fa fa-trash-o btn-pad" title="Delete Document" href="#" onclick="file_del('${caregiver.userId}','${file.s3Key}','${pageContext.request.contextPath}');"></a>
									</c:when>
									<c:when test="${file.type eq 'attachment'}">
										<a class="fa fa-download" title="Download Document" download href="${file.path}"></a>
									</c:when>
									<c:otherwise>
										<a class="fa fa-download" title="Download Document" download href="${file.path}"></a>
										<a class="fa fa-trash-o btn-pad" title="Delete Document" href="#" onclick="file_del('${caregiver.userId}','${file.s3Key}','${pageContext.request.contextPath}');"></a>
										<a class="fa fa-edit" title="Rename Document" href="#" onclick="rename('${caregiver.userId}','${file.s3Key}','${file.name}');"></a>
										<a class="fa fa-file btn-pad" title="Change FileType" href="#" onclick="renameType('${caregiver.userId}','${file.s3Key}','${file.type}','${file.extension}');"></a>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
		</div> 
	</div>
</body>
</html>