$(document).ready(function(){
	$('#fileList').DataTable({
		bFilter: false,
		bLengthChange: false,		//each page display
		"aaSorting": [				//sort 3:means the fourth column
            [ 2, "desc" ]
        ],
		"aoColumnDefs": [
			{ type: 'date-euro', targets: 2 }
		]
	});
	
	//upload document's modal
	$("#i-add").click(function (){
		$("#a_d").modal({backdrop: 'static', keyboard: false});
	});
	
	//the upload document's type is changing
	$("#s_type").change(function (){
		var typeVal = $(this).val();
		if(typeVal == 'resume'){
			//$("#verify_file").text("If you want to update resume,please upload a resume file named 'resume.pdf'!");
			$("#verify_file").text("If you want to update resume,the old resume will put into the type of 'other_file'!");
			$("#a_file").removeAttr("multiple");					//only upload one file
			$("#a_file").attr("accept","application/pdf");		//only upload .pdf file
			//$("#a_file").removeAttr("accept");		//only upload .pdf file
			//$("#a_file").attr("multiple","multiple");
			//$("#a_file").val("");
		}else if(typeVal == 'my_photo'){
			$("#a_file").val("");
			//$("#verify_file").text("jpg/jpeg/png format only please");
			$("#a_file").attr("accept",".jpg,.jpeg,.png");		//only upload jpg/jpeg/png file
			$("#a_file").removeAttr("multiple");					//only upload one file
		}else{
			$("#a_file").val("");
			$("#verify_file").text("");
			$("#a_file").removeAttr("accept");		//only upload .pdf file
			$("#a_file").attr("multiple","multiple");
		}
	});
	
	//Rename pop up cursor to rename text box.
	$('#i-modal-rename').on('shown.bs.modal', function (e) {
		$("#newName").focus();				
	})
	
});

//delete document
function file_del(userId, key, context){
	bootbox.confirm({  
		buttons: {  
		    confirm: {  
		        label: 'ok'  
		    },  
		    cancel: {  
		        label: 'cancel'
		    }  
		},  
		message: "Do you want to delete this document?",  
		callback: function(result) {
            if(result){
            	var href=context+"/file/delete?active=6&userId="+userId +"&key="+key;
            	window.location = href;
    			$(".loading").show();
            }
		}
	});  
}

function getContextPath() {
	   return "${pageContext.request.contextPath}";
	}

//rename modal
function rename(userId, path, name){
	$("#i-modal-rename").modal({backdrop: 'static', keyboard: false});		//pop up modal
	$("#i-bucketName").val(path);
	$("#newName").val(name);			//will enter name
	$("#originalName").val(name);		//original name
}

//rename fileType
function renameType(userId, path, type,extension){
	var array = new Array(); 
	$("#select-type option").each(function(){ 
		var value = $(this).text(); 
		if(value == type){
			$(this).attr("selected","selected");
		}
	});
	
	$("#input-path").val(path);			
	$("#input-originalType").val(type);
	$("#input-extension").val(extension);
	
	//pop up modal
	$("#i-modal-renameFileType").modal({});
}

//rename document
function file_rename(photoDegrees, photoPassport, photo, otherFiles, resume){
	var newName = $("#newName").val();
	var originalName = $("#originalName").val();
	
	if(originalName == newName){
		alertMsg("Rename to same name should not be allowed!");
	}else if(newName != ''){
		if(resume.indexOf(newName)!=-1 || photoDegrees.indexOf(newName)!=-1 || photoPassport.indexOf(newName)!=-1 || photo.indexOf(newName)!=-1 || otherFiles.indexOf(newName)!=-1){
			alertMsg("This document is existing,please rename again!");
		}else{
			$("#i-rename").submit();
			$(".loading").show();
		}
	}else{
		alertMsg("The name can not be null!");
	}
	
}

//rename file type
function fileType_rename(){
	var originalType = $("#input-originalType").val();
	var type = $("#select-type").val();
	var extension = $("#input-extension").val();
	
	if(type == originalType){
		alertMsg("Rename to same type should not be allowed!");
	}/*else if(type == "resume"){
		if(extension != 'PDF'){
			alertMsg("The resume's extension must be 'PDF'!");
		}else{
			bootbox.confirm({  
				buttons: {  
					confirm: {  
						label: 'yes'  
					},  
					cancel: {  
						label: 'cancel'
					}  
				},  
				message: "The resume is existing.If you want to replace original resume.Please choose 'yes'",  
				callback: function(result) {
					$("#form-fileType").submit();
					$(".loading").show();
				}
			});  
		}
	}*/else{
		$("#form-fileType").submit();
		$(".loading").show();
	}
	
}


