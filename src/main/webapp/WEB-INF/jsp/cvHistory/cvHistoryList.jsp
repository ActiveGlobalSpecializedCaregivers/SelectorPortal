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
			<h1>CV Sent history</h1>
		</div>
		<div class="container">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
				<div class="row">
					<div class="col-md-8">
						<table class="table table-hover" id="sendCV">
							<thead>
								<tr style="background: grey">
									<th style="color: white">Name</th>
									<th style="color: white">Addressee</th>
									<th style="color: white">Date</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list}" var="sendCV">
									<tr>
										<td>${sendCV.fullName}</td>
										<td>${sendCV.sentTo}</td>
										<td><fmt:formatDate value="${sendCV.date}"
												type="both" pattern="dd/MM/yyyy" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
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
</html>

<script type="text/javascript">
	$(document).ready(function() {
		$("#sendCV").DataTable({
			bLengthChange : false,
			"aaSorting": [[ 2, "desc" ]],  
		 	columnDefs: [
              { type: 'date-eu', targets: 2 }
            ]
		});	
	});

</script>