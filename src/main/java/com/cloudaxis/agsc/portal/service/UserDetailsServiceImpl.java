package com.cloudaxis.agsc.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cloudaxis.agsc.portal.dao.UserDAO;
import com.cloudaxis.agsc.portal.model.User;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDAO userDao;

	/**
	 * Will be called by both the Spring authentication service and internally
	 * in order to retrieve the User Profile information.
	 * 
	 * @param the
	 *            user name which is the primary key of the user_entity table.
	 * @returns a UserDetails object containing all information about the user
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = (User) userDao.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		else {
			userDao.recordLogon(user.getUserId());
		}

		return user;
	}

}
