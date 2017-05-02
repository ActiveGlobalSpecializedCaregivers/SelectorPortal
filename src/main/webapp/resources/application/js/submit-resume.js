$(document).ready(function() {
	window.dv_language || (window.dv_language = 'en_us');

	curLanguage = translationsObj[window.dv_language];
	
	if (typeof DV_RESPONSIVE_LAYOUT !== 'undefined' && DV_RESPONSIVE_LAYOUT) {
		var $window         = $(window),
			$body           = $('body'),
			$pageHeader     = $('.page-header'),
			$welcomeChevron = $pageHeader.find('.fa-chevron-down'),
			$jobDescription = $('.job-description'),
			$backButton     = $('#applicant-back-button'),
			$wmyuValue      = $('#applicant-wmyu-value'),
			$wmyuCount      = $('#applicant-wmyu-count'),
			$detectMobile   = $('#applicant-detect-mobile');

		function toggleWelcomeMessage() {
			if ($pageHeader.css('cursor') == 'pointer') {
				$body.toggleClass('welcome-message-open');
				$welcomeChevron.toggleClass('fa-flip-vertical');
				return false;
			}
		}

		function detectMobile() {
			if ($window.width() > 480) {
				$detectMobile.val(0);
			} else {
				$detectMobile.val(1);
			}
		}

		$pageHeader.click(function() {
			return toggleWelcomeMessage();
		});

		// detect browser width
		// enables server to apply correct body class when server-side errors are thrown
		detectMobile();
		$window.resize(detectMobile);

		if ($jobDescription.length) {
			// job position view
			var height    = $jobDescription.height(),
				$readMore = $('#read-more-description');

			if (height > 180) {
				$jobDescription.addClass('summary');

				$readMore.one('click', function() {
					$jobDescription.animate({
						height: height + 20
					}, 100);

					$readMore.slideUp(100);

					return false;
				});
			}

			$('#applicant-mobile-apply-button').click(function() {
				$body.addClass('applying');
			});

			$backButton.click(function() {
				if ($body.hasClass('applying')) {
					// if we are applying, back button should reset jobs view
					$body.removeClass('applying');
					window.scrollTo(0, 0);
					return false;
				}
			});

			if ($wmyuValue.length) {
				$wmyuValue.on('input propertychange', function() {
					var value = $wmyuValue.val(),
						diff  = 150 - value.length;

					$wmyuCount.text(diff);

					if (diff <= 20) {
						$wmyuCount.addClass('danger');
					} else if (diff <= 40) {
						$wmyuCount.removeClass('danger').addClass('warning');
					} else {
						$wmyuCount.removeClass('danger warning');
					}

					// support browsers that don't implement the maxlength attribute
					if (value.length > 150) {
						$wmyuValue.val(value.slice(0, 150));
					}
				});
			}
		}
	} else {
		// Set the WMYU field character count
		if(dv_language == 'es_us'){  
			$('#applicant-wmyu-value').textareaCount({
				'maxCharacterSize': 150, 
			    'warningNumber': 40,
			    'displayFormat': '#left Letras'
			}); 
		}else if(dv_language == 'nl_nl'){  
			$('#applicant-wmyu-value').textareaCount({
				'maxCharacterSize': 150, 
			    'warningNumber': 40,
			    'displayFormat': '#left Tekens'
			}); 
		}else{
			$('#applicant-wmyu-value').textareaCount({
				'maxCharacterSize': 150, 
			    'warningNumber': 40,
			    'displayFormat': '#left Characters Left'
			}); 
		}
	}
	
	// Set the start date datepicker
	// moved to view.jobboard_deault.php
	$('.applicant-datepicker').datepicker({dateFormat: "yy-mm-dd"});
	
	// Manage resume upload options
	$('#applicant-choose-upload').click(function(){
		$('#applicant-resume-upload-wrapper').removeClass('none');
		$('#applicant-resume-options').hide();
		return false;
	});
	
	$('#applicant-choose-paste').click(function(){
		$('#applicant-resume-paste-wrapper').removeClass('none');
		$('#applicant-resume-options').hide();
		return false;
	});
	
	$('#applicant-resume-switch-paste').click(function(){
		$('#applicant-resume-paste-wrapper').removeClass('none');
		$("#applicant-resume-value").attr('value','');
		$('#applicant-resume-upload-wrapper').addClass('none');
		return false;
	});
	
	$('#applicant-resume-switch-upload').click(function(){
		$('#applicant-resume-paste-wrapper').addClass('none');
		$("#applicant-resumetext-value").attr('value','');
		$('#applicant-resume-upload-wrapper').removeClass('none');
		return false;
	});
	
	// Submit resume form
	$("#applicant-submit-resume, #applicant-submit-resume-button").click(function(){
		$(".loading").show();
		send_form(this);
		return false;
	});
	
	// Count the WMYU length
	$('#applicant-wmyu-value').keyup(function() {
		var len = this.value.length;
	    if (len >= 150) {
	        this.value = this.value.substring(0, 150);
	    }
	});
	
	$('#applicant-optional-fields-link').clickr({
		'#applicant-optional-fields':'slideDown',
		'#applicant-show-optional-fields':'hide'
	});


	// Show tooltip for IE11 and its over-intense security settings
	if (!!navigator.userAgent.match(/Trident.*rv:11./)) {
		$('#ie11-warning').css('display', 'block');
	}

	// For Apply With LinkedIn Widget
	window.INFrame = $('#res_apply_with_linkedin_frame');
	
});

