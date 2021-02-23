package com.cloudaxis.agsc.portal.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cloudaxis.agsc.portal.dao.UserDAO;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.model.UsersChangePassword;


@Service
public class UserService {
	
	protected Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private UserDAO			userDao;

	@Autowired
	private PasswordEncoder	passwordEncoder;

	public void createUser(User user) {
		user.setFirstName(user.getFirstName());
		user.setLastName(user.getLastName());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(user.getRole());
		user.setEnabled(true);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setRegistrationNumber(user.getRegistrationNumber());
		userDao.createUser(user);

	}


	public User getUser(String username) {
		return userDao.getUser(username);
	}

	public void setNewPwd(User user) {
		userDao.setNewPwd(user);
	}
	
	public List<User> getUsers() {
		List<User> users = null;
		try {
			users = userDao.getUsers();
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			
		}
		return users;
		
	}

	public void deleteUser(String[] users) {
		userDao.deleteUser(users);
		
	}

	public User getUserByFirstNameAndLastName(String firstName, String lastName) {
		return userDao.getUserByFirstNameAndLastName(firstName,lastName);
	}

	public void editUser(User user) {
		userDao.editUser(user);
	}


	public User getUserById(User user) {
		return userDao.getUserById(user);
	}

	public User getUserById(Integer userid) {
		return userDao.getUserById(userid);
	}

	public UsersChangePassword getKey(User user) {
		return userDao.getKey(user);
	}


	public void changePassword(User user) {
		userDao.changePassword(user);
	}

	public boolean changePassword(String userid, String currentPassword, String newPassword) {
		boolean isSuccessful = false;
		User user = new User();
		user.setUserId(Integer.parseInt(userid));
		user = userDao.getUserById(user);
		if(passwordEncoder.matches(currentPassword, user.getPassword())){
			user.setPassword(newPassword);
			userDao.setNewPwd(user);
			isSuccessful = true;
		}
		return isSuccessful;
	}

	public boolean resetPassword(String userid, String newPassword) {
		User user = new User();
		user.setUserId(Integer.parseInt(userid));
		user = userDao.getUserById(user);
		user.setPassword(newPassword);
		userDao.setNewPwd(user);
		return true;
	}

	public List<String> getAllUserEmails() {
		return userDao.getAllUserEmails();
	}

}
