<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <title>Active Global Caregiver Resume</title>
    <style type="text/css">
        body {
            -webkit-text-size-adjust: none;
            -ms-text-size-adjust: none;
        }
        body {
            margin: 0;
            padding: 0;
        }
        @media only screen and (max-device-width: 480px),
        only screen and (max-width: 480px) {
            /* mobile-specific CSS styles go here */
            a img {
                border: none;
            }
            a[href^=tel"],
            a[href^="sms"],
            .notMobileLink {
                text-decoration: none;
                color: #333333;
                pointer-events: none;
                cursor: default;
            }
            img[class=lba] {
                width: 80% !important;
                height: auto !important;
            }
            img[class=logo] {
                width: 90% !important;
                height: auto !important;
            }
            img[class=edmbanner] {
                width: 100% !important;
            }
            img[class=promoimage] {
                width: 80% !important;
                height: auto !important;
                display: block;
            }
            img[class=safestbank] {
                float: none !important;
            }
            img[class=cards] {
                float: none !important;
            }
            img {
                outline: none;
                text-decoration: none;
                -ms-interpolation-mode: bicubic;
            }
            img[class=edmbottom] {
                width: 100% !important;
            }
            table[class=wrapper] {
                width: 100% !important;
            }
            table[class=brandbartable] {
                width: 100% !important;
            }
            table[class=contenttable] {
                float: none !important;
                width: 100% !important;
            }
            table[class=contentpromoimage] {
                float: none !important;
                width: 100% !important;
                margin: 10px 0 !important;
            }
            table[class=bottombar] {
                float: none !important;
                width: 100% !important;
                margin: 10px 0 !important;
            }
            td [class=contenttd] {
                padding: 10px 10px !important;
            }
            td[class=contentheader] {
                padding: 0px 10px !important;
            }
        }
    </style>
</head>

<body bgcolor="#F2F2F2" style="margin:0px;background-color:#F2F2F2;font-family: Arial, Helvetica, sans-serif;color:#333333;font-size:13px;">
    <table class="wrapper" border="0" align="center" cellpadding="0" cellspacing="0" style="width:600px">
        <tr>
            <td>
                <table class="brandbartable" width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="background-color:#ffffff;">
                    <tr>
                        <td align="left" style="background-color:#ffffff;"><a href="http://www.activeglobalcaregiver.com"><img src="${pageContext.request.contextPath}/resources/images/logo-active-global-caregiver.png" alt="" class="logo" border="0"/></a>
                        </td>
                        <td width="100%" height="21" class="contentheader" style="font-family: Arial, Helvetica, sans-serif;color:#000000;font-size:11px;font-weight:bold;padding-right:15px;" align="right"><a href="http://www.activeglobalcaregiver.com" target="_blank" class="notMobileLink" style="font-family: Arial, Helvetica, sans-serif;color:#000000;font-size:11px;font-weight:bold;text-decoration:none;:hover{color:#000000}">www.activeglobalcaregiver.com</a>
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background-color:#fff">
                    <tr>
                        <td style="padding:10px 15px;border-bottom:1px solid #f2f2f2;"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td style="background-color:#FFFFFF;font-size:13px;font-family:arial;line-height:20px">
                <br/>
                <table class="contenttable" cellspacing="0" cellpadding="0" border="0" width="100%">
                    <tr>
                        <td class="contenttd" style="padding:10px 15px;border-bottom:1px solid #f2f2f2;">
                            <table cellspacing="0" cellpadding="0" border="0" width="100%">
                                <tr>
                                    <td style="width: 100px;" valign="top">
                                        <table cellspacing="0" cellpadding="0" border="0" width="100%">
                                            <tr>
                                                <td>
                                                    <c:choose>
														<c:when test="${photoPath!= '' && photoPath != null}">
															<img src="${photoPath}" style="max-height: 100px" width="100px">
														</c:when>
														<c:otherwise>
															<img src="${pageContext.request.contextPath}/resources/img/no_image.png" style="max-height: 100px" width="100px">
														</c:otherwise>
													</c:choose>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td align="left" valign="top">
                                        <table cellspacing="0" cellpadding="0" border="0" width="100%">
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <p style="font:normal 15px/1.5em arial;font-weight: bold;border-bottom: 1px solid #f2f2f2;">
                                                    	${caregiver.fullName}
                                                    </p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Sex</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                	${caregiver.gender}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Highest education</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                	${caregiver.educationalLevel}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Years of experience</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                    ${caregiver.exp}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Availability</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                               		${caregiver.availability}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Languages spoken additionally to English</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                    ${caregiver.languages}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
	                                                    <sec:authorize access="hasAnyRole('ROLE_SALES_TW')">
	                                                    	<tr>
	                                                            <td style="width: 60%;">
	                                                                <p style="font:normal 13px/1.5em arial;">Expected salary in TWD</p>
	                                                            </td>
	                                                            <td style="width: 40%;">
	                                                                <p style="font:normal 13px/1.5em arial;">
	                                                                 ${caregiver.salaryTWD}</p>
	                                                            </td>
	                                                        </tr>
	                                                    </sec:authorize>
	                                                    
	                                                    <sec:authorize access="hasAnyRole('ROLE_SALES_HK')">
	                                                    	<tr>
	                                                            <td style="width: 60%;">
	                                                                <p style="font:normal 13px/1.5em arial;">Expected salary in HKD</p>
	                                                            </td>
	                                                            <td style="width: 40%;">
	                                                                <p style="font:normal 13px/1.5em arial;">
	                                                                 ${caregiver.salaryHKD}</p>
	                                                            </td>
	                                                        </tr>
	                                                    </sec:authorize>
	                                                    <sec:authorize access="!hasAnyRole('ROLE_SALES_TW','ROLE_SALES_HK')">
	                                                    	<tr>
	                                                            <td style="width: 60%;">
	                                                                <p style="font:normal 13px/1.5em arial;">Expected salary in SGD</p>
	                                                            </td>
	                                                            <td style="width: 40%;">
	                                                                <p style="font:normal 13px/1.5em arial;">
	                                                                 ${caregiver.salarySGD}</p>
	                                                            </td>
	                                                        </tr>
	                                                    </sec:authorize>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Marital status</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                	${caregiver.maritalStatus}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Age</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                    ${caregiver.age}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Date of birth</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                    ${caregiver.dateOfBirth}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Sibling Info</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                    ${caregiver.siblings}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Religion</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                              		${caregiver.religion}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Food choice</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                    ${caregiver.foodChoice}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Nationality</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                	${caregiver.nationality}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Place of birth</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                	${caregiver.countryOfBirth}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Has children?</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                    ${caregiver.hasChildren}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Height (cm)</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                               		${caregiver.height}
                                                               	</p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding: 5px 10px;">
                                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-bottom: 1px solid #f2f2f2;">
                                                        <tr>
                                                            <td style="width: 60%;">
                                                                <p style="font:normal 13px/1.5em arial;">Weight (kg)</p>
                                                            </td>
                                                            <td style="width: 40%;">
                                                                <p style="font:normal 13px/1.5em arial;">
                                                                	${caregiver.weight}
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                            <br>
                            <br>
                            <table cellspacing="0" cellpadding="0" border="0" width="100%">
                                <tr>
                                    <td style="text-align: left;" width="23">
                                        <img src="${pageContext.request.contextPath}/resources/img/assets-edm/blockquote.jpg">
                                    </td>
                                    <td style="text-align: left;">
                                        <p style="font:normal 14px/1.5em arial;font-style: italic;">
                                        	${caregiver.motivation}
                                        </p>
                                    </td>
                                </tr>
                            </table>
                            <br>
                            <p class="ptext" style="font:normal 14px/1.5em arial;text-transform: uppercase;font-weight: bold;">
                                <img src="${pageContext.request.contextPath}/resources/img/assets-edm/header-about.jpg">
                            </p>
                            <p class="ptext" style="font:normal 13px/1.5em arial;">
                                ${caregiver.bio.candidateBasicInformation}
                            </p>
                            <p class="ptext" style="font:normal 14px/1.5em arial;text-transform: uppercase;font-weight: bold;">
                                <img src="${pageContext.request.contextPath}/resources/img/assets-edm/header-education.jpg">
                            </p>
                            <p class="ptext" style="font:normal 13px/1.5em arial;">
                                 ${caregiver.bio.educationAndExperience}
                            </p>
                            <p class="ptext" style="font:normal 14px/1.5em arial;text-transform: uppercase;font-weight: bold;">
                                <img src="${pageContext.request.contextPath}/resources/img/assets-edm/header-certified.jpg">
                            </p>
                            <p class="ptext" style="font:normal 13px/1.5em arial;">
                            	${caregiver.bio.trainedToCprOrFA}
                            </p>
                            <p class="ptext" style="font:normal 14px/1.5em arial;text-transform: uppercase;font-weight: bold;">
                                <img src="${pageContext.request.contextPath}/resources/img/assets-edm/header-nursing-exp.jpg">
                            </p>
                            <p class="ptext" style="font:normal 13px/1.5em arial;">
                                ${caregiver.bio.nursingExperience}
                            </p>
                            <p class="ptext" style="font:normal 14px/1.5em arial;text-transform: uppercase;font-weight: bold;">
                                <img src="${pageContext.request.contextPath}/resources/img/assets-edm/header-hobbies.jpg">
                            </p>
                            <p class="ptext" style="font:normal 13px/1.5em arial;">
                                ${caregiver.bio.hobby}
                            </p>
                            <br>
                            <br>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding:20px 15px 20px 15px;font-family: Arial, Helvetica, sans-serif;font-size: 10px;font-weight: normal;color: #666666;">Active Global Specialised Caregivers</td>
                    </tr>
                    <tr>
                        <td style="padding:20px 15px 20px 15px;font-family: Arial, Helvetica, sans-serif;font-size: 14px;font-weight: bold;color: #666666; font-style: italic; text-align: justify;">Disclaimer: This biodata has been prepared by our team, to the best of our knowledge, based on the information provided by the candidate, and checked during our selection process. Please make sure to double-confirm things that are most important to you during the interview with the candidate. Please also take note that many caregivers do not eat pork or beef, despite declaring 'no food restrictions' in our questionnaire, so please check during your interview with them if this is important to you. </td>
                    </tr>
                    <tr>
                        <td style="padding:20px 15px 20px 15px;font-family: Arial, Helvetica, sans-serif;font-size: 10px;font-weight: normal;color: #666666;">Employee Registration Number: ${user.registrationNumber}</td>
                    </tr>                  
                    <tr>
                        <td style="padding:20px 15px 20px 15px;font-family: Arial, Helvetica, sans-serif;font-size: 10px;font-weight: normal;color: #666666;">Employment Agency Licence Number: 13C6324</td>
                    </tr>                    
                </table>
            </td>
        </tr>
    </table>
<form>
    <center><input type="button" value="Print this page" onClick="printMe();" id="print_btn"></center>
</form>    
</body>

</html>
<script>
function printMe(){
    window.print();
}
</script>