var translationsObj = {
	"es_us":  {
		submitStatus: 'Presentación de la solicitud',
		alertMessage: 'Por favor corrige los errores resaltados e inténtalo de nuevo.',
		//resumeLabel: 'Attach resume ( .pdf, .doc, .docx)',
		firstNameLabel: 'Introduce tu nombre',
		lastNameLabel: 'Introduce tus apellidos',
		emailLabel: 'Introduce una dirección de correo válida',
		cityLabel: 'Introduce tu ciudad',
		stateLabel: 'Introduce tu estado',
		postalLabel: 'Introduce postal',
		phoneLabel: 'Introduce tu número de teléfono',
		errorLabel: 'Introducir texto',
		birthLabel:'Enter birth date',
		birthPlaceLabel:'Enter birth place',
		nearestAirpotrLabel:'Enter airport nearest to you',
		statusLabel:"Choose YES or NO",
		applyLocationsLabel:"Tick the locations",
		genderLabel:"Choose your gender",
		citizenshipLabel:"Choose your country of citizenship",
		residentialAddressLabel:"Enter your current residential address",
		heightLabel:"Enter correct height for number",
		weightLabel:"Enter correct weight for number",
		maritalLabel:"Choose your country of marital status",
		childLabel:"Choose your correct answer",
		religionLabel:"Choose your religion",
		dietLabel:"Choose your diet",
		passportLabel:"Choose YES or NO",
		nursingSchoolLabel:"Enter correct answer",
		graduationLabel:"Enter year of graduation for number",
		nursingDegreeLabel:"Choose your Nursing degree",
		nurseExperienceLabel:"Enter your correct for number",
		certifiedAidLabel:"Choose YES or NO",
		workedBeforeSingaporeLabel:"Choose YES or NO",
		careCategoryLabel:"Tick at least one",
		hobbyLabel:"Enter your hobbies",
		studiesLabel:"Enter correct for number",
		photoLabel:"Upload your photo",
		diagnosed_conditionsLabel:"Tick at least one",
		languagesLabel:"Tick at least one",
		tesdaNciiLabel:"Choose YES/NO"
	},
	"nl_nl":  {
		submitStatus: 'Indienen van ... even geduld aub ...',
		alertMessage: 'Corrigeer de fouten gevonden met de applicatie.',
		//resumeLabel: 'Attach resume ( .pdf, .doc, .docx)',
		firstNameLabel: 'Voer de voornaam',
		lastNameLabel: 'Voer de achternaam',
		emailLabel: 'Voer geldige email adress',
		cityLabel: 'voer',
		stateLabel: 'Voer State',
		postalLabel: 'Voer postcode',
		phoneLabel: 'Voer het telefoonnummer',
		errorLabel: 'Voer tekst',
		birthLabel:'Enter birth date',
		birthPlaceLabel:'Enter birth place',
		nearestAirpotrLabel:'Enter airport nearest to you',
		statusLabel:"Choose YES or NO",
		applyLocationsLabel:"Tick the locations",
		genderLabel:"Choose your gender",
		citizenshipLabel:"Choose your country of citizenship",
		residentialAddressLabel:"Enter your current residential address",
		heightLabel:"Enter correct height for number",
		weightLabel:"Enter correct weight for number",
		maritalLabel:"Choose your country of marital status",
		childLabel:"Choose your correct answer",
		religionLabel:"Choose your religion",
		dietLabel:"Choose your diet",
		passportLabel:"Choose YES or NO",
		nursingSchoolLabel:"Enter correct answer",
		graduationLabel:"Enter year of graduation for number",
		nursingDegreeLabel:"Choose your Nursing degree",
		nurseExperienceLabel:"Enter your correct for number",
		certifiedAidLabel:"Choose YES or NO",
		workedBeforeSingaporeLabel:"Choose YES or NO",
		careCategoryLabel:"Tick at least one",
		hobbyLabel:"Enter your hobbies",
		studiesLabel:"Enter correct for number",
		photoLabel:"Upload your photo",
		diagnosed_conditionsLabel:"Tick at least one",
		languagesLabel:"Tick at least one",
		tesdaNciiLabel:"Choose YES/NO"
	},
	"en_us":  {
		submitStatus: 'Submitting...please wait...',
		alertMessage: 'Please correct the errors found with the application.',
		//resumeLabel: 'Attach resume ( .pdf)',
		firstNameLabel: 'Enter first name',
		lastNameLabel: 'Enter last name',
		emailLabel: 'Enter valid email address; eg:asd231@outlook.com',
		cityLabel: 'Enter City',
		stateLabel: 'Enter State',
		postalLabel: 'Enter postal code',
		phoneLabel: 'Enter phone number',
		errorLabel: 'Enter text',
		birthLabel:'Enter birth date',
		birthPlaceLabel:'Enter birth place',
		nearestAirpotrLabel:'Enter airport nearest to you',
		statusLabel:"Choose YES or NO",
		applyLocationsLabel:"Tick the locations",
		genderLabel:"Choose your gender",
		citizenshipLabel:"Choose your country of citizenship",
		residentialAddressLabel:"Enter your current residential address",
		heightLabel:"Enter correct height for number",
		weightLabel:"Enter correct weight for number",
		maritalLabel:"Choose your country of marital status",
		childLabel:"Choose your correct answer",
		religionLabel:"Choose your religion",
		dietLabel:"Choose your diet",
		passportLabel:"Choose YES or NO",
		nursingSchoolLabel:"Enter correct answer",
		graduationLabel:"Enter year of graduation for number",
		nursingDegreeLabel:"Choose your Nursing degree",
		nurseExperienceLabel:"Enter your correct for number",
		certifiedAidLabel:"Choose YES or NO",
		workedBeforeSingaporeLabel:"Choose YES or NO",
		careCategoryLabel:"Tick at least one",
		hobbyLabel:"Enter your hobbies",
		studiesLabel:"Enter correct for number",
		photoLabel:"Upload your photo",
		diagnosed_conditionsLabel:"Tick at least one",
		languagesLabel:"Tick at least one",
		tesdaNciiLabel:"Choose YES/NO"
	}
};

