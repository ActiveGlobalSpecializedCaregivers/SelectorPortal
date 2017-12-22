<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="../header.jsp" />

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

.ui-datepicker-trigger {left: 10px; top: -1px; position: relative;}
</style>
<div id='dialog-modal' style="display: none;" title="Send CV">
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
<div id="page-content">
    <div id='wrap'>
        <div id="page-heading">
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/shortlist/getStatusAmount" >Shortlist</a></li>
                	<li class="active" id="smallTitle"></li>
            </ol>
            <div id="normalTitle"></div>
        </div>


        <div class="container">
            <div class="row">
              <div class="col-md-12">
                    <div class="panel panel-ag-blue">
                        <div class="panel-heading">
                        </div>
                        <div class="panel-body collapse in">
                        	<div id="accordioninpanel" class="accordion-group">
								<div class="accordion-item">
                                    <div class="clearfix">
                                        <button  class="btn btn-primary btn-xs" id="cancel_send" onclick="clearFilter();">
                                            <span>Clear Filter</span>
                                        </button>
                                    </div>                                    
									<a id="href_accordion" class="accordion-title" data-toggle="collapse" data-parent="#accordioninpanel" href="#collapseinOne"><h4 style="margin-bottom:0px;">Filtering Criteria</h4></a>
									<div id="collapseinOne" class="collapse bg-light-blue">
										<div class="accordion-body clearfix" style="padding-bottom:0;">
                                        	<div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-md-4 col-xs-4 col-sm-4 small">
                                                    	<strong>Nursing experience:</strong>
                                                        <ul id="no-style">
                                                        	<li><label><input name="exp_specialty[]" type="checkbox" value="Bed sores" data-category="medical" class="filter" > Bed sores</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Cancer" data-category="medical" class="filter" > Cancer</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Diabetes patients (including administering insulin)" data-category="medical" class="filter" > Diabetes patients (including administering insulin)</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Dementia / Alzheimer\'s" data-category="medical" class="filter" > Dementia / Alzheimer's</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Geriatric" data-category="medical" class="filter" > Geriatric</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="General wound care" data-category="medical" class="filter" > General wound care</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="New-born babies (healthy)" data-category="medical" class="filter" > New-born babies (healthy)</label></li>
                                                        </ul>
                                                    </div>
                                                    <div class="col-md-4 col-xs-4 col-sm-4 small">
                                                    	<ul id="no-style">
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="New-born babies (with medical problems)" data-category="medical" class="filter"> New-born babies (with medical problems)</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="NGT (nasogastric tube feeding)" data-category="medical" class="filter" > NGT (nasogastric tube feeding)</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Paediatric" data-category="medical" class="filter" > Paediatric</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="PEG (percutaneous endoscopic gastrostomy feeding tube)" data-category="medical" class="filter" > PEG (percutaneous endoscopic gastrostomy feeding tube)</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Psychiatric/Special needs patients" data-category="medical" class="filter" > Psychiatric/Special needs patients</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Simple cases only" data-category="medical" class="filter" > Simple cases only</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Suctioning of airways" data-category="medical" class="filter" > Suctioning of airways</label></li>
                                                         </ul>   
                                                    </div>
                                                    <div class="col-md-4 col-xs-4 col-sm-4 small">
                                                    	<ul id="no-style">
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Stoma care" data-category="medical" class="filter" > Stoma care</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Stroke patients" data-category="medical" class="filter" > Stroke patients</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Tracheostomy care" data-category="medical" class="filter" > Tracheostomy care</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Urinary catheter" data-category="medical" class="filter" > Urinary catheter</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Chemotherapy" data-category="medical" class="filter" > Chemotherapy</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Dialysis" data-category="medical" class="filter" > Dialysis</label></li>
                                                            <li><label><input name="exp_specialty[]" type="checkbox" value="Respiratory Therapy" data-category="medical" class="filter" > Respiratory Therapy</label></li>
                                                        </ul>
                                                    </div>
                                                    <div class="clearfix"></div>
                                                    <div id="accordionininnerpanel" class="accordion-group" style="margin-bottom:20px;">
                                                        <div class="accordion-item">
                                                            <a class="accordion-title" data-toggle="collapse" data-parent="#accordionininnerpanel" href="#innercollapseinOne"><h4 style="margin-bottom:0px;">Advanced Criteria</h4></a>
                                                            <div id="innercollapseinOne" class="collapse bg-light-blue">
                                                                <div class="accordion-body clearfix" style="padding-bottom:0;">
                                                                    <div class="col-sm-12">
                                                                        <div class="row">
                                                                        	<div class="col-md-3 col-xs-3 col-sm-3 small">
                                                                                <strong>Languages Spoken:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Arabic" data-category="language" class="filter" > Arabic</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Bengali" data-category="language" class="filter" > Bengali</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Burmese" data-category="language" class="filter" > Burmese</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Cantonese" data-category="language" class="filter" > Cantonese</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="English" data-category="language" class="filter" > English</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Hakka" data-category="language" class="filter" > Hakka</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Hindi" data-category="language" class="filter" > Hindi</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Hokkien" data-category="language" class="filter" > Hokkien</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Malay" data-category="language" class="filter" > Malay</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Malayalam" data-category="language" class="filter" > Malayalam</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Mandarin Chinese" data-category="language" class="filter" > Mandarin Chinese</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Tagalog" data-category="language" class="filter" > Tagalog</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Tamil" data-category="language" class="filter" > Tamil</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Telugu" data-category="language" class="filter" > Telugu</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Teochew" data-category="language" class="filter" > Teochew</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Sinhalese" data-category="language" class="filter" > Sinhalese</label></li>
                                                                                    <li><label><input name="languages[]" type="checkbox" value="Others" data-category="language" class="filter" > Others</label></li>
                                                                                </ul>
                                                                            </div>
                                                                            <div class="col-md-3 col-xs-3 col-sm-3 small">
                                                                            	<strong>Religion:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="religion[]" type="checkbox" value="Christian" data-category="religion" class="filter" > Christian</label></li>
                                                                                    <li><label><input name="religion[]" type="checkbox" value="Buddhist" data-category="religion" class="filter" > Buddhist</label></li>
                                                                                    <li><label><input name="religion[]" type="checkbox" value="Hindu" data-category="religion" class="filter" > Hindu</label></li>
                                                                                    <li><label><input name="religion[]" type="checkbox" value="Muslim" data-category="religion" class="filter" > Muslim</label></li>
                                                                                    <li><label><input name="religion[]" type="checkbox" value="Sikh" data-category="religion" class="filter" > Sikh</label></li>
                                                                                    <li><label><input name="religion[]" type="checkbox" value="Others" data-category="religion" class="filter" > Others</label></li>
                                                                                </ul>
                                                                                 <br />
                                                                                <strong>Gender:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="gender[]" type="checkbox" value="Male" data-category="gender" class="filter" > Male</label></li>
                                                                                    <li><label><input name="gender[]" type="checkbox" value="Female" data-category="gender" class="filter" > Female</label></li>
                                                                                </ul>
                                                                            </div>
                                                                            <div class="col-md-3 col-xs-3 col-sm-3 small">
                                                                                <strong>Education level:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="educational_level[]" type="checkbox" value="Caregiver Certificate" data-category="level" class="filter" > Caregiver Certificate</label></li>
                                                                                    <li><label><input name="educational_level[]" type="checkbox" value="Nurse Assistant Diploma" data-category="level" class="filter" > Nurse Assistant Diploma</label></li>
                                                                                    <li><label><input name="educational_level[]" type="checkbox" value="Nurse Diploma" data-category="level" class="filter" > Nurse Diploma</label></li>
                                                                                    <li><label><input name="educational_level[]" type="checkbox" value="Bachelor of Sciences in Nursing or higher" data-category="level" class="filter" > Bachelor of Sciences in Nursing or higher</label></li>
                                                                                </ul>
                                                                                <br />
                                                                                <strong>Food choice:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="food_choice[]" type="checkbox" value="No Restrictions" data-category="food" class="filter" > No Restrictions</label></li>
                                                                                    <li><label><input name="food_choice[]" type="checkbox" value="Vegetarian" data-category="food" class="filter" > Vegetarian</label></li>
                                                                                    <li><label><input name="food_choice[]" type="checkbox" value="Halal" data-category="food" class="filter" > Halal</label></li>
                                                                                    <li><label><input name="food_choice[]" type="text" data-category="food" class="filter" > Other</label></li>
                                                                                </ul>
                                                                                <br />
                                                                                <strong>Marital status:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="marital_status[]" type="checkbox" value="Single" data-category="marital" class="filter" > Single</label></li>
                                                                                    <li><label><input name="marital_status[]" type="checkbox" value="Married" data-category="marital" class="filter" > Married</label></li>
                                                                                    <li><label><input name="marital_status[]" type="checkbox" value="Divorced/Seperated" data-category="marital" class="filter" > Divorced/Seperated</label></li>
                                                                                    <li><label><input name="marital_status[]" type="checkbox" value="Widowed" data-category="marital" class="filter" > Widowed</label></li>
                                                                                </ul>
                                                                            </div>
                                                                            <div class="col-md-3 col-xs-3 col-sm-3 small">
                                                                                <strong>Availability:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="availability[]" type="checkbox" value="Immediate" data-category="avail" class="filter" > Immediate</label></li>
                                                                                    <li><label><input name="availability[]" type="checkbox" value="2-3 weeks" data-category="avail" class="filter"> 2-3 weeks</label></li>
                                                                                    <li><label><input name="availability[]" type="checkbox" value="1 month" data-category="avail" class="filter" > 1 month</label></li>
                                                                                </ul>
                                                                                <br />
                                                                                <strong>Age:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="age[]" type="checkbox" value="23-30" data-category="age" class="filter" > 23-30</label></li>
                                                                                    <li><label><input name="age[]" type="checkbox" value="31-40" data-category="age" class="filter" > 31-40</label></li>
                                                                                    <li><label><input name="age[]" type="checkbox" value="40+" data-category="age" class="filter" > 40+</label></li>
                                                                                </ul>
                                                                                <br />
                                                                                <strong>Has children:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="has_children[]" type="checkbox" value="Has Children" data-category="children" class="filter" > Has Children</label></li>
                                                                                    <li><label><input name="has_children[]" type="checkbox" value="No Children" data-category="children" class="filter"> No Children</label></li>
                                                                                </ul>
                                                                                <br />
                                                                                <strong>Years of Experience:</strong>
                                                                                <ul id="no-style">
                                                                                    <li><label><input name="years_exp[]" type="checkbox" value="<=5" data-category="exp" class="filter" > Less or equal to 5 years</label></li>
                                                                                    <li><label><input name="years_exp[]" type="checkbox" value=">5" data-category="exp" class="filter" > Strictly more than 5 years</label></li>
                                                                                </ul>                                                                                
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
									</div>
								</div>
							</div>
                            <hr />
                            <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered datatables refresh" id="candidateTable">
                                <thead>
                                    <tr>
                                        <th width="2%">ID</th>
                                        <th width="15%">Name</th>
                                        <th width="10%">Contact No.</th>
	                                    <th width="6%">Age</th>
                                        <th width="15%">Education level</th>
                                        <th width="5%">Years of experience</th>
                                        <th width="10%">Expected salary</th>
                                        <th width="8%">Availability</th>
                                        <th width="5%">R&eacute;sum&eacute;</th>
                                        <th width="8%">Status</th>
                                        <th width="10%">Tagged To</th>
	                                    <th width="15%">Contracted To</th>
                                    </tr>
                                </thead>

                            </table>
                            <!--end table-->
                        </div>
                    </div>
                </div>
            </div>

        </div> <!-- container -->
    </div> <!--wrap -->
