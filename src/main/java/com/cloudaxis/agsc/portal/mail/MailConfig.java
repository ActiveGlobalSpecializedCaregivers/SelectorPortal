package com.cloudaxis.agsc.portal.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 
 * @author Vineela Sharma
 *
 */
@Configuration
public class MailConfig {
	
	@Autowired
	private Environment environment;

	@Bean
	public JavaMailSenderImpl javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setUsername(environment.getProperty("mail.username"));
		javaMailSender.setPassword(environment.getProperty("mail.password"));
		javaMailSender.setHost(environment.getProperty("mail.smtp.host"));
		javaMailSender.setProtocol(environment.getProperty("mail.protocol"));
		javaMailSender.setPort(Integer.valueOf(environment.getProperty("mail.smtp.port")));
		javaMailSender.setJavaMailProperties(getAdditionalMailProperties());

		return javaMailSender;
	}

	/**
	 * Set additional mail properties
	 * @return
	 */
	private Properties getAdditionalMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.sender", environment.getProperty("mail.sender"));
		properties.put("mail.subject.acknowledge", environment.getProperty("mail.subject.acknowledge"));
		properties.put("mail.subject.forgot.password", environment.getProperty("mail.subject.forgot.password"));
		properties.put("mail.subject.shortlisted", environment.getProperty("mail.subject.shortlisted"));
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.starttls.required", "true");
		properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		return properties;
	}

}