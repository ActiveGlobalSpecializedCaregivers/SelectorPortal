<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Email history</title>
<style type="text/css">
.modal-backdrop{z-index:0;}
</style>
<script type="text/javascript">
$(document).ready(function(){
	
	var receiveEmailCount = '${count}';
	if(receiveEmailCount > 0){
		window.onload=function(){ alertMsg("You have "+receiveEmailCount+" letters to check!");} 
	}
	
	//datatable of the email history
	 $('#emailList').DataTable({
		bFilter: false,
    	bLengthChange: false,		//each page display
    	"aaSorting": [				//sort 3:means the fourth column
            [ 3, "desc" ]
        ],
		"aoColumnDefs": [
			{ type: 'date-euro', targets: 3 },
   			{ "bSortable": false, "aTargets": [ 4 ] }
		]
	 
    });
	
	 $('#receiveEmailList').DataTable({
		bFilter: false,
	  	bLengthChange: false,		//each page display
	  	"aaSorting": [				//sort 3:means the fourth column
	    	[ 3, "desc" ]
		],
		"aoColumnDefs": [			
      		{ type: 'date-euro', targets: 3 }, //order the date sort(Date)
      	  	{ "bSortable" : false, "aTargets" : [4]}
     	]
	});
	
	//select all and cancel selection
	$(".all_check").click(function () {
		var type = $("#i-type").val();			//select inbox or sent
		if(type == '0'){
			if($(this).is(':checked')){
				$("[name = check_email]:checkbox").prop("checked",true);
			}else{
				$("[name = check_email]:checkbox").prop("checked",false);
			}
		}else{
			if($(this).is(':checked')){
				$("[name = inbox-email]:checkbox").prop("checked",true);
			}else{
				$("[name = inbox-email]:checkbox").prop("checked",false);
			}
		}
    });
	
});

function sendEmail(){
	$("#sendMailModal").modal({backdrop: 'static', keyboard: false});
}