var applicant_errors, curLanguage;

function send_form(btn){
	checkAddress();

    // remove all previous error messages
    $(".applicant_label_error").remove();
    
	if( check_submission_form() ){
		$("#applicant-cancel-resume").remove();

			$("#applicant-manual-upload,#applicant-submit").html(curLanguage.submitStatus);
		
		$('.applicant-questionnaire-checkbox').each(function() {
			var box = $(this);
			var input = box.val()+'-||-';
			var parent = (typeof DV_RESPONSIVE_LAYOUT !== 'undefined' && DV_RESPONSIVE_LAYOUT) ? '.form-group' : '.applicant-input';
			var hidden_field = box.closest(parent).find('.applicant-questionnaire-checkbox-answer');
			if( box.is(':checked') ){
				hidden_field.val( hidden_field.val() + input );
			}else{
				hidden_field.val( hidden_field.val().replace(input,'') );
			}
		});
		$("#form_submit_new_resume").submit();
	}else{
		 $(".loading").hide();
		return false;
	}
}

function check_submission_form(){
	if (typeof DV_RESPONSIVE_LAYOUT !== 'undefined' && DV_RESPONSIVE_LAYOUT) {
		var applicantErrors = false,
			scroll          = true,
			emailRegex      = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;

		$('.control-label').each(function() {
			var $label = $(this);

			if ($label.find('.asterisk').length) {
				var $formGroup = $label.closest('.form-group'),
					$input 	   = $formGroup.find('.form-control, .applicant-file-field'),
					error      = true;

				switch ($label.attr('id')) {
					case 'applicant-resume-label':
						error = $.trim($("#applicant-resume-value").val()) == '' && $.trim($("#applicant-resumetext-value").val()) == '';
						break;
					case 'applicant-address-label':
						$input.each(function() {
							error = error && $.trim($(this).val()) == '';
						});
						break;
					case 'applicant-email-label':
						var value = $.trim($input.val());
						error = value == '' || !emailRegex.test(value);
						break;
					case 'applicant-phone-label':
						var value = $input.val().replace(/[^0-9]/g, '');
						error = value.length < 7;
						break;
					default:
						if ($input.length !== 0) {
							error = $.trim($input.val()) == '';
						} else {
							// case for required custom questionnaire checkboxes
							error = $formGroup.find('.applicant-questionnaire-checkbox:checked').length == 0;
						}
						break;
				}

				if (error) {
					$formGroup.addClass('has-error');
					applicantErrors = true;

					if (scroll) {
						// scroll to first invalid field
						$('html, body').animate({
							scrollTop: $formGroup.offset().top - 20
						}, 200);
						scroll = false;
					}
				} else {
					$formGroup.removeClass('has-error');
					applicantErrors = applicantErrors || false;
				}
			}
		});
		
		// special, ugly case for EEOC disability status
		if ($("[name='applicant-eeoc_disability-value']:checked").val() > 0) {
			var $disabilitySignature = $('#disability-signature-area');

			if ($('#applicant-eeoc_disability_signature-value').val().trim() == '' || $('#applicant-eeoc_disability_date-value').val().trim() == '') {
				$disabilitySignature.addClass('has-error');
				applicantErrors = true;
			}

			if (scroll) {
				$('html, body').animate({
					scrollTop: $disabilitySignature.offset().top - 20
				}, 200);
			}
		}

		if (applicantErrors) {
			return false;
		} else {
			return true;
		}
	} else {
		applicant_errors = false;
		applicant_firstname = $("#applicant-firstname-value").attr("value");
		applicant_lastname = $("#applicant-lastname-value").attr("value");
		applicant_email = $("#applicant-email-value").attr("value");
		applicant_city = $("#applicant-city-value").attr("value");
		applicant_state = $("#applicant-state-value").attr("value");
		applicant_postal = $("#applicant-postal-value").attr("value"); 
		applicant_phone = $("#applicant-phone-value").attr("value");
		applicant_address = $("#applicant-address-value").attr("value");
		applicant_file = $("#applicant-resume-value").attr("value");
		resume_text = $("#applicant-resumetext-value").attr("value");
		resume_manual = $("#manual").attr("value");
		eeoc_disability = $("#applicant-eeoc_disability-value:checked").attr("value");
		eeoc_signature = $("#applicant-eeoc_disability_signature-value").attr("value");
		eeoc_date = $("#applicant-eeoc_disability_date-value").attr("value");
		
		applicant_birth = $("#applicant-birth-value").attr("value");
		applicant_birthPlace = $("#applicant-birthPlace-value").attr("value");
		applicant_nearestAirpotr = $("#applicant-nearestAirpotr-value").attr("value");
		applicant_residentialAddress = $("#applicant-residentialAddress-value").attr("value");
		applicant_height = $("#applicant-height-value").attr("value");
		applicant_weight = $("#applicant-weight-value").attr("value");
		applicant_nursingSchool = $("#applicant-nursingSchool-value").attr("value");
		applicant_graduation = $("#applicant-graduation-value").attr("value");
		applicant_studies = $("#applicant-studies-value").attr("value");
		applicant_nurseExperience = $("#applicant-nurseExperience-value").attr("value");	
		applicant_hobby = $("#applicant-hobby-value").attr("value");
		applicant_photo = $("#applicant-photo-value").attr("value");
		photo = document.getElementById("applicant-photo-value");
		/*if(applicant_photo != ""){
			applicant_photo_size = photo.files[0].size;
		}*/
		
        var resume_html = $('#applicant-resume-label').html();
        var resume_required = (resume_html !== null && resume_html.indexOf('*') !== -1);
        
        /*if( applicant_file !='' && !/\.(pdf|doc|docx)$/.test(applicant_file)){
			add_error("#applicant-resume-label", curLanguage.resumeLabel);
			window.location.hash = "#applicant-resume-label";
		}*/
        
        /*if( applicant_hobby == "" ){
			add_error("#applicant-hobby-label", curLanguage.hobbyLabel);
			window.location.hash = "#applicant-hobby-label";
		}*/
        if( applicant_nursingSchool == "" ){
			add_error("#applicant-nursingSchool-label", curLanguage.nursingSchoolLabel);
				window.location.hash = "#applicant-nursingSchool-label";
		}
        if(applicant_nurseExperience == "" || isNaN(applicant_nurseExperience)){
        	add_error("#applicant-nurseExperience-label", curLanguage.nurseExperienceLabel);
				window.location.hash = "#applicant-nurseExperience-label";
        }
        if(applicant_studies == "" || isNaN(applicant_studies)){
        	add_error("#applicant-studies-label", curLanguage.studiesLabel);
				window.location.hash = "#applicant-studies-label";
        }
        if(applicant_graduation == "" || isNaN(applicant_graduation)){
        	add_error("#applicant-graduation-label", curLanguage.graduationLabel);
				window.location.hash = "#applicant-graduation-label";
        }
        if(applicant_weight == "" || isNaN(applicant_weight)){
        	add_error("#applicant-weight-label", curLanguage.weightLabel);
				window.location.hash = "#applicant-weight-label";
        }
        if(applicant_height == "" || isNaN(applicant_height)){
        	add_error("#applicant-height-label", curLanguage.heightLabel);
				window.location.hash = "#applicant-height-label";
        }
        if(applicant_residentialAddress == ""){
        	add_error("#applicant-residentialAddress-label", curLanguage.residentialAddressLabel);
				window.location.hash = "#applicant-residentialAddress-label";
        }
        if($("#applicant-workedBeforeSingapore-value").val() == "applicant_no_selection"){
        	add_error("#applicant-workedBeforeSingapore-label", curLanguage.workedBeforeSingaporeLabel);
				window.location.hash = "#applicant-workedBeforeSingapore-label";
        }
        if($("#applicant-certifiedAid-value").val() == "applicant_no_selection"){
        	add_error("#applicant-certifiedAid-label", curLanguage.certifiedAidLabel);
				window.location.hash = "#applicant-certifiedAid-label";
        }
        if($("#applicant-nursingDegree-value").val() == "applicant_no_selection"){
        	add_error("#applicant-nursingDegree-label", curLanguage.nursingDegreeLabel);
				window.location.hash = "#applicant-nursingDegree-label";
        }
        if($("#applicant-citizenship-value").val() == "applicant_no_selection"){
        	add_error("#applicant-citizenship-label", curLanguage.citizenshipLabel);
				window.location.hash = "#applicant-citizenship-label";
        }
        if($("#applicant-marital-value").val() == "applicant_no_selection"){
        	add_error("#applicant-marital-label", curLanguage.maritalLabel);
				window.location.hash = "#applicant-marital-label";
        }
        if($("#applicant-gender-value").val() == "applicant_no_selection"){
        	add_error("#applicant-gender-label", curLanguage.genderLabel);
				window.location.hash = "#applicant-gender-label";
        }
        if($("#applicant-child-value").val() == "applicant_no_selection"){
        	add_error("#applicant-child-label", curLanguage.childLabel);
				window.location.hash = "#applicant-child-label";
        }
    	if($("#applicant-religion-value").val() == "applicant_no_selection"){
        	add_error("#applicant-religion-label", curLanguage.religionLabel);
				window.location.hash = "#applicant-religion-label";
        }
        if($("#applicant-diet-value").val() == "applicant_no_selection"){
        	add_error("#applicant-diet-label", curLanguage.dietLabel);
				window.location.hash = "#applicant-diet-label";
        }
        if($("#applicant-passport-value").val() == "applicant_no_selection"){
        	add_error("#applicant-passport-label", curLanguage.passportLabel);
				window.location.hash = "#applicant-passport-label";
        }
        if($("input[name=specialties]:checked").length < 1){
        	add_error("#applicant-careCategory-label", curLanguage.careCategoryLabel);
				window.location.hash = "#applicant-careCategory-label";
    	}
        if($("input[name=applicant_status]:checked").length < 1){
        	add_error("#applicant-status-label", curLanguage.statusLabel);
				window.location.hash = "#applicant-status-label";
    	}
        if($("input[name=applyLocations]:checked").length < 1){
        	add_error("#applicant-applyLocations-label", curLanguage.applyLocationsLabel);
				window.location.hash = "#applicant-applyLocations-label";
        }
        /*if($("input[name=diagnosed_conditions]:checked").length < 1){
        	add_error("#applicant-diagnosed_conditions-label", curLanguage.diagnosed_conditionsLabel);
				window.location.hash = "#applicant-diagnosed_conditions-label";
        }*/
        if($("input[name=languages]:checked").length < 1){
        	add_error("#applicant-languages-label", curLanguage.languagesLabel);
				window.location.hash = "#applicant-languages-label";
        }
		/*if(applicant_photo == "" ){
			add_error("#applicant-photo-label", curLanguage.photoLabel);
				window.location.hash = "#applicant-photo-label";
		}else if(!/\.(jpg|jpeg|png|JPG|PNG)$/.test(applicant_photo)){
			add_error("#applicant-photo-label", "jpg/jpeg/png format only please");
				window.location.hash = "#applicant-photo-label";
		}else if(applicant_photo_size > 5*1024*1024){
			add_error("#applicant-photo-label", "The size of this file can not exceed 5MB!");
				window.location.hash = "#applicant-photo-label";
		}*/
		if( applicant_firstname == "" ){
			add_error("#applicant-firstname-label", curLanguage.firstNameLabel);
				window.location.hash = "#applicant-firstname-label";
		}
		if( applicant_lastname == "" ){
			add_error("#applicant-lastname-label", curLanguage.lastNameLabel);
				window.location.hash = "#applicant-lastname-label";
		}
		
		var checkEmail = $("#checkEmail").val();
		if(checkEmail != null && checkEmail != "" && checkEmail != 'The mailbox can be used.'){
			add_error("#applicant-email-label", checkEmail);
			window.location.hash = "#applicant-email-label";
		}else if(checkEmail == null || checkEmail == ''){
			var Regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			if(!Regex.test(applicant_email)){
				add_error("#applicant-email-label", curLanguage.emailLabel);
					window.location.hash = "#applicant-email-label";
			}
		}
		
	    /*var Regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		if(!Regex.test(applicant_email)){
			add_error("#applicant-email-label", curLanguage.emailLabel);
				window.location.hash = "#applicant-email-label";
		}*/
		
		if( DV_ADDRESS_MANDATORY ){
			if( applicant_city == "" ){
				add_error("#applicant-city-label", curLanguage.cityLabel);
					window.location.hash = "#applicant-city-label";
			}
			if( applicant_state == "" ){
				add_error("#applicant-state-label", curLanguage.stateLabel);
					window.location.hash = "#applicant-state-label";
			}
			if( applicant_postal == "" ){
				add_error("#applicant-postal-label", curLanguage.postalLabel);
					window.location.hash = "#applicant-postal-label";
			}
		}
		if( applicant_phone == "" || isNaN(applicant_phone)){
			add_error("#applicant-phone-label", curLanguage.phoneLabel);
				window.location.hash = "#applicant-phone-label";
		}
		if ( eeoc_disability > 0 ){
			if ( eeoc_signature == "" ){
				add_error("#eeoc_disability_signature-label", curLanguage.errorLabel);
					window.location.hash = "#eeoc_disability_signature-label";
			}
			if ( eeoc_date == "" ){
				add_error("#eeoc_disability_date-label", curLanguage.errorLabel);
					window.location.hash = "#eeoc_disability_date-label";
			}
		}
		if(applicant_birth == ""){
			add_error("#applicant-birth-label", curLanguage.birthLabel);
				window.location.hash = "#applicant-birth-label";
		}
		if(applicant_birthPlace == ""){
			add_error("#applicant-birthPlace-label", curLanguage.birthPlaceLabel);
				window.location.hash = "#applicant-birthPlace-label";
		}
		if(applicant_nearestAirpotr == ""){
			add_error("#applicant-nearestAirpotr-label", curLanguage.nearestAirpotrLabel);
				window.location.hash = "#applicant-nearestAirpotr-label";
		}
		/*if($("input[name=tesda_ncii]:checked").length < 1){
        	add_error("#applicant-tesdaNcii-label", curLanguage.tesdaNciiLabel);
				window.location.hash = "#applicant-tesdaNcii-label";
    	}*/
		
		if( applicant_errors ){
			if( applicant_address == "" ){
				$("#applicant-address-value").attr("value", "Address");
					window.location.hash = "#applicant-address-label";
			}
			if(dv_language == 'es' || dv_language == 'nl'){ // Some sort of catch all where language is not set properly
				$('.applicant_label_error').html(curLanguage.errorLabel);
			}
			msgAlert(curLanguage.alertMessage);
			return false;
		}else{
			return true;
		}
		window.location.hash = "#";
	}
}

