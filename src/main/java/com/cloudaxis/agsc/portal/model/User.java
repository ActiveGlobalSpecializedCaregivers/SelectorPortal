package com.cloudaxis.agsc.portal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {

	private Integer userId;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String passwordConfirm;
	private String location;
	private Date createDate;
	private Date updateDate;
	private boolean enabled;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean accountNonLocked;
	private String registrationNumber;
	private Date lastPasswordChangeDate;
	private Collection<? extends GrantedAuthority> authorities;

	public User() {
	}

	public User(
			int userId,
			String firstName,
			String lastName,
			String username,
			String password,
			String location,
			Collection<? extends GrantedAuthority> authorities) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.location = location;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * As the application only allows for one role per user, we cheat here and
	 * give easy access to the 'authorities'.
	 * 
	 * The GrantedAuthority collection is still necessary for the Spring
	 * Security framwork.
	 * 
	 * @return the single role for this user.
	 */
	public String getRole() {

		// This application only allows for one role per user
		String userRole = null;
		Collection<SimpleGrantedAuthority> coll = (Collection<SimpleGrantedAuthority>) authorities;
		Iterable<SimpleGrantedAuthority> it = (Iterable<SimpleGrantedAuthority>) coll;
		for (SimpleGrantedAuthority sga : it) {
			userRole = sga.getAuthority();
		}
		return userRole;
	}

	/**
	 * As the application only allows for one role per user, we cheat here and
	 * give easy access to the 'authorities'.
	 * 
	 * @param role
	 *            the role to be added to the GrantedAuthorities collection
	 */
	public void setRole(String role) {
		this.authorities = new ArrayList<SimpleGrantedAuthority>();
		((ArrayList<SimpleGrantedAuthority>) authorities).add(new SimpleGrantedAuthority(role));
	}

	
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	@Override
	public String toString() {
		return "User [location=" + location + ", username=" + username + ", password=" + password
				+ ", enabled=" + enabled + ", email=" + email + ", accountNonExpired="
				+ accountNonExpired + ", credentialsNonExpired="
				+ credentialsNonExpired + ", accountNonLocked="
				+ accountNonLocked + ", authorities=" + authorities + ", userId=" + userId + "]";
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getLastPasswordChangeDate() {
		return lastPasswordChangeDate;
	}

	public void setLastPasswordChangeDate(Date lastPasswordChangeDate) {
		this.lastPasswordChangeDate = lastPasswordChangeDate;
	}
}