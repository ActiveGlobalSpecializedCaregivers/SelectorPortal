<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../header.jsp" />
	
        <div id="page-content" style="overflow:auto;max-height: 850px; width: 95%">
		    <div id="wrap">
		       	<c:if test="${tag == 'search'}">
		    		<div id="messages_wrapper" style="line-height: 2; height: 35px;">
						<div class="dv_message msg_caution">
							<div class="container">
								<p style="font-size: medium; font-weight: 400; ">
									Your search returned ${fn:length(list)} candidate(s). 
									<c:choose>
										<c:when test="${searchStatus == 'old' }">
											<a class="bold" href="#" id="link_delete_search">Delete search</a>
											<input type="hidden" id="search_id" value="${searchHistory.id}">
										</c:when>
										<c:otherwise>
											<a class="bold" href="#" id="link_save_search">Save search</a> 
										</c:otherwise>
									</c:choose>
									| <a class="bold" href="${pageContext.request.contextPath}/candidates">Cancel search</a> 
									<a class="btn_collapse" href="#"><i class="fa fa-times"></i></a>
								</p>
							</div>
						</div>
					</div>
		    	</c:if>
		    	
		    	<!-- Save Search -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog" 
				   aria-labelledby="myModalLabel" aria-hidden="true">
				   <div class="modal-dialog">
				      <div class="modal-content">
				         <div class="modal-header" style="background-color: #414b55;">
				            <button type="button" class="close" data-dismiss="modal" style="color: #fff;" 
				               aria-hidden="true">�
				            </button>
				            <h4 class="modal-title" id="myModalLabel" style="color: #fff;" >
				               Save this Search
				            </h4>
				         </div>
				         <div class="modal-body">
							 <p>You can save complex search queries to avoid reconstructing them later. 
								 Please note you are saving the search <strong style="color: black;">query</strong>, not the search <strong style="color: black;">results</strong>.</p>
							 <p style="color: black;font-weight: bold;">Name this search</p>
							 <input id="searchTitle" class="form-control" type="text" style="width:100%;"/>
							 <br/>
							 <input id="share_status" type="checkbox"/>&nbsp;<label>Allow other AGSC users to use this search</label>
						 </div>
				         <div class="modal-footer" style="text-align: left;">
				              <button type="button" class="btn btn-primary" onclick="savingSearch('${tag}', '${searchList}');">
				               SAVE SEARCH
				            </button>
				            <button type="button" class="btn btn-default" 
				               data-dismiss="modal">
				               CANCEL
				            </button>
				         </div>
				      </div><!-- /.modal-content -->
				   </div><!-- /.modal-dialog -->
				</div><!-- /.modal -->
		    
	        	<form id="form_search" method="get">
			        <div id="page-heading">
						<div id="f_show" >
				            <h1 style="width:60%;">Applicants</h1>
				            <button style="margin-top: 25px;" type="button" class="btn btn-default" aria-label="Left Align"
				            	onclick="openSearch();">
							  <span class="fa fa-search" aria-hidden="true"></span>
							</button>
							<sec:authorize access="!hasAnyRole('ROLE_RECRUITER', 'ROLE_PH_RECRUITING_PARTNER', 'ROLE_SALES', 'ROLE_SALES_SG', 'ROLE_SALES_HK', 'ROLE_SALES_TW')">
								<button class="btn btn-danger" style="margin-top: 25px;" onclick="deleteCandidates()">DELETE</button>
							</sec:authorize>
						</div>
						
						<div id="p_show" style="display: none;margin: 10px;">
							<div style="float: left;"> 
								<h1>Search Applicants</h1>
								<button id="ps_show" type="button" class="btn btn-link" onclick="a_search();">Advanced Search...</button>
								<button id="t_show" style="display: none;" type="button" class="btn btn-link" onclick="t_search();">Basic Search...</button>
							</div>
							<!-- dropdown the saved search -->
							<div class="btn-group search-drop">
								<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									<c:choose>
										<c:when test="${searchHistory.name != null}">${searchHistory.name}</c:when>
										<c:otherwise>Run a saved search... </c:otherwise>
									</c:choose>
									<span class="caret"></span>
							  	</button>
							 	<ul class="dropdown-menu">
							 		<c:forEach items="${shlist}" var="searchHistory" >
							 			<c:choose>
							 				<c:when test="${searchHistory.shareStatus eq 'NO'}">
							 					<c:if test="${user.userId eq searchHistory.userId }">
													<li><a href="${pageContext.request.contextPath}/candidates?tag=search&searchStatus=old&id=${searchHistory.id}">${searchHistory.name}</a></li>
												</c:if>
							 				</c:when>
							 				<c:otherwise>
							 					<li><a href="${pageContext.request.contextPath}/candidates?tag=search&searchStatus=old&id=${searchHistory.id}">${searchHistory.name}</a></li>
							 				</c:otherwise>
							 			</c:choose>
							 		</c:forEach>
								</ul>
							</div>
							<br/>
							
							<div class="row" style="padding: 10px; margin-left:1px">
								<div id="p_searchParam"></div>
								<div id="q_searchParam"></div>
							</div>

							<!-- profile filter -->
							<div style="padding: 20px; margin-top: -30px">
								<div class="row">
									<div class="col-md-2"><h4>Profile Filters</h4></div>
									<div class="col-md-10">
										<button id="search_c" type="button" class="btn btn-success">SEARCH CANDIDATES</button> &nbsp; &nbsp;
										<button class="btn btn-default" type="button" onclick="closeSearch();">CLOSE</button>
									</div>
								</div>
								<div class="row">
									<!--Begin add the search -->
									<div id="p_father" style="margin-top: 0px; !important">
										<div id="p_search" class="p_filter">
											<div class="form-group col-md-1 c_field" style="width: 20%">
												<select class="form-control" name="p_field" onchange="p_fieldSelect(this);">
												  <option value="0">(Select column)</option>
												  <option value="p_full_name">Name</option>
												  <option value="p_mobile">Phone Number</option>
												  <option value="p_age">Age</option>
												  <option value="p_email">Email</option>
												  <option value="p_date_applied">Date Applied</option>
												  <option value="p_status">Status</option>
												  <option value="p_registered_concorde">Registered with concorde</option>
												  <option value="p_pre_deployment">Advance Placement Scheme</option>
												  <option value="p_medical_cert_verified">Medical Cert Verified</option>
												  <option value="p_date_of_placement">Date Of Placement</option>
												  <option value="p_date_of_change">Date Of Change</option>
												  <option value="p_numbers_of_placement">Numbers Of Placements</option>
												  <option value="p_work_in_sg">Work in SG</option>
												  <option value="p_work_in_hk">Work in HK</option>
												  <option value="p_work_in_tw">Work in TW</option>
												</select>
											</div>
											<div class="form-group col-md-2 div_match">
												<select class="form-control" name="p_match">
												  <option value="exactly">is exactly</option>
												  <option value="greater">is greater than</option>
												  <option value="greaterequal">is greater than or equal to</option>
												  <option value="less">is less than</option>
												  <option value="lessequal">is less than or equal to</option>
												  <option value="contain">contains</option>
												  <option value="notcontain">does not contain</option>
												  <option value="empty">is empty</option>
												  <option value="notempty">is not empty</option>
												</select>
											</div>
											<div class="form-group col-md-2 profile_text">
												<input name="p_value" type="text" class="form-control"/>
											</div>
											<button type="button" id="p_remove" class="btn btn-default" aria-label="Left Align"
								            	onclick="DeleteDiv(this);">
											  <span class="fa fa-trash-o fa-lg" aria-hidden="true"></span>
											</button>
										</div>
										<div id="p_add_searchParam"></div>
									</div>
									<div style="margin-left: 10px;margin-top: 20px;">
										<button type="button" class="btn btn-default" aria-label="Left Align"
							            	onclick="AddDiv('p_search','p_father');">
										  <span class="fa fa-plus" aria-hidden="true">Add Profile Filter</span>
										</button>
									</div>
									<!--End the add -->
								</div>
							</div>
							<!-- end profile filter  -->
							
							<!-- custom question filter -->
							<div id="q_filters" style="display: none; padding: 20px;">
								<h4>Custom Question Filters</h4>
								<div class="row">
									<!--Begin add the search -->
									<div id="q_father">
										<div id="q_search" class="p_filter">
											<div class="form-group col-md-1 custom_question" style="width: 20%">
												<select class="form-control question_select" name="p_field" onchange="selectChange(this);">
												  <option value="0">Select Question</option>
												  <option value="q_gender">What is your Gender?</option>
												  <option value="q_dob">what is your date of birth?</option>
												  <option value="q_nationality">What is your country of citizenship?</option>
												  <option value="q_height">What is your height in centimeters?</option>
												  <option value="q_weight">What is your weight in kg?</option>
												  <option value="q_marital_status"> What is your marital status?</option>
												  <option value="q_has_children">Do you have children and how many?</option>
												  <option value="q_languages">Which languages do you speak?</option>
												  <option value="q_religion">What is your religion?</option>
												  <option value="q_food_choice">Do you follow a specific diet?</option>
												  <option value="q_hold_passport">Are you currently holding a passport?</option>
												  <option value="q_educational_level">What Nursing degree / Diploma did you obtain?</option>
												  <option value="q_exp">How many years of experience do you have as a nurse?</option>
												  <option value="q_specialties">Please let us know if you have experience in the following areas</option>
												  <option value="q_allergies">If you have any allergies, please state which one(s)</option>
												  <option value="q_diagnosed_conditions">Do you have or were you ever diagnosed with any of the following conditions?</option>
												  <option value="q_specialties">Do you have Nursing Experience?</option>
												  <option value="q_workingplace">We are currently offering Live-In Caregiver opportunities both in HONG KONG and SINGAPORE. You CAN apply to BOTH locations to maximize your chances of getting a placement quickly. Please tick ALL locations that interest you</option>
												  <option value="q_certified_cpr">Have you obtained TESDA NCII certification?</option>
												</select>
											</div>
											<div class="form-group col-md-2 question_expression">
												<select class="form-control" name="p_match">
												  <option value="exactly">is exactly</option>
												  <option value="greater">is greater than</option>
												  <option value="greaterequal">is greater than or equal to</option>
												  <option value="less">is less than</option>
												  <option value="lessequal">is less than or equal to</option>
												  <option value="contain">contains</option>
												  <option value="notcontain">does not contain</option>
												  <option value="empty">is empty</option>
												  <option value="notempty">is not empty</option>
												</select>
											</div>
											<div class="form-group col-md-2 question_text" >
												<input name="p_value" type="text" class="form-control"/>
											</div>
											<button type="button" id="q_remove" class="btn btn-default" aria-label="Left Align"
								            	onclick="DeleteDiv(this);">
											  <span class="fa fa-trash-o fa-lg" aria-hidden="true"></span>
											</button>
										</div>
										<div id="q_add_searchParam"></div>
									</div>
									<div style="margin-left: 10px;margin-top: 20px;">
										<button type="button" class="btn btn-default" aria-label="Left Align"
							            	onclick="AddDiv('q_search','q_father');">
										  <span class="fa fa-plus" aria-hidden="true">Add Question Filter</span>
										</button>
									</div>
									<!--End the add -->
								</div>
							</div>
							<!-- end custom question filter -->
						</div>
			        </div>
					<div id="searchCandidate" class="col-md-8" style="margin-left: 20px;display: none;margin-top: -20px; margin-bottom: 10px">
						<sec:authorize access="!hasAnyRole('ROLE_RECRUITER', 'ROLE_PH_RECRUITING_PARTNER', 'ROLE_SALES', 'ROLE_SALES_SG', 'ROLE_SALES_HK', 'ROLE_SALES_TW')">
							<button class="btn btn-danger" style="float:right;margin-right: 20px;" onclick="deleteCandidates()">DELETE</button>
						</sec:authorize>
						<button class="btn btn-success" style="float:right;margin-right: 20px;" type="button" id="exprot_results">EXPORT RESULTS</button>
					</div>
				</form>
				<form id="export_form" action="" method="post" style="display:none"></form>
		        <div class="container" style="padding-left: 30px;">
		            <div class="row">
		            	<div class="col-md-8">
		            		<table class="table table-hover" id="caregiverList">
		            			<thead>
		            				<tr style="background:grey">
		            					<sec:authorize access="hasRole('ROLE_ADMIN')">
		            						<th style="color:white"><input id="selectAllIds" type="checkbox" onclick="selectAll()"/></th>
		            					</sec:authorize>
		            					<th style="color:white">APPLICANT</th>
		            					<th style="color:white">STATUS</th>
		            					<th style="color:white">RECEIVED</th>
		            					<th style="color:white">LAST UPDATED</th>
		            				</tr>
		            			</thead>
		            			<tbody>
		            				<c:forEach items="${list}" var="caregiver">
		            					<tr>
		            						<c:if test="${user.authorities == '[ROLE_ADMIN]'}">	
		            							<td><input type="checkbox" name="candidateId" value="${caregiver.userId}" /></td>
		            						</c:if>
		            						<td><a href="${pageContext.request.contextPath}/dashboard/getCandidate?userId=${caregiver.userId}">${caregiver.fullName}</a></td>
		            						<td>
			            						<c:forEach items="${selectStatusList}" var="status">
			            							<c:if test="${status.id eq caregiver.status}">
			            								${status.name}
			            							</c:if>
			            						</c:forEach>
		            						</td>
		            						<td><fmt:formatDate value="${caregiver.dateApplied}" type="both" pattern="dd/MM/yyyy"/></td>
		            						<td><fmt:formatDate value="${caregiver.lastModified}" type="both" pattern="dd/MM/yyyy"/></td>
		            					</tr>
		            				</c:forEach>
		            			</tbody>
							</table>
		            	</div>
		            </div>
				</div> <!-- container -->
				
		    </div> <!--wrap -->
		</div> <!-- page-content -->

	    <footer role="contentinfo">
	        <div class="clearfix">
	            <ul class="list-unstyled list-inline pull-left">
	                <li>Active Global Specialised Caregivers Pte Ltd &copy; 2016</li>
	            </ul>
			<button class="pull-right btn btn-inverse-alt btn-xs hidden-print" id="back-to-top"><i class="fa fa-arrow-up"></i></button>
	        </div>
	    </footer>
	<!-- page-content -->
	
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/dashboard/addNewSearch.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/dashboard/customFilter.js"></script>

	
</html>

