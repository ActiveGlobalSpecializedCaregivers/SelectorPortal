<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.cloudaxis.agsc.portal.helpers.ApplicationDict"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<head>
<script type="text/javascript">
$(function (){

	$( ".mh_date" ).datepicker({
        changeYear: true,
        changeMonth: true,
        yearRange: '1950:+0',
        buttonImage: 'calendar_1.png',
        dateFormat: 'yy-mm-dd'
	});
	
	//Identify duplicate candidates(by email)
	$("#email").blur(function (){
		var email = $("#email").val();
		var userEmail = '${caregiver.email}';
		if(email != userEmail){
			var Regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			if(!Regex.test(email)){
				$("#checkEmail").html("Enter valid email address; eg:agsc@outlook.com");
				$("#checkEmail").css("color","#f00");
			}else{
				$.get("${pageContext.request.contextPath}/dashboard/checkExistCandidate", { email:email,userEmail:'${caregiver.email}' } ,function(data){
					if(data.status == 'error') {
						$("#checkEmail").html(data.msg);
						$("#checkEmail").css("color","#f00");
					}else if(data.status == 'success'){
						$("#checkEmail").html("The mailbox can be used.");
						$("#checkEmail").css("color","green");
					}
				},"json");
			}
		}else{
			$("#checkEmail").html("");
		}
	});
	
});

