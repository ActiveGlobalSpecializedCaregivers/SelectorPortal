var qtips = [];
$(document).ready(function(){

    /* Set datepickers */
    if ($('.datefield, .datepopup').length != 0) {
        $('.datefield, .datepopup').datepicker({dateFormat: "yy-mm-dd"});
    }

    $('a.btn_collapse').click(function(){ $(this).parents('.dv_message').slideUp('fast'); });
    $('a.btn_collapse_msg').click(function(){ $(this).parents('.dv_message').slideUp('fast'); });


    function postMessage(action, location) {
        if (window != window.top && typeof window.top.postMessage == 'function') {
            window.top.postMessage({action: action}, location);
        }
    }

    if (typeof $.fn.qtip != 'undefined') {
        /* $et styles for qtips */
        $.fn.qtip.styles.applicant = {
            padding: 15,
            color: '#666',
            background: '#FFF',
            width: {
                min: 250
            },
            border: {
                width: 1,
                radius: 5,
                color: '#7773A7'
            },
            tip: 'topLeft',
            name: 'light'
        }

        $.fn.qtip.styles.applicant_tour = {
            padding: 15,
            color: '#4D4D4D',
            background: '#FFFCA5',
            width: {
                min: 250
            },
            border: {
                width: 5,
                color: '#FFDD00'
            },
            tip: 'topLeft',
            name: 'light'
        }

        $.fn.qtip.styles.modal = {
            padding: 0,
            color: '#4D4D4D',
            background: '#FFF',
            width: {
                min: 400,
                max: 900
            },
            border: {
                width: 0
            },
            name: 'light'
        }

        /* Make default adjustments to qtip */
        $.fn.qtip.defaults.position.corner.target = "bottomcenter";
        $.fn.qtip.defaults.position.adjust = {
            x: 0,
            y: -10,
            'screen': true
        }

        /* Get the button widgets */
        $('#btn_get_widgets').qtip({
            content: $('#widgetbox'),
            position: {
                target: $('body'),
                corner: {
                    target: 'topMiddle',
                    tooltip: 'topMiddle'
                },
                adjust: {
                    y: 75
                }
            },
            show: { when: 'click', solo: true },
            hide: { when: 'unfocus' },
            style: 'modal',
            api: {
                beforeShow: function() {
                    postMessage('modalShow', getLocationOrigin());
                    $('#dimmer').show();
                },
                beforeHide: function() {
                    postMessage('modalHide', getLocationOrigin());
                    $('#dimmer').hide();
                }
            }
        });
        $('#btn_get_widgets').click(function(){
            return false;
        });

        $('.link_request_demo').qtip({
            content: $('#div_request_demo').clone(),
            position: {
                target: $('#wrapper'),
                corner: {
                    target: 'topMiddle',
                    tooltip: 'topMiddle'
                },
                adjust: {
                    y: 60
                }
            },
            show: { when: 'click', solo: true },
            hide: { when: 'unfocus' },
            style: {
                name: 'modal',
                width: 500
            },
            api: {
                beforeShow: function(){
                    postMessage('modalShow', getLocationOrigin());
                    show_dimmer(); window.scrollTo(0,0);
                },
                onShow: function(){
                    this.elements.content.find('.inneriframe')
                        .attr('src','/home/demo?url='+document.URL)
                        .css('position','absolute')
                        .css('top','-290px')
                        .css('left','-50px')
                        .css('width','990px');
                },
                beforeHide: function(){
                    postMessage('modalHide', getLocationOrigin());
                    $('#dimmer').hide();
                }
            }
        });
        $('.link_request_demo').click(function(){
            return false;
        });
    }

    /* Close the widgetbox */
    $('#close_widgetbox').click(function(){
        $('#btn_get_widgets').qtip('hide');
        return false;
    });

    $('#nav_refer, .refer_link').click(function(){
        refer_a_friend(10);
        return false;
    });

    set_dimmer();

});

function set_dimmer(){
    if( $('#dimmer').is(':visible') ) {
        dimmer_visible = true;
    }else{
        dimmer_visible = false;
    }
    $('#dimmer').remove();
    $('<div id="dimmer">')
        .css({
            position: 'fixed',
            top: 0,
            left: 0,
            height: $(document).height(),
            width: '100%',
            opacity: 0.7,
            backgroundColor: '#000',
            zIndex: 5600
            })
        .appendTo(document.body)
        .addClass('none');
    if(dimmer_visible){
        $('#dimmer').fadeIn('fast');
    }
}

