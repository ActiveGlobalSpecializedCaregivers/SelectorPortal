<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en">
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div style="width: 662px">
	<form:form id="commentsForm" modelAttribute="comments" method="post">
		<c:forEach items="${comments}" var="comment">
			<div style="margin-top: 15px;">
				<div style="border: #a1a1a1; padding: 7px 7px; background: #802b00; width: 35px; height: 35px; text-align: center; border-radius: 50%; float: left">
					<div id="initials" style="color: #F6F2F7; font-size: larger">${comment.userInitials}
					</div>
				</div>
				<div style="margin-left: 50px; margin-top: 20px; font-weight: bold;">${comment.userName}
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<button type="button" class="btn btn-danger" style="float:right;" onclick="delComment('${comment.id}',this);">
						<span class="fa fa-close"></span> DEL
					</button>
				</sec:authorize>
				</div>
				<label style="margin-left: 15px; margin-left: 15px">reported
					on <fmt:formatDate value="${comment.createDate}" type="both"
						pattern="dd/MM/yyyy" />
				</label>

				<div style="height: 55px; padding: 10px; background-color: #edeef0; width: 660px; border-style: ridge;"
					data-min-length="10" data-max-length="300">${comment.comment }
				</div>
			</div>
		</c:forEach>

		<div style="margin-top: 15px;">
			<div
				style="border: #a1a1a1; padding: 7px 7px; background: #802b00; width: 35px; height: 35px; text-align: center; border-radius: 50%; float: left">
				<div style="color: #F6F2F7; font-size: larger">${userInitials}</div>
			</div>
			<div style="margin-left: 50px; margin-top: 20px; font-weight: bold;">${loginUser.firstName}&nbsp;${loginUser.lastName}</div>
			<div style="margin-top: 20px; height: 55px; padding: 10px; background-color: #edeef0; width: 660px; border-style: ridge;">
				<input id="newComment" type="text" maxlength="300" size="60"
					name="newComment" tabindex="109"
					style="margin-top: 2px; width: 633px;"
					data-min-length="10" data-max-length="300"
					placeholder="click here to add a new comment"  onkeyup="toggleCommentBtn(this,'commentBtn')"/>
			</div>
			<div align="right" style="margin-top: 10px;">
				<input id="commentBtn" class="btn btn-default" type="submit" value="comment" onclick="return addComment();" disabled/>
			</div>
		</div>
	</form:form>

</div>
</html>

<script type="text/javascript">

  function toggleCommentBtn(ref,bttnID){
	  document.getElementById(bttnID).removeAttribute("disabled");
	}
	
	function addComment() {
		var caregiverId = ${caregiver.userId};
		var newComment = $("#newComment").val();
		
		if($.trim(newComment).length != 0){
		$("#commentsForm").attr(
				"action",
				"${pageContext.request.contextPath}/comment?caregiverId="
						+ caregiverId + "&comment=" + newComment);
		document.getElementById("commentsForm").submit();
		}
	}
	
	//delete comment
	function delComment(id,obj){
		$.ajax({  
            type:"GET",  
			async : false,
			cache: false,
            url:"${pageContext.request.contextPath}/caregiverSelector/deleteComment",  
            data:{id:id}, 
            dataType:"json",
            success: function(data){
            	alert(data.msg);
            	$(obj).parent().parent().remove();		//remove this comment
			},error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg("Server error")
            },
		});
	}
</script>