package com.cloudaxis.agsc.portal.dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.cloudaxis.agsc.portal.dao.mapper.UserRowMapper;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.model.UserLogon;
import com.cloudaxis.agsc.portal.model.UsersChangePassword;

@Repository
public class UserDAOImpl implements UserDAO {

	protected Logger logger = Logger.getLogger(UserDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private PasswordEncoder	passwordEncoder;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public UserDetails getUserByUsername(String username) {

		logger.info("getUserByUsername:'"+username+"'");
		String sql = " SELECT t.*  FROM users t  WHERE t.username = ? ";

		User user = null;

		try {
			user = jdbcTemplate.queryForObject(sql, new Object[] { username }, new UserRowMapper());

		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception getting the user information", e);
		}
		logger.info("Loaded user:"+user);

		return user;
	}

	/**
	 * Searches the Cloudaxis database looking for Strings in the USERNAME
	 * column that contain the text provided as a parameter.
	 * 
	 * @param searchParam
	 * @return a list of UserEntity objects that correspond to the search
	 *         criteria.
	 */

	public List<User> listUsers() {

		ArrayList<User> userList = new ArrayList<User>();

		String sql = " SELECT t.user_id t.username, t.enabled, t.role FROM users t";

		try {
			List<Map<String, Object>> users = jdbcTemplate.queryForList(sql);

			for (Map<String, Object> user : users) {
				User ue = new User();
				ue.setUserId((int) user.get("USER_ID"));
				ue.setUsername(String.valueOf(user.get("USERNAME")));
				ue.setEnabled(((Boolean) user.get("ENABLED")));
				ue.setRole(String.valueOf(user.get("ROLE")));

				userList.add(ue);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of users", e);
		}

		return userList;
	}

	public void createUser(User user) {

		String sql = " insert into users(username, first_name, last_name, email, password, enabled, account_non_expired, account_non_locked, creds_non_expired, role, registration_number, location) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";


		try {

			jdbcTemplate.update(sql, new Object[] { user.getUsername(),
					user.getFirstName(),
					user.getLastName(),
					user.getEmail(),
					user.getPassword(),
					user.isEnabled(),
					user.isAccountNonExpired(),
					user.isAccountNonLocked(),
					user.isCredentialsNonExpired(),
					user.getRole(),
					user.getRegistrationNumber(),
					"Singapore"
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception inserting a User into the database", e);
		}

	}

	public void updateUser(User user) {

		String sql = " update users set password = ?, " +
				" enabled = ?, account_non_expired = ?, creds_non_expired = ?, account_non_locked = ?, " +
				" role = ? where username = ?";

		try {

			jdbcTemplate.update(sql, new Object[] { user.getPassword(),
					user.isEnabled(),
					1, 1, 1, // security enablers
					user.getRole(),
					user.getUsername(),
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception updating a User into the database", e);
		}

	}

	/**
	 * Deletes the given user from the Cloudaxis database.
	 */
	public void deleteUser(String[] usernames) {

		String sql = " delete from users where username in (:usernames) ";

		try {
			List<String> usernamesList = Arrays.asList(usernames);
		    namedParameterJdbcTemplate.update(sql, Collections.singletonMap("usernames", usernamesList));
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception deleting a user from the database", e);
		}
	}

	public void recordLogon(Integer userId) {

		String sql = " insert into user_logons_history(user_id, logon_date) values("+userId+ ", "+ "SYSDATE()" +");";
		
		try {
			jdbcTemplate.update(sql);
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception inserting a logon record into the database", e);
		}
	}

	public List<UserLogon> getLogons() {

		List<UserLogon> logonList = new ArrayList<UserLogon>();

		String sql = " SELECT * FROM user_logons_history ";

		try {
			List<Map<String, Object>> logons = jdbcTemplate.queryForList(sql);

			for (Map<String, Object> logon : logons) {
				UserLogon u = new UserLogon();
				u.setUsername(String.valueOf(logon.get("user_id")));
				u.setDate((Timestamp) logon.get("DATE"));

				logonList.add(u);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of user logons", e);
		}

		return logonList;
	}

	@Override
	public User getUser(String username) {
		String sql = " SELECT t.*  FROM users t WHERE t.username = ? ";

		User user = null;

		try {
			user = jdbcTemplate.queryForObject(sql, new Object[] { username }, new UserRowMapper());

		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}

		return user;
	}

	@Override
	public void setNewPwd(User user) {
		String sql = "UPDATE users SET password=?,update_date=?, last_password_change_date=now() where username=?";

		try {
			jdbcTemplate.update(sql, new Object[]{passwordEncoder.encode(user.getPassword()), new Date(), user.getUsername()});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception update password to the database", e);
		}
		
	}
	
	@Override
	public List<User> getUsers() throws ParseException{
		List<User> users = new ArrayList<User>();
		String sql = "select * from users;";

		try {
			List<Map<String, Object>> usersList = jdbcTemplate.queryForList(sql);

			for (Map<String, Object> row : usersList) {
				User user = new User();
				user.setUserId((Integer) row.get("USER_ID"));
				user.setUsername(String.valueOf(row.get("USERNAME")));
				user.setFirstName((String) row.get("FIRST_NAME"));
				user.setLastName((String) row.get("LAST_NAME"));
				user.setEmail(String.valueOf(row.get("EMAIL")));
				user.setEnabled(Boolean.parseBoolean(row.get("ENABLED").toString()));
				user.setRole(String.valueOf(row.get("ROLE")));
				user.setCreateDate((Date)row.get("CREATE_DATE"));
				user.setLastPasswordChangeDate((Date)row.get("LAST_PASSWORD_CHANGE_DATE"));
				user.setRegistrationNumber((String)row.get("registration_number"));
				user.setLocation((String) row.get("location"));
				users.add(user);
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception retrieving the list of companies", e);
		}

		return users;
	}

	@Override
	public User getUserByFirstNameAndLastName(String firstName, String lastName) {
		String sql = " SELECT *  FROM users WHERE first_name=? and last_name=?; ";

		User user = null;

		try {
			user = jdbcTemplate.queryForObject(sql, new Object[] {firstName,lastName}, new UserRowMapper());

		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}

		return user;
	}

	@Override
	public void editUser(User user) {
		String sql = "UPDATE users SET first_name=?,last_name=?,email=?,role=?,update_date=?, registration_number=? WHERE username=?";
		
		try {
			jdbcTemplate.update(sql, new Object[]{user.getFirstName(),user.getLastName(),user.getEmail(),
					user.getRole(),new Date(), user.getRegistrationNumber(), user.getUsername()});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception updating a User into the database", e);
		}
		
	}
	
	public User getUserById(User user) {
		return getUserById(user.getUserId());
	}
	
	@Override
	public User getUserById(Integer userId) {
		String sql = " SELECT t.*  FROM users t WHERE t.user_id = ? ";
		User user = new User();
		try {
			user = jdbcTemplate.queryForObject(sql, new Object[] { userId }, new UserRowMapper());

		}
		catch (EmptyResultDataAccessException e) {
			logger.error("Data Access Exception searching a User from the database", e);
		}

		return user;
	}

	/**
	 * new user change password
	 */
	@Override
	public void saveKey(UsersChangePassword changePassword) {
		String sql = "INSERT INTO users_change_password values(?,?,?,?,?)";
		
		try {
			jdbcTemplate.update(sql, new Object[]{
					changePassword.getId(),
					changePassword.getUsername(),
					changePassword.getKey(),
					changePassword.getExpiredDate(),
					changePassword.getCreateDate()
			});
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception insert changePwd history from the database", e);
		}
	}

	@Override
	public UsersChangePassword getKey(User user) {
		UsersChangePassword changePassword = new UsersChangePassword();
		String sql = "SELECT * FROM users_change_password where username=?";

		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, user.getUsername());
			if(map.size() > 0){
				changePassword.setCreateDate((Date) map.get("create_date"));
				changePassword.setExpiredDate((Date) map.get("expired_date"));
				changePassword.setId((Integer) map.get("id"));
				changePassword.setKey((String) map.get("key"));
				changePassword.setUsername((String) map.get("username"));
			}
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception searching key from the database", e);
		}
		
		return changePassword;
	}

	@Override
	public void changePassword(User user) {
		String sql = "UPDATE users SET password=?,update_date=?,last_password_change_date=new() where username=?";

		try {List<String> emailList = new ArrayList<String>();
			jdbcTemplate.update(sql, new Object[]{passwordEncoder.encode(user.getPassword()), new Date(), user.getUsername()});
			
		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception update password to the database", e);
		}
		
	}

	@Override
	public void updateSecretKey(User user) {
		String sql = "UPDATE users SET secret_key=? where username=?";

		try {List<String> emailList = new ArrayList<>();
			jdbcTemplate.update(sql, new Object[]{user.getSecretKey(), user.getUsername()});

		}
		catch (DataAccessException e) {
			logger.error("Data Access Exception update password to the database", e);
		}

	}

	@Override
	public List<String> getAllUserEmails() {
		List<String> emailList = new ArrayList<String>();
		String sql = "select email from users ;";
		try{
			emailList = jdbcTemplate.queryForList(sql, null, String.class);
		}catch(DataAccessException e){
			logger.error("Data Access Exception retrieving email list from table users");
		}
		return emailList;
	}
	
}