//get the email data
function getEmail(id,type,obj){
	$.ajax({
		type: "get",
		async: false,
		url: "${pageContext.request.contextPath}/emailTemplates/getEmail",
		data: {id:id,userId:'${caregiver.userId}'},
		dataType: "json",
		success:function(data){
			$(obj).find(".linew").remove();
			
			//attachments
			var attachment = data.attachments;
			if(attachment != null && attachment.length>0){
				for(var i=0; i<attachment.length; i++){
					var size = attachment[i].size;
					if(size < 1024 && size > 0){					//B
						size = (size).toFixed(2) +"B";
					}else if(size < 1024*1024){			//KB
						size = (size/1024).toFixed(2) +"KB";
					}else if(size < 1024*1024*1024){	//M
						size = (size/1024/1024).toFixed(2) +"M";
					}else if(size < 1024*1024*1024*1024){
						size = (size/1024/1024/1024).toFixed(2) +"G";
					}else if(size > 1024*1024*1024*1024){
						size = (size/1024/1024/1024).toFixed(2) +"G";
					}
					
					if(type == 'send'){
						$("#reply-attachment").append("<li>"
							+"<a class='b-file fa fa-download' target='_blank' href='${pageContext.request.contextPath}/file/downFile?fileName="+attachment[i].name+"&path="+attachment[i].path+"' title='Download Attachment'></a>"
							+"<i class='fa fa-paperclip'></i>"
							+"<label class='l-name'>"+attachment[i].name+"</label>"
							+"<label>("+size+")</label>"
							+"</li>");
					}else if(type == 'receive'){
						if(attachment[i].type == 'local'){			//the file save in the server
							$("#reply-attachment").append("<li>"
									+"<a class='b-file fa fa-download' target='_blank' href='${pageContext.request.contextPath}/file/downFile?type=local&fileName="+attachment[i].name+"&path="+attachment[i].path+"' title='Download Attachment'></a>"
									+"<i class='fa fa-paperclip'></i>"
									+"<label class='l-name'>"+attachment[i].name+"</label>"
									+"<label>("+size+")</label>"
									//+"<button class='fa fa-upload' title='Upload Attachment' onclick='uploadAttachment(this,"+attachment[i].path+");'></button>"
									+"<button class=\"btn btn-success\" style=\"width:inherit;color:white;margin-left:10px;\" title=\"Upload Attachment\" onclick=\"uploadAttachment(this,'"+attachment[i].name+"','"+attachment[i].path+"');\"><span class=\"glyphicon glyphicon-plus\"></span> Upload to Client Folder</button>"
									+"</li>");		
						}else{			//S3 file(has been uploaded)
							$("#reply-attachment").append("<li>"
									+"<a class='b-file fa fa-download' target='_blank' href='${pageContext.request.contextPath}/file/downFile?fileName="+attachment[i].name+"&path="+attachment[i].path+"' title='Download Attachment'></a>"
									+"<i class='fa fa-paperclip'></i>"
									+"<label class='l-name'>"+attachment[i].name+"</label>"
									+"<label>("+size+")</label>"
									+"</li>");	
						}
						
						//select option(inbox)
						var linew = $(obj).find(".linew").val();
						if(linew != 'undefined'){
							var inboxVal = $("#inbox").val();
							if(inboxVal > 1){
								$("#emailType option[value='1']").remove();
								var inbox = inboxVal - 1;
								$("#emailType").append("<option value='1'>Inbox("+inbox+")</option>");
								$("#emailType option[value='1']").attr('selected','selected');
								
								$("#inbox").val(inbox);
							}else if(inboxVal == '1'){
								$("#emailType option[value='1']").remove();
								$("#emailType").append("<option value='1'>Inbox</option>");
								$("#emailType option[value='1']").attr('selected','selected');
								
								$("#inbox").val("0");
							}
						}
						
					}
				}
			}
			
			//show email history
			$("#emailFromName").html(data.senderName+"("+data.senderEmail+")");
			$("#emailToName").html(data.receiveName+"("+data.receiveEmail+")");
			$("#emailToSubject").html(data.subject);
			//String turn to Date
			$("#send_date").html(new Date(parseInt(data.sendDate)).toLocaleString().replace(/:\d{1,2}$/,' '));
			CKEDITOR.instances.g_content.setData(data.content);		//set the content value
			if(data.ccEmail != null && data.ccEmail !=''){
				$("#cc_email").html(data.ccEmail);
				$("#ccDiv").show();
				
				//reply
				$("input[name='ccEmail']").val(data.ccEmail);
				$("#reply-cc").show();
			}else{
				$("#ccDiv").hide();
				$("#reply-cc").hide();		//reply
			}
			if(data.bccEmail != null && data.bccEmail !=''){
				$("#bcc_email").html(data.bccEmail);
				$("#bccDiv").show();
				
				//reply
				$("input[name='bcc_email']").val(data.bccEmail);
				$("#reply-bcc").show();
			}else{
				$("#bccDiv").hide();
				$("#reply-bcc").hide();
			}
			$("#email-modal").modal({backdrop: 'static', keyboard: false});
			
			//ready to reply (show email history)
			$("input[name='senderName']").val(data.senderName);
			$("input[name='subject']").val("RE:"+data.subject);
			$("input[name='content']").val("RE:"+data.subject);
			CKEDITOR.instances.content.setData("<br><br>----------------------<br>"+data.content);
			
		}
	});
}

function del(){
	var type = $("#i-type").val();			//select inbox or sent
	var checked_e_id="";
	var checked;
	if(type == '0'){
		checked = $("input[name='check_email']:checked");
	}else{
		checked = $("input[name='inbox-email']:checked");
	}
	
	bootbox.confirm({  
		buttons: {  
		    confirm: {  
		        label: 'Ok'  
		    },  
		    cancel: {  
		        label: 'Cancel'
		    }  
		},  
		message: 'Are you sure you want to delete these emails?',  
		callback: function(result) {
            if(result){
            	if(checked.length > 0){
            		$(".loading").show();
            		for(var i=0; i<checked.length; i++){
            			if(i < checked.length- 1){
            				checked_e_id = checked_e_id + checked[i].value+",";
            			}else{
            				checked_e_id = checked_e_id + checked[i].value;
            			}
            		}
            		var href = "${pageContext.request.contextPath}/emailTemplates/delEmail?ids="+checked_e_id +"&active=5"+"&userId="+${caregiver.userId}+"&type="+type;
            		window.location = href;
            	}else{
            		alertMsg("No emails were selected. Nothing will be deleted. ");
            	}
            }
		}
	});  
	
}

