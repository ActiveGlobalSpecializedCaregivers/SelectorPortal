var i =0;

function selectChange(obj){
	var questionSelect = $(obj).parents('.custom_question');
	var questionValue = $(obj).val();
	var questionExpression = $(questionSelect).next(".question_expression");	//question
	var question_text = $(questionExpression).next(".question_text");				//input
	if(questionValue == 'q_gender'){
		$(questionExpression).remove();		//delete original element
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
						"<select class='form-control' name='p_match'>" +
								"<option value='Female'>Female</option>" +
								"<option value='Male'>Male</option>" +
						"</select></div>");
	}else if(questionValue == "q_nationality"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
						"<select class='form-control' name='p_match'>" +
							"<option value='Bangladesh'>Bangladesh</option>"+
							"<option value='India'>India</option>"+
							"<option value='Indonesia'>Indonesia</option>"+
							"<option value='Myanmar'>Myanmar</option>"+
							"<option value='Philippines'>Philippines</option>"+
							"<option value='Sri Lanka'>Sri Lanka</option>"+
							"<option value='Others'>Others</option>"+
						"</select></div>");
	}else if(questionValue == "q_marital_status"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
						"<select class='form-control' name='p_match'>" +
							"<option value='Single'>Single</option>"+
							"<option value='Married'>Married</option>"+
							"<option value='Divorced/Separated'>Divorced/Separated</option>"+
							"<option value='Widowed'>Widowed</option>"+
						"</select></div>");
	}else if(questionValue == "q_has_children"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression' >" +
						"<select class='form-control' name='p_match'>" +
							"<option value='No Child'>No Children</option>"+
							"<option value='1 child'>1 child</option>"+
							"<option value='2 children'>2 children</option>"+
							"<option value='3 children'>3 children</option>"+
							"<option value='4 children'>4 children</option>"+
							"<option value='5 children'>5 children</option>"+
							"<option value='6 children'>6 children</option>"+
							"<option value='7 children'>7 children</option>"+
							"<option value='8 children'>8 children</option>"+
						"</select></div>");
	}else if(questionValue == "q_religion"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
						"<select class='form-control' name='p_match'>" +
							"<option value='Christian'>Christian</option>"+
							"<option value='Buddhist'>Buddhist</option>"+
							"<option value='Hindu'>Hindu</option>"+
							"<option value='Muslim'>Muslim</option>"+
							"<option value='Sikh'>Sikh</option>"+
							"<option value='Others'>Others</option>"+
						"</select></div>");
	}else if(questionValue == "q_hold_passport"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
						"<select class='form-control' name='p_match'>" +
							"<option value='Yes'>Yes</option>"+
							"<option value='No'>No</option>"+
						"</select></div>");
	}else if(questionValue == "q_certified_cpr"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
						"<select class='form-control' name='p_match'>" +
							"<option value='Yes'>Yes</option>"+
							"<option value='No'>No</option>"+
						"</select></div>");
	}else if(questionValue == "q_educational_level"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
				"<select class='form-control' name='p_match'>" +
				"<option value='Caregiver Certificate'>Caregiver Certificate</option>"+
				"<option value='Nurse Assistant Diploma'>Nurse Assistant Diploma</option>"+
				"<option value='Nurse Diploma'>Nurse Diploma</option>"+
				"<option value='Bachelor of Sciences in Nursing or higher'>Bachelor of Sciences in Nursing or higher</option>"+
				"</select></div>");
	}else if(questionValue == "q_dob"){
		var q = "q_mh_date_"+i;
		$(questionExpression).remove();		
		$(question_text).remove();
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
							"<select class='form-control' name='p_match'>" +
							  "<option value='exactly'>is exactly</option>" +
							  "<option value='greater'>is greater than</option>" +
							  "<option value='greaterequal'>is greater than or equal to</option>" +
							  "<option value='less'>is less than</option>" +
							  "<option value='lessequal'>is less than or equal to</option>" +
							"</select>" +
						"</div>" +
						"<div class='form-group col-md-2 question_text'>" +
							"<input id='" +q+ "' name='p_value' type='text' class='form-control'/>" +
						"</div>");
		$("#"+q).datepicker();
		
		i++;
	}else if(questionValue == "q_specialties"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
						"<select class='form-control' name='p_match'>" +
							"<option value='Bed sores'>Bed sores</option>"+
							"<option value='Cancer'>Cancer</option>"+
							"<option value='Dementia / Alzheimer's'>Dementia / Alzheimer's</option>"+
							"<option value='Diabetes patients (including administering insulin)'>Diabetes patients (including administering insulin)</option>"+
							"<option value='General wound care'>General wound care</option>"+
							"<option value='Geriatric'>Geriatric</option>"+
							"<option value='New-born babies (healthy)'>New-born babies (healthy)</option>"+
							"<option value='New-born babies (with medical problems)'>New-born babies (with medical problems)</option>"+
							"<option value='NGT (nasogastric tube feeding)'>NGT (nasogastric tube feeding)</option>"+
							"<option value='Paediatric'>Paediatric</option>"+
							"<option value='PEG (percutaneous endoscopic gastrostomy feeding tube)'>PEG (percutaneous endoscopic gastrostomy feeding tube)</option>"+
							"<option value='Psychiatric/Special needs patients'>Psychiatric/Special needs patients</option>"+
							"<option value='Stoma care'>Stoma care</option>"+
							"<option value='Stroke patients'>Stroke patients</option>"+
							"<option value='Suctioning of airways'>Suctioning of airways</option>"+
							"<option value='Tracheostomy care'>Tracheostomy care</option>"+
							"<option value='Urinary catheter'>Urinary catheter</option>"+
							"<option value='Simple cases only'>Simple cases only</option>"+
							"<option value='Chemotherapy'>Chemotherapy</option>"+
							"<option value='Dialysis'>Dialysis</option>"+
							"<option value='Respiratory Therapy'>Respiratory Therapy</option>"+
						"</select></div>");
	}else if(questionValue == "q_languages"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
						"<select class='form-control' name='p_match'>" +
							"<option value='Arabic'>Arabic</option>"+
							"<option value='Bengali'>Bengali</option>"+
							"<option value='Burmese'>Burmese</option>"+
							"<option value='Cantonese'>Cantonese</option>"+
							"<option value='English'>English</option>"+
							"<option value='Hokkien'>Hokkien</option>"+
							"<option value='Hakka'>Hakka</option>"+
							"<option value='Mandarin Chinese'>Mandarin Chinese</option>"+
							"<option value='Malay'>Malay</option>"+
							"<option value='Tamil'>Tamil</option>"+
							"<option value='Tagalog'>Tagalog</option>"+
							"<option value='Telugu'>Telugu</option>"+
							"<option value='Teochew'>Teochew</option>"+
							"<option value='Hindi'>Hindi</option>"+
							"<option value='Malayalam'>Malayalam</option>"+
							"<option value='Sinhalese'>Sinhalese</option>"+
							"<option value='Others'>Others</option>"+
						"</select></div>");
	}else if(questionValue == "q_workingplace"){
		$(questionExpression).remove();		
		$(question_text).remove();
		
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
					"<input type='checkbox' name='p_match' id='work_in_hk'>Hong Kong</input>" +
					"<input type='checkbox' name='p_match' id='work_in_sg'>Singapore </input>" +
				"</div>");
	}else if($(question_text).length<1 || questionValue != "q_dob"){   
		$(questionExpression).remove();	
		$(question_text).remove();
		$(questionSelect).after("<div class='form-group col-md-2 question_expression'>" +
							"<select class='form-control' name='p_match'>" +
							  "<option value='exactly'>is exactly</option>" +
							  "<option value='contain'>contains</option>" +
							  "<option value='notcontain'>does not contain</option>" +
							  "<option value='greater'>is greater than</option>" +
							  "<option value='greaterequal'>is greater than or equal to</option>" +
							  "<option value='less'>is less than</option>" +
							  "<option value='lessequal'>is less than or equal to</option>" +
							  "<option value='empty'>is empty</option>" +
							  "<option value='notempty'>is not empty</option>" +
							"</select>" +
						"</div>" +
						"<div class='form-group col-md-2 question_text'>" +
							"<input name='p_value' type='text' class='form-control'/>" +
						"</div>");
	}
}