function show_dimmer(){
    doc_height = Math.max(
        Math.max(document.body.scrollHeight, document.documentElement.scrollHeight),
        Math.max(document.body.offsetHeight, document.documentElement.offsetHeight),
        Math.max(document.body.clientHeight, document.documentElement.clientHeight)
    );
    document.getElementById('dimmer').style.height = doc_height + 'px';
    $('#dimmer').show();
}

function urlencode(str) {
    //       discuss at: http://phpjs.org/functions/urlencode/
    //      original by: Philip Peterson
    //      improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    //      improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    //      improved by: Brett Zamir (http://brett-zamir.me)
    //      improved by: Lars Fischer
    //         input by: AJ
    //         input by: travc
    //         input by: Brett Zamir (http://brett-zamir.me)
    //         input by: Ratheous
    //      bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    //      bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    //      bugfixed by: Joris
    // reimplemented by: Brett Zamir (http://brett-zamir.me)
    // reimplemented by: Brett Zamir (http://brett-zamir.me)
    //             note: This reflects PHP 5.3/6.0+ behavior
    //             note: Please be aware that this function expects to encode into UTF-8 encoded strings, as found on
    //             note: pages served as UTF-8
    //        example 1: urlencode('Kevin van Zonneveld!');
    //        returns 1: 'Kevin+van+Zonneveld%21'
    //        example 2: urlencode('http://kevin.vanzonneveld.net/');
    //        returns 2: 'http%3A%2F%2Fkevin.vanzonneveld.net%2F'
    //        example 3: urlencode('http://www.google.nl/search?q=php.js&ie=utf-8&oe=utf-8&aq=t&rls=com.ubuntu:en-US:unofficial&client=firefox-a');
    //        returns 3: 'http%3A%2F%2Fwww.google.nl%2Fsearch%3Fq%3Dphp.js%26ie%3Dutf-8%26oe%3Dutf-8%26aq%3Dt%26rls%3Dcom.ubuntu%3Aen-US%3Aunofficial%26client%3Dfirefox-a'
    str = (str + '')
    .toString();

    // Tilde should be allowed unescaped in future versions of PHP (as reflected below), but if you want to reflect current
    // PHP behavior, you would need to add ".replace(/~/g, '%7E');" to the following.
    return encodeURIComponent(str)
        .replace(/!/g, '%21')
        .replace(/'/g, '%27')
        .replace(/\(/g, '%28')
        .replace(/\)/g, '%29')
        .replace(/\*/g, '%2A')
        .replace(/%20/g, '+')
        .replace(/~/g, '%7E');
}

function setCookie(c_name,value,expiredays,path){
    var exdate=new Date();
    exdate.setDate(exdate.getDate()+expiredays);
    document.cookie=c_name+ "=" +escape(value)+
    ((expiredays==null) ? "" : ";expires="+exdate.toGMTString())+
    ";path="+path;
    return false;
}

function check_submission_form(){
    $('.signup_error').remove();
    applicant_errors = false;
    applicant_firstname = $("#usr_first_name");
    applicant_lastname = $("#usr_last_name");
    applicant_email = $("#usr_email");
    applicant_phone = $("#usr_phone");
    applicant_customer = $("#customer_company_name");
    applicant_subdomain = $("#customer_subdomain");
    usr_password = $("#usr_password");
    usr_password_confirm = $("#usr_password_confirm");
    demo_jobs = $("#demo_jobs");
    var $customer_size = $("#customer_size");

    if( applicant_customer.attr("value") == "" ){
        add_signup_error(applicant_customer,"Enter your company name");
        applicant_errors = true;
    }
    if( applicant_subdomain.attr("value") == "" ){
        add_signup_error(applicant_subdomain,"Choose a customer subdomain");
        applicant_errors = true;
    }
    if( applicant_firstname.attr("value") == "" ){
        add_signup_error(applicant_firstname,"Enter your first name");
        applicant_errors = true;
    }
    if( applicant_lastname.attr("value") == "" ){
        add_signup_error(applicant_lastname,"Enter your last name");
        applicant_errors = true;
    }
    /*
    if( applicant_phone.attr("value") == "" ){
        add_signup_error(applicant_phone,"Enter your phone number");
        applicant_errors = true;
    }
    */
    if( !check_email( applicant_email.attr("value") ) ){
        add_signup_error(applicant_email,"Enter a valid email address");
        applicant_errors = true;
    }
    if( usr_password.attr("value") == "" || usr_password.attr("value").length < 7 || usr_password.attr("value").length > 30 ){
        add_signup_error(usr_password,"Enter a password between 8 and 30 characters");
        applicant_errors = true;
    }

    if ($customer_size.val() == 0) {
        add_signup_error($customer_size, "Please select a customer size.");
        applicant_errors = true;
    }

    /*
    if( usr_password.attr("value") == "" || usr_password_confirm.attr("value") !== usr_password.attr("value") ){
        add_signup_error(usr_password_confirm,"Confirm the password by retyping it here");
        applicant_errors = true;
    }
    */
    /*
    if( demo_jobs.attr("value") == 0 ){
        add_signup_error(demo_jobs,"Select the number of open positions");
        applicant_errors = true;
    }
    */
    if( typeof DV_CHECK_CC != 'undefined' && DV_CHECK_CC ){
        applicant_cc_number = $("#cc_number");
        applicant_cc_expire_year = $("#cc_expire_year");
        applicant_cc_expire_month = $("#cc_expire_month");
        applicant_cc_postal = $("#cc_postal");
        if( applicant_cc_number.attr("value") == "" ){
            add_signup_error(applicant_cc_number,"Enter your credit card number");
            applicant_errors = true;
        }
        if( applicant_cc_postal.attr("value") == "" ){
            add_signup_error(applicant_cc_postal,"Enter the card ZIP of postal code");
            applicant_errors = true;
        }
    }
    if( applicant_errors ){
        alert('Sorry - we found a few errors you need to fix!');
        return false;
    }else{
        return true;
    }
}

function add_signup_error( field, msg ){
    $(field).before('<p class="signup_error">'+msg+'.</p>');
}

function add_demo_error( field, msg ){
    $(field).before('<p class="demo_error">'+msg+'.</p>');
}

function check_email(email_address){
    if(email_address.search(/^[a-zA-Z0-9_\+]+((-[a-zA-Z0-9_\+]+)|(\.[a-zA-Z0-9_\+]+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) == -1) {
        return false;
    }else{
        return true;
    }
}

function urlparams(params){
    var result = '';
    for(var i = 0; i < params.length; i++){
        if( params[i] ){
            result += '/'+urlencode($(params[i]).attr('value'));
        }else{
            result +='/';
        }
    }
    return result;
}

function refer_a_friend(id){
    var width  = 860;
    var height = 640;
    var left   = (screen.width  - width)/2;
    var top    = 50;
    var params = 'width='+width+', height='+height;
    params += ', top='+top+', left='+left;
    params += ', location=no';
    params += ', menubar=no';
    params += ', resizable=no';
    params += ', scrollbars=no';
    params += ', status=no';
    params += ', toolbar=no';
    newwin=window.open('http://theapplicant.sendvite.com/share?b='+id,'sendviteWindow', params);
    if (window.focus) {
        newwin.focus()
    }
    return false;
}

function check_demo_form(){
    $('.demo_error').remove();
    demo_name = $("#demo_name");
    demo_company = $("#demo_company");
    demo_email = $("#demo_email");
    demo_phone = $("#demo_phone");
    demo_jobs = $("#demo_jobs");
    demo_industry = $("#demo_industry");
    demo_country = $("#demo_country");
    //demo_title = $("#demo_title");
    demo_form_errors = false;
    if( demo_name.attr("value") == "" ){
        add_demo_error(demo_name,"Enter your name");
        demo_form_errors = true;
    }
    if( demo_company.attr("value") == "" ){
        add_demo_error(demo_company,"Enter your company name");
        demo_form_errors = true;
    }
    if( !check_email( demo_email.attr("value") ) ){
        add_demo_error(demo_email,"Enter a valid email address");
        applicant_errors = true;
    }
    if( demo_phone.attr("value") == "" ){
        add_demo_error(demo_phone,"Enter your phone number");
        applicant_errors = true;
    }
    /*
    if( demo_title.attr("value") == "" ){
        add_demo_error(demo_title,"Enter your job title");
        applicant_errors = true;
    }*/
    if( demo_industry.attr("value") == 0 ){
        add_demo_error(demo_industry,"Please make a selection below");
        applicant_errors = true;
    }
    if( demo_jobs.attr("value") == 0 ){
        add_demo_error(demo_jobs,"Please make a selection below");
        applicant_errors = true;
    }
    if( demo_country.attr("value") == 0 ){
        add_demo_error(demo_country,"Please make a selection below");
        applicant_errors = true;
    }

    if( demo_form_errors ){
        return false;
    }else{
        return true;
    }
}

function resizeDemoForm(){
    $('.outerdiv').css('height','500px');
}