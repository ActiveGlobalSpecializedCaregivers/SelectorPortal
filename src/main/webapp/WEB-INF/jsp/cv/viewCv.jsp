<%@ page import="com.cloudaxis.agsc.portal.helpers.ApplicationDict" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="site_url" value="${pageContext.request.contextPath}/" />

<jsp:include page="../header.jsp" />
<link href="${pageContext.request.contextPath}/resources/js/viewCv/css/main.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/plugins/jcrop/css/jquery.Jcrop.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/plugins/jcrop/css/jquery.Jcrop.min.css" rel="stylesheet" type="text/css" />

<script src="${pageContext.request.contextPath}/resources/plugins/jcrop/js/jquery.Jcrop.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/viewCv/js/script.js"></script>

<style type="text/css">
.ui-dialog-titlebar-close {
  visibility: hidden;
}
.button_bar {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    border-color: #aaa #fff #fff;
    border-image: none;
    border-right: 1px solid #fff;
    border-style: solid;
    border-width: 1px;
    clear: both;
    padding-top:10px;
}

#languages_edit span,#specialties_edit span{
	display: flex;
}
</style>

<div id="page-content">
	<div id='wrap'>
		<div id="page-heading">
			<ol class="breadcrumb">
				<li><a href="${pageContext.request.contextPath}/shortlist/getStatusAmount">Dashboard</a></li>
				<li class="active">Caregiver R&eacute;sum&eacute;</li>
			</ol>

			<h1>Caregiver R&eacute;sum&eacute;</h1>
		</div>
		<div class="container">

			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-midnightblue">
						<div class="panel-body">

							<div class="row">
								<form:form id="update_cv_frm" method="post" action="${pageContext.request.contextPath}/admin/ajax/update_cv" modelAttribute="caregiver" class="form-signin">
								<div class="col-md-6">
									<c:choose>
										<c:when test="${photoPath != '' && photoPath != null}">
											<img src="${photoPath}" alt="" class="pull-left" style="margin: 0 20px 20px 0;max-height: 250px;" width="250px;">
										</c:when>
										<c:otherwise>
											<img src="${pageContext.request.contextPath}/resources/img/no_image.png" alt="" class="pull-left" style="margin: 0 20px 20px 0" width="250px;">
										</c:otherwise>
									</c:choose>
									<form:input path="userId" name="userId" type="hidden"/>
									<form:input path="dateApplied" type="hidden"/>
									<input type="hidden" name="loginUserId" value="${user.userId}">
									<input type="hidden" name="loginUserName" value="${user.firstName} ${user.lastName}">
									<div class="table-responsive">
										<table class="table table-condensed">
											<h3>
												<strong>
													<span id="fullname_span">${caregiver.fullName}</span>
													<form:input path="fullName" name="full_name" id="fullname_edit" style="display:none;"/>
												</strong>
											</h3>										
											<tbody>
                                                <tr>
													<td>Status</td>
													<td>
														<span id="tag_span">
															<c:choose>
																<c:when test="${caregiver.tag == '0'}">Available</c:when>
																<c:when test="${caregiver.tag == '1'}">Tagged</c:when>
																<c:when test="${caregiver.tag == '2'}">Contracted</c:when>
																<c:when test="${caregiver.tag == '3'}">On hold</c:when>
															</c:choose>
														</span>
														<form:select name="tag" id="tag_edit" path="tag" style="display:none;">  
												            <form:options items="<%=ApplicationDict.getTag()%>"/>  
												        </form:select>
												        <input type="hidden" name="tag_flag" value="${caregiver.tag}">
													</td>
												</tr>
                                                <tr>
													<td>Tagged to</td>
													<td>
														<span id="tagged_to_span">${caregiver.taggedTo}</span>
														<form:input path="taggedTo" id="tagged_to_edit" style="display:none;"/>
														<input type="hidden" id="tagged_to" name="tagged_to" value="${caregiver.taggedTo}">
														<input type="hidden" name="tagId" value="${caregiver.tagId}">
													</td>
												</tr>
                                                <tr>
													<td>Contracted to</td>
													<td>
														<span id="contracted_to_span">${caregiver.contractedTo}</span>
														<form:input path="contractedTo" id="contracted_to_edit" style="display:none;"/>
														<input type="hidden" id="contracted_to" name="contracted_to" value="${caregiver.contractedTo}">
														<input type="hidden" name="numbersOfPlacement" value="${caregiver.numbersOfPlacement}"/>
													</td>
												</tr>			
												<tr>
													<td>Sex</td>
													<td>
														<span id="gender_span">${caregiver.gender}</span>
														<form:select path="gender" id="gender_edit" style="display:none;">
															<form:options items="<%=ApplicationDict.getGender()%>"/>
														</form:select>
													</td>
												</tr>												
                                                <tr>
													<td>Highest education</td>
													<td>
														<span id="level_span">${caregiver.educationalLevel}</span>
														<form:select path="educationalLevel" id="level_edit" style="display:none;">
															<form:options items="<%=ApplicationDict.getEducationallevel()%>"/>
														</form:select>														
													</td>
												</tr>
                                                <tr>
													<td>Years of experience</td>
													<td>
														<span id="exp_span">${caregiver.exp}</span>
														<form:input path="exp" name="exp" id="exp_edit" style="display:none;"/>
													</td>
												</tr>
                                                <tr>
													<td>Availability</td>
													<td>
														<span id="availability_span">${caregiver.availability}</span>
														<form:select path="availability" id="availability_edit" style="display:none;">
															<form:options items="<%=ApplicationDict.getAvailability()%>"/>
														</form:select>															
													</td>
												</tr>
                                                <tr>
													<td>Languages spoken</td>
													<td>
														<span id="languages_span">${caregiver.languages}</span>
														<div id="languages_edit" style="display:none;">
															<form:checkboxes id="DTE_Field_languages" path="languageList" items="<%=ApplicationDict.getLanguages()%>"/>
														</div>														
													</td>
												</tr>
												<sec:authorize access="hasAnyRole('ROLE_SALES_TW', 'ROLE_ADMIN', 'ROLE_SUB_ADMIN')">
													<tr>
														<td>Expected salary in TWD</td>
														<td>
															<span id="salary_twd_span">${caregiver.salaryTWD}</span>
															<form:input name="salary_twd" id="salary_twd_edit" path="salaryTWD" style="display:none;"/>
														</td>
													</tr>
												</sec:authorize>
												<sec:authorize access="hasAnyRole('ROLE_SALES_SG', 'ROLE_ADMIN', 'ROLE_SUB_ADMIN')">
													<tr>
														<td>Expected salary in SGD</td>
														<td>
															<span id="salary_sgd_span">${caregiver.salarySGD}</span>
															<form:input name="salary_sgd" id="salary_sgd_edit" path="salarySGD" style="display:none;"/>
														</td>
													</tr>
												</sec:authorize>
												<sec:authorize access="hasAnyRole('ROLE_SALES_HK', 'ROLE_ADMIN', 'ROLE_SUB_ADMIN')">
													<tr>
														<td>Expected salary in HKD</td>
														<td>
															<span id="salary_hkd_span">${caregiver.salaryHKD}</span>
															<form:input name="salary_hkd" id="salary_hkd_edit" path="salaryHKD" style="display:none;"/>
														</td>
													</tr>
												</sec:authorize>
                                                <tr>
													<td>Marital status</td>
													<td>
														<span id="marital_span">${caregiver.maritalStatus}</span>
														<form:select path="maritalStatus" name="marital_status" id="marital_edit" style="display:none;">
															<form:options items="<%=ApplicationDict.getMaritalstatus()%>"/>
														</form:select>														
													</td>
												</tr>
                                                <tr>
													<td>Age</td>
													<td>
														${caregiver.age}
													</td>
												</tr>
                                                <tr>
													<td>Date of birth</td>
													<td>
														<span id="dob_span"><fmt:formatDate value="${caregiver.dateOfBirth}" pattern="dd/MM/yyyy" /></span>
														<%-- <form:input path="dateOfBirth" name="dob" id="dob_edit" style="display:none;"/> --%>
														<input id="dob_edit" style="display: none;" name="dob" value="<fmt:formatDate value="${caregiver.dateOfBirth}" pattern="dd/MM/yyyy" />"/>
													</td>
												</tr>
                                                <tr>
													<td>Sibling Info</td>
													<td>
														<span id="siblings_span">${caregiver.siblings}</span>
														<form:input path="siblings" name="siblings" id="siblings_edit" style="display:none;"/>
													</td>
												</tr>												
                                                <tr>
													<td>Religion</td>
													<td>
														<span id="religion_span">${caregiver.religion}</span>
														<form:select path="religion" name="religion" id="religion_edit" style="display:none;">
															<form:options items="<%=ApplicationDict.getReligion()%>"/>
														</form:select>														
													</td>
												</tr>
                                                <tr>
													<td>Food choice</td>
													<td>
														<span id="food_span">${caregiver.foodChoice}</span>
														<form:input path="foodChoice" title="for example, No restriction, Vegeterian, Halal" name="food_choice" id="food_edit" style="display:none;"/>
													</td>
												</tr>
												<tr>
													<td>Nationality</td>
													<td>
														<span id="nationality_span">${caregiver.nationality}</span>
														<form:select path="nationality" name="nationality" id="nationality_edit" style="display:none;">
															<form:options items="<%=ApplicationDict.getNationality()%>"/>
														</form:select>															
													</td>
												</tr>
												<tr>
													<td>Place of birth</td>
													<td>
														<span id="cob_span">${caregiver.countryOfBirth}</span>
														<form:input path="countryOfBirth" name="country_of_birth" id="cob_edit" style="display:none;"/>
													</td>
												</tr>
												<tr>
													<td>Has children?</td>
													<td>
														<span id="children_span">${caregiver.hasChildren}</span>
														<form:select path="hasChildren" name="has_children" id="children_edit" style="display:none;">
															<form:options items="<%=ApplicationDict.getHaschildren()%>"/>
														</form:select>														
													</td>
												</tr>
												<tr>
													<td>Height (cm)</td>
													<td>
														<span id="height_span">${caregiver.height}</span>
														<form:input path="height" name="height" id="height_edit" style="display:none;"/>
													</td>
												</tr>
												<tr>
													<td>Weight (kg)</td>
													<td>
														<span id="weight_span">${caregiver.weight}</span>
														<form:input path="weight" name="weight" id="weight_edit" style="display:none;"/>
													</td>
												</tr>
												<tr>
													<td>Video URL</td>
													<td>
														<span id="video_span">${caregiver.videoURL}</span>
														<form:input path="videoURL" name="video" id="video_edit" style="display:none;"/>
													</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<div class="col-md-6">
                                	<p style="text-align:right;">
										<a href="/dashboard/getCandidate?active=6&userId=${caregiver.userId}" class='btn btn-primary btn-s' id="view_files">View Files</a>
                                    	<a href='#' onclick="printCV()" class='btn btn-primary btn-s' id="print_cv">Print CV</a>
                                    	<a href='#' onclick="showDialog('${caregiver.userId}')" class='btn btn-primary btn-s' id="email_cv">Email CV</a>
                                    	<sec:authorize access="!hasAnyRole('ROLE_PH_RECRUITING_PARTNER','ROLE_HOSPITAL')">
	                                    	<a href='#' onclick="editCV()" class='btn btn-primary btn-s' id="update_cv">Update CV</a>
	                                    	<a href='#' onclick="saveCV()" class='btn btn-primary btn-s' id="save_cv" style="display:none;">Save Changes</a>
	                                    	<a href='#' onclick="cancelCV()" class='btn btn-primary btn-s' id="cancel_cv" style="display:none;">Cancel</a>                                    	
						                    <span class="btn btn-default btn-file" id="upload_photo" data-toggle="modal" data-target="#pictureModal">
						                        <span class="fileinput-new">Upload Photo</span>
						                        <!-- <input type="file" name="userphoto" id="userphoto" size="20"> -->
						                    </span>
						                    <a href="${pageContext.request.contextPath}/admin/download/pdf?userId=${caregiver.userId}" class='btn btn-primary btn-s' id="print_cv">MOM biodata</a>
						                    
						                    <span class="fileinput-filename"></span>
						                    <div style="margin-left:50px; display:none" id="userphoto_container">Uploading...</div>
					                    </sec:authorize>
                                    </p>
                                	<blockquote>
                                		<p><span id="motivation_span">${caregiver.motivation}</span></p>
                                    </blockquote>
                            		<form:textarea path="motivation" rows="4" cols="80" name="motivation" id="motivation_edit" style="display:none;"/>
									<h3>About</h3>
									<p>
										<span id="about_span">${caregiver.bio.candidateBasicInformation}</span>
									</p>
									<form:textarea path="bio.candidateBasicInformation" rows="4" cols="80" name="about" id="about_edit" style="display:none;"/>
                                    <br />
                                    <h3>Education & Experience</h3>
									<p>
										<span id="education_span">${caregiver.bio.educationAndExperience}</span>
									</p>
									<form:textarea path="bio.educationAndExperience" rows="4" cols="80" name="education" id="education_edit" style="display:none;"/>
                                    <br />
                                    <h3>Nursing experience</h3>
									<p>
										<span id="specialties_span">${caregiver.bio.nursingExperience}</span>
									</p>
									<div id="specialties_edit" style="display:none;">
										<form:checkboxes id="DTE_Field_specialties_0" path="bio.experienceList" items="<%=ApplicationDict.getSpecialities()%>"/>
									</div>									
                                    <h3>Trained to CPR/First Aid?</h3>
									<p>
										<span id="cpr_span">${caregiver.bio.trainedToCprOrFA}</span>
									</p>
									<form:select path="bio.trainedToCprOrFA" name="certified_cpr" id="cpr_edit" style="display:none;">
										<form:options items="<%=ApplicationDict.getyN()%>"/>
									</form:select>																		
                                    <br />
                                    <h3>Hobbies</h3>
									<p>
										<span id="hobbies_span">${caregiver.bio.hobby}</span>
									</p>
									<form:textarea path="bio.hobby" rows="4" cols="80" name="hobbies" id="hobbies_edit" style="display:none;"/>
									<sec:authorize access="!hasAnyRole('ROLE_HOSPITAL')">
	                                    <h3>Contact Information</h3>
										<p>
											Mobile:&nbsp;&nbsp;<a href="#"><span id="mobile_span">${caregiver.mobile}</span></a>
											<form:input path="mobile" name="mobile" id="mobile_edit" style="display:none;"/>
											<br><br>
											Skype:&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"><span id="skype_span">${caregiver.skype}</span></a>
											<form:input path="skype" name="skype" id="skype_edit" style="display:none;"/>
											<br><br>
											Email:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"><span id="email_span">${caregiver.email}</span></a>
											<form:input path="email" name="email" id="email_edit" style="display:none"/>
											<span id="checkEmail" style="display:none"></span>
										</p>
									</sec:authorize>
	                                <h3>Remarks</h3>
									<p>
										<span id="remarks_span">
										<c:choose>
											<c:when test="${caregiver.remarks != null}">${caregiver.remarks}</c:when>
											<c:otherwise>No remarks yet, click update cv to add remarks</c:otherwise>
										</c:choose>
										</span>
									</p>
									<form:textarea path="remarks" rows="4" cols="80" name="remarks" id="remarks_edit" style="display:none;"/>
								</div>								
								
								</form:form>
							</div>
							
							<hr>
							<p style="text-align:left; font: italic bold 14px/30px Arial, Helvetica, sans-serif;;">
								Disclaimer: This biodata has been prepared by our team, to the best of our knowledge, based on the information provided by the candidate. While some information was checked during our interviewing process, we are unable to verify 100% of the information provided by the candidate.

								Please make sure to double-confirm the things that are most important to you during the interview with the candidate. Please also take note that many caregivers do not eat pork or beef, despite declaring 'no food restrictions' in our questionnaire, so please check during your interview with them if this is important to you.
							</p>
							<p style="text-align:right;">  
								Employee Registration Number: ${user.registrationNumber}
							</p>														
							<p style="text-align:right;">  
								Employment Agency Licence Number: 13C6324
							</p>							
						</div>
					</div>
				</div>
			</div>
			
	<!-- Picture (Modal) begin-->
			<div class="modal fade" id="pictureModal" tabindex="-1" role="dialog" aria-hidden="true">
				<div class="modal-dialog" style="width:1000px">
					<div class="modal-content" style="width:1000px">
						<div class="modal-body">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
								&times;
							</button>
					        <div class="demo" style=" margin-top:60px;">
					            <div class="bheader"><h2>--File Upload--</h2></div>
					            <div class="bbody">
					                <!-- upload form -->
					                <form id="upload_form" enctype="multipart/form-data" method="post" 
					                action="${pageContext.request.contextPath}/admin/users/photoCrop?userId=${caregiver.userId}">
			
					                    <!-- hidden crop params -->
					                    <input type="hidden" id="x1" name="x1" />
					                    <input type="hidden" id="y1" name="y1" />
					                    <input type="hidden" id="x2" name="x2" />
					                    <input type="hidden" id="y2" name="y2" />
					
					                    <div id="file-clear"><input type="file" name="image_file" id="image_file" onChange="fileSelectHandler()" /></div>
					
					                    <div class="error"></div>
					
					                    <div class="step2">
											<div id="newImg">
						                        <img id="preview" />
											</div>
					                        <div class="info">
					                            <label>Size</label> <input class="info-input" type="text" id="filesize" name="filesize" />
					                            <label>Type</label> <input class="info-input" type="text" id="filetype" name="filetype" />
					                            <label>Image size</label> <input class="info-input" type="text" id="filedim" name="filedim" />
					                            <label>Width</label> <input class="info-input" type="text" id="w" name="w" />
					                            <label>Heigth</label> <input class="info-input" type="text" id="h" name="h" />
					                        </div>
					
					                        <input class="btn btn-primary" type="submit" onclick="return checkForm();" value="Upload">
					                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					                    </div>
					                </form>
					            </div>
					        </div>
							<div style="text-align:center;clear:both"><br>
							
							</div>
						</div>
					</div><!-- /.modal-content -->
				</div><!-- /.modal -->
			</div>
			                           <!-- Picture (Modal) end-->


		</div> <!-- container -->
	</div> <!--wrap -->
