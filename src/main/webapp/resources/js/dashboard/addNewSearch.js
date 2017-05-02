//add and delete the div
var searchNum;
var lastDivId;

//profile filter
var p_id;
var p_num=1;

//question filter
var q_id;
var q_num=1;


function add(){
	
}

//add div
function AddDiv(search,type){
	
	if(type.indexOf("p")>-1){
		var searchHtml = "<div class=\"p_filter\">" 
				+	"<div class=\"form-group col-md-1 c_field\" style=\"width: 20%\">"
				+	"<select class=\"form-control\" name=\"p_field\" onchange=\"p_fieldSelect(this);\">"
				+   "<option value=\"0\">(Select column)</option>"
				+	"<option value=\"p_full_name\">Name</option>"								  
				+	"<option value=\"p_mobile\">Phone Number</option>"								  
				+	"<option value=\"p_age\">Age</option>"								  
				+	"<option value=\"p_email\">Email</option>"								  
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
				+	"</select>"								
				+	"</div>"							
				+	"<div class=\"form-group col-md-2 div_match\">"							
				+	"<select class=\"form-control\" name=\"p_match\">"								
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
				+	"</div>"
				+	"<div class=\"form-group col-md-2 profile_text\">"
				+	"<input name=\"p_value\" type=\"text\" class=\"form-control\"/>"							
				+	"</div>"							
				+	"<button type=\"button\" id=\"p_remove\" class=\"btn btn-default\" aria-label=\"Left Align\" onclick=\"DeleteDiv(this);\">"
				+	"<span class=\"fa fa-trash-o fa-lg\" aria-hidden=\"true\"></span>"
				+	"</button>"
				+ 	"</div>";
		$("#p_add_searchParam").append(searchHtml);
	}else{
		var searchHtml = "<div class=\"p_filter\">"
				+	"<div class=\"form-group col-md-1 custom_question\" style=\"width: 20%\">"
				+	"<select class=\"form-control question_select\" name=\p_field\ onchange=\"selectChange(this);\">"
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
				+	"</div>"
				+	"<div class=\"form-group col-md-2 question_expression\">"							
				+	"<select class=\"form-control\" name=\"p_match\">"								
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
				+	"</div>"
				+	"<div class=\"form-group col-md-2 question_text\" >"
				+	"<input name=\"p_value\" type=\"text\" class=\"form-control\"/>"
				+	"</div>"
				+	"<button type=\"button\" id=\"q_remove\" class=\"btn btn-default\" aria-label=\"Left Align\" onclick=\"DeleteDiv(this);\">"
				+	"<span class=\"fa fa-trash-o fa-lg\" aria-hidden=\"true\"></span>"
				+	"</button>"
				+	"</div>";

		$("#q_add_searchParam").append(searchHtml);
	}
	
}  

//delete
function DeleteDiv(obj){
	var parentDom = obj.parentElement;
	var grandpaDom = parentDom.parentElement;
	grandpaDom.removeChild(parentDom);
}

