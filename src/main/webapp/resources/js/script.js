$(document).ready(function(){	
	$('#forgot_frm').validate({
			errorClass: "errormessage",
			onkeyup: false,
			errorClass: 'error',
			validClass: 'valid',
			rules: {
				nric: { required: true}											
			},
			messages: {
				nric: "Please enter your NRIC"						
			},
			errorPlacement: function(error, element)
			{
				QtipErrorPlacement(error, element);
			},
			submitHandler: function(form) {		   			
				
				$.post(site_url+"user/ajax/process_forgot_password",  $("#forgot_frm").serialize(), function(data) {								
					//console.log(data);
					if(data.status=='error')
					{
						showQtipMsg(data);
					}else
					{
						
						$("#forgot-password-container").html("<table width='100%'><tr><td align='center'><br /><br /><p class='success_msg'>"+data.msg+"</p>If you are facing any technical difficulties, please click <a href='"+site_url+"page/support'>here</a>.<br /><br /></td></tr><tr><td align='center'><a href='"+site_url+"' class='btn btn-primary'>Back</a></td></tr></table>");
						
					}					
				},'json');			
				return false;		
		 	},
			success: $.noop
	});

	$('#change_pwd_frm').validate({
			errorClass: "errormessage",
			onkeyup: false,
			errorClass: 'error',
			validClass: 'valid',
			rules: {
				password: { required: true, minlength: 6 },
				password2: { required: true, equalTo: '#password' }
				
			},
			messages: {
				password: {
					required: "Please enter your password.",
					minlength: "Passwords must contain at least 6 characters"
				},
				password2: {
					required: "Please enter your password.",
					minlength: "Passwords must contain at least 6 characters",
					equalTo: "Please enter the same password as above"
				}				
			},
			errorPlacement: function(error, element)
			{
				// Set positioning based on the elements position in the form
				var elem = $(element),
					corners = ['right center', 'left center'],
					flipIt = elem.parents('span.right').length > 0;

				// Check we have a valid error message
				if(!error.is(':empty')) {
					// Apply the tooltip only if it isn't valid
					elem.filter(':not(.valid)').qtip({
						overwrite: false,
						content: error,
						position: {
							my: corners[ flipIt ? 0 : 1 ],
							at: corners[ flipIt ? 1 : 0 ],
							viewport: $(window)
						},
						show: {
							event: false,
							ready: true
						},
						hide: false,
						style: {
							classes: 'qtip-red' // Make it red... the classic error colour!
						}
					})

					// If we have a tooltip on this element already, just update its content
					.qtip('option', 'content.text', error);
				}

				// If the error is empty, remove the qTip
				else { elem.qtip('destroy'); }
			},
			submitHandler: function(form) {		   			
				
				$.post(site_url+"user/ajax/process_change_password",  $("#change_pwd_frm").serialize(), function(data) {								
					//console.log(data);
					if(data.status=='error')
					{
						for (var key in data.msg) {
						   var obj = data.msg[key];
						   
						   var elem = $('#'+key),
							corners = ['right center', 'left center'],
							flipIt = elem.parents('span.right').length > 0;
						   
						   elem.qtip({
						   	overwrite: false,
							content: obj,
							position: {
								my: corners[ flipIt ? 0 : 1 ],
								at: corners[ flipIt ? 1 : 0 ],
								viewport: $(window)
							},
							show: {
								event: false,
								ready: true
							},
							hide: false,
							style: {
								classes: 'qtip-red' // Make it red... the classic error colour!
							}
							}).qtip('option', 'content.text', obj);
						}
						
					}else
					{
						
						$("#form_container").html("<table width='100%'><tr><td align='center'><br /><p class='success_msg'>"+data.msg+"</p><br />You are being redirected to the login page.<br />If you are not redirected in the next 5 seconds, please click <a href='"+site_url+"' class='btn btn-primary'>here</a> to proceed.<br /><br /></td></tr></table>");
						$("#form_container").scrollTop(0, 0);
						autoRedirect();
					}					
				},'json');			
				return false;		
		 	},
			success: $.noop
	});

	$('#first_time_frm').validate({
			errorClass: "errormessage",
			onkeyup: false,
			errorClass: 'error',
			validClass: 'valid',
			rules: {
				password: { required: true, minlength: 6 },
				password2: { required: true, equalTo: '#password' }
				
			},
			messages: {
				password: {
					required: "Please enter your password.",
					minlength: "Passwords must contain at least 6 characters"
				},
				password2: {
					required: "Please enter your password.",
					minlength: "Passwords must contain at least 6 characters",
					equalTo: "Please enter the same password as above"
				}				
			},
			errorPlacement: function(error, element)
			{
				// Set positioning based on the elements position in the form
				var elem = $(element),
					corners = ['right center', 'left center'],
					flipIt = elem.parents('span.right').length > 0;

				// Check we have a valid error message
				if(!error.is(':empty')) {
					// Apply the tooltip only if it isn't valid
					elem.filter(':not(.valid)').qtip({
						overwrite: false,
						content: error,
						position: {
							my: corners[ flipIt ? 0 : 1 ],
							at: corners[ flipIt ? 1 : 0 ],
							viewport: $(window)
						},
						show: {
							event: false,
							ready: true
						},
						hide: false,
						style: {
							classes: 'qtip-red' // Make it red... the classic error colour!
						}
					})

					// If we have a tooltip on this element already, just update its content
					.qtip('option', 'content.text', error);
				}

				// If the error is empty, remove the qTip
				else { elem.qtip('destroy'); }
			},
			submitHandler: function(form) {		   			
				
				$.post(site_url+"user/ajax/process_first_change_password",  $("#first_time_frm").serialize(), function(data) {								
					//console.log(data);
					if(data.status=='error')
					{
						for (var key in data.msg) {
						   var obj = data.msg[key];
						   
						   var elem = $('#'+key),
							corners = ['right center', 'left center'],
							flipIt = elem.parents('span.right').length > 0;
						   
						   elem.qtip({
						   	overwrite: false,
							content: obj,
							position: {
								my: corners[ flipIt ? 0 : 1 ],
								at: corners[ flipIt ? 1 : 0 ],
								viewport: $(window)
							},
							show: {
								event: false,
								ready: true
							},
							hide: false,
							style: {
								classes: 'qtip-red' // Make it red... the classic error colour!
							}
							}).qtip('option', 'content.text', obj);
						}
						
					}else
					{
						
						$("#form_container").html("<table width='100%'><tr><td align='center'><br /><p class='success_msg'>"+data.msg+"</p><br />You are being redirected to the login page.<br />If you are not redirected in the next 5 seconds, please click <a href='"+site_url+"' class='btn btn-primary'>here</a> to proceed.<br /><br /></td></tr></table>");
						$("#form_container").scrollTop(0, 0);
						location.reload();
					}					
				},'json');			
				return false;		
		 	},
			success: $.noop
	});
});

