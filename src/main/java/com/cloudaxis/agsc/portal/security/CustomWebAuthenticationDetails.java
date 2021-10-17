package com.cloudaxis.agsc.portal.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Extension that retrieves verification code from the request.
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String verificationCode;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        verificationCode = request.getParameter("verification_code");
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
