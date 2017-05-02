<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<html lang="en">
<script type="text/javascript">
	
	function editBio(){
		$("#basicInfo").attr("readonly", false);
		$("#basicInfo").css({"border":"black solid 1px"});
		
		$("#educationAndExperience").attr("readonly", false);
		$("#educationAndExperience").css({"border":"black solid 1px"});
		
		$("#experienceDetails").hide();
		$("#experienceDetailsEdit").css({"display":"block"});
		
		$("#trained").css({"display":"none"});
		$("#editTrained").css({"display":"block"});
		$("#trainedEdit").val("${bio.trainedToCprOrFA}");
		
		if("${loginUser.role}" == "ROLE_ADMIN" || "${loginUser.role}" == "ROLE_RECRUITER"){
			$("#workInSGId").css({"display":"none"});
			$("#editWorkInSG").css({"display":"block"});
			$("#workInSGEdit").val("${bio.workInSG}");
			
			$("#workInHKId").css({"display":"none"});
			$("#editWorkInHK").css({"display":"block"});
			$("#workInHKEdit").val("${bio.workInHK}");
			
			$("#workInTWId").css({"display":"none"});
			$("#editWorkInTW").css({"display":"block"});
			$("#workInTWEdit").val("${bio.workInTW}");
			
			$("#salaryInSGD").attr("readonly", false);
			$("#salaryInSGD").css({"border":"black solid 1px"});
			
			$("#salaryInHKD").attr("readonly", false);
			$("#salaryInHKD").css({"border":"black solid 1px"});
			
			$("#salaryInTWD").attr("readonly", false);
			$("#salaryInTWD").css({"border":"black solid 1px"});
			
			$("#numberOfPlacements").attr("readonly", false);
			$("#numberOfPlacements").css({"border":"black solid 1px"});
			
		}
		
		$("#nursingExperience").attr("readonly", false);
		$("#nursingExperience").css({"border":"black solid 1px"});
		
		$("#hobby").attr("readonly", false);
		$("#hobby").css({"border":"black solid 1px"});
		
		$("#editBioBtn").hide();
		$("#saveBioBtn").show();
		$("#cancelBtn").show();
	};
	
	function saveBio(){
		var cerp = $("#trainedEdit").val();
		$("#trained").val(cerp);
		var workInSG = $("#workInSGEdit").val(); 
		$("#workInSGId").val(workInSG);
		var workInHK = $("#workInHKEdit").val(); 
		$("#workInHKId").val(workInHK);
		var workInTW = $("#workInTWEdit").val(); 
		$("#workInTWId").val(workInTW);
		$("#bioForm").submit();
	};

	function cancelEditBio(){
		$("#basicInfo").attr("readonly", true);
		$("#basicInfo").css({"border":"none"});
		$("#basicInfo").val("${bio.candidateBasicInformation}");
		
		$("#educationAndExperience").attr("readonly", true);
		$("#educationAndExperience").css({"border":"none"});
		$("#educationAndExperience").val("${bio.educationAndExperience}");
		$("#experienceDetails").show();
		$("#experienceDetailsEdit").css({"display":"none"});
		
		$("#editTrained").css({"display":"none"});
		$("#trained").attr("readonly", true);
		$("#trained").css({"border":"none", "display":"block"});
		$("#trained").val("${bio.trainedToCprOrFA}");
		
		$("#editWorkInSG").css({"display":"none"});
		$("#workInSGId").attr("readonly", true);
		$("#workInSGId").css({"border":"none", "display":"block"});
		$("#workInSGId").val("${bio.workInSG}");
		
		$("#editWorkInHK").css({"display":"none"});
		$("#workInHKId").attr("readonly", true);
		$("#workInHKId").css({"border":"none", "display":"block"});
		$("#workInHKId").val("${bio.workInHK}");
		
		$("#editWorkInTW").css({"display":"none"});
		$("#workInTWId").attr("readonly", true);
		$("#workInTWId").css({"border":"none", "display":"block"});
		$("#workInTWId").val("${bio.workInTW}");
		
		$("#nursingExperience").attr("readonly", true);
		$("#nursingExperience").css({"border":"none"});
		$("#nursingExperience").val("${bio.nursingExperience}");
		
		$("#hobby").attr("readonly", true);
		$("#hobby").css({"border":"none"});
		$("#hobby").val("${bio.hobby}");
		
		$("#salaryInSGD").attr("readonly", true);
		$("#salaryInSGD").css({"border":"none"});
		$("#salaryInSGD").val("${bio.salaryInSGD}");
		
		$("#salaryInHKD").attr("readonly", true);
		$("#salaryInHKD").css({"border":"none"});
		$("#salaryInHKD").val("${bio.salaryInHKD}");
		
		$("#salaryInTWD").attr("readonly", true);
		$("#salaryInTWD").css({"border":"none"});
		$("#salaryInTWD").val("${bio.salaryInTWD}");
		
		$("#numberOfPlacements").attr("readonly", true);
		$("#numberOfPlacements").css({"border":"none"});
		$("#numberOfPlacements").val("${bio.numberOfPlacements}");
		
		$("#editBioBtn").show();
		$("#saveBioBtn").hide();
		$("#cancelBtn").hide();
	};
	
