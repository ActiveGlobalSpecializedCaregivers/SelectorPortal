<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

        <nav id="page-leftbar" role="navigation">
                <!-- BEGIN SIDEBAR MENU -->
           
            <ul class="acc-menu" id="sidebar">
                <!-- <li id="search">
                    <a href="javascript:;"><i class="fa fa-search opacity-control"></i></a>
                     <form>
                        <input type="text" id="searchbox" class="search-query" placeholder="Search...">
                        <button type="submit"><i class="fa fa-search"></i></button>
                    </form>
                </li> -->
                <li class="divider"></li>
                
                <sec:authorize access="!hasAnyRole('ROLE_HOSPITAL')">
                	<li><a href="${pageContext.request.contextPath}/candidates"><i class="fa fa-home"></i> <span>Applicants List</span></a></li>
                </sec:authorize>
                
                <sec:authorize access="!hasAnyRole('ROLE_PH_RECRUITING_PARTNER')">
                	<li><a href="${pageContext.request.contextPath}/shortlist/getStatusAmount"><i class="fa fa-user"></i> <span>Dashboard</span></a></li>
                </sec:authorize>
                
                <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUB_ADMIN','ROLE_RECRUITER')">
                	<li><a href="${pageContext.request.contextPath}/admin/users/sent_history"><i class="fa fa-envelope"></i> <span>CV Sent History</span></a></li>
                	<sec:authorize access="!hasAnyRole('ROLE_RECRUITER')">
	                	<li><a href="${pageContext.request.contextPath}/emailTemplates/setEmailTemplate"><i class="fa fa-cogs"></i> <span>Automated Emails</span> </a></li>
	                </sec:authorize>
				</sec:authorize>
				
				<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUB_ADMIN')">
	                <li class="divider"></li>
	                <li><a href="${pageContext.request.contextPath}/admin/users"><i class="fa fa-key"></i> <span>Admin Users</span> </a></li>
				</sec:authorize>
            </ul>
            <!-- END SIDEBAR MENU -->
        </nav>