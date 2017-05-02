<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>forgotPwd</title>
<link rel="shortcut icon" href="http://activeglobalcaregiver.sg/misc/favicon.ico" type="image/vnd.microsoft.icon" />
<link type="text/css" href="${pageContext.request.contextPath}/resources/css/forgot/css.css" rel="stylesheet" />
<link type="text/css" href="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootbox.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
<script type="text/javascript">

 $(function(){
	 $(".selyz").change(function(){
	   var selval=$(this).find("option:selected").val();
		 if(selval=="0"){
			 $(".sel-yzsj").show()
			 $(".sel-yzyx").hide()
			 }
		 else if(selval=="1"){
			 $(".sel-yzsj").hide()
			 $(".sel-yzyx").show()
			 }
		 })
	 })
	 
</script>

<script type="text/javascript">
	function getMcode(t){
		var email = $("#email").val();
		if(email !=null && email !=''){
			$.ajax({
			type:"get",
			async:false,
			url:"${pageContext.request.contextPath}/user/verify",
			data:{email:email},
			dataType: "text", 
				success: function(data){
					var json = JSON.parse(data);
			             $.each(json, function (index, item) { 
			             	$("#hCode").val(item);
			             	alertMsg("A verification code has been sent to your Email, Please check!");
			             }); 
				}
			});
			
			$(".button").attr('disabled',true);
		    for(i=1;i<=t;i++) { 
		        window.setTimeout("update_p(" + i + ","+t+")", i * 1000); 
		    } 
		}else{
		 alertMsg("Please enter the email!");
		}
	}
	
	function checkCode(){
		var code = $("#code").val();
		var hCode = $("#hCode").val();
		if(code != '' && code != null){
			if(code == hCode){
				$("#myForm").submit();
			}else{
				alertMsg("The code is wrong!");
			}
		}else{
			alertMsg("The code should not be null!");
		}
	}
	
	function update_p(num,t) {
		if(num == t) {
			$(".button").html("Resend");
			$(".button").attr("disabled",false);
		}
		else {
			var printnr = t-num;
			$(".button").html("Resend after(" + printnr +")seconds");
		}
	}
	
	function alertMsg(msg){
		 bootbox.alert({  
	         buttons: {  
	            ok: {  
	                 label: 'ok',  
	             }  
	         },  
	         message: msg,  
	         title: "Message",  
	     });
	}
</script>
</head>

<body>

  <div class="content">
   <div class="web-width">
     <div class="for-liucheng">
      <div class="liulist for-cur"></div>
      <div class="liulist for-cur"></div>
      <div class="liulist"></div>
      <div class="liulist"></div>
      <div class="liutextbox">
       <div class="liutext for-cur"><em>1</em><br /><strong>Fill in the user information</strong></div>
       <div class="liutext"><em>2</em><br /><strong>check identity</strong></div>
       <div class="liutext"><em>3</em><br /><strong>set password</strong></div>
       <div class="liutext"><em>4</em><br /><strong>complete</strong></div>
      </div>
     </div>
     <form:form id ="myForm" action="${pageContext.request.contextPath}/user/setNewPassword" commandName="user" method="post" class="forget-pwd">
     	<input type="hidden" name="username" value="${user.username}"/>
     	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
       <dl>
        <dt>Verify for:</dt>
        <dd>
         <select class="selyz">
          <!-- <option value="0">phone</option> -->
          <option value="1">mailbox</option>
         </select>
        </dd>
        <div class="clears"></div>
       </dl>
       <dl>
        <dt>username:</dt>
        <dd><input type="text" class="form-control" disabled="disabled" value="${user.username}"/></dd>
        <div class="clears"></div>
       </dl>
       <!-- <dl class="sel-yzsj">
        <dt>Verified phone:</dt>
        <dd><input type="text" value="134******57" readonly  /></dd>
        <div class="clears"></div>
       </dl> -->
       <dl class="sel-yzyx">
        <dt>Verified mailbox:</dt>
        <dd class="${status.error ? 'has-error' : ''}">
        	<spring:bind path="email">
       			<form:input type="text" id="email" name="email" path="email" placeholder="Email"/>
	        	<button class="btn btn-info button" style="width:170px;" type="button" onclick="getMcode(60);" >Get verification code</button>
       		</spring:bind>
        </dd>
        <form:errors style="color:red;margin-left:220px;" path="email"/>
        <div class="clears"></div>
        <br>
       </dl>
       <dl>
        <!-- <dt>Phone verification:</dt>
        <dd><input type="text" /> <button>SMS CODE</button></dd> -->
        <dt>Mail verification code:</dt>
        <dd>
        	<input type="text" id="code" name="code" placeholder="Code"/>
        	<input type="hidden" id="hCode"/>
        </dd>
        <div class="clears"></div>
       </dl>
       <div class="subtijiao">
       	<input type="button" value="submit" onclick="return checkCode();"/>
       </div> 
      </form:form><!--forget-pwd/-->
   </div><!--web-width/-->
  </div><!--content/-->
  
</body>
</html>
