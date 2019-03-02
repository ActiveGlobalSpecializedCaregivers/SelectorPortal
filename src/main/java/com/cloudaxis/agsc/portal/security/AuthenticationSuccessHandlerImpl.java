package com.cloudaxis.agsc.portal.security;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.cloudaxis.agsc.portal.model.User;

/**
 * Class to determine the behavior following a successful login attempt
 * 
 * @author Mike
 *
 */
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {

		HttpSession session = request.getSession();

		// Set some session variables
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		session.setAttribute("userId", authUser.getUserId());
		session.setAttribute("firstName", authUser.getFirstName());
		session.setAttribute("lastName", authUser.getLastName());
		session.setAttribute("username", authUser.getUsername());
		session.setAttribute("email", authUser.getEmail());
		
		// Retrieve information for the Dashboard display

		// Set target URL to redirect
		String targetUrl = determineTargetUrl(authentication);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	/**
	 * Determines the page to redirect to according to the roles of the user.
	 * 
	 * @param authentication
	 * @return
	 */
	protected String determineTargetUrl(Authentication authentication) {
		Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		if (authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_SUB_ADMIN") || authorities.contains("ROLE_RECRUITER") || authorities.contains("ROLE_PH_RECRUITING_PARTNER") ) {
			return "/candidates"; 
		}
		else if (authorities.contains("ROLE_SALES") || authorities.contains("ROLE_HOSPITAL") || authorities.contains("ROLE_SALES_SG") || authorities.contains("ROLE_SALES_HK") || authorities.contains("ROLE_SALES_TW")) {
			return "/shortlist/getStatusAmount";
		}
		else {
			throw new IllegalStateException();
		}
	}

	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
}

