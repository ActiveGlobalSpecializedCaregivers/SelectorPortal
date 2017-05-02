<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
	function changeCode(){
		var changeCode = Math.random();
		$('#codeImg').attr('src','${pageContext.request.contextPath}/user/createCode?'+changeCode);
	}
	
	function check(){
		var inputCode = $("#inputCode").val();
		var username = $("#username").val();
		if(inputCode != null && inputCode != '' && username != null && username != ''){
			$.ajax({
				type:"get",
				async:false,
	            url:"${pageContext.request.contextPath}/user/checkRandomCode",
				data:{inputCode:inputCode,username:username},
				dataType: "text", 
	            success: function(data){
	            	 var json = JSON.parse(data);
	                 $.each(json, function (index, item) {  
	                     if(index =='username' && item){
	                    	 $("#myForm").submit();
	                     }else if(index !='username'){
	                    	 alertMsg("Username does not exist!");
	                     }else{
	                    	 alertMsg("The code is wrong. Please re-enter!");
	                     }
	                 }); 
	            },error:function(e){
	            	alertMsg("Server exception!");
	            	return false;
	            } 
			});
		}else{
			alertMsg("Username and Verification code cannot be empty!");
			return false;
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
      <div class="liulist"></div>
      <div class="liulist"></div>
      <div class="liulist"></div>
      <div class="liutextbox">
       <div class="liutext for-cur"><em>1</em><br /><strong>Fill in the user information</strong></div>
       <div class="liutext"><em>2</em><br /><strong>check identity</strong></div>
       <div class="liutext"><em>3</em><br /><strong>set password</strong></div>
       <div class="liutext"><em>4</em><br /><strong>complete</strong></div>
      </div>
     </div>
     <form id="myForm" action="${pageContext.request.contextPath}/user/checkIdentity" method="post" class="forget-pwd">
       <dl>
        <dt>username:</dt>
        <dd><input id="username" class="form-control" type="text" name="username" placeholder="Username" autofocus/></dd>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <div class="clears"></div>
       </dl> 
       <dl>
        <dt>code:</dt>
        <dd>
         <input type="text" class="form-control" name="code" id="inputCode" placeholder="Code"/> 
         <%-- <input type="hidden" name="codeValue" value="${random}"/> --%>
         <div class="yanzma">
          <img id="codeImg" src="${pageContext.request.contextPath}/user/createCode"/>
          <a class="btn btn-default" style="border: 0px;" href="#" onclick="changeCode();">change</a>
         </div>
        </dd>
        <div class="clears"></div>
       </dl>
       <div class="subtijiao">
       	<input type="button" value="submit" onclick="return check();"/>
       </div> 
       
      </form><!--forget-pwd/-->
   </div><!--web-width/-->
  </div><!--content/-->
  
</body>
</html>