//change the email type
function changEemailType(obj){
	var emailId = $(obj).val();
	$("#i-type").val(emailId);
	$(".all_check").prop("checked",false);
	if(emailId == '0'){
		$("[name = inbox-email]:checkbox").prop("checked",false);
		$("#email_0").show();
		$("#email_1").hide();
	}else{
		$("[name = check_email]:checkbox").prop("checked",false);
		$("#email_0").hide();
		$("#email_1").show();
	}
}

//upload file to S3
function uploadAttachment(obj,fileName,path){
	bootbox.confirm({  
		buttons: {  
		    confirm: {  
		        label: 'Ok'  
		    },  
		    cancel: {  
		        label: 'Cancel'
		    }  
		},  
		message: 'Are you sure you want to upload this attachment to the client’s folder?',  
		callback: function(result) {
            if(result){
            	
            	$.get("${pageContext.request.contextPath}/file/checkFile", {userId:'${caregiver.userId}',fileName:fileName} ,function(data){
					if(data.status == 'error') {
						bootbox.confirm({  
							buttons: {  
							    confirm: {  
							        label: 'Ok'  
							    },  
							    cancel: {  
							        label: 'Cancel'
							    }  
							},  
							message: data.msg,  
							callback: function(result) {
					            if(result){
					            	$.ajax({
						            	type: "post",
						        		url: "${pageContext.request.contextPath}/emailTemplates/uploadAttachment",
						        		data: {path:path,fileName:fileName,userId:'${caregiver.userId}'},
						        		dataType: "json",
						        		success:function (data){
						        			alertMsg(data.msg);
						        			if(data.url != null && data.url != ""){
							        			$(obj).parent().find(".b-file").attr("href","${pageContext.request.contextPath}/file/downFile?fileName="+fileName+"&path="+data.url);
						        			}
						        			$(obj).remove();
						        		},error:function (e){
						        			alertMsg("Server error!");
						        		}
					            	});
					            }
							}
						});
					}else if(data.status == 'success'){
		            	$.ajax({
			            	type: "post",
			        		url: "${pageContext.request.contextPath}/emailTemplates/uploadAttachment",
			        		data: {path:path,fileName:fileName,userId:'${caregiver.userId}'},
			        		dataType: "json",
			        		success:function (data){
			        			alertMsg(data.msg);
			        			if(data.url != null && data.url != ""){
				        			$(obj).parent().find(".b-file").attr("href","${pageContext.request.contextPath}/file/downFile?fileName="+fileName+"&path="+data.url);
			        			}
			        			$(obj).remove();
			        		},error:function (e){
			        			alertMsg("Server error!");
			        		}
		            	});
					}
				},"json");
            	
            	
            	
            }
		}
	});  
}

//refresh page to get the receive email
function myrefresh(){
	window.location.reload();
}

var active = '${active}';

if(active == '5'){
	setTimeout('myrefresh()',300000);
}

