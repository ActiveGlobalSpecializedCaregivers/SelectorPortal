<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<jsp:include page="../header.jsp" />

<div id="page-content" style="overflow: auto; max-height: 850px;">
	<div id='wrap'>
		<div id="page-heading">
			<h1>Users</h1>
		</div>
		<div class="container">
			<form id="usersListForm">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
				<div class="row">
					<div class="col-md-8">
						<sec:authorize access="hasRole('ROLE_ADMIN')">
							<div style="float: right; margin-bottom: 5px">
								<button class="btn btn-danger" type="button"
										onclick="deleteUser()">
									<i class="fa fa-times"
									   style="margin-right: 5px; line-height: 2;"></i>Delete
								</button>
								<button class="btn btn-success"
										onclick="createUser()">
									<i class="fa fa-plus" style="margin-right: 5px; line-height: 2;"></i>Create
								</button>
							</div>
						</sec:authorize>
						<table class="table table-hover" id="users">
							<thead>
								<tr style="background: grey">
									<th class="t-c"><input type="checkbox" id="checkBoxAll" /></th>
									<th style="color: white">USERNAME</th>
									<th style="color: white">EMAIL</th>
									<th style="color: white">ENABLED</th>
									<th style="color: white">ROLE</th>
									<th style="color: white">REGISTRATION NUMBER</th>
									<th style="color: white">DATE CREATED</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${users}" var="user">
									<tr>
										<td class="t-c">
											<c:if test="${user.username != sessionScope.username}">
												<input id="username" type="checkbox"
												class="chkCheckboxId" value="${user.username}"
												name="username" />
											</c:if>
										</td>
										<td><a href="${contextPath}/user/edit?userId=${user.userId}">${user.username}</a></td>
										<td>${user.email}</td>
										<td>${user.enabled}</td>
										<td>${user.role}</td>
										<td>${user.registrationNumber}</td>
										<td><fmt:formatDate value="${user.createDate}"
												type="both" pattern="dd/MM/yyyy" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

					</div>
				</div>
			</form>

		</div>


		<!-- container -->
	</div>
	<!--wrap -->
</div>
<!-- page-content -->

<footer role="contentinfo">
	<div class="clearfix">
		<ul class="list-unstyled list-inline pull-left">
			<li>Active Global Specialised Caregivers Pte Ltd &copy; 2016</li>
		</ul>
		<button class="pull-right btn btn-inverse-alt btn-xs hidden-print"
			id="back-to-top">
			<i class="fa fa-arrow-up"></i>
		</button>
	</div>
</footer>

<style type="text/css" media="all">
@import
	url("${pageContext.request.contextPath}/resources/application/css/main.css?o203kp")
	;
</style>
</html>

<script>
	//Delete all User Checkbox
	$(document).ready(function() {
		$('#checkBoxAll').click(function() {
			if ($(this).is(":checked"))
				$('.chkCheckboxId').prop('checked', true);
			else
				$('.chkCheckboxId').prop('checked', false);
		});
	});

	$("#has_notif")
			.click(
					function() {

						$('#badge').text("0");
						$('#badge2').text("0");

						$
								.post(
										"http://localhost:8080/agsc-portal/admin/ajax/update_status_notification",
										{
											"user_id" : 1127
										}, function(data) {

											if (data.status == "success") {
												check_notif();

												$('#badge').text(
														data.badge_count);
												$('#badge2').text(
														data.badge_count);
											}
										}, 'json');
					});

	var polling;

	$(document).ready(
			function() {
				//universal search
				$("#searchbox").keypress(
						function(e) {
							if (e.which == 13) {
								var search_key = $('#searchbox').val();

								window.location = site_url
										+ "selector/shortlist/?search="
										+ escape(search_key);

								return false;
							}
						});

				$('#users').DataTable({
					bLengthChange : false,
					"searching" : false,
					"aaSorting": [				//sort 3:means the fourth column
           	             [ 5, "desc" ]
           	         ],
					"aoColumnDefs": [
			   			{ "bSortable": false, "aTargets": [ 0 ] }
			   		]
				//each page display
				});
			});

	function check_notif() {

		$.ajax({
			type : "GET",
			url : site_url + "admin/ajax/check_notifications",
			async : true,
			cache : false,
			timeout : 50000,
			success : function(data) {
				if (data.status == 'success') {
					clearTimeout(polling);
					// $("body").html(data);
				}

				polling = setTimeout(check_notif, 5000);
			}
		});

	};

	function deleteUser() {
		 if (!$('#usersListForm input[type="checkbox"]').is(':checked')) {
			 msgAlert("Please select at least one User!");
			return false;
		}
		else{			
			bootbox.confirm({  
				buttons: {  
				    confirm: {  
				        label: 'Yes'  
				    },  
				    cancel: {  
				        label: 'Cancel'
				    }  
				},  
				message: 'Are you sure want to delete?', 
				callback: function(result) {
					if(result){
						$("#usersListForm").attr({
							action : "${contextPath}/user/delete",
							method : "POST"
						});
						$("#usersListForm").submit();					
					}
				},
				title:'Delete User(s)',
			});
			
			window.location.hash = "#";						
		}
		
	}

	function createUser() {
		$("#usersListForm").attr({
			action : "${contextPath}/user/create",
			method : "GET"
		});
	}

	function msgAlert(msg) {
		bootbox.alert({
			buttons : {
				ok : {
					label : 'ok',
				}
			},
			message : msg,
			title : "Delete User",
		});
	}
</script>