//profile filter:select date search
function p_fieldSelect(field){  
	var fieldVal = $(field).val();			//find name="p_field" selection's value
	var fieldParent = $(field).parent(".c_field");
	
	//match 
	var matchDiv = $(fieldParent).next(".div_match");		//div search
	
	//input  value
	var valueDiv = $(matchDiv).next(".profile_text");				//find name='p_value' input's parent
	
	if(fieldVal == 'p_date_applied'){
		var p = "p_mh_date_"+i;
		$(matchDiv).remove();
		$(valueDiv).remove();
		
		$(fieldParent).after("<div class='form-group col-md-2 div_match'>" +
							"<select class='form-control' name='p_match'>" +
							  "<option value='exactly'>is exactly</option>" +
							  "<option value='greater'>is greater than</option>" +
							  "<option value='greaterequal'>is greater than or equal to</option>" +
							  "<option value='less'>is less than</option>" +
							  "<option value='lessequal'>is less than or equal to</option>" +
							"</select>" +
						"</div>" +
						"<div class='form-group col-md-2 profile_text'>" +
							"<input id='" +p+ "' name='p_value' type='text' class='form-control'>" +
						"</div>");
		
		$("#"+p).datepicker();
		
		i++;
	}else if(fieldVal == 'p_date_of_placement'){
		var p = "p_mh_date_"+i;
		$(matchDiv).remove();
		$(valueDiv).remove();
		
		$(fieldParent).after("<div class='form-group col-md-2 div_match'>" +
							"<select class='form-control' name='p_match'>" +
							  "<option value='exactly'>is exactly</option>" +
							  "<option value='greater'>is greater than</option>" +
							  "<option value='greaterequal'>is greater than or equal to</option>" +
							  "<option value='less'>is less than</option>" +
							  "<option value='lessequal'>is less than or equal to</option>" +
							"</select>" +
						"</div>" +
						"<div class='form-group col-md-2 profile_text'>" +
							"<input id='" +p+ "' name='p_value' type='text' class='form-control'>" +
						"</div>");
		
		$("#"+p).datepicker();
		
		i++;
	}else if(fieldVal == 'p_date_of_change'){
		var p = "p_mh_date_"+i;
		$(matchDiv).remove();
		$(valueDiv).remove();
		
		$(fieldParent).after("<div class='form-group col-md-2 div_match'>" +
							"<select class='form-control' name='p_match'>" +
							  "<option value='exactly'>is exactly</option>" +
							  "<option value='greater'>is greater than</option>" +
							  "<option value='greaterequal'>is greater than or equal to</option>" +
							  "<option value='less'>is less than</option>" +
							  "<option value='lessequal'>is less than or equal to</option>" +
							"</select>" +
						"</div>" +
						"<div class='form-group col-md-2 profile_text'>" +
							"<input id='" +p+ "' name='p_value' type='text' class='form-control'>" +
						"</div>");
		
		$("#"+p).datepicker();
		
		i++;
	}else if(fieldVal == 'p_status'){
		$(matchDiv).remove();
		$(valueDiv).remove();
		
		$(fieldParent).after("<div class='form-group col-md-2 div_match'>" +
							"<select class='form-control' name='p_match'>" +
								"<option value='0'>1-New Applicant</option>"
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
            				+"</select>" +
						    "</div>");
		
	}else if(fieldVal == 'p_registered_concorde' || fieldVal== 'p_pre_deployment' || fieldVal=='p_medical_cert_verified' || fieldVal == 'p_work_in_sg' || fieldVal == 'p_work_in_hk' || fieldVal == 'p_work_in_tw'){
		$(matchDiv).remove();
		$(valueDiv).remove();
		
		$(fieldParent).after("<div class='form-group col-md-2 div_match'>" +
							"<select class='form-control' name='p_match'>" +
							  "<option value='YES'>YES</option>" +
							  "<option value='NO'>NO</option>" +
							"</select>"+
							"</div>");
		
	}else if($(valueDiv).length<1 || fieldVal != "p_date_applied"){   
		$(matchDiv).remove();	
		$(valueDiv).remove();
		$(fieldParent).after("<div class='form-group col-md-2 div_match'>" +
							"<select class='form-control' name='p_match'>" +
							  "<option value='exactly'>is exactly</option>" +
							  "<option value='contain'>contains</option>" +
							  "<option value='notcontain'>does not contain</option>" +
							  "<option value='greater'>is greater than</option>" +
							  "<option value='greaterequal'>is greater than or equal to</option>" +
							  "<option value='less'>is less than</option>" +
							  "<option value='lessequal'>is less than or equal to</option>" +
							  "<option value='empty'>is empty</option>" +
							  "<option value='notempty'>is not empty</option>" +
							"</select>" +
						"</div>" +
						"<div class='form-group col-md-2 profile_text'>" +
							"<input name='p_value' type='text' class='form-control'/>" +
						"</div>");
	}	
	
}