package com.cloudaxis.agsc.portal.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.service.UserService;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "not.empty");
        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "username.size");
        }
        if (userService.getUser(user.getUsername()) != null) {
            errors.rejectValue("username", "username.duplicate");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "not.empty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "password.size");
        }
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "password.confirm.not.match");
        }
        
        List<String> emailList = userService.getAllUserEmails();
        for(String email : emailList){
			if (email.equals(user.getEmail())) {
				errors.rejectValue("email", "email.duplicate");
				break;
			}
        }
    }

	public void validateEdit(User user, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "not.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "not.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "not.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "not.empty");
		
	}
	
	public void validatePwd(User user,Errors errors){
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "not.empty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "password.size");
        }
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "password.confirm.not.match");
        }
	}
	
	public void validateEmail(User user,Errors errors){
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "not.empty");
        if(user.getEmail() != null && user.getEmail() != ""){
        	User u = userService.getUser(user.getUsername());
        	if(!user.getEmail().equals(u.getEmail())){
        		errors.rejectValue("email", "email.difference");
        	}	
        	
        }
	}
	
}