</div> <!-- page-content -->
<jsp:include page="../footer.jsp" />


<script>

	var medical=new Array();
	var language=new Array();
	var religion=new Array();
	var level=new Array();
	var food=new Array();
	var marital=new Array();
	var gender=new Array();
	var avail=new Array();
	var age=new Array();
	var children=new Array();
	var exp=new Array();
	var emailVerify;

	
	// -------------------------------
	// Initialize Data Tables
	// -------------------------------

	$(document).ready(function() {
		
		initTitle();
		
	    $('#ToolTables_candidateTable_3').addClass("disabled");

	    $('.datatables').dataTable({
	        "sDom": "<'row'<'col-xs-6'l><'col-xs-6'f>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
	        "sPaginationType": "bootstrap",
	        "oLanguage": {
	            "sLengthMenu": "_MENU_ records per page",
	            "sSearch": ""
	        }   
	    });
	    $('.dataTables_filter input').addClass('form-control').attr('placeholder','Search...');
	    $('.dataTables_length select').addClass('form-control');} );

	    if(getParameterByName('shortlist') != ''){         

	        var medicalspr = "";  
	        var languagespr = "";  
	        var religionspr = "";  
	        var levelspr = "";  
	        var foodspr = "";  
	        var maritalspr = "";  
	        var genderspr = "";  
	        var availspr = ""; 
	        var agespr = "";  
	        var expspr = ""; 
	        var childrenspr = "";      
	    
	    }
	    
	    $(".filter").click(function(){
	        var checkboxes = $("input[type='checkbox']")
	        if(!checkboxes.is(":checked")){       
	        	medicalsp="",
                languagesp="",
                religionsp="",
                levelsp="",
                foodsp="",
                maritalsp="",
                availsp="",
                agesp="",
                expsp="",
                childrensp="";    
	        	
	            medical=[],
	            language=[],
	            religion=[],
	            level=[],
	            food=[],
	            marital=[],
	            gender=[],
	            avail=[],
	            age=[],
	            exp=[],
	            children=[];

	            initTable('');
	        }else{
	        	
		        if ($(this).is(":checked")){
		            eval($(this).attr("data-category")+".push('"+$(this).val()+"')");
		        }else{
		            if ($(this).attr("data-category")=="medical"){				
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){					
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }
	
		            if ($(this).attr("data-category")=="language"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }
	
		            if ($(this).attr("data-category")=="religion"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }                          
	
		            if ($(this).attr("data-category")=="level"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }     
	
		            if ($(this).attr("data-category")=="food"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }
	
		            if ($(this).attr("data-category")=="marital"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }
	
		            if ($(this).attr("data-category")=="gender"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }
	
		            if ($(this).attr("data-category")=="avail"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }  
	
		            if ($(this).attr("data-category")=="age"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            } 
	
		            if ($(this).attr("data-category")=="children"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }
	
		            if ($(this).attr("data-category")=="exp"){
		                eval("var index = "+$(this).attr("data-category")+".indexOf('"+$(this).val()+"')");
		                if (index > -1){
		                    eval($(this).attr("data-category")+".splice("+index+",1)");
		                }    
		            }                                                              
	        }

		    medicalsp="",
	        languagesp="",
	        religionsp="",
	        levelsp="",
	        foodsp="",
	        maritalsp="",
	        gendersp="",
	        availsp="",
	        agesp="",
	        expsp="",
	        childrensp="";
		        
	        if (medical.length > 0){
	            medicalsp = medical.join(",");
	        }

	        if (language.length > 0){
	            languagesp = language.join(",");
	        }

	        if (religion.length > 0){
	            religionsp = religion.join(",");
	        }

	        if (level.length > 0){
	            levelsp = level.join(",");
	        }

	        if (food.length > 0){
	            foodsp = food.join(",");
	        }

	        if (marital.length > 0){
	            maritalsp = marital.join(",");
	        }

	        if (gender.length > 0){
	            gendersp = gender.join(",");
	        }

	        if (avail.length > 0){
	            availsp = avail.join(",");
	        }

	        if (age.length > 0){
	            agesp = age.join(",");
	        }

	        if (children.length > 0){
	            childrensp = children.join(",");
	        }

	        if (exp.length > 0){
	            expsp = exp.join(",");
	        }

	        var data =  '{"medical": "'+medicalsp+'", "language": "'+languagesp+'", "religion": "'+religionsp+'", "level": "'+levelsp+'", "food": "'+foodsp+'", "marital": "'+maritalsp+'", "gender": "'+gendersp+'", "avail": "'+availsp+'", "age": "'+agesp+'", "children": "'+childrensp+'", "exp": "'+expsp+'"}';

	        initTable(data);
	        }
	    });

	function clearFilter(){
		$('input:checkbox').removeAttr('checked');
		initTable('');
	}

	function initTable(data) {
		var status = getParameterByName("status");

		var candidateTable = $('#candidateTable').dataTable(
			{
				"sDom" : "<'row'<'col-sm-6'T><'col-sm-6'f>r>t<'row'<'col-sm-6'i><'col-sm-6'p>>",
				"sAjaxSource" : "${pageContext.request.contextPath}/shortlist/getCandidatesByStatus",
				"aaSorting" : [ [ 0, "desc" ] ],
				"sAjaxDataProp" : "",
				"bProcessing" : true,
				"bAutoWidth" : false,
				"bDestroy" : true,
		        "sServerMethod": "POST",
		        "fnServerParams": function ( aoData ) {
		        	aoData.push( {"name": "filter", "value": data}, {"name": "status", "value": status});	           
		         },
				"fnDrawCallback" : function() {
					var page_num = this.fnPagingInfo().iPage;
					var header = $("meta[name='_csrf_header']").attr("content");
					var token = $("meta[name='_csrf']").attr("content");
				},
				"iDisplayStart" : 0,         
				"aoColumns" : [
				               {
				            	   "mData" : "user_id",
								   "bVisible" : false
							   },
							   {
								   "mData" : "full_name",
								   "mRender" : function(data, type, full) {
									   if (full['newFlag'] == "new") {
										   return data + ' <a href="#" class="btn btn-brown btn-xs">New!</a>';
									   } else {
										   return data;
									   }
								   }
							   },
							   {"mData" : "mobile"},
							   {"mData" : "age"},
							   {"mData" : "educational_level"},
							   {"mData" : "exp"},
							   {
								   "mData" : "salary_sgd",
								   "mRender" : function(data, type, full){
									   if("${user.role}" == "ROLE_SALES_HK"){
										   return full['salary_hkd'];
									   }else if("${user.role}" == "ROLE_SALES_TW"){
										   return full['salary_twd'];
									   }else{
										   return full['salary_sgd'];
									   }
								   }
							   },
							   {"mData" : "availability"},
							   {
								   "mData" : "resume",
								   "mRender" : function(data, type, full) {
									   return "<a href='${pageContext.request.contextPath}/admin/users/view_cv?userId=" + full['user_id'] + "' class='btn btn-primary btn-xs'  target='_blank'>View R&eacute;sum&eacute;</a>";
								   }
							   },
							   {
								   "mData" : "tag_status",
								   "mRender" : function(data, type, full) {
									   if (full['tag'] == 0) {
										   return "<a href='#' class='btn btn-default btn-xs'>Available</a>";
									   } else if (full['tag'] == 3) {
										   return "<a href='#' class='btn btn-default btn-xs'>On hold</a>";
									   } else {
										   return full['tag_status'];
									   }
								   }
							   }, 
							   {"mData" : "tagged_to"},
							   {"mData" : "contracted_to"} 
							],
				"oTableTools":{
					"sRowSelect":"multi",
					"aButtons":[
					    <sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_RECRUITER', 'ROLE_SALES', 'ROLE_SALES_SG', 'ROLE_SALES_HK', 'ROLE_SALES_TW')">
					        { "sExtends":"editor_edit", "editor":editor },
					        { "sExtends":"send_cv", 
					          "sButtonText":"Send CV",
					          "bSelectedOnly": "true",
					        }
					    </sec:authorize>
					 ]
				}
			});
		
		candidateTable.fnDraw();

		$('.dataTables_filter input').addClass('form-control').attr('placeholder', 'Search...');
		$('.dataTables_length select').addClass('form-control');
	}

	function initTitle(){
		var status = getParameterByName("status");
		if(status == "-1"){
			$("#smallTitle").append("Newly Available");
			$("#normalTitle").append("<h1>Newly Available</h1>");
		}else if(status == "8"){
			$("#smallTitle").append("Tagged");
			$("#normalTitle").append("<h1>Tagged</h1>");
		}else if(status == "9"){
			$("#smallTitle").append("Contracted");
			$("#normalTitle").append("<h1>Contracted</h1>");
		}else if(status == "5"){
			$("#smallTitle").append("On Hold");
			$("#normalTitle").append("<h1>On Hold</h1>");
		}else if(status == "7"){
			$("#smallTitle").append("Ready For Placement");
			$("#normalTitle").append("<h1>Ready For Placement</h1>");
		}else{
			$("#smallTitle").append("All Caregivers");
			$("#normalTitle").append("<h1>All Caregivers</h1>");
		}
	}
	
	function getParameterByName(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
		return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}
	

	function showDialog(sData)
	{
	    $("#dialog-modal").dialog(
	    {
	        width: 500,
	        height: 300,
	        modal: true,
	    });
	     
	    
	    $("#continue_send").attr("onclick", "sendCV('"+sData+"')");
	}

	function closeDialog(){
	    $("#dialog-modal").dialog( "close" );
	    return false;
	}

	function sendCV(sData){

	    var client_email = $('#client_email').val();
	    var email_msg = $('#email_msg').val();

	    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	     
	    if ( client_email == '' ) {
	        alert('Email must be given');
	        return false;
	    }else{
	    	flag = re.test(client_email);
			if(flag){
	            $(".loading").show();
		        $.post("${pageContext.request.contextPath}/admin/ajax/send_client_cv", {"sData":sData, "client_email":client_email, "email_msg":email_msg, "userId":"${user.userId}"}, function(data) {                                
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
	
	function addslashes(str) {
	    str=str.replace(/\\/g,'\\\\');
	    str=str.replace(/\'/g,'\\\'');
	    str=str.replace(/\"/g,'\\"');
	    str=str.replace(/\0/g,'\\0');
	    return str;
	}

	TableTools.BUTTONS.send_cv = {
	    "sAction": "text",
	    "sFieldBoundary": "",
	    "sFieldSeperator": "\t",
	    "sNewLine": ",",
	    "sToolTip": "",
	    "sButtonClass": "DTTT_button_text",
	    "sButtonClassHover": "DTTT_button_text_hover",
	    "sButtonText": "Send CV to Client",
	    "mColumns": [0],
	    "bFooter": true,
	    "sDiv": "",
	    "fnMouseover": null,
	    "fnMouseout": null,
	    "bSelectedOnly": "true",
	    "fnClick": function( nButton, oConfig ) {
	        var sData = this.fnGetTableData(oConfig);
	        showDialog(sData);
	    },
	    "fnSelect": function ( nButton, oConfig, nRow ) {
	        if ( this.fnGetSelected().length == 0 ) {
	            $(nButton).addClass('disabled');
	        }else{
	            $(nButton).removeClass('disabled');
	        }
	    },
	    "fnComplete": null,
	    "fnInit": function ( nButton, oConfig ) {
	        if ( this.fnGetSelected().length == 0 ) {
	            $(nButton).addClass('disabled');
	        }
	    }
	};

	//-------------------------
	// With Table Tools Editor
	//-------------------------

	var editor;

	$(function () {
		$.datepicker.setDefaults( {
			changeYear: true,
			yearRange: '1950:+0',
			buttonImage: 'calendar_1.png',
			dateFormat: 'dd-mm-yyyy'
		} );

	    editor = new $.fn.dataTable.Editor({
	        "ajaxUrl":"${pageContext.request.contextPath}/shortlist/saveCandidate?adminId=${sessionScope.userId}&adminFirstName=${sessionScope.firstName}&adminLastName=${sessionScope.lastName}",
	        "domTable":"#candidateTable",
	        "fields":[
	            {
	                "label":"user_id:",
	                "name":"user_id",
					"type": "hidden"
	            },
	            {
	                "label":"tag_id:",
	                "name":"tag_id",
	                "type": "hidden"
	            },   
	            {
	                "label":"tag_origin:",
	                "name":"tag_origin",
	                "type": "hidden"
	            },  
	            {
	                "label":"tag_status:",
	                "name":"tag_status",
	                "type": "hidden"
	            },  
	            {
	                "label":"resume:",
	                "name":"resume",
					"type": "hidden"
	            },  
	            {
	                "label":"tag_status:",
	                "name":"tag_status",
	                "type": "hidden"
	            },                       
	            {
	                "label":"Status:",
	                "name":"tag",
	                "type": "select",
	                ipOpts: [
	                    { label: "Available", value: "0" },
	                    { label: "Tagged", value: "1" },
	                    { label: "Contracted", value: "2" },
	                    { label: "On hold", value: "3" }
	                ]
	            },
	            {
	                "label":"numbersOfPlacement:",
	                "name":"numbersOfPlacement",
					"type": "hidden"
	            },
	            {
	                "label":"Contracted to:",
	                "name":"contracted_to"
	            },
	            {
	                "label":"Tagged to:",
	                "name":"tagged_to"
	            },
	            {
	                "label":"Date Applied:",
	                "name":"d_applied",
	                type:"date",
	                def: function () { return new Date();},
	                dateFormat: 'dd/mm/yy'
	            },
	            {
	                "label":"Full Name:",
	                "name":"full_name"
	            },                        
	            {
	                "label":"Applicant ID:",
	                "name":"app_id"
	            },
	            {
	                "label":"Email:",
	                "name":"email"
	            },
	            {
	                "label":"Mobile #:",
	                "name":"mobile",
	                "type": "textarea"
	            },
	            {
	                "label":"Video URL:",
	                "name":"video_url"
	            },            
	            {
	                "label":"Work In Singapore?:",
	                "name":"work_in_sg",
	                "type": "select",
	                ipOpts: [
	                    { label: "Yes", value: "Yes" },
	                    { label: "No", value: "No" }
	                ]
	            },
	            {
	                "label":"Work In Hong Kong?:",
	                "name":"work_in_hk",
	                "type": "select",
	                ipOpts: [
	                    { label: "Yes", value: "Yes" },
	                    { label: "No", value: "No" }
	                ]
	            },
	            {
	                "label":"Work In Taiwan?:",
	                "name":"work_in_tw",
	                "type": "select",
	                ipOpts: [
	                    { label: "Yes", value: "Yes" },
	                    { label: "No", value: "No" }
	                ]
	            },
	            {
	                "label":"Skype ID:",
	                "name":"skype"
	            },
				{
	                "label":"Gender:",
	                "name":"gender",
					"type": "select",
					ipOpts: [
	                    { label: "Female", value: "Female" },
						{ label: "Male", value: "Male" }
	                ]
	            },
	            {
	                "label":"Date of Birth:",
	                "name":"d_of_birth",
	                type:"date",
	                def: function () { return new Date(); },
	                dateFormat: 'dd/mm/yy'
	            }, 
	            {
	                "label":"Age:",
	                "name":"age",
	                "type": "hidden"
	            },            
	            {
	                "label":"Place of birth:",
	                "name":"country_of_birth"
	            },
	            {
	                "label":"Nearest Airport:",
	                "name":"nearest_airport",
	                "type": "textarea"
	            },    
	            {
	                "label":"Nationality:",
	                "name":"nationality",
	                "type": "select",
	                ipOpts: [
	                    { label: "Bangladesh", value: "Bangladesh" },
	                    { label: "India", value: "India" },
	                    { label: "Indonesia", value: "Indonesia" },
	                    { label: "Myanmar", value: "Myanmar" },
	                    { label: "Philippines", value: "Philippines" },
	                    { label: "Sri Lanka", value: "Sri Lanka" },
	                    { label: "Others", value: "Others" }
	                ]                
	            },
	            {
	                "label":"Height (cm):",
	                "name":"height"
	            },
	            {
	                "label":"Weight (kg):",
	                "name":"weight"
	            },
	            {
	                "label":"Marital Status:",
	                "name":"marital_status",
	                "type": "select",
	                ipOpts: [
	                    { label: "Single", value: "Single" },
	                    { label: "Married", value: "Married" },
	                    { label: "Divorced/Seperated", value: "Divorced/Seperated" },
	                    { label: "Widowed", value: "Widowed" }
	                ]
	            },
	            {
	                "label":"Has children:",
	                "name":"has_children",
	                "type": "select",
	                ipOpts: [
	                    { label: "No Answer", value: "No Answer" },
	                    { label: "No Children", value: "No Children" },
	                    { label: "Has Children", value: "Has Children" },
	                    { label: "1 Child", value: "1 Child" },
	                    { label: "2 Children", value: "2 Children" },
	                    { label: "3 Children", value: "3 Children" },
	                    { label: "4 Children", value: "4 Children" },
	                    { label: "5 Children", value: "5 Children" },
	                    { label: "6 Children", value: "6 Children" },
	                    { label: "7 Children", value: "7 Children" },
	                    { label: "8 Children", value: "8 Children" }
	                ]
	            },
	            {
	                "label":"Children Names:",
	                "name":"children_names",
	                "type": "textarea"
	            },            
	            {
	                "label":"Siblings?:",
	                "name":"siblings",
	                "type": "textarea"
	            },            
	            {
	                "label":"Languages Spoken:",
	                "name":"languages",
	                "type": "checkbox",
	                "separator": ",",
	                ipOpts: [
	                    { label: "Arabic", value: "Arabic" },
	                    { label: "Bengali", value: "Bengali" },
	                    { label: "Burmese", value: "Burmese" },
	                    { label: "Cantonese", value: "Cantonese" },
	                    { label: "English", value: "English" },
	                    { label: "Hakka", value: "Hakka" },
	                    { label: "Hindi", value: "Hindi" },
	                    { label: "Hokkien", value: "Hokkien" },
	                    { label: "Malay", value: "Malay" },
	                    { label: "Malayalam", value: "Malayalam" },
	                    { label: "Mandarin Chinese", value: "Mandarin Chinese" },
	                    { label: "Tagalog", value: "Tagalog" },
	                    { label: "Tamil", value: "Tamil" },
	                    { label: "Telugu", value: "Telugu" },
	                    { label: "Teochew", value: "Teochew" },
	                    { label: "Sinhalese", value: "Sinhalese" },
	                    { label: "Others", value: "Others" }
	                ]
	            },
	            {
	                "label":"Religion:",
	                "name":"religion",
	                "type": "select",
	                ipOpts: [
	                    { label: "Christian", value: "Christian" },
	                    { label: "Buddhist", value: "Buddhist" },
	                    { label: "Hindu", value: "Hindu" },
	                    { label: "Muslim", value: "Muslim" },
	                    { label: "Sikh", value: "Sikh" },
	                    { label: "Others", value: "Others" }
	                ]
	            },
	            {
	                "label":"Food choice:",
	                "name":"food_choice"
	            },
				{
	                "label":"Education level:",
	                "name":"educational_level",
					"type": "select",
					ipOpts: [
	                    { label: "Caregiver Certificate", value: "Caregiver Certificate" },
	                    { label: "Nurse Assistant Diploma", value: "Nurse Assistant Diploma" },
						{ label: "Nurse Diploma", value: "Nurse Diploma" },
						{ label: "Bachelor of Sciences in Nursing or higher", value: "Bachelor of Sciences in Nursing or higher" }
	                ]				
	            },
	            {
	                "label":"Years of experience:",
	                "name":"exp"
	            },            
	            {
	                "label":"Are you Trained to CPR / First Aid?:",
	                "name":"certified_cpr",
	                "type": "select",
	                ipOpts: [
	                    { label: "Yes", value: "Yes" },
	                    { label: "No", value: "No" }
	                ]
	            },
	            {
	                "label":"Nursing experience:",
	                "name":"specialities",
	                "type": "checkbox",
	                "separator": ",",
	                ipOpts: [
	                    { label: "Bed sores", value: "Bed sores" },
	                    { label: "Cancer", value: "Cancer" },
	                    { label: "Diabetes patients (including administering insulin)", value: "Diabetes patients (including administering insulin)" },
	                    { label: "Dementia / Alzheimer's", value: "Dementia / Alzheimer's" },
	                    { label: "Geriatric", value: "Geriatric" },
	                    { label: "General wound care", value: "General wound care" },
	                    { label: "New-born babies (healthy)", value: "New-born babies (healthy)" },
	                    { label: "New-born babies (with medical problems)", value: "New-born babies (with medical problems)" },
	                    { label: "NGT (nasogastric tube feeding)", value: "NGT (nasogastric tube feeding)" },
	                    { label: "Paediatric", value: "Paediatric" },
	                    { label: "PEG (percutaneous endoscopic gastrostomy feeding tube)", value: "PEG (percutaneous endoscopic gastrostomy feeding tube)" },
	                    { label: "Psychiatric/Special needs patients", value: "Psychiatric/Special needs patients" },
	                    { label: "Simple cases only", value: "Simple cases only" },
	                    { label: "Suctioning of airways", value: "Suctioning of airways" },
	                    { label: "Stoma care", value: "Stoma care" },
	                    { label: "Stroke patients", value: "Stroke patients" },
	                    { label: "Tracheostomy care", value: "Tracheostomy care" },
	                    { label: "Urinary catheter", value: "Urinary catheter" },
	                    { label: "Chemotherapy", value: "Chemotherapy" },
	                    { label: "Dialysis", value: "Dialysis" },
	                    { label: "Respiratory Therapy", value: "Respiratory Therapy" }
	                ]                
	            },
	            {
	                "label":"Availability:",
	                "name":"availability",
	                "type": "select",
	                ipOpts: [
	                    { label: "Immediate", value: "Immediate" },
	                    { label: "2-3 weeks", value: "2-3 weeks" },
	                    { label: "1 month", value: "1 month" }
	                ]
	            },
	            {
	                "label":"Motivation:",
	                "name":"motivation",
	                "type": "textarea"
	            },
	            {
	                "label":"About:",
	                "name":"about",
	                "type": "textarea"
	            },
	            {
	                "label":"Education and experience:",
	                "name":"education",
	                "type": "textarea"
	            },
	            {
	                "label":"Hobbies:",
	                "name":"hobbies",
	                "type": "textarea"
	            },
	            {
	                "label":"Worked In Singapore Before?:",
	                "name":"worked_in_sg",
	                "type": "select",
	                ipOpts: [
	                    { label: "Yes", value: "Yes" },
	                    { label: "No", value: "No" }
	                ]
	            },            
	            {
	                "label":"Residential Address in Home Country:",
	                "name":"current_address",
	                "type": "textarea"
	            },            
	            {
	                "label":"Allergies:",
	                "name":"allergies",
	                "type": "textarea"
	            },
	            {
	                "label":"Diagnosed Conditions:",
	                "name":"diagnosed_conditions",
	                "type": "checkbox",
	                "separator": ",",
	                ipOpts: [
	                    { label: "Mental Illness", value: "Mental Illness" },
	                    { label: "Epilepsy", value: "Epilepsy" },
	                    { label: "Asthma", value: "Asthma" },
	                    { label: "Diabetes", value: "Diabetes" },
	                    { label: "Hypertension", value: "Hypertension" },
	                    { label: "Tuberculosis", value: "Tuberculosis" },
	                    { label: "Heart disease", value: "Heart disease" },
	                    { label: "Malaria", value: "Malaria" },
	                    { label: "Operations", value: "Operations" }
	                ]                
	            }
                <sec:authorize access="hasRole('ROLE_ADMIN')">
	            ,
	            {
	            	"label" : "Expected salary in HKD:",
	            	"name" : "salary_hkd",
	            	"type" : "textarea"
	            },
	            {
	            	"label" : "Expected salary in SGD:",
	            	"name" : "salary_sgd",
	            	"type" : "textarea"
	            },
	            {
	            	"label" : "Expected salary in TW:",
	            	"name" : "salary_twd",
	            	"type" : "textarea"
	            }
	            </sec:authorize>
	            
	         ]
	    });
	    
	    editor.on( 'onInitCreate', function () {
	        status_tag = editor.get('tag');
	        if(status_tag == null){
	            editor.hide('tagged_to');        
	            editor.hide('contracted_to');        
	        }else if(status_tag == 1){
	            editor.show('tagged_to');
	            editor.hide('contracted_to');
	        }else if(status_tag == 2){
	            editor.show('contracted_to');
	            editor.hide('tagged_to');
	        }else if(status_tag == 3){
	            editor.hide('tagged_to');        
	            editor.hide('contracted_to');   
	        }
	        
        	editor.enable('work_in_sg');
        	editor.enable('work_in_hk');
        	editor.enable('work_in_tw');
	     } 
	    );
	    
	    $( 'select', editor.node( 'tag' ) ).on( 'change', function () {
	        status_tag = editor.get('tag');
	        if(status_tag == 1){
	            editor.show('tagged_to');
	            editor.hide('contracted_to');
	        }else if(status_tag == 2){
	            editor.show('contracted_to');
	            editor.hide('tagged_to');
	        }else{
	            editor.hide('tagged_to');
	            editor.hide('contracted_to');
	        }        
	    });


		editor.on( 'onInitEdit', function () {
	        
	        var tag_id;

	        tag_id = '';
	        
	        status_tag = editor.get('tag');
	        if(status_tag == 0){
	            editor.hide('tagged_to');
	            editor.hide('contracted_to');
	        }else if(status_tag == 1){
	            editor.show('tagged_to');
	            editor.hide('contracted_to');
	        }else if(status_tag == 2){
	            editor.show('contracted_to');
	            editor.hide('tagged_to');
	        }else if(status_tag == 3){
	            editor.hide('tagged_to');
	            editor.hide('contracted_to');
	        }

	        tag_id = editor.get('tag_id');
	        
	        var session_user_id = 2;  
	        var role = 1;  
	        
	        if(role == 1 || role == 3){
	            editor.enable('tag');
	            editor.enable('tagged_to');
	            editor.enable('contracted_to');  
	        }else{
	            if(tag_id != session_user_id && tag_id != 0){
	                editor.disable('tag');
	                editor.disable('tagged_to');
	                editor.disable('contracted_to');
	            }else{
	                editor.enable('tag');
	                editor.enable('tagged_to');
	                editor.enable('contracted_to');              
	            }
	        }
	       
	        if("${user.role}" == "ROLE_SALES" || "${user.role}" == "ROLE_SALES_SG" || "${user.role}" == "ROLE_SALES_HK" || "${user.role}" == "ROLE_SALES_TW"){
	        	editor.disable('work_in_sg');
	        	editor.disable('work_in_hk');
	        	editor.disable('work_in_tw');
	        }
	      
		} );		
		
	    editor.on( 'onPreSubmit', function ( e, o ) {
	        if(o.action == 'remove'){
	            return true;
	        }else{ 

	            presub = this;

	            var result = "";
	            var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	               
	            if ( o.data.email === '' ) {
	                presub.error('email', 'Email must be given');
	                return false;
	            }else if(!re.test(o.data.email)){
	                presub.error('email', 'Email format is incorrect');
	                return false;
	            }else{
	            	$.ajax({
	        			type:"get",
	        			async: false,
	        			url:"${pageContext.request.contextPath}/shortlist/emailVerify?candidateId=" + o.data.user_id + "&email=" + o.data.email,
	        			success:function(data){
	        				emailVerify = JSON.parse(data).emailVerify;
	        			}
	        		});
	            	if(!emailVerify){
	            		presub.error('email', 'The email has been used');
	            		return false;
	            	}
	            }
	            


	            if ( o.data.full_name === '' ) {
	            	presub.error('full_name', 'Full name must be given');
	                return false;
	            } 

	            if ( o.data.exp === '' ) {
	            	presub.error('exp', 'Years of experience must be given');
	                return false;
	            }

	            if ( o.data.mobile === '' ) {
	            	presub.error('mobile', 'Mobile must be given');
	                return false;
	            }
	            
	            if( o.data.work_in_hk === null ){
	            	presub.error('work_in_hk', 'work in hk or not must be given');
	            	return false;
	            }
	            
	            if( o.data.work_in_sg === null){
	            	presub.error('work_in_sg', 'work in sg or not must be given');
	            	return false;
	            }
	            
	            if( o.data.work_in_tw === null){
	            	presub.error('work_in_tw', 'work in tw or not must be given');
	            	return false;
	            }

	            if( o.data.worked_in_sg === null){
	            	presub.error('worked_in_sg', 'worked in sg or not must be given');
	            	return false;
	            }
	            
	            if( o.data.certified_cpr === null){
	            	presub.error('certified_cpr', 'have a cpr or not must be given');
	            	return false;
	            }
	            
	            if ( o.data.d_of_birth === '' ) {
	            	presub.error('d_of_birth', 'Date of birth must be given');
	                return false;
	            }

	            if ( o.data.d_applied === '' ) {
	            	presub.error('d_applied', 'Date applied must be given');
	                return false;
	            }            
	        }         
	    }); 
	    
	    editor.on( 'onSubmitSuccess', function () {
	        location.reload();
	    } );    

	    if(getParameterByName('shortlist') == ''){
	        $('input:checkbox').removeAttr('checked'); 
	        initTable('');
	    }else if(!$("input[type='checkbox']").is(":checked")){  
	        $('input:checkbox').removeAttr('checked'); 
	        initTable('');
	    }else{
	        medicalsp = ''; 
	        languagesp = ''; 
	        religionsp = ''; 
	        levelsp = ''; 
	        foodsp = '';
	        maritalsp = ''; 
	        gendersp = ''; 
	        availsp = ''; 
	        agesp = ''; 
	        expsp = ''; 
	        childrensp = ''; 

	        var data =  '{"medical": "'+medicalsp+'", "language": "'+languagesp+'", "religion": "'+religionsp+'", "level": "'+levelsp+'", "food": "'+foodsp+'", "marital": "'+maritalsp+'", "gender": "'+gendersp+'", "avail": "'+availsp+'", "age": "'+agesp+'", "children": "'+childrensp+'", "exp": "'+expsp+'"}';
	        
	        initTable(data);
	    }
	    //add icons
	    $("#ToolTables_candidateTable_0").prepend('<i class="fa fa-plus"/> ');
	    $("#ToolTables_candidateTable_1").prepend('<i class="fa fa-pencil-square-o"/> ');
	    $("#ToolTables_candidateTable_2").prepend('<i class="fa fa-times-circle"/> ');
	    
	});

	$(document).ready(function() {
		$.fn.dataTableExt.oApi.fnPagingInfo = function(oSettings) {
			return {
				"iStart" : oSettings._iDisplayStart,
				"iEnd" : oSettings.fnDisplayEnd(),
				"iLength" : oSettings._iDisplayLength,
				"iTotal" : oSettings.fnRecordsTotal(),
				"iFilteredTotal" : oSettings.fnRecordsDisplay(),
				"iPage" : Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
				"iTotalPages" : Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength)
			};
		}
		$('.btn-default').click(function() {
			setTimeout(function() {
				var oldSrc = '../media/images/calender.png';
				var newSrc = '${pageContext.request.contextPath}/resources/img/icon-calendar.png';
				$('.ui-datepicker-trigger').attr('src',newSrc);
			}, 700);

		});
		
	});
	
</script>