</script>
<div>
	
	<div class="row" style="margin-left: 10px;margin-top:20px;">
		<div class="col-sm-1">
			<h3 style="float:left;">
				<span class="fa fa-book"></span><strong> BIO</strong>
			</h3>
		</div>
		
		<div class="col-sm-1">
			<button id="editBioBtn" type="button" class="btn btn-primary" onclick="editBio();">Edit Bio</button>
		</div>
		
		<c:if test="${bio.validator != null}">
			<div class="col-sm-4 alert alert-success">
				<label>Validated by ${bio.validator} on <fmt:formatDate value="${bio.lastModifyTime}" type="both" pattern="dd/MM/yyyy" /> . </label>
			</div>
		</c:if>
	</div>
	
	<c:if test="${bio != null}">
		<form id="bioForm" action="${pageContext.request.contextPath}/bio/editBioInfoByCaregiverId" method="post">
			<input type="hidden" name="caregiverId"  value="${bio.caregiverId}"/>
			<input type="hidden" name="validatorId" value="${loginUser.userId}" />
			<div id="about" class="panel">
				<div class="form-group summary-subtitle" >ABOUT</div>
				<div class="form-group summary-subbody">
					<textarea id="basicInfo" name="candidateBasicInformation" rows="3" cols="160" style="border:none" readonly>${bio.candidateBasicInformation}</textarea>
				</div>
			</div>
			<div id="educationAndExperienceInfo" class="panel">
				<div class="form-group summary-subtitle">EDUCATION AND EXPERIENCE</div>
				<div class="form-group summary-subbody">
					<textarea id="educationAndExperience" name="educationAndExperience" rows="3" cols="160" style="border:none" readonly>${bio.educationAndExperience}</textarea></div>
				<c:set var="experienceInfo" value="${bio.experienceList}"></c:set>
				<c:if test="${experienceInfo != null}">
					<div class="form-group summary-subbody">${bio.experienceTitle}</div>
					<input type="hidden" name="experienceTitle" value="${bio.experienceTitle}"/>
					<div id="experienceDetails" class="form-group summary-subbody">
						<ul>
							<c:forEach items="${bio.experienceList}" var="experience">
								<li style="margin-left:50px;">${experience}</li>
							</c:forEach>
						</ul>
					</div>
					<div id="experienceDetailsEdit" class="form-group summary-subbody" style="display: none">
						<div class="form-group summary-subbody">
							<textarea id="experienceDetailsInfo" name="experienceDetails" rows="3" cols="160" style="border: 1px solid black;">${bio.experienceDetails}</textarea>
						</div>
					</div>
				</c:if>
			</div>
			
			<div id="trainedToCpr" class="panel">
				<div class="form-group summary-subtitle">TRAINED in CPR / FIRST AID</div>
				<div class="form-group summary-subbody">
					<textarea id="trained" name="trainedToCprOrFA" cols="160" style="border:none" readonly>${bio.trainedToCprOrFA}</textarea>
					<div id="editTrained" style="display:none">
						<select id="trainedEdit">
							<option value="Yes">Yes</option>
							<option value="No">No</option>
						</select>
					</div>
				</div>
			</div>
			
			<div id="nursingExperienceInfo" class="panel">
				<div class="form-group summary-subtitle">NURSING EXPERIENCE</div>
				<div class="form-group summary-subbody">
					<textarea id="nursingExperience" name="nursingExperience" rows="3" cols="160" style="border:none" readonly>${bio.nursingExperience}</textarea>
				</div>
			</div>
			
			<div id="hobbyInfo" class="panel">
				<div class="form-group summary-subtitle">HOBBIES</div>
				<div class="form-group summary-subbody">
					<textarea id="hobby" name="hobby" rows="3" cols="160" style="border:none" readonly>${bio.hobby}</textarea>
				</div>
			</div>
			
			<div id="numberOfPlacementsInfo" class="panel">
				<div class="form-group summary-subtitle">Number Of Placements</div>
				<div class="form-group summary-subbody">
					<input type="text" id="numberOfPlacements" name="numberOfPlacements" value="${bio.numberOfPlacements}" style="border:none" readonly/>
				</div>
			</div>
			
			<div id="work_in_sg" class="row">
				<div class="col-md-3">
					<div class="form-group summary-subtitle">Work in Singapore</div>
					<div class="form-group summary-subbody">
						<textarea id="workInSGId" name="workInSG" cols="160" style="border:none" readonly>${bio.workInSG}</textarea>
						<div id="editWorkInSG" style="display:none">
							<select id="workInSGEdit">
								<option value="Yes">Yes</option>
								<option value="No">No</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-9">
					<div class="form-group summary-subtitle">Expected Salary in SGD</div>
					<div class="form-group summary-subbody">
						<input type="text" id="salaryInSGD" name="salaryInSGD" value="${bio.salaryInSGD}" style="border:none" readonly />
					</div>
				</div>
			</div>
			
			<div id="work_in_hk" class="row">
				<div class="col-md-3">
					<div class="form-group summary-subtitle">Work in Hong Kong</div>
					<div class="form-group summary-subbody">
						<textarea id="workInHKId" name="workInHK" cols="160" style="border:none" readonly>${bio.workInHK}</textarea>
						<div id="editWorkInHK" style="display:none">
							<select id="workInHKEdit">
								<option value="Yes">Yes</option>
								<option value="No">No</option>
							</select>
						</div>
					</div>	
				</div>
				<div class="col-md-9">
					<div class="form-group summary-subtitle">Expected Salary in HKD</div>
					<div class="form-group summary-subbody">
						<input type="text" id="salaryInHKD" name="salaryInHKD" value="${bio.salaryInHKD}" style="border:none" readonly />
					</div>
				</div>
			</div>
			
			<div id="work_in_tw" class="row">
				<div class="col-md-3">
					<div class="form-group summary-subtitle">Work in Taiwan</div>
					<div class="form-group summary-subbody">
						<textarea id="workInTWId" name="workInTW" cols="160" style="border:none" readonly>${bio.workInTW}</textarea>
						<div id="editWorkInTW" style="display:none">
							<select id="workInTWEdit">
								<option value="Yes">Yes</option>
								<option value="No">No</option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-9">
					<div class="form-group summary-subtitle">Expected Salary in TWD</div>
					<div class="form-group summary-subbody">
						<input type="text" id="salaryInTWD" name="salaryInTWD" value="${bio.salaryInTWD}" style="border:none" readonly />
					</div>
				</div>
			</div>
			
			<button id="saveBioBtn" type="button" style="display:none" class="btn btn-primary form-group summary-subbody" onclick="saveBio()">Save Bio</button>
			<button id="cancelBtn" type="button" style="display:none" class="btn btn-primary form-group summary-subbody" onclick="cancelEditBio()">Cancel</button>
		</form>
	</c:if>
	
</div>
</html>