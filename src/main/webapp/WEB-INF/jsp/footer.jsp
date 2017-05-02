    <footer role="contentinfo">
        <div class="clearfix">
            <ul class="list-unstyled list-inline pull-left">
                <li>Active Global Specialised Caregivers Pte Ltd &copy; 2016</li>
            </ul>
            <button class="pull-right btn btn-inverse-alt btn-xs hidden-print" id="back-to-top"><i class="fa fa-arrow-up"></i></button>
        </div>
    </footer>

</div>
	<!-- /#Log out form -->        
	<form id="logoutForm" action="/portal/logout" method="post">
		 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>

		<jsp:include page="scripts.jsp" />
</body>
</html>

<script>
/*
$( "#has_notif" ).click(function() {
    
    $('#badge').text("0");
    $('#badge2').text("0");


    $.post("<?php echo site_url('admin/ajax/update_status_notification');?>", {"user_id":<?php echo $this->session->userdata('agr_user_id'); ?>}, function(data) {                                
        
        if(data.status=="success")
        {
            check_notif();

            $('#badge').text(data.badge_count);
            $('#badge2').text(data.badge_count);
        } 
    },'json');    
});

*/
var polling;

$(document).ready(function(){
    //universal search
    $("#searchbox").keypress(function(e) {
        if(e.which == 13) {
            var search_key = $('#searchbox').val();

            window.location = site_url+"admin/users/caregivers/?search=" + escape(search_key);

            return false;
        }
    });


});


function check_notif(){

    $.ajax({
        type: "GET",
        url: site_url+"admin/ajax/check_notifications",
        async: true,
        cache: false,
        timeout:50000,
        success: function(data){
            if(data.status=='success')
            {
                clearTimeout(polling); 
                // $("body").html(data);
            }          

            polling = setTimeout(
                check_notif, 
                5000 
            );
        }
    });    

};
</script>