function autoRedirect()
{
	window.setTimeout(function() {
		window.location.href = 'login';
	}, 5000);
}


function showQtipMsg(data)
{

	if(typeOf(data.msg)=='object')
	{
		for (var key in data.msg) {
		   var obj = data.msg[key];
		   
		   var elem = $('#'+key),
			corners = ['right center', 'left center'],
			flipIt = elem.parents('span.right').length > 0;
		   
		   elem.qtip({
		   	overwrite: false,
			content: obj,
			position: {
				my: corners[ flipIt ? 0 : 1 ],
				at: corners[ flipIt ? 1 : 0 ],
				viewport: $(window)
			},
			show: {
				event: false,
				ready: true
			},
			hide: false,
			style: {
				classes: 'qtip-red' // Make it red... the classic error colour!
			}
			}).qtip('option', 'content.text', obj);
		}

	}else{
			console.log(typeOf(data.msg));
	}
}

function QtipErrorPlacement(error, element)
{
	// Set positioning based on the elements position in the form
	var elem = $(element),
		corners = ['right center', 'left center'],
		flipIt = elem.parents('span.right').length > 0;

	// Check we have a valid error message
	if(!error.is(':empty')) {
		// Apply the tooltip only if it isn't valid
		elem.filter(':not(.valid)').qtip({
			overwrite: false,
			content: error,
			position: {
				my: corners[ flipIt ? 0 : 1 ],
				at: corners[ flipIt ? 1 : 0 ],
				viewport: $(window)
			},
			show: {
				event: false,
				ready: true
			},
			hide: false,
			style: {
				classes: 'qtip-red' // Make it red... the classic error colour!
			}
		})

		// If we have a tooltip on this element already, just update its content
		.qtip('option', 'content.text', error);
	}

	// If the error is empty, remove the qTip
	else { elem.qtip('destroy'); }
}


function typeOf(value) {
	    var s = typeof value;
	    if (s === 'object') {
	        if (value) {
	            if (value instanceof Array) {
	                s = 'array';
	            }
	        } else {
	            s = 'null';
	        }
	    }
	    return s;
	}


function submit_form(frm)
{
	$('#'+frm).submit();
}