<script>

var polling;

$.fn.dataTable.ext.search.push(
	    function( settings, data, dataIndex ) {
	        var min = parseInt( $('#min').val(), 10 );
	        var max = parseInt( $('#max').val(), 10 );
	        var age = parseFloat( data[3] ) || 0; // use data for the age column
	 
	        if ( ( isNaN( min ) && isNaN( max ) ) ||
	             ( isNaN( min ) && age <= max ) ||
	             ( min <= age   && isNaN( max ) ) ||
	             ( min <= age   && age <= max ) )
	        {
	            return true;
	        }
	        return false;
	    }
	);

$(document).ready(function(){
	
	 //universal search
    $("#searchbox").keypress(function(e) {
        if(e.which == 13) {
            var search_key = $('#searchbox').val();

            window.location = site_url+"selector/shortlist/?search=" + escape(search_key);

            return false;
        }
    });
    
    $.datepicker.setDefaults( {
		changeYear: true,
		yearRange: '1900:+0',
		buttonImage: 'calendar_1.png',
		dateFormat: 'yy-mm-dd'
	} );
	 
	if("${user.authorities}" == '[ROLE_ADMIN]'){
		$('#caregiverList').DataTable({
	    	bLengthChange: false,		//each page display
	    	"aaSorting": [				//sort 2:means the third column
	            [ 3, "desc" ]
	        ],
	        "aoColumnDefs": [			
	  			{ type: 'date-eu', targets: 3 }, //order the date sort(Date)
	  			{ "bSortable" : false, "aTargets" : [0]}
	  		],
	  		"searching":false
	    });	
	}else{
		$('#caregiverList').DataTable({
	    	bLengthChange: false,		//each page display
	    	"aaSorting": [				//sort 2:means the third column
	            [ 2, "desc" ]
	        ],
	        "aoColumnDefs": [			//order the date sort(Date) 
	  			{ type: 'date-eu', targets: 2 }
	  		],
	  		"searching":false
	    });
	}
	
	//   init the search box
	var searchParamList = ${searchParamList};
	var footer = "</div> </div>"

	var p_button = "<button type=\"button\" id=\"p_remove\" class=\"btn btn-default\" aria-label=\"Left Align\" onclick=\"DeleteDiv(this);\">"
		+	"<span class=\"fa fa-trash-o fa-lg\" aria-hidden=\"true\"></span>"
		+	"</button>";
	var q_button = "<button type=\"button\" id=\"q_remove\" class=\"btn btn-default\" aria-label=\"Left Align\" onclick=\"DeleteDiv(this);\">"
		+	"<span class=\"fa fa-trash-o fa-lg\" aria-hidden=\"true\"></span>"
		+	"</button>";
		
	$.each(searchParamList, function(n, value){
		if(value.questionFlag == 'p'){
			var p_question_id = "p_question" + n;
			var p_condition_id = "p_condition" + n;
			
			var p_title = "<div style=\"margin-top: 0px; !important\"> <div id=\"p_search\" class=\"p_filter\">";
			var p_question  = "<div class=\"form-group col-md-1 c_field\" style=\"width: 20%\">"
					+	"<select class=\"form-control\" name=\"p_field\" onchange=\"p_fieldSelect(this);\" id=\"" + p_question_id + "\">"
					+	"<option value=\"0\">(Select column)</option>"
					+	"<option value=\"p_full_name\">Name</option>"
					+	"<option value=\"p_mobile\">Phone Number</option>"
					+	"<option value=\"p_age\">Age</option>"
					+   "<option value=\"p_email\">Email</option>"
					+	"<option value=\"p_date_applied\">Date Applied</option>"
					+	"<option value=\"p_status\">Status</option>"
					+	"<option value=\"p_registered_concorde\">Registered with concorde</option>"
					+	"<option value=\"p_pre_deployment\">Advance Placement Scheme</option>"
					+	"<option value=\"p_medical_cert_verified\">Medical Cert Verified</option>"
					+	"<option value=\"p_date_of_placement\">Date Of Placement</option>"
					+	"<option value=\"p_date_of_change\">Date Of Change</option>"
					+	"<option value=\"p_numbers_of_placement\">Numbers Of Placements</option>"
					+	"<option value=\"p_work_in_sg\">Work in SG</option>"
					+	"<option value=\"p_work_in_hk\">Work in HK</option>"
					+	"<option value=\"p_work_in_tw\">Work in TW</option>"
					+   "</select>"
					+	"</div>";
			
			if(value.title != "p_status" && value.title != "p_registered_concorde" && value.title != "p_pre_deployment" && value.title != "p_medical_cert_verified" && value.title != "p_date_applied" && value.title != "p_work_in_sg" && value.title != "p_work_in_hk" && value.title != "p_work_in_tw"){
				var condition = "<div class=\"form-group col-md-2 div_match\">"
					+	"<select class=\"form-control\" name=\"p_match\" id=\"" + p_condition_id + "\">"
					+	"<option value=\"exactly\">is exactly</option>"
					+	"<option value=\"greater\">is greater than</option>"
					+	"<option value=\"greaterequal\">is greater than or equal to</option>"
					+	"<option value=\"less\">is less than</option>"
					+	"<option value=\"lessequal\">is less than or equal to</option>"
					+	"<option value=\"contain\">contains</option>"
					+	"<option value=\"notcontain\">does not contain</option>"
					+	"<option value=\"empty\">is empty</option>"
					+	"<option value=\"notempty\">is not empty</option>"
					+	"</select>"
					+	"</div>";

				var p_input = "<div class=\"form-group col-md-2 profile_text\">"
						+	"<input name=\"p_value\" type=\"text\" class=\"form-control\" value=\"" + value.input + "\"/>"
						+	"</div>";
				
				var searchHtml = p_title + p_question + condition + p_input + p_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+p_question_id).val(value.title);
				$("#"+p_condition_id).val(value.condition);
			}else if(value.title == "p_status"){
				var p_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 div_match'>" 
					+	"<select class='form-control' name='p_match' id='" + p_condition_id + "'>" 
					    +	"<option value='0'>1-New Applicant</option>" 
						+	"<option value='1'>2-Scheduling Interview</option>" 
						+   "<option value='10'>3-Interview Scheduled</option>"
						+	"<option value='2'>4-Shortlisted</option>" 
						+	"<option value='3'>5-Awaiting Documents</option>" 
						+	"<option value='4'>6-Not Selected / Not Interested for now</option>" 
						+	"<option value='5'>7-On Hold</option>" 
						+	"<option value='6'>8-Shortlisted with differed availability</option>" 
						+	"<option value='7'>9-Ready For Placement</option>" 
						+	"<option value='8'>10-Tagged</option>" 
						+	"<option value='9'>11-Contracted</option>" 
						+	"<option value='11'>12-Blacklisted</option>" 
					+	"</select>" 
					+	"</div>";
				var searchHtml = p_title + p_question + condition + p_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+p_question_id).val(value.title);
				$("#"+p_condition_id).val(value.input);
			}else if(value.title == "p_date_applied"){
				var p = "p_date_applied"+n;
				
				var p_condition_id = "condition" + n;
				var condition = "<div class=\"form-group col-md-2 div_match\">"
					+	"<select class=\"form-control\" name=\"p_match\" id=\"" + p_condition_id + "\">"
					+	"<option value=\"exactly\">is exactly</option>"
					+	"<option value=\"greater\">is greater than</option>"
					+	"<option value=\"greaterequal\">is greater than or equal to</option>"
					+	"<option value=\"less\">is less than</option>"
					+	"<option value=\"lessequal\">is less than or equal to</option>"
					+	"</select>"
					+	"</div>";

				var p_input = "<div class=\"form-group col-md-2 profile_text\">"
						+	"<input id='" +p+ "' name=\"p_value\" type=\"text\" class=\"form-control\" value=\"" + value.input + "\"/>"
						+	"</div>";
				
				var searchHtml = p_title + p_question + condition + p_input + p_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+p_question_id).val(value.title);
				$("#"+p_condition_id).val(value.condition);
			
				$("#"+p).datepicker();		//initialize the Date Calendar
			}else if(value.title == "p_dateOfPlacement"){
				var p = "p_date_applied"+n;
				
				var p_condition_id = "condition" + n;
				var condition = "<div class=\"form-group col-md-2 div_match\">"
					+	"<select class=\"form-control\" name=\"p_match\" id=\"" + p_condition_id + "\">"
					+	"<option value=\"exactly\">is exactly</option>"
					+	"<option value=\"greater\">is greater than</option>"
					+	"<option value=\"greaterequal\">is greater than or equal to</option>"
					+	"<option value=\"less\">is less than</option>"
					+	"<option value=\"lessequal\">is less than or equal to</option>"
					+	"</select>"
					+	"</div>";

				var p_input = "<div class=\"form-group col-md-2 profile_text\">"
						+	"<input id='" +p+ "' name=\"p_value\" type=\"text\" class=\"form-control\" value=\"" + value.input + "\"/>"
						+	"</div>";
				
				var searchHtml = p_title + p_question + condition + p_input + p_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+p_question_id).val(value.title);
				$("#"+p_condition_id).val(value.condition);
			
				$("#"+p).datepicker();		//initialize the Date Calendar
			}else if(value.title == "p_dateOfChange"){
				var p = "p_date_applied"+n;
				
				var p_condition_id = "condition" + n;
				var condition = "<div class=\"form-group col-md-2 div_match\">"
					+	"<select class=\"form-control\" name=\"p_match\" id=\"" + p_condition_id + "\">"
					+	"<option value=\"exactly\">is exactly</option>"
					+	"<option value=\"greater\">is greater than</option>"
					+	"<option value=\"greaterequal\">is greater than or equal to</option>"
					+	"<option value=\"less\">is less than</option>"
					+	"<option value=\"lessequal\">is less than or equal to</option>"
					+	"</select>"
					+	"</div>";

				var p_input = "<div class=\"form-group col-md-2 profile_text\">"
						+	"<input id='" +p+ "' name=\"p_value\" type=\"text\" class=\"form-control\" value=\"" + value.input + "\"/>"
						+	"</div>";
				
				var searchHtml = p_title + p_question + condition + p_input + p_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+p_question_id).val(value.title);
				$("#"+p_condition_id).val(value.condition);
			
				$("#"+p).datepicker();		//initialize the Date Calendar
			}else{
				var p_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 div_match'>" 
					+	"<select class='form-control' name='p_match' id='" + p_condition_id + "'>" 
					+	"<option value='YES'>YES</option>" 
					+	"<option value='NO'>NO</option>" 
					+	"</select>"
					+	"</div>";
				
				var searchHtml = p_title + p_question + condition + p_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+p_question_id).val(value.title);
				$("#"+p_condition_id).val(value.input);
			}		
			
		}else{
			var q_question_id = "q_question" + n;
			var q_condition_id = "q_condition" + n;
			
			var q_title = "<div> <div id=\"q_search\" class=\"p_filter\">"
			
			var q_question = "<div class=\"form-group col-md-1 custom_question\" style=\"width: 20%\">"
					+	"<select class=\"form-control question_select\" name=\"p_field\" onchange=\"selectChange(this);\" id=\"" + q_question_id + "\">"
					+	"<option value=\"0\">Select Question</option>"
					+	"<option value=\"q_gender\">What is your Gender?</option>"
					+	"<option value=\"q_dob\">what is your date of birth?</option>"
					+	"<option value=\"q_nationality\">What is your country of citizenship?</option>"
					+	"<option value=\"q_height\">What is your height in centimeters?</option>"
					+	"<option value=\"q_weight\">What is your weight in kg?</option>"
					+	"<option value=\"q_marital_status\"> What is your marital status?</option>"
					+	"<option value=\"q_has_children\">Do you have children and how many?</option>"
					+	"<option value=\"q_languages\">Which languages do you speak?</option>"
					+	"<option value=\"q_religion\">What is your religion?</option>"
					+	"<option value=\"q_food_choice\">Do you follow a specific diet?</option>"
					+	"<option value=\"q_hold_passport\">Are you currently holding a passport?</option>"
					+	"<option value=\"q_educational_level\">What Nursing degree / Diploma did you obtain?</option>"
					+	"<option value=\"q_exp\">How many years of experience do you have as a nurse?</option>"
					+	"<option value=\"q_specialties\">Please let us know if you have experience in the following areas</option>"
					+	"<option value=\"q_allergies\">If you have any allergies, please state which one(s)</option>"
					+	"<option value=\"q_diagnosed_conditions\">Do you have or were you ever diagnosed with any of the following conditions?</option>"
					+	"<option value=\"q_specialties\">Do you have Nursing Experience?</option>"
					+	"<option value=\"q_workingplace\">We are currently offering Live-In Caregiver opportunities both in HONG KONG and SINGAPORE. You CAN apply to BOTH locations to maximize your chances of getting a placement quickly. Please tick ALL locations that interest you</option>"
					+	"<option value=\"q_certified_cpr\">Have you obtained TESDA NCII certification?</option>"					
					+	"</select>"
					+	"</div>";

			if(value.title != "q_gender" && value.title != "q_dob" && value.title != "q_nationality" && value.title != "q_marital_status" && value.title != "q_has_children" &&value.title != "q_religion" && value.title != "q_food_choice" && value.title != "q_hold_passport" && value.title != "q_educational_level" && value.title != "q_certified_cpr" && value.title != "q_workingplace"){
				var condition = "<div class=\"form-group col-md-2 div_match\">"
					+	"<select class=\"form-control\" name=\"p_match\" id=\"" + q_condition_id + "\">"
					+	"<option value=\"exactly\">is exactly</option>"
					+	"<option value=\"greater\">is greater than</option>"
					+	"<option value=\"greaterequal\">is greater than or equal to</option>"
					+	"<option value=\"less\">is less than</option>"
					+	"<option value=\"lessequal\">is less than or equal to</option>"
					+	"<option value=\"contain\">contains</option>"
					+	"<option value=\"notcontain\">does not contain</option>"
					+	"<option value=\"empty\">is empty</option>"
					+	"<option value=\"notempty\">is not empty</option>"
					+	"</select>"
					+	"</div>";
						
			var q_input = "<div class=\"form-group col-md-2 question_text\" >"
					+	"<input name=\"p_value\" type=\"text\" class=\"form-control\" value=\"" + value.input + "\"/>"
					+	"</div>";
						
			var searchHtml = q_title + q_question + condition + q_input + q_button + footer;		
			$("#p_searchParam").append(searchHtml);
			$("#"+q_question_id).val(value.title);
			$("#"+q_condition_id).val(value.condition);
			}else if(value.title == "q_gender"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression'>" 
					+	"<select class='form-control' name='p_match' id='" + q_condition_id + "'>" 
					+	"<option value='Female'>Female</option>" 
					+	"<option value='Male'>Male</option>" 
					+	"</select>"
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.input);
			}else if(value.title == "q_certified_cpr"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression'>"
					+	"<select class='form-control' name='p_match' id='" + q_condition_id + "'>" 
					+	"<option value='Yes'>Yes</option>"
					+	"<option value='No'>No</option>"
					+	"</select>"
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.input);
			}else if(value.title == "q_workingplace"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression'>" 
					+	"<input type='checkbox' id='work_in_hk' name=\"p_match\">Hong Kong</input>" 
					+	"<input type='checkbox' id='work_in_sg' name=\"p_match\">Singapore </input>" 
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				if(value.input.indexOf("1") == 0){
					$("#work_in_hk").attr("checked", true);
				}else{
					$("#work_in_hk").attr("checked", false);
				}
				if(value.input.endWith("1")){
					$("#work_in_sg").attr("checked", true);
				}else{
					$("#work_in_sg").attr("checked", false);
				}
			}else if(value.title == "q_nationality"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression'>" 
					+	"<select class='form-control' name='p_match' id='" + q_condition_id + "'>" 
					+	"<option value='Bangladesh'>Bangladesh</option>"
					+	"<option value='India'>India</option>"
					+	"<option value='Indonesia'>Indonesia</option>"
					+	"<option value='Myanmar'>Myanmar</option>"
					+	"<option value='Philippines'>Philippines</option>"
					+	"<option value='Sri Lanka'>Sri Lanka</option>"
					+	"<option value='Others'>Others</option>"
					+	"</select>"
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.input);
			}else if(value.title == "q_marital_status"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression'>" 
					+	"<select class='form-control' name='p_match' id='" + q_condition_id + "'>" 
					+	"<option value='Single'>Single</option>"
					+	"<option value='Married'>Married</option>"
					+	"<option value='Divorced/Separated'>Divorced/Separated</option>"
					+	"<option value='Widowed'>Widowed</option>"
					+	"</select>"
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.input);
			}else if(value.title == "q_has_children"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression' >" 
					+	"<select class='form-control' name='p_match' id='" + q_condition_id + "'>" 
					+	"<option value='No Child'>No Children</option>"
					+	"<option value='1 child'>1 child</option>"
					+	"<option value='2 children'>2 children</option>"
					+	"<option value='3 children'>3 children</option>"
					+	"<option value='4 children'>4 children</option>"
					+	"<option value='5 children'>5 children</option>"
					+	"<option value='6 children'>6 children</option>"
					+	"<option value='7 children'>7 children</option>"
					+	"<option value='8 children'>8 children</option>"
					+	"</select>"
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.input);
			}else if(value.title == "q_religion"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression'>" 
					+	"<select class='form-control' name='p_match' id='" + q_condition_id + "'>"  
					+	"<option value='Christian'>Christian</option>"
					+	"<option value='Buddhist'>Buddhist</option>"
					+	"<option value='Hindu'>Hindu</option>"
					+	"<option value='Muslim'>Muslim</option>"
					+	"<option value='Sikh'>Sikh</option>"
					+	"<option value='Others'>Others</option>"
					+	"</select>"
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.input);
			}else if(value.title == "q_food_choice"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression'>" 
					+	"<select class='form-control' name='p_match' id='" + q_condition_id + "'>"   
					+	"<option value='No restrictions'>No restrictions</option>"
					+	"<option value='Vegetarian'>Vegetarian</option>"
					+	"<option value='Halal'>Halal</option>"
					+	"</select>"
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.input);
			}else if(value.title == "q_hold_passport"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression'>" 
					+	"<select class='form-control' name='p_match' id='" + q_condition_id + "'>"    
					+	"<option value='Yes'>Yes</option>"
					+	"<option value='No'>No</option>"
					+	"</select>"
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.input);
			}else if(value.title == "q_educational_level"){
				var q_condition_id = "condition" + n;
				var condition = "<div class='form-group col-md-2 question_expression'>" 
					+	"<select class='form-control' name='p_match' id='" + q_condition_id + "'>"    
					+	"<option value='Caregiver Certificate'>Caregiver Certificate</option>"
					+	"<option value='Nurse Assistant Diploma'>Nurse Assistant Diploma</option>"
					+	"<option value='Nurse Diploma'>Nurse Diploma</option>"
					+	"<option value='Bachelor of Sciences in Nursing or higher'>Bachelor of Sciences in Nursing or higher</option>"
					+	"</select>"
					+	"</div>";
				var searchHtml = q_title + q_question + condition + q_button + footer;
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.input);
			}else if(value.title == "q_dob"){
				var q = "q_mh_date_"+i;
				var q_condition_id = "condition" + n;
				var condition = "<div class=\"form-group col-md-2 div_match\">"
					+	"<select class=\"form-control\" name=\"p_match\" id=\"" + q_condition_id + "\">"
					+	"<option value=\"exactly\">is exactly</option>"
					+	"<option value=\"greater\">is greater than</option>"
					+	"<option value=\"greaterequal\">is greater than or equal to</option>"
					+	"<option value=\"less\">is less than</option>"
					+	"<option value=\"lessequal\">is less than or equal to</option>"
					+	"</select>"
					+	"</div>";
						
				var q_input = "<div class=\"form-group col-md-2 question_text\" >"
						+	"<input id='" +q+ "' name=\"p_value\" type=\"text\" class=\"form-control\" value=\"" + value.input + "\"/>"
						+	"</div>";
							
				var searchHtml = q_title + q_question + condition + q_input + q_button + footer;		
				$("#p_searchParam").append(searchHtml);
				$("#"+q_question_id).val(value.title);
				$("#"+q_condition_id).val(value.condition);
				
				$("#"+q).datepicker();
			}
		}
	});
});

