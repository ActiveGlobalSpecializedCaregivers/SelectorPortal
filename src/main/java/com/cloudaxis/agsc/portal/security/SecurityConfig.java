package com.cloudaxis.agsc.portal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService				userDetailsService;

	@Autowired
	private AuthenticationSuccessHandler	authSuccessHandler;

	@Autowired
	private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(authenticationProvider());
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new TwoFADaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = "filterMultipartResolver")
	public CommonsMultipartResolver filterMultipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		commonsMultipartResolver.setMaxUploadSize(50000000);
		return commonsMultipartResolver;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable(); // add this can open the .pdf
													// file
		http
				.authorizeRequests()
				.antMatchers("/admin/**")
				.access("hasAnyRole('ROLE_ADMIN','ROLE_SUB_ADMIN','ROLE_RECRUITER','ROLE_HOSPITAL','ROLE_SALES','ROLE_SALES_SG','ROLE_SALES_HK','ROLE_SALES_TW')")
				.antMatchers("/dashboard/**")
				.access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUB_ADMIN') or hasRole('ROLE_HOSPITAL') or hasRole('ROLE_SALES') or hasRole('ROLE_PH_RECRUITING_PARTNER') or hasRole('ROLE_RECRUITER') or hasRole('ROLE_SALES_SG') or hasRole('ROLE_SALES_HK') or hasRole('ROLE_SALES_TW')")
				.and()
				.formLogin()
				.loginPage("/login")
				.authenticationDetailsSource(authenticationDetailsSource)
				.defaultSuccessUrl("/candidates")
				.failureUrl("/login?error")
				.usernameParameter("username")
				.passwordParameter("password")
				.successHandler(authSuccessHandler)
				.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.and()
				.csrf().disable();

		http.requiresChannel().anyRequest().requiresSecure();

		http.sessionManagement()
				.sessionConcurrency(configurer -> configurer.expiredUrl("/login"))
				.invalidSessionUrl("/login");
	}

}