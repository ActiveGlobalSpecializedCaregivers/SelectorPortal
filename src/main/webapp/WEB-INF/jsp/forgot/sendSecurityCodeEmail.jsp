<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Send email with security code</title>
<link rel="shortcut icon" href="http://activeglobalcaregiver.sg/misc/favicon.ico" type="image/vnd.microsoft.icon" />
<link type="text/css" href="${pageContext.request.contextPath}/resources/css/forgot/css.css" rel="stylesheet" />
<link type="text/css" href="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/bootbox.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
</head>
<script type="text/javascript">

    function check(){
        var username = $("#username").val();
        if(username != null && username !== ''){
            $("#submit_button").prop('disabled', true);
            $("#myForm").submit();
        }else{
            alertMsg("Username cannot be empty!");
            return false;
        }
    }

    function alertMsg(msg){
        bootbox.alert({
            buttons: {
                ok: {
                    label: 'ok'
                }
            },
            message: msg,
            title: "Message"
        });
    }

</script>

<body>
 
  <div class="content">
   <div class="web-width">
     <form id="myForm" action="${pageContext.request.contextPath}/user/sendSecurityCode" method="post" class="forget-pwd">
       <dl>
        <dt>username:</dt>
        <dd><input id="username" class="form-control" type="text" name="username" placeholder="Username" autofocus/></dd>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <div class="clears"></div>
       </dl> 
       <div class="subtijiao">
       	<input type="button" value="submit" id="submit_button" onclick="return check();"/>
       </div> 
       
      </form>
   </div>
  </div>

</body>
</html>
