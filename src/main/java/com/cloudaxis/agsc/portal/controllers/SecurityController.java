package com.cloudaxis.agsc.portal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class SecurityController extends AbstractController {

	public SecurityController() {
		super(SecurityController.class);
	}

	/**
	 * Determines the behavior of invalid login credentials and logout activity
	 * 
	 * @param error
	 * @param logout
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and/or password and/or security code!");
		}

		if (logout != null) {
			model.addObject("logout", "You've been logged out successfully.");
		}
		model.setViewName("login");
		
		return model;
	}
	
}
