package com.cloudaxis.agsc.portal.dao;

import java.text.ParseException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.model.UsersChangePassword;

public interface UserDAO {

	public UserDetails getUserByUsername(String username);

	public List<User> listUsers();

	public void createUser(User user);

	public void updateUser(User user);

	public void deleteUser(String[] users);

	public void recordLogon(Integer userId);

	public User getUser(String username);

	public void setNewPwd(User user);

	public List<User> getUsers() throws ParseException;

	public User getUserByFirstNameAndLastName(String firstName, String lastName);

	public void editUser(User user);

	public User getUserById(User user);
	
	public User getUserById(Integer userId);

	public void saveKey(UsersChangePassword changePassword);

	public UsersChangePassword getKey(User user);

	public void changePassword(User user);

	void updateSecretKey(User user);

	public List<String> getAllUserEmails();

}