function msgAlert(msg){
	 bootbox.alert({  
        buttons: {  
           ok: {  
                label: 'ok',  
            }  
        },  
        message: msg,  
        title: "Submit Applicaion",  
    });  
}

function checkAddress(){
	// Store address values
	applicant_address = $("#applicant-address-value").attr("value");
	applicant_city = $("#applicant-city-value").attr("value");
	applicant_state = $("#applicant-state-value").attr("value");
	applicant_postal = $("#applicant-postal-value").attr("value");
	// Remove placeholder text (plugin does not perform on IE)
	if( applicant_address == "Address" ){ $("#applicant-address-value").attr("value",""); }
	if( applicant_city == "City" ){ $("#applicant-city-value").attr("value",""); }
	if( applicant_state == "State" ){ $("#applicant-state-value").attr("value",""); }
	if( applicant_postal == "Postal" ){ $("#applicant-postal-value").attr("value",""); }
}

function check_email(email_address) {
	if(email_address.search(/^[a-zA-Z0-9_\+]+((-[a-zA-Z0-9_\+]+)|(\.[a-zA-Z0-9_\+]+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) == -1) {
		return false;
	}else{
		return true;
	}
}

function add_error( label, msg ){
    $(label).append(' <span class="applicant_label_error" style="color: #f00 !important;">'+msg+'.</span>');
    applicant_errors = true;
}

// APPLY WITH LINKEDIN
(function() {
    window.addEventListener('message', function(e) {
        // Domain whitelist
        // TODO: Best way to retrieve these...
        if (e.origin === 'http://app.applytojob.com' || e.origin === 'https://app.applytojob.com' || e.origin === 'http://app.performjazz.com' || e.origin === 'https://app.performjazz.com' || e.origin === 'http://app.dennisnedry.com') {
        	fillApplicantData(e.data);
        } else {
        	//console.log('domain did not match');
        }
    });
})();

function fillApplicantData(data) {
    data = decodeURIComponent(data).replace(/\+/g, " ");
    var obj = JSON && JSON.parse(data) || $.parseJSON(data);
    if (obj.err) alert(obj.err);
    $('#applicant-firstname-value').val(obj.usr_first_name);
    $('#applicant-lastname-value').val(obj.usr_last_name);
    $('#applicant-email-value').val(obj.usr_email);
    $('#applicant-address-value').val(obj.usr_address);
    $('#applicant-resumetext-value').val(obj.usr_resume);
    $('#applicant-phone-value').val(obj.usr_phone);
	$('#applicant-skills').val(obj.usr_skills);	
    $('#applicant-choose-paste').click();
    
    //新加
    //$('#applicant-birth-value')
}


function get_base_domain(hostname) {
	if(!hostname) {
		hostname = document.location.hostname;
	}
	if(hostname == 'localhost') {
		return hostname;
	}
	var host_parts = hostname.split('.');
	var l = host_parts.length;
	return host_parts[l-2] + '.' + host_parts[l-1];
}
// the parent and child document.domain must match. They can only be set to a subset of the original hostname, so we set cmsprep.theapplicant.com to theapplicant.com
document.domain = get_base_domain();

function get_linkedin_profile(profile) {
	
	// handle resizing issues
	$('#applicant-resume-options').hide();
	$('#applicant-resume-switch-paste').click();
	
	var p = profile.person;
	
	// see https://developer.linkedin.com/application-response-data-structure for all available fields
	$('#applicant-firstname-value').val(p.firstName);
	$('#applicant-lastname-value').val(p.lastName);
	$('#applicant-email-value').val(p.emailAddress);
	$('#applicant-linkedin-value').val(p.publicProfileUrl);
	if(p.educations && p.educations.values && p.educations.values[0]) {
		$('#applicant-college-value').val(p.educations.values[0].schoolName);
	}
				
	if(p.location.name) {
		$('#applicant-city-value').val(p.location.name).removeClass('resumtor-dimmed-text');		
	}
	
	if(p.location.postalCode) {
		$('#applicant-postal-value').val(p.location.postalCode).removeClass('resumtor-dimmed-text');
	}
	if(p.phoneNumbers && p.phoneNumbers.values && p.phoneNumbers.values[0]) {
		$('#applicant-phone-value').val(p.phoneNumbers.values[0].phoneNumber);
	}
	
	$('#applicant-resumetext-value').val(li_build_text_resume(profile));
}

/*
 *  test for a linkedin date object and return it as a string - the object may have any combination of day, month, year as properties
 *  dte - the linkedin date object
 *  seperator - string to seperate elements, defaults to '/'
 *  append_text - string to append if the dte object exists
 *  no_date_text - string to return if dte object does not exist
 */
function li_date_text(dte, seperator, append_text, no_date_text) {
	if(!seperator) {
		seperator = '/';
	}
	if(!append_text) {
		append_text = '';
	}
	if(!no_date_text) {
		no_date_text = '';
	}	
	var dte_text = '';
	if(dte) {				
		dte_text += (dte.day) ? dte.day + seperator : '';
		dte_text += (dte.month) ? dte.month + seperator : '';
		dte_text += (dte.year) ? dte.year : '';
	}	
	if(dte_text && append_text) {
		dte_text += append_text;
	} else {
		dte_text = no_date_text;
	}	
	return dte_text;
}			

/*
 * checks if a value exists and returns it with append_text appended
 */
function li_value(val, append_text) {
	if(!append_text) {
		append_text = '';
	}	
	if(val) {
		return val + append_text;
	} else {
		return '';
	}		
}	

function li_build_text_resume(profile) {
	var lb = "\n";			
	var divider = '--------------------------------------------------------------------------------------------------------------------';
	var sm_divider = '-----';
	var p = profile.person;
	var res = li_value(p.firstName, ' ') + li_value(p.lastName) + lb
		+ li_value(p.headline, lb)
		+ li_value(p.emailAddress, lb)
		+ lb;
		
	if(p.positions && p.positions.values) { 
		res += divider + lb
			+ 'Experience' + lb
			+ divider + lb + lb;
		$.each(p.positions.values, function(idx, pos) {
			res += sm_divider + lb;
			res += li_value(pos.title, ' at ') + li_value(pos.company.name) + lb;
			var p_start = li_date_text(pos.startDate, '/', ' to ');	//i.e. "7/2009 to "	
			res += p_start;
			var p_present = (p_start) ? 'Present ' + lb : '';		
			res += li_date_text(pos.endDate, '/', lb, p_present); //i.e. "7/2010\n" or "Present\n"
			if(pos.summary) {
				var summary = li_value(pos.summary);	
				summary = summary.replace(/(\r\n|\n|\r)/gm,"");
				res += lb + summary;
			}
			res += lb;		
		});	
	}
	
	if(p.recommendationsReceived && p.recommendationsReceived.values) {
		var rec_count = p.recommendationsReceived.values.length;
		var s = (rec_count > 1) ? 's' : '';
		res += sm_divider + lb;
		res += p.recommendationsReceived.values.length + ' recommendation' + s + ' available upon request' + lb;
	}	
	res += lb;
	
	if(p.educations && p.educations.values) {
		res += divider + lb			
			+ 'Education' + lb
			+ divider + lb + lb;
		$.each(p.educations.values, function(idx, edu) {
			res += sm_divider + lb;
			res += li_value(edu.schoolName, lb);			
			res += li_value(edu.degree, ' ');		
			var e_start = li_date_text(edu.startDate, '/', ' to '); 			
			res += 	e_start;	
			var e_present = (e_start) ? 'Present' + lb: ''
			res += li_date_text(edu.endDate, '/', lb, e_present);
			res += lb;
		});
	}				
	return res;
}