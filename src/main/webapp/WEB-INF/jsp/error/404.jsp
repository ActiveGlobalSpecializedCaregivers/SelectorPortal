
<jsp:include page="../header.jsp" />

        <div id="page-content" style="max-height: 850px;">
		    <div id='wrap'>
		    
                <!-- Page Heading -->
                <div class="row">
                    <div class="col-lg-12 col-lg-offset-3">
                        <h1 class="page-header">
                            Whoops...
                        </h1>
                    </div>
                </div>
                <!-- /.row -->
                
                                <!-- Error Panel -->
                <div class="row">
                    <div class="col-lg-4 col-lg-offset-3">
		                 <div class="form-group">
								    <div class="panel panel-danger">
								        <div class="panel-heading">
								            <h3 class="panel-title">Page Not Found</h3>
								        </div>	
								        <div class="panel-body">
								        The page you are looking for doesn't seem to exist. <br><br>
								        Click below to return to the previous page. <br><br>
									      			<a class="btn btn-sm btn-danger" href="${pageContext.request.contextPath}" role="button">Go Back...</a>       
								        
										</div>			 
	                               </div>		                               
						</div>
                    </div>
                </div>
                <!-- /.row -->
        </div>
    </div>

 <jsp:include page="../footer.jsp" />
 