function openSearch(){
	$("#f_show").hide();
	$("#p_show").show();
	$("#searchCandidate").show();
}

function a_search(){
	$("#q_filters").show();
	$("#ps_show").hide();
	$("#t_show").show();
}

function closeSearch(){
	$("#p_show").hide();
	$("#t_show").hide();
	$("#searchCandidate").hide();
	$("#f_show").show();
}

function t_search(){
	$("#t_show").hide();
	$("#ps_show").show();
	$("#q_filters").hide();
}

//submit search
$("#search_c").click(function (){
	var searchList = new Array();
	var divList = $(".p_filter");										//in according to the div'classname to get all the div
	for(var i=0;i<divList.length;i++){
		var sf = $(divList[i]).find("select[name='p_field']").val();		//get the select value
		var sm = $(divList[i]).find("select[name='p_match']").val();
		var sv = $(divList[i]).find("input[name='p_value']").val();	
		if(sf != '0'){
			if(sv != null){
				searchList[i] = sf +"="+ sm +"="+ sv;							//build up a list;
			}else{
				if(sf == "q_workingplace"){
					sm = "";
					if($("#work_in_hk").is(":checked")){
						sm = sm + "1"
					}else{
						sm = sm + "0";
					}
					if($("#work_in_sg").is(":checked")){
						sm = sm + "1";
					}else{
						sm = sm + "0";
					}
				}
				searchList[i] = sf +"=exactly="+ sm;	
			}
		}
	}
	
	var href = "${pageContext.request.contextPath}/candidates?searchList="+searchList +"&tag=search";
	window.location.href = href;
});

