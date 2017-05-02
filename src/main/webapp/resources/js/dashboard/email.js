$(document).ready(function(){

});

//delete exists file(show email history)
function closeShow(){
	$("#reply-attachment").html("");
}

function closeSendMailModal(){
	var templateName = $("#chooseT font").text();
	if( templateName !="CHOOSE TEMPLATE" ){
		$("#toSubject").val("");
		CKEDITOR.instances.toContent.setData("");
		$("#chooseT font").text("CHOOSE TEMPLATE");
		$("#attachment-file").html("");
	}
}

//clear show template data
function closeEditTemplate(){
	$("#e-t-subject").val("");
	$("#ETcontent").val("");
	$("#e-t-name").val("");
	$("#e-t-attachment li").remove();
}