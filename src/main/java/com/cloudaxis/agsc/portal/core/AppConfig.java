package com.cloudaxis.agsc.portal.core;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.cloudaxis.agsc.portal.dao.SelectedCaregiverDAO;
import com.cloudaxis.agsc.portal.dao.SelectedCaregiverDAOImpl;
import com.cloudaxis.agsc.portal.exception.ExceptionResolver;
import com.cloudaxis.agsc.portal.mail.MailConfig;
import com.cloudaxis.agsc.portal.security.AuthenticationSuccessHandlerImpl;
import com.cloudaxis.agsc.portal.security.SecurityConfig;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;
import com.cloudaxis.agsc.portal.service.UserDetailsServiceImpl;

@EnableWebMvc
@EnableScheduling
@Configuration
@ComponentScan({ "com.cloudaxis.agsc.portal.*" })
@Import({ SecurityConfig.class, MailConfig.class})
@PropertySources({
		@PropertySource("file:${portal.conf.folder}/conf/database.properties"),
		@PropertySource("file:${portal.conf.folder}/conf/application.properties") })
public class AppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment environment;

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean
	public CommonsMultipartResolver commonsMultipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		commonsMultipartResolver.setMaxUploadSize(50000000);
		return commonsMultipartResolver;
	}

	@Bean
	public ExceptionResolver exceptionsResolver() {
		return new ExceptionResolver();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandlerImpl();
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/resources/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("login");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
		return dataSource;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("com/cloudaxis/i18n/text", "com/cloudaxis/i18n/validation");
		messageSource.setCacheSeconds(1);
		return messageSource;
	}

	@Bean(name = "userDetailsService")
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean(name = "selectedCaregiverService")
	public SelectedCaregiverService selectedCaregiverService() {
		return new SelectedCaregiverService();
	}

	@Bean
	public SelectedCaregiverDAO selectedCaregiverDAO() {
		return new SelectedCaregiverDAOImpl();
	}

}