function closeEdit(){
	location.href="${pageContext.request.contextPath}/dashboard/getCandidate?userId="+${caregiver.userId}+"&active=1";
}
</script>
</head>
<body>
	<div class="loading" style="display: none;"><i class="fa fa-refresh fa-5x fa-spin"></i></div>
	<div style="width:950px;height: 800px;overflow: auto;">
	<form:form id="myForm" action="${pageContext.request.contextPath}/dashboard/candidateForm" modelAttribute="caregiver" method="post">
		<form:input type="hidden" path="userId"/>
		<form:input type="hidden" path="workInTW" />
		<form:input type="hidden" path="numbersOfPlacement" />
		<input type="hidden" name="active" value="1"/>
		<div class="content-slide">
			<div class="questionnaire1">
				<div class="divLeft1">Firstname:</div>
				<div class="divRitht">
					<form:input id="firstName" path="firstName" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.firstName}</div>
				<div id="cleanStyle"></div>
				<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Lastname:</div>
				<div class="divRitht">
					<form:input id="lastName" path="lastName" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.lastName}</div>
				<div id="cleanStyle"></div>
				<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Mobile:</div>
				<div class="divRitht">
					<form:input id="mobile" path="mobile" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.mobile}</div>
				<div id="cleanStyle"></div>
				<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Email:</div>
				<div class="divRitht">
					<form:input id="email" path="email" class="i-c"/>
					<span id="checkEmail"></span>
				</div>
				<div class="divRitht1">${caregiver.email}</div>
				<div id="cleanStyle"></div>
				<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Work in Singapore?:</div>
				<div class="divRitht">
					<form:select path="workInSG" items="<%=ApplicationDict.getyN()%>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.workInSG}</div>
				<div id="cleanStyle"></div>
				<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Work In Hong Kong?:</div>
				<div class="divRitht">
					<form:select path="workInHK" items="<%=ApplicationDict.getyN()%>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.workInHK}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Skype ID:</div>
				<div class="divRitht">
					<form:input path="skype" id="skype" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.skype}</div>
				<div id="cleanStyle"></div>
				<hr/>
			</div>
			
		   	<div class="questionnaire1">
				<div class="divLeft1">What is your Gender?</div>
				<div class="divRitht">
					<form:select id="gender" path="gender" items="<%=ApplicationDict.getGender() %>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.gender}</div>
				<div id="cleanStyle"></div>
				<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">What is your date of birth?</div>
				<div class="divRitht">
					<form:input id="dob" class="mh_date" path="dateOfBirth"/>
				</div>
				<div class="divRitht1"><fmt:formatDate value="${caregiver.dateOfBirth}" pattern="dd/MM/yyyy"/></div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Place of birth:</div>
				<div class="divRitht">
					<form:input id="pob" path="countryOfBirth" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.countryOfBirth}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Nearest Airport:</div>
				<div class="divRitht">
					<form:input id="nearestAirport" path="nearestAirport" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.nearestAirport}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Nationality:</div>
				<div class="divRitht">
					<form:select path="nationality" items="<%=ApplicationDict.getNationality() %>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.nationality}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Current residential address:</div>
				<div class="divRitht">
					<form:input id="address" path="currentAddress" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.currentAddress}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Height(cm):</div>
				<div class="divRitht">
					<form:input id="height" path="height" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.height}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Weight(kg):</div>
				<div class="divRitht">
					<form:input id="weight" path="weight" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.weight}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Marital Status:</div>
				<div class="divRitht">
					<form:select path="maritalStatus" items="<%=ApplicationDict.getMaritalstatus() %>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.maritalStatus}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Number of children:</div>
				<div class="divRitht">
					<form:select path="hasChildren" items="<%=ApplicationDict.getHaschildren()%>"/>
				</div>
				<div class="divRitht1">${caregiver.hasChildren}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			<div class="questionnaire1">
				<div class="divLeft1">their names & ages:</div>
				<div class="divRitht">
					<form:textarea path="childrenNameAge"/>
				</div>
				<div class="divRitht1">${caregiver.childrenNameAge}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			<div class="questionnaire1">
				<div class="divLeft1">siblings(example: 1 brother and 2 sisters):</div>
				<div class="divRitht">
					<form:input path="siblings" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.siblings}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Languages Spoken:</div>
				<div class="divRitht">
					<form:checkboxes id="languageList" path="languageList" items="<%=ApplicationDict.getLanguages()%>"/>
				</div>
				<div class="divRitht1">${caregiver.languages}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Religion:</div>
				<div class="divRitht">
					<form:select path="religion" items="<%=ApplicationDict.getReligion()%>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.religion}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Food choice:</div>
				<div class="divRitht">
					<form:select path="foodChoice" items="<%=ApplicationDict.getFoodchoice()%>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.foodChoice}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Holding a passport:</div>
				<div class="divRitht">
					<form:select path="holdPassport" items="<%=ApplicationDict.getyN()%>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.holdPassport}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Nursing School:</div>
				<div class="divRitht">
					<form:textarea id="education" path="education"/>
				</div>
				<div class="divRitht1">${caregiver.education}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Year of Graduation:</div>
				<div class="divRitht">
					<form:input id="yearGraduation" path="yearGraduation" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.yearGraduation}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Worked as a Caregiver(give details,per exp should start with *):</div>
				<div class="divRitht">
					<form:textarea id="caregiverBeforeExp" path="caregiverBeforeExp"/>
				</div>
				<div class="divRitht1">${caregiver.caregiverBeforeExp}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Years of studies(Number):</div>
				<div class="divRitht">
					<form:input id="yearStudies" path="yearStudies" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.yearStudies}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			
			<div class="questionnaire1">
				<div class="divLeft1">Education level:</div>
				<div class="divRitht">
					<form:select path="educationalLevel" items="<%=ApplicationDict.getEducationallevel()%>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.educationalLevel}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Years of experience:</div>
				<div class="divRitht">
					<form:input id="exp" path="exp" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.exp}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Are you certified for CPR / First Aid?:</div>
				<div class="divRitht">
					<form:select path="certifiedCpr" items="<%=ApplicationDict.getyN()%>"></form:select>
				</div>
				<div class="divRitht1">${caregiver.certifiedCpr}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Worked in Singapore:</div>
				<div class="divRitht">
					<form:select path="workedInSG" items="<%=ApplicationDict.getyN()%>"/>
				</div>
				<div class="divRitht1">${caregiver.workedInSG}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			<div class="questionnaire1">
				<div class="divLeft1">Worked in Singapore(details,work permit or FIN number):</div>
				<div class="divRitht">
					<form:textarea path="sgFin"/>
				</div>
				<div class="divRitht1">${caregiver.sgFin}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Current employer / city:</div>
				<div class="divRitht">
					<form:input path="location" id="location" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.location}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Nursing experience:</div>
				<div class="divRitht">
					<form:checkboxes id="specialities" path="specialitiesList" items="<%=ApplicationDict.getSpecialities()%>"/>
				</div>
				<div class="divRitht1">${caregiver.specialities}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Current role / job:</div>
				<div class="divRitht">
					<form:textarea path="currentJob" id="currentJob"/>
				</div>
				<div class="divRitht1">${caregiver.currentJob}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			<div class="questionnaire1">
				<div class="divLeft1">Current role / job time:</div>
				<div class="divRitht">
					<form:input path="currentJobTime" id="currentJobTime" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.currentJobTime}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Motivation:</div>
				<div class="divRitht">
					<form:textarea path="motivation" id="motivation"/>
				</div>
				<div class="divRitht1">${caregiver.motivation}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Work as a Caregiver for how long in Singapore:</div>
				<div class="divRitht">
					<form:textarea path="timeOfSg" id="timeOfSg"/>
				</div>
				<div class="divRitht1">${caregiver.timeOfSg}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Availability:</div>
				<div class="divRitht">
					<form:select path="availability" items="<%=ApplicationDict.getAvailability()%>"/>
				</div>
				<div class="divRitht1">${caregiver.availability}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Allergies:</div>
				<div class="divRitht">
					<form:textarea path="allergies" id="allergies"/>
				</div>
				<div class="divRitht1">${caregiver.allergies}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Diagnosed Conditions:</div>
				<div class="divRitht">
					<form:checkboxes id="diagnosedList" path="diagnosedList" items="<%=ApplicationDict.getDiagnosedconditions()%>"/>
				</div>
				<div class="divRitht1">${caregiver.diagnosedConditions}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">About the treatment / medication :</div>
				<div class="divRitht">
					<form:textarea path="historyOfTreatment"/>
				</div>
				<div class="divRitht1">${caregiver.historyOfTreatment}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Hobbies:</div>
				<div class="divRitht">
					<form:input id="hobbies" path="hobbies" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.hobbies}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Have you obtained TESDA NCII Certification?</div>
				<div class="divRitht">
					<form:select path="tesdaNcii" items="<%=ApplicationDict.getyN()%>"/>
				</div>
				<div class="divRitht1">${caregiver.tesdaNcii}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Best Days / Times to schedule an interview:</div>
				<div class="divRitht">
					<form:input path="interviewTime" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.interviewTime}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
			
			<div class="questionnaire1">
				<div class="divLeft1">Referred by a friend? Please enter his / her name and email:</div>
				<div class="divRitht">
					<form:input path="referrer" class="i-c"/>
				</div>
				<div class="divRitht1">${caregiver.referrer}</div>
				<div id="cleanStyle"></div>
			<hr/>
			</div>
		</div>
		<sec:authorize access="!hasAnyRole('ROLE_PH_RECRUITING_PARTNER')">
			<div class="center" style="height:100px">
				<center>
					<button type="button" id="edit" class="btn btn-primary btn-lg" onclick="change();">Edit questionnaire</button>
					<button type="button" style="display: none;" class="btn btn-primary btn-lg cBtn" onclick="editQuestionnaire();">submit</button>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" style="display: none;" class="btn btn-primary btn-lg cBtn" onclick="closeEdit();">close</button>
				</center>
			</div>
		</sec:authorize>
	</form:form>
	</div>
</body>
</html>