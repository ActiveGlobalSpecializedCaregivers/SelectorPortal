function editQuestionnaire(){
	
	if($("#dob").val()=="" || $("#dob").val()==null){
		msgAlert("Please specify your date of birth!");
		//$("#dob").after("<span style='color:red'>Not be null!</span>");
		window.location.hash = "#gender";
	}else if($("#pob").val()=="" || $("#pob").val()==null){
		msgAlert("Please specify your place of birth!");
		//$("#pob").after("<span style='color:red'>Not be null!</span>")
		window.location.hash = "#dob";
	}else if($("#education").val()=="" || $("#education").val()==null){
		msgAlert("Please specify Nursing School you go to!");
		window.location.hash = "#education";
	}else if($("#nearestAirport").val()=="" || $("#nearestAirport").val()==null){
		msgAlert("Please specify the nearest airport to your home town!");
		//$("#nearestAirport").after("<span style='color:red'>Not be null!</span>")
		window.location.hash = "#pob";
	}else if($("#address").val()=="" || $("#address").val()==null){
		msgAlert("Please specify your current address!");
		//$("#address").after("<span style='color:red'>Not be null!</span>")
		window.location.hash = "#nearestAirport";
	}else if($("#height").val()=="" || isNaN($("#height").val())){
		msgAlert("Please specify your height in centimeters!(only enter number)");
		//$("#height").after("<span style='color:red'>Enter only number!</span>")
		window.location.hash = "#address";
	}else if($("#weight").val()=="" || isNaN($("#weight").val())){
		msgAlert("Please specify your weight in kilograms!(only enter number)");
		//$("#weight").after("<span style='color:red'>Enter only number!</span>")
		window.location.hash = "#height";
	}else if($("input[name=languageList]:checked").length < 1){
		msgAlert("Please select at least one language!");
		window.location.hash = "#weight";
	}else if($("#exp").val()=="" || isNaN($("#exp").val())){
		msgAlert("Please enter years of experience as a nurse!(only enter number)");
		//$("#exp").after("<span style='color:red'>Not be null!</span>")
		window.location.hash = "#education";
	}else if($("input[name=diagnosedList]:checked").length < 1){
		msgAlert("Please tick your ever diagnosed!");
		window.location.hash = "#diagnosedList1";
	}else if($("#hobbies").val()=="" || $("#hobbies").val()==null){
		msgAlert("Please enter your hobbies!");
		//$("#hobbies").after("<span style='color:red'>Not be null!</span>")
		window.location.hash = "#hobbies";
	}else if($("#mobile").val()=="" || $("#mobile").val()==null){
		msgAlert("Please enter your mobile!");
		window.location.hash = "#lastName";
	}else if($("#firstName").val()=="" || $("#firstName").val()==null){
		msgAlert("Please enter your firstname!");
		window.location.hash = "#firstName";
	}else if($("#lastName").val()=="" || $("#lastName").val()==null){
		msgAlert("Please enter your lastname!");
		window.location.hash = "#firstName";
	}else if($("#email").val()=="" || $("#email").val()==null){		
		msgAlert("Please enter your email!");
		window.location.hash = "#mobile";
	}else if($("#yearGraduation").val()=="" || $("#yearGraduation").val()==null){
		msgAlert("Please enter your year of Graduation!");
		window.location.hash = "#education";
	}else if($("#yearStudies").val()=="" || $("#yearStudies").val()==null){
		msgAlert("Please enter your year of studies!");
		window.location.hash = "#yearGraduation";
	}else if($("input[name=specialitiesList]:checked").length < 1){
		msgAlert("Please tick your nursing experience!");
		window.location.hash = "#location";
	}else if($("#checkEmail").html() != '' && $("#checkEmail").html() != 'The mailbox can be used.'){
		window.location.hash = "#mobile";
		msgAlert("Please enter your correct mailbox!");
		return false;
	}else{		
		bootbox.confirm({  
			buttons: {  
			    confirm: {  
			        label: 'ok'  
			    },  
			    cancel: {  
			        label: 'cancel'
			    }  
			},  
			message: 'Are you sure want to change it?', 
			callback: function(result) {
				if(result){
					$("#myForm").submit();					
					$(".loading").show();
				}
			},
			title:'Edit questionnaire',
		});
		
		window.location.hash = "#";		
	}

}

function change(){
	$("#edit").hide();		//hide the (edit the questionnaire)'s button
	$(".cBtn").show();		//show the form's button
	
	$(".divRitht").show();		//show the (to modify)'s data
	$(".divRitht1").hide();		//hidden data show
	window.location.hash = "#gender";
}

function msgAlert(msg){
	 bootbox.alert({  
         buttons: {  
            ok: {  
                 label: 'ok',  
             }  
         },  
         message: msg,  
         title: "Edit the questionnaire",  
     });  
}
