package com.cloudaxis.agsc.portal.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AbstractController {

	protected Logger logger = null;

	public AbstractController(Class<?> daoClass) {
		this.logger = Logger.getLogger(daoClass);
	}

	/**
	 * get the current HttpRequest instance
	 * 
	 * @return HttpServletRequest
	 */
	protected HttpServletRequest getServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

}