//export search result
$("#exprot_results").click(function (){
	var searchList = new Array();
	var divList = $(".p_filter");										//in according to the div'classname to get all the div
	for(var i=0;i<divList.length;i++){
		var sf = $(divList[i]).find("select[name='p_field']").val();		//get the select value
		var sm = $(divList[i]).find("select[name='p_match']").val();
		var sv = $(divList[i]).find("input[name='p_value']").val();	
		if(sf != '0'){
			if(sv != null){
				searchList[i] = sf +"="+ sm +"="+ sv;							//build up a list;
			}else{
				searchList[i] = sf +"=exactly="+ sm;	
			}
		}
	}
  	var href = "${pageContext.request.contextPath}/candidates/search/exprot?searchList="+searchList +"&tag=search";
  	$("#export_form").attr("action",href).submit();
	
});


$(".btn_collapse").click(function (){
	$("#messages_wrapper").hide();
});

$("#link_save_search").click(function (){
	$("#myModal").modal({backdrop: 'static', keyboard: false});
});

$("#link_delete_search").click(function (){
	bootbox.confirm({  
		buttons: {  
		    confirm: {  
		        label: 'OK'  
		    },  
		    cancel: {  
		        label: 'CANCEL'
		    }  
		},  
		message: 'Do you really want to delete this saved search?',  
		callback: function(result) {
            if(result){
            	var searchId = $("#search_id").val();
				$.ajax({
					type: "get",
					async:false,
					url: "${pageContext.request.contextPath}/search/delete",
					data: {id:searchId},
					dataType: "json",
		            success: function(data){
						var href = "${pageContext.request.contextPath}/candidates";
		            	location.href = href;
					}
				});
            }
		}
	});  
});


