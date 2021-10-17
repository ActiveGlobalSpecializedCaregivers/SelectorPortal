package com.cloudaxis.agsc.portal.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cloudaxis.agsc.portal.model.User;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
		User user = new User();
		user.setUserId(rs.getInt("USER_ID"));
		user.setFirstName(rs.getString("FIRST_NAME"));
		user.setLastName(rs.getString("LAST_NAME"));
		user.setUsername(rs.getString("USERNAME"));
		user.setEmail(rs.getString("EMAIL"));
		user.setPassword(rs.getString("PASSWORD"));
		user.setCreateDate(rs.getDate("CREATE_DATE"));
		user.setUpdateDate(rs.getDate("UPDATE_DATE"));
		user.setAccountNonExpired(rs.getBoolean("ACCOUNT_NON_EXPIRED"));
		user.setEnabled(rs.getBoolean("ENABLED"));
		user.setAccountNonLocked(rs.getBoolean("ACCOUNT_NON_LOCKED"));
		user.setCredentialsNonExpired(rs.getBoolean("CREDS_NON_EXPIRED"));
		user.setRegistrationNumber(rs.getString("registration_number"));
		user.setLocation(rs.getString("location"));
		user.setLastPasswordChangeDate(rs.getDate("LAST_PASSWORD_CHANGE_DATE"));
		user.setSecretKey(rs.getString("SECRET_KEY"));

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(rs.getString("ROLE")));

		user.setAuthorities(authorities);

		return user;
	}

}
