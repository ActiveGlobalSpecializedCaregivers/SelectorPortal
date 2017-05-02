<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<script  type="text/javascript">

	var evaluationQuestionList;  
	
	function addNewEvaluation(){
		$("#templateList").val(-1);
		$("#assessmentNotify").attr("style", "display:none");
		$("#assessmentSummary").attr("style", "display:none");
		$("#addNewEvaluationModal").show();
	};
	
	function getEvaluationQuestionsByTemplateId(){
		$("#evaluationQuestionList").empty();
		$("#evaluationSummary").val("");
		var id = $("#templateList option:selected").val();
		if(id != -1){
			$.ajax({
				type : "post",
				async : false,
				cache : false,
				url : "${pageContext.request.contextPath}/assessment/getEvaluationQuestionListByTemplateId",
				data : {templateId : id},
				dataType : "json",
				success : function(data){
					$("#assessmentNotify").attr("style", "display:block");
					$("#assessmentSummary").attr("style", "display:block");
					evaluationQuestionList = data;
		 			for(var i in evaluationQuestionList){
		 				var index = parseInt(i) + 1;
		 				var divTitle = "<div class=\"form-group\" style=\"margin-top:-15px;\"><div class=\"col-sm-3\"></div><div class=\"col-sm-9\">";
		 				var divFooter = "</div></div>";
		 				var questionId = "<input type=\"hidden\" name=\"questionId\" value=\""+ evaluationQuestionList[i].questionid + "\"ss</input>";
		 				var question = divTitle + questionId + "<h4>" + index +"."+evaluationQuestionList[i].question+"   -  Worth " + evaluationQuestionList[i].percentage+ "% of score" + "</h4>" +divFooter;
		 				var selections = "";
		 				for(var j in evaluationQuestionList[i].selectionList){
		 					var index = parseInt(j) + 1;
		 					selections = selections + "<input type=\"radio\" value=\"" + index + " \"  name =\"evaluationScore" + evaluationQuestionList[i].questionid +"\"" +"\">" + evaluationQuestionList[i].selectionList[j] + "</input>" + "&nbsp;";
		 				}
		 				selections = divTitle + selections + divFooter;
		 				var comment = divTitle + "<label class=\"control-label\">Comments:</label>" + divFooter;
		 				comment = comment + divTitle + "<textarea name=\"evaluationComment\" class=\"form-control\" rows=\"5\" />" +divFooter;
		 				$("#evaluationQuestionList").append(question, selections,comment);
		 				index = index + 1;
		  			}
				}
			});
		}else{
			$("#assessmentNotify").attr("style", "display:none");
			$("#assessmentSummary").attr("style", "display:none");
		}
	};
	
	function saveEvaluation(){
		var templateIds;
		var templateId = $("#templateList option:selected").val();
		if(templateId != -1){
		  	$("#requiredFieldsForEvaluation").submit();
		}
	};
	
	function cancelEvaluation(){
		$("#addNewEvaluationModal").hide();
		$("#evaluationQuestionList").empty();
		$("#evaluationSummary").empty();
	}

	function deleteEvaluation(applicantId, templateId){
		$.ajax({
			type : "get",
			url : "${pageContext.request.contextPath}/assessment/deleteEvaluationByApplicantIdAndTemplateId",
			data : {applicantId : applicantId, templateId : templateId},
			dataType : "json",
			// Unexpected end of JSON input
			error: function(jqXHR, textStatus, errorThrown) {
 				console.log(textStatus, errorThrown);
 				location.reload();
			}
		});
	}
