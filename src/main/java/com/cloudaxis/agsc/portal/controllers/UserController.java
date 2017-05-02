package com.cloudaxis.agsc.portal.controllers;

import java.io.IOException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloudaxis.agsc.portal.helpers.MD5Util;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.model.UsersChangePassword;
import com.cloudaxis.agsc.portal.service.EmailService;
import com.cloudaxis.agsc.portal.service.UserService;
import com.cloudaxis.agsc.portal.validator.UserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService		userService;

	@Autowired
	private UserValidator	userValidator;
	
	@Autowired
	private EmailService emailService;

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createUser(Model model) {
		model.addAttribute("user", new User());

		return "admin/createUser";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createUser(@ModelAttribute("user") User user, BindingResult bindingResult, Model model, HttpServletRequest request) throws MessagingException {
		userValidator.validate(user, bindingResult);

		if (bindingResult.hasErrors()) {
			return "admin/createUser";
		}else{
			userService.createUser(user);
			emailService.setPwd(user, request);
			return "redirect:/admin/users";
		}

	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteUser(HttpServletRequest request, Model model) {
		if (request.getParameterValues("username") != null) {
			String[] users = request.getParameterValues("username");
			userService.deleteUser(users);
		}

		return "redirect:/admin/users";

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String editUser(Model model, User user) {

		user = userService.getUserById(user);
		model.addAttribute("user", user);

		return "admin/editUser";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editUser(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
		userValidator.validateEdit(user, bindingResult);

		if (bindingResult.hasErrors()) {
			return "admin/editUser";
		}

		userService.editUser(user);
		return "redirect:/admin/users";
	}
	
	/**
	 * set new password for new account
	 * 
	 * @param model
	 * @param user			user data
	 * @param key			MD5 key
	 * @param request
	 * @return
	 */
	@RequestMapping("/setPwd/{key}")
	public String setPwd(Model model, User user, @ModelAttribute("key")String key, HttpServletRequest request){
		UsersChangePassword ucp = userService.getKey(user);
		Date today = new Date();
		Date expiredDate = ucp.getExpiredDate();
		//String MD5key = MD5Util.convertMD5(key);
		String MD5key = MD5Util.string2MD5(ucp.getKey());
		if( today.getTime() < expiredDate.getTime() && key.equals(MD5key)){		
			model.addAttribute("user", user);
			return "admin/setPassword";
		}else{
			return "admin/expiredLink";
		}
	}
	
	@RequestMapping("/changePwd")
	public String changePwd(@ModelAttribute("user") User user, BindingResult bindingResult){
		userValidator.validatePwd(user, bindingResult);

		if (bindingResult.hasErrors()) {
			return "admin/setPassword";
		}else{
			userService.changePassword(user);
			return "forgot/complete";
		}
		
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public void changePassword(@RequestParam("userid") String userid, @RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword, HttpServletRequest request, HttpServletResponse response) throws IOException{
		boolean isSuccessful = userService.changePassword(userid, currentPassword, newPassword);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(isSuccessful);  
		response.getWriter().print(json);
	}
}