</script>
</head>
<body style="overflow: scroll;">
<div>
	<div class="row" style="margin-left: 10px;">
		<div class="col-md-8" style="margin-bottom: 20px;margin-top: 30px;">
			<h3 style="float:left;">
				<span class="glyphicon glyphicon-envelope"></span> <strong>Emails</strong>
			</h3>
			<select id="emailType" class="btn btn-success" style="float:right;margin-left: 20px;" onchange="changEemailType(this);">  
	            <option value="0">Sent</option>
	            <c:choose>
	            	<c:when test="${count != '0'}">
	            		<option value="1" selected="selected">Inbox(${count})</option>
	            		<input type="hidden" id="inbox" value="${count}"/>  
	            	</c:when>
	            	<c:otherwise>
			            <option value="1" selected="selected">Inbox</option>  
	            	</c:otherwise>
	            </c:choose>
			</select>
			<!-- The default is to display the inbox -->
			<input type="hidden" id="i-type" value="1">
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<button type="button" style="float:right" class="btn btn-success" onclick="del();">
					<span class="fa fa-trash-o"></span> Delete
				</button>
			</sec:authorize>
			<button type="button" style="float:right;margin-right: 20px;" class="btn btn-success" onclick="sendEmail();">
				<span class="glyphicon glyphicon-plus"></span> New Mail
			</button>
			<button type="button" style="float:right;margin-right: 20px;" class="btn btn-primary" data-toggle="modal" data-target="#templates">
				Manage Templates
			</button>
		</div>
		
		<!-- received begin-->
	    <div class="col-md-8" id="email_1">
			<table id="receiveEmailList" class="table table-hover row-border order-column ">
		        <thead>
		            <tr style="background-color:grey;color: white;">
		                <th>TO</th>
		                <th>FROM</th>
		                <th>SUBJECT</th>
		                <th>DATE</th>
		                <th style="text-align: center;"><input class="all_check" type="checkbox"></th>
		            </tr>
		        </thead>
		        <tbody>
		        	<c:forEach items="${receiveEmailList}" var="emailHistory">
		        		<tr>
		        			<td>${emailHistory.receiveName}</td>
		        			<td>${emailHistory.senderName}</td>
		        			<td>
		        			<c:choose>
		        				<c:when test="${emailHistory.flag eq '1'}">
		        					<a href="#" onclick="getEmail(${emailHistory.id},'receive',this);">${emailHistory.subject}
		        						<span class="linew"></span>
		        					</a>
		        				</c:when>
		        				<c:otherwise>
		        					<a href="#" onclick="getEmail(${emailHistory.id},'receive',this);">${emailHistory.subject}
		        					</a>
		        				</c:otherwise>
		        			</c:choose>
		        			</td>
		        			<td><fmt:formatDate value="${emailHistory.createDate}" type="both" pattern="dd/MM/yyyy HH:mm:ss"/></td>
		        			<td align="center"><input type="checkbox" name="inbox-email" value="${emailHistory.id}"></td>
		        		</tr>
		        	</c:forEach>
		        </tbody>
		    </table>
	    </div>
		<!-- received end -->
		
		<!-- Has been sent begin -->
		<div class="col-md-8" style="display:none;" id="email_0">
			<table id="emailList" class="table table-hover row-border order-column ">
		        <thead>
		            <tr style="background-color:grey;color: white;">
		                <th>TO</th>
		                <th>FROM</th>
		                <th>SUBJECT</th>
		                <th>DATE</th>
	                	<th style="text-align: center;">
			                <sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUB_ADMIN')">
		                		<input class="all_check" type="checkbox">
			                </sec:authorize>
	                	</th>
		            </tr>
		        </thead>
		        <tbody>
		        	<c:forEach items="${emailList}" var="emailHistory">
		        		<tr>
		        			<td>${emailHistory.receiveName}</td>
		        			<td>${emailHistory.senderName}</td>
		        			<td><a href="#" onclick="getEmail(${emailHistory.id},'send',this);">${emailHistory.subject}</a></td>
		        			<td><fmt:formatDate value="${emailHistory.createDate}" type="both" pattern="dd/MM/yyyy HH:mm:ss"/></td>
		        				<td align="center">
		        			<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUB_ADMIN')">
		        					<input type="checkbox" name="check_email" value="${emailHistory.id}">
		        			</sec:authorize>
		        				</td>
		        		</tr>
		        	</c:forEach>
		        </tbody>
		    </table>
	    </div>
	    <!-- Has been sent end-->

    </div>
</div>
</body>
</html>