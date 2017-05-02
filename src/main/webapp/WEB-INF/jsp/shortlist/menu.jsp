<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="../header.jsp" />

  <div id="page-content">
    <div id='wrap'>
        <div id="page-heading">
            <ol class="breadcrumb">
                <li class='active'><a href="">Shortlist</a></li>
            </ol>

            <h1>Shortlist</h1>
        </div>


        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="row">
                    	<sec:authorize access="!hasRole('ROLE_HOSPITAL')">
	                    	<div class="col-md-6 col-xs-12 col-sm-6">
	                            <a class="info-tiles tiles-orange" href="${pageContext.request.contextPath}/shortlist/search?status=0">
	                                <div class="tiles-heading">All Caregivers</div>
	                                <div class="tiles-body-alt">
	                                    <i class="fa fa-group"></i>
	                                    <div class="text-center">${candidateStatusAmount.totalAmount}</div>
	                                    <small>caregivers joined</small>
	                                </div>
	                                <div class="tiles-footer">view all caregivers</div>
	                            </a>
	                        </div>

	                        <div class="col-md-6 col-xs-12 col-sm-6">
	                            <a class="info-tiles tiles-sky" href="${pageContext.request.contextPath}/shortlist/search?status=-1">
	                                <div class="tiles-heading">Newly Available</div>
	                                <div class="tiles-body-alt">
	                                    <i class="fa fa-group"></i>
	                                    <div class="text-center">${candidateStatusAmount.newAmount}</div>
	                                    <small>new caregivers available</small>
	                                </div>
	                                <div class="tiles-footer">view new available caregivers</div>
	                            </a>
	                        </div>
	                        
	                        <div class="col-md-6 col-xs-12 col-sm-6">
	                            <a class="info-tiles tiles-success" href="${pageContext.request.contextPath}/shortlist/search?status=9">
	                                <div class="tiles-heading">Contracted</div>
	                                <div class="tiles-body-alt">
	                                    <i class="fa fa-link"></i>
	                                    <div class="text-center">${candidateStatusAmount.contractedAmount}</div>
	                                    <small>caregivers contracted</small>
	                                </div>
	                                <div class="tiles-footer">view contracted caregivers</div>
	                            </a>
	                        </div>
	                        
	                        <div class="col-md-6 col-xs-12 col-sm-6">
	                            <a class="info-tiles tiles-toyo" href="${pageContext.request.contextPath}/shortlist/search?status=8">
	                                <div class="tiles-heading">Tagged</div>
	                                <div class="tiles-body-alt">
	                                    <i class="fa fa-lock"></i>
	                                    <div class="text-center">${candidateStatusAmount.taggedAmount}</div>
	                                    <small>caregivers tagged</small>
	                                </div>
	                                <div class="tiles-footer">view tagged caregivers</div>
	                            </a>
	                        </div>
                        </sec:authorize>
                        
                        <div class="col-md-6 col-xs-12 col-sm-6">
                            <a class="info-tiles tiles-alizarin" href="${pageContext.request.contextPath}/shortlist/search?status=7">
                                <div class="tiles-heading">Ready for Placement</div>
                                <div class="tiles-body-alt">
                                    <i class="fa fa-unlock"></i>
                                    <div class="text-center">${candidateStatusAmount.availableAmount}</div>
                                    <small>caregivers ready for Placement</small>
                                </div>
                                <div class="tiles-footer">view ready for placement caregivers</div>
                            </a>
                        </div>
                        
                        <sec:authorize access="!hasRole('ROLE_HOSPITAL')">
	                        <div class="col-md-6 col-xs-12 col-sm-6">
	                            <a class="info-tiles tiles-purple" href="${pageContext.request.contextPath}/shortlist/search?status=5">
	                                <div class="tiles-heading">On Hold</div>
	                                <div class="tiles-body-alt">
	                                    <i class="fa fa-unlock"></i>
	                                    <div class="text-center">${candidateStatusAmount.onHoldAmount}</div>
	                                    <small>caregivers on hold</small>
	                                </div>
	                                <div class="tiles-footer">view on hold caregivers</div>
	                            </a>
	                        </div>      
                        </sec:authorize>                    
                    </div>                 
                </div>
            </div>
			
		</div> <!-- container -->
    </div> <!--wrap -->
</div> <!-- page-content -->

    <footer role="contentinfo">
        <div class="clearfix">
            <ul class="list-unstyled list-inline pull-left">
                <li>Active Global Specialised Caregivers Pte Ltd &copy; 2016</li>
            </ul>
            <button class="pull-right btn btn-inverse-alt btn-xs hidden-print" id="back-to-top"><i class="fa fa-arrow-up"></i></button>
        </div>
    </footer>
</div>
</html>
