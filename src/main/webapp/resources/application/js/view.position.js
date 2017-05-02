$(document).ready(function() {
    if (typeof DV_RESPONSIVE_LAYOUT !== 'undefined' && DV_RESPONSIVE_LAYOUT) {
        var $forwardFields  = $('#applicant-job-forward-form .form-control'),
            emailRegex      = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;

        $('#applicant-btn-send-forward').click(function() {
            var applicantErrors = false;

            $forwardFields.each(function() {
                var $input     = $(this),
                    $formGroup = $input.closest('.form-group'),
                    value      = $input.val().trim(),
                    error      = false;

                switch ($(this).attr('id')) {
                    case 'applicant_forward_message':
                        // this is pretty naive but models the existing server-side validation
                        error = value.indexOf('www') > -1 || value.indexOf('http') > -1;
                        break;
                    case 'applicant_forward_email_recipient':
                    case 'applicant_forward_email_sender':
                        error = !emailRegex.test(value);
                    default:
                        error = error || value == '';
                        break;
                }

                if (error) {
                    $formGroup.addClass('has-error');
                    applicantErrors = true;
                } else {
                    $formGroup.removeClass('has-error');
                    applicantErrors = applicantErrors || false;
                }
            });

            if (applicantErrors) {
                return false;
            } else {
                return true;
            }
        });
    } else {
        $('#applicant-forward-button').click(function(){
            $("#applicant-job-forward-form").slideDown('fast');
            $("#applicant-job-button-email").addClass("none");
            return false;
        });
        
        $('#applicant-btn-cancel-forward').click(function(){
            $("#applicant-job-forward-form").slideUp("fast");
            $("#applicant-job-button-email").removeClass("none");
            return false;
        });
    }
});