function savingSearch(tag, searchList){

	var searchTitle= $('#searchTitle').val();
	var shareWithOthers= "NO";
	if ($('#share_status').is(":checked"))
	{
		shareWithOthers = "YES";
	}
	
	if($.trim(searchTitle).length != 0){
		$(".loading").show();
		$.ajax({
			type: "get",
			async:false,
			url: "${pageContext.request.contextPath}/candidates/search",
			data: {searchTitle:searchTitle, searchList:searchList, shareWithOthers:shareWithOthers},
			dataType: "json",
            success: function(data){
				if(!data.flag){		//true(the search name is exists)
					var href = "${pageContext.request.contextPath}/candidates";
	            	location.href = href;
				}
			}
		});
	}else{
		$(".loading").hide();
		alertMsg("the search name could not be null!");
	}
}

//alert message
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

function selectAll(){
	if($("#selectAllIds").is(":checked")){
		$("input[name='candidateId']").prop('checked',true);
	}else{
		$("input[name='candidateId']").prop('checked',false);
	}
}

function deleteCandidates(){
	var ids = "";
	$("input[name='candidateId']:checked").each(function () {
		ids = ids + this.value + ",";
    });
	if(ids.length > 0){
		var confirmation = confirm("Do you want to delete these candidates?");
		if(confirmation){
	    	$.ajax({
	    		type : "post",
	    		url : "${pageContext.request.contextPath}/candidates/deleteCandidatesByIds",
			   	data : {ids : ids},
		 		dataType : "json"
	    	});

		}
	}else{
		alert("Please check at least one candidate");
	}
	
}

String.prototype.endWith=function(endStr){
	  var d=this.length-endStr.length;
	  return (d>=0&&this.lastIndexOf(endStr)==d);
}
</script>
