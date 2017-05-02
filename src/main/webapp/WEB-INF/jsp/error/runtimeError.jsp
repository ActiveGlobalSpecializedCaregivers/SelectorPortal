
<jsp:include page="../header.jsp" />

    <div id="wrapper">

<jsp:include page="../navAndSidebar.jsp" />

        <div id="page-wrapper">

            <div class="container-fluid">


                <!-- Page Heading -->
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">
                            Whoops...
                        </h1>
                    </div>
                </div>
                <!-- /.row -->


                <!-- Error Panel -->
                <div class="row">
                    <div class="col-lg-8">
		                 <div class="form-group">
								    <div class="panel panel-danger">
								        <div class="panel-heading">
								            <h3 class="panel-title">${exceptionCause}</h3>
								        </div>	
								        <div class="panel-body">								        
								        An error just occurred. Sorry about that. <br><br>
								        
								        This problem has been logged and will be analyzed by the Cloudaxis tech team. <br><br>
								        
								        Click below to return to the previous page. <br><br>
									      			<a class="btn btn-sm btn-danger" href="javascript:history.back();" role="button">Go Back...</a>       
								        
										</div>			 
	                               </div>		                               
						</div>
                    </div>
                </div>
                <!-- /.row -->

            </div>
        </div>

    </div>

 <jsp:include page="../footer.jsp" />
 