</script>
<html lang="en">
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div>
	
	<div class="row" style="margin-left: 10px;margin-top:20px;">
		<h3 style="float:left;">
			<span class="fa fa-star-o"></span><strong> Assessments</strong>
		</h3>
	</div>
	<div id="evaluations" class="panel" style="margin-top: 20px;">
		<div id="evaluationsTitle" class="row panel-title">
			<div class="col-md-6" data-toggle="collapse" href="#evaluationsBody">
				<h3 style="margin-left: 10px;">Evaluations</h3>
			</div>
			<div class="col-md-6">
				<button type="button" class="btn btn-success" onclick="addNewEvaluation()">ADD EVALUATION</button>
			</div>
		</div>
		<div id="evaluationsBody" class="row panel-collapse collapse in"
			style="margin-top: -20px;">
			<c:forEach items="${evaluationModelList}" var="evaluationModel">
				<div class="panel-body" style="border: 0px;">
					<div class="col-md-3">${evaluationModel.templateName}</div>
					<div class="col-md-1" data-toggle="collapse" href="#${evaluationModel.divId}" >
						<button type="button" class="btn btn-sm btn-primary" >VIEW DETAILS</button>		
					</div>
					<sec:authorize access="hasRole('ROLE_ADMIN')">
						<div class="col-md-2">
							<button type="button" class="btn btn-sm btn-danger" onclick="deleteEvaluation(${evaluationModel.applicantId}, ${evaluationModel.templateId})">DELETE</button>			
						</div>
					</sec:authorize>
					<div id="${evaluationModel.divId}" class="panel-collapse collapse">
						<table class="table">
							<thead>
								<tr>
									<th class="col-sm-1">PCT</th>
									<th class="col-sm-1">SCORE</th>
									<th class="col-sm-4">ASSESSMENT/MEASUREMENT</th>
									<th class="col-sm-6">COMMENTS</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${evaluationModel.anwserList}" var="evaluationAnswer">
								<c:if test="${evaluationAnswer.questionid != -1}">
										<tr>
											<td>${evaluationAnswer.percentage}</td>
											<td>${evaluationAnswer.evaluationScore}</td>
											<td>${evaluationAnswer.question} <br />
												${evaluationAnswer.questionSelection}
											</td>
											<td>${evaluationAnswer.comment}</td>
										</tr>
									</c:if>
							</c:forEach>
							</tbody>
						</table>
						<div id="summary">
								<div class="col-md-9">SUMMARY COMMENTS</div>
								<div class="panel-body" style="border: 0px;">${evaluationModel.summary}</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>

	</div>


	<!-- Add New Evaluation -->
	<div id="addNewEvaluationModal" style="display:none">
		<div>
			<div class="modal-header" style="background-color: currentColor;">
				<h4 class="modal-title" style="margin-left: 10px; color: white">NEW
					EVALUATION</h4>
			</div>
			<div class="modal-body">
				<div>* Required fields</div>
				<form id="requiredFieldsForEvaluation"
					action="${pageContext.request.contextPath}/assessment/saveEvaluationAnswerList"
					method="post" class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-sm-3">Applicant:</label>
						<div class="col-sm-9">
							<input type="hidden" id="caregiverId" name="caregiverId"
								value="${caregiver.userId}"></input> <label
								class="control-label">${caregiver.fullName}</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-3">Evaluated By:</label>
						<div class="col-sm-9">
							<input type="hidden" name="evaluatedId"
								value="${loginUser.userId}"></input> <label
								class="control-label">${loginUser.firstName}
								${loginUser.lastName}</label>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-3">Regarding this job:</label>
						<div class="col-sm-9">
							<select class="form-control">
								<option>Singapore / Hong Kong Live-In Caregiver
									(Indonesia Candidates) - Singapore</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-3">Evaluation Template:</label>
						<div class="col-sm-9">
							<select id="templateList" name="templateId" class="form-control"
								onchange="getEvaluationQuestionsByTemplateId()">
								<option value="-1">Choose evaluation template</option>
								<c:forEach items="${evaluationTemplateList}"
									var="evaluationTemplate">
									<option value="${evaluationTemplate.id}">${evaluationTemplate.name}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-group" id="assessmentNotify"
						style="display: none;">
						<label class="control-label col-sm-3">Assessments:</label>
						<div class="col-sm-9">
							<label class="control-label">Please score this applicant
								using the following assessments:</label>
						</div>
					</div>
					<div id="evaluationQuestionList"></div>

					<div id="assessmentSummary" style="display: none">
						<div class="form-group">
							<label class="control-label col-sm-3">Comments</label>
							<div class="col-sm-9">
								<label class="control-label">Please enter any additional
									thoughts to include with your evaluation: </label>
							</div>
						</div>
						<div class="form-group">
								<div class="col-sm-3"></div>
								<div class="col-sm-9">
									<textarea id="evaluationSummary" name="evaluationSummary" class="form-control"
										rows="10"></textarea>
								</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary"
							onclick="saveEvaluation();">SAVE EVALUATION</button>
						<button type="button" class="btn btn-default" onclick="cancelEvaluation();">CANCEL</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	
</div>
</html>