package com.cloudaxis.agsc.portal.exception;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//import com.ai.exception.BusinessException;
//import com.ai.exception.InvalidIDException;
//import com.ai.exception.ValidatorException;

public class ExceptionResolver implements HandlerExceptionResolver {

	protected Logger logger = Logger.getLogger(ExceptionResolver.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		// TODO make this pretty. This error shows up in the ticket comment and
		// new comments pages. Apparently it is erratic
		// and well known error. Comes in as a HTTP 422 Unprocessable Entity
		/*
		 * if (ex instanceof org.zendesk.client.v2.ZendeskResponseException){
		 * model.addAttribute("exceptionMessage", ex.getMessage()); }
		 */

		if (ex == null) {
			ex = new Exception("A Generic Exception occurred and is being handled in the ExceptionResolver");
		}

		request.setAttribute("Exception", ex);

		logger.error("--ExceptionResolver printout:", (ex.getCause() != null ? ex.getCause() : ex));
		logger.error(Arrays.toString(ex.getStackTrace()));

		ModelMap model = new ModelMap();
		model.addAttribute("exceptionMessage", ex.getMessage());
		model.addAttribute("exceptionCause", (ex.getCause() != null ? ex.getCause() : ex));

		if (ex instanceof org.springframework.security.access.AccessDeniedException) {
			// Access denied exception
			return new ModelAndView("error/accessDenied");
		}
		else {
			// other runtime exception
			return new ModelAndView("error/runTimeError");
		}

	}

}
