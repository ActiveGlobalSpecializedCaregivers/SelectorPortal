<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<script type="text/javascript">
	function changeEmailTemplate(obj,id,templateId){
		var val = $(obj).val();		//change option value
		
		if(val != templateId){
			$.ajax({
				type : "GET",
				async : false,
				cache : false,
				url : "${pageContext.request.contextPath}/emailTemplates/editAutomatedEmailsTemplate",
				data : {templateId : val, id : id},
				success: function (data){
					
				},error: function(e){
					alert("Please refresh page!")
				}
			});
		}

	}
</script>
<jsp:include page="../header.jsp" />
<div id="page-content" style="overflow: auto;max-width: 700px;">
	<div id='wrap'>
		<div id="page-heading">
			<h1>Automated Emails</h1>
		</div>
		<div class="container">
			<table class="table">
				<thead>
					<tr style="color: blue;text-align: center;">
						<td>Workflow step</td>
						<td></td>
						<td>Template</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="automatedEmailsTemplates" varStatus="status">
						<tr>
							<td style="color:black;background-color:rgba(169, 169, 169, 0.35);">
								${status.count}-${automatedEmailsTemplates.name}
							</td>
							<td style="width:120px;"><img style="padding-top: 10px;" src="${contextPath}/resources/application/img/right_arrow.jpg"/></td>
							<td>
								<select class="form-control" onchange="changeEmailTemplate(this,'${automatedEmailsTemplates.id}','${automatedEmailsTemplates.templateId}');">
									<option value="None">None</option>
									<c:forEach items="${listTemplates}" var="emailTemplate">
										<c:choose>
											<c:when test="${emailTemplate.id eq automatedEmailsTemplates.templateId}">
												<option value="${emailTemplate.id}" selected="selected">${emailTemplate.name}</option>
											</c:when>
											<c:otherwise><option value="${emailTemplate.id}">${emailTemplate.name}</option></c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
						</tr>
					</c:forEach>
				</tbody>
		  	</table>
		</div>
		<!-- container -->
		
	</div>
	<!--wrap -->
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
	</div>
</footer>
</html>