</div> <!-- page-content -->
<div id='dialog-modal' style="display:none;" title="Send CV">
    <div class="block">
        <div class="section" id="dialog_section">
            <h1 style="letter-spacing: -1px !important; font-family: sans-serif; font-size: 15px; margin-bottom: 15px;"> Enter Message </h1>
            <div class="dashed_line"></div>
            <textarea name="email_msg" class="form-control" id="email_msg" cols="5" row="3"></textarea>
        </div>		
        <div class="section" id="dialog_section">
            <h1 style="letter-spacing: -1px !important; font-family: sans-serif; font-size: 15px; margin-bottom: 15px;"> Enter Client E-mail </h1>
            <div class="dashed_line"></div>
            <p>Please enter client email address to send their CV. </p>
            <input id="client_email" type="text" class="form-control">
        </div>
        <div class="button_bar clearfix">
            <button class="btn btn-primary btn-xs" id="continue_send">
                <span>Continue</span>
            </button>
            <button  class="btn btn-primary btn-xs" id="cancel_send" onclick="closeDialog();">
                <span>Cancel</span>
            </button>
        </div>
    </div>
</div>
<jsp:include page="../footer.jsp" />
<a href='#' class="overlay"></a>
<script type="text/javascript">
$(document).ready(function() {
	$(function () {
	    $('#userphoto').fileupload({
	        url: '${site_url}admin/users/upload_img',
	        formData: {userId:"${caregiver.userId}"},
	        dataType: 'json',
	        autoUpload: true,
			fileInput : $('#userphoto'),
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 102400, // 5 MB        
	        done: function (e, data) {
					//alert(data.result.msg);
					$(location).attr('href',"${site_url}"+'admin/users/view_cv/?userId='+"${caregiver.userId}");
				},
			add: function (e, data) {
				data.submit();
	       		$(".loading").show();
	            $("#userphoto_container").show();
	            
	        }
		});
	});

	$('#pictureModal').on('hide.bs.modal', function () {
		$('.info #w').val('');
		$('.info #h').val('');
		
		$(".error").hide();
		$(".step2").hide();
		
		$("#image_file").val("");
	});
	
	var chk_tag = ${caregiver.tag} ;

	if(chk_tag == 0 || chk_tag == 3){
		$("#tagged_to_edit").attr("disabled", "disabled"); 
		$("#contracted_to_edit").attr("disabled", "disabled");
	}else if(chk_tag == 1){
		$("#contracted_to_edit").attr("disabled", "disabled");
	}else if(chk_tag == 2){
		$("#tagged_to_edit").attr("disabled", "disabled"); 
	}
	$( "#tag_edit" ).change(function() {
		var tag = $('select[name="tag"]').val();
		var tagVal = $("#tagged_to_edit").val();
		var contractVal = $("#contracted_to_edit").val();
		
		if(tag == 0){			//Available
			$("#tagged_to_edit").val("");
			$("#contracted_to_edit").val("");
		
			$("#tagged_to_edit").attr("disabled", "disabled"); 
			$("#contracted_to_edit").attr("disabled", "disabled"); 
		}else if(tag == 1){		//tagged
			$("#contracted_to_edit").val("");
			$("#tagged_to_edit").removeAttr("disabled"); 
			$("#contracted_to_edit").attr("disabled", "disabled"); 
		}else if(tag == 2){		//contracted
			$("#tagged_to_edit").val("");
			$("#tagged_to_edit").attr("disabled", "disabled"); 
			$("#contracted_to_edit").removeAttr("disabled");
		}else if(tag == 3){		//on hold
			$("#tagged_to_edit").val("");
			$("#contracted_to_edit").val("");
		
			$("#tagged_to_edit").attr("disabled", "disabled"); 
			$("#contracted_to_edit").attr("disabled", "disabled"); 			
		}
	});
	
	//Identify duplicate candidates(by email)
	$("#email_edit").blur(function (){
		var email = $("#email_edit").val();
		var userEmail = $("#email_span").html();
		if(email != userEmail){
			var Regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			if(!Regex.test(email)){
				$("#checkEmail").html("Enter valid email address; eg:agsc@outlook.com");
				$("#checkEmail").css("color","#f00");
			}else{
				$.get("${pageContext.request.contextPath}/dashboard/checkExistCandidate", { email:email,userEmail:userEmail } ,function(data){
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

$(function() {
	$( "#dob_edit" ).datepicker({
        changeYear: true,
        // changeMonth: true,
        yearRange: '1950:+0',
        buttonImage: 'calendar_1.png',
        dateFormat: 'dd/mm/yy'
	});
});

function printCV(){
	window.open("${site_url}"+'admin/users/view_cv?tab=print&userId='+"${caregiver.userId}",'_blank','width=770, height=750')
}

function downloadBiodata(){
	$(location).attr('href',"${site_url}"+'admin/download/pdf?userId='+"${caregiver.userId}");
}

function editCV(){
	
		//spans
		$('#fullname_span').hide();
		$('#gender_span').hide();
		$('#education_span').hide();
		$('#exp_span').hide();
		$('#availability_span').hide();
		$('#languages_span').hide();
		<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUB_ADMIN')">
		$('#salary_hkd_span').hide();
		$('#salary_sgd_span').hide();
		$('#salary_twd_span').hide();
		</sec:authorize>
		$('#marital_span').hide();
		$('#dob_span').hide();
		$('#siblings_span').hide();
		$('#religion_span').hide();
		$('#food_span').hide();
		$('#nationality_span').hide();
		$('#cob_span').hide();
		$('#children_span').hide();
		$('#height_span').hide();
		$('#weight_span').hide();
		$('#video_span').hide();
		$('#motivation_span').hide();
		$('#level_span').hide();
		$('#about_span').hide();
		$('#specialties_span').hide();
		$('#cpr_span').hide();
		$('#hobbies_span').hide();
		$('#mobile_span').hide();
		$('#skype_span').hide();
		$('#email_span').hide();
		$('#tag_span').hide();
		$('#tagged_to_span').hide();
		$('#contracted_to_span').hide();
		$('#remarks_span').hide();

		//hide buttons
		$('#view_files').hide();
		$('#print_cv').hide();
		$('#email_cv').hide();
		$('#upload_photo').hide();
		$('#update_cv').hide();
		$('#back_to').hide();

		//show input text
		$('#fullname_edit').show();
		$('#gender_edit').show();
		$('#education_edit').show();
		$('#exp_edit').show();
		$('#availability_edit').show();
		$('#languages_edit').show();
		<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUB_ADMIN')">
		$('#salary_hkd_edit').show();
		$('#salary_sgd_edit').show();
		$('#salary_twd_edit').show();
		</sec:authorize>
		$('#marital_edit').show();
		$('#dob_edit').show();
		$('#siblings_edit').show();
		$('#religion_edit').show();
		$('#food_edit').show();
		$('#nationality_edit').show();
		$('#cob_edit').show();
		$('#children_edit').show();
		$('#height_edit').show();
		$('#weight_edit').show();
		$('#video_edit').show();
		$('#motivation_edit').show();
		$('#level_edit').show();
		$('#about_edit').show();
		$('#specialties_edit').show();
		$('#cpr_edit').show();
		$('#hobbies_edit').show();
		$('#mobile_edit').show();
		$('#skype_edit').show();
		$('#email_edit').show();
		$("#checkEmail").show();
		$('#tag_edit').show();
		$('#tagged_to_edit').show();
		$('#contracted_to_edit').show();
		$('#remarks_edit').show();

		//show buttons
		$('#save_cv').show();
		$('#cancel_cv').show();
	// }
}

function cancelCV(){

		$('#fullname_span').show();
		$('#gender_span').show();
		$('#education_span').show();
		$('#exp_span').show();
		$('#availability_span').show();
		$('#languages_span').show();
		$('#salary_hkd_span').show();
		$('#salary_sgd_span').show();
		$('#salary_twd_span').show();
		$('#marital_span').show();
		$('#dob_span').show();
		$('#siblings_span').show();
		$('#religion_span').show();
		$('#food_span').show();
		$('#nationality_span').show();
		$('#cob_span').show();
		$('#children_span').show();
		$('#height_span').show();
		$('#weight_span').show();
		$('#video_span').show();
		$('#motivation_span').show();
		$('#level_span').show();
		$('#about_span').show();
		$('#specialties_span').show();
		$('#cpr_span').show();
		$('#hobbies_span').show();
		$('#mobile_span').show();
		$('#skype_span').show();
		$('#email_span').show();
		$('#tag_span').show();
		$('#tagged_to_span').show();
		$('#contracted_to_span').show();
		$('#remarks_span').show();

		//hide buttons
		$('#view_files').show();
		$('#print_cv').show();
		$('#email_cv').show();
		$('#upload_photo').show();
		$('#update_cv').show();
		$('#back_to').show();

		//show input text
		$('#fullname_edit').hide();
		$('#gender_edit').hide();
		$('#education_edit').hide();
		$('#exp_edit').hide();
		$('#availability_edit').hide();
		$('#languages_edit').hide();
		$('#salary_hkd_edit').hide();
		$('#salary_sgd_edit').hide();
		$('#salary_twd_edit').hide();
		$('#marital_edit').hide();
		$('#dob_edit').hide();
		$('#siblings_edit').hide();
		$('#religion_edit').hide();
		$('#food_edit').hide();
		$('#nationality_edit').hide();
		$('#cob_edit').hide();
		$('#children_edit').hide();
		$('#height_edit').hide();
		$('#weight_edit').hide();
		$('#video_edit').hide();
		$('#motivation_edit').hide();
		$('#level_edit').hide();
		$('#about_edit').hide();
		$('#specialties_edit').hide();
		$('#cpr_edit').hide();
		$('#hobbies_edit').hide();
		$('#mobile_edit').hide();
		$('#skype_edit').hide();
		$('#email_edit').hide();
		$("#checkEmail").hide();
		$('#tag_edit').hide();
		$('#tagged_to_edit').hide();
		$('#contracted_to_edit').hide();
		$('#remarks_edit').hide();

		//show buttons
		$('#save_cv').hide();
		$('#cancel_cv').hide();
	// }
}

function saveCV(){
	var checkEmail = $("#checkEmail").html();
	var tag = $('select[name="tag"]').val();
	var tagVal = $("#tagged_to_edit").val();
	var contractVal = $("#contracted_to_edit").val();
	if(checkEmail == 'The mailbox can be used.' || checkEmail == '' || checkEmail ==null){
		if(tag == '1' && tagVal == ''){
			alert("Please enter Tagged to someone!");
		}else if(tag == '2' && contractVal == ''){
			alert("Please enter Contracted to someone!");
		}else{
			$("#update_cv_frm").submit();
			$(".loading").show();
		}
	}else{
		alert("Please enter your correct mailbox!");
	}
}

function closeFancybox()
{	
	$.fancybox.close();
	$(location).attr('href',"${site_url}"+'admin/users/view_cv?userId='+"${caregiver.userId}");
}

function showCrop()
{
	var rand = Math.floor(Math.random()*11);
	// console.log(rand);
	$(".overlay").fancybox({
		href: "${site_url}"+'admin/ajax/crop/?'+rand+'&user_id='+"${caregiver.userId}",
		type: 'iframe',
		autoSize : false,
		width:1024,
		height:700,
		padding : 5,
		closeBtn :false,
		helpers : { 
		  overlay : {closeClick: false}
		},
		afterClose : function(){
			setTimeout(function() {
      			location.reload();
			}, 2000);
		}
		}).trigger('click');    
		
}

function showDialog(user_id)
{
    $("#dialog-modal").dialog(
    {
        width: 500,
        height: 300,
        modal: true,
    });
     
    
    $("#continue_send").attr("onclick", "sendCV('"+user_id+"')");
}

function closeDialog(){
    $("#dialog-modal").dialog( "close" );
    return false;
}

function sendCV(user_id){

    var client_email = $('#client_email').val();
	var email_msg = $('#email_msg').val();

    var email = "[A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}";
    var re = new RegExp('^'+email+'(;\\n*'+email+')*;?$');
    if ( client_email === '' ) {
        alert('Email must be given');
        return false;
    }else{
    	flag = re.test(client_email);
		if(flag){
	    	$(".loading").show();
	        $.post("${pageContext.request.contextPath}/admin/ajax/send_client_cv", {"sData":user_id, "client_email":client_email, "email_msg":email_msg,"userId":"${user.userId}"}, function(data) {                                
	            if(data.status=="success")
	            {
	                //alert(data.msg);
	                location.reload();
	            }else{
	            	$(".loading").hide();
	            	alert("Server error!");
	            } 
	        },'json').error(function(xhr,errorText,errorType){$(".loading").hide(); alert("Server error!Please resend!");});
		}else{
			alert("Email format is incorrect!");
		}

    }
}
</script>
	
