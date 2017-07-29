package com.toptal.migration;

import com.cloudaxis.agsc.portal.dao.EmailHistoryDAO;
import com.cloudaxis.agsc.portal.dao.EmailHistoryDAOImpl;
import com.cloudaxis.agsc.portal.dao.EmailTemplatesDAO;
import com.cloudaxis.agsc.portal.dao.EmailTemplatesDAOImpl;
import com.cloudaxis.agsc.portal.dao.EvaluationDAO;
import com.cloudaxis.agsc.portal.dao.EvaluationDAOImpl;
import com.cloudaxis.agsc.portal.dao.SelectedCaregiverDAO;
import com.cloudaxis.agsc.portal.dao.SelectedCaregiverDAOImpl;
import com.cloudaxis.agsc.portal.dao.UserDAO;
import com.cloudaxis.agsc.portal.dao.UserDAOImpl;
import com.cloudaxis.agsc.portal.dao.UserProfileDAO;
import com.cloudaxis.agsc.portal.dao.UserProfileDAOImpl;
import com.cloudaxis.agsc.portal.exception.ExceptionResolver;
import com.cloudaxis.agsc.portal.mail.MailConfig;
import com.cloudaxis.agsc.portal.security.AuthenticationSuccessHandlerImpl;
import com.cloudaxis.agsc.portal.security.SecurityConfig;
import com.cloudaxis.agsc.portal.service.AssessmentService;
import com.cloudaxis.agsc.portal.service.AssessmentServiceImpl;
import com.cloudaxis.agsc.portal.service.EmailService;
import com.cloudaxis.agsc.portal.service.FileService;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;
import com.cloudaxis.agsc.portal.service.UserDetailsServiceImpl;
import com.cloudaxis.agsc.portal.service.UserService;
import com.toptal.migration.dao.MigrationDao;
import java.io.IOException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@ComponentScan({ "com.cloudaxis.agsc.portal.dao.*",
                 "com.cloudaxis.agsc.portal.service.SelectedCaregiverService",
                 "com.cloudaxis.agsc.portal.service.AssessmentService",
                 "com.toptal.migration.*" })
@Import({ MailConfig.class})
@PropertySources({
		@PropertySource("file:${portal.conf.folder}/conf/database.properties"),
		@PropertySource("file:${portal.conf.folder}/conf/application.properties") })
@Profile("migration")
public class MigrationAppConfig {

    private static final Logger logger = Logger.getLogger(MigrationAppConfig.class);

	@Autowired
	private Environment environment;

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

        logger.info("Creating datasource with parameters:");
        logger.info("jdbc.url: "+environment.getRequiredProperty("jdbc.url"));
        logger.info("jdbc.username: "+environment.getRequiredProperty("jdbc.username"));
        logger.info("jdbc.password: "+environment.getRequiredProperty("jdbc.password"));

		return dataSource;
	}

	@Bean(name = "selectedCaregiverService")
	public SelectedCaregiverService selectedCaregiverService() {
		return new SelectedCaregiverService();
	}

	@Bean
	public SelectedCaregiverDAO selectedCaregiverDAO() {
		return new SelectedCaregiverDAOImpl();
	}

    @Bean
    public UserProfileDAO userProfileDAO(){return new UserProfileDAOImpl();}

    @Bean
    public EmailService emailService() {return new EmailService();}

    @Bean
    public EmailHistoryDAO emailHistoryDAO() {return new EmailHistoryDAOImpl();}

    @Bean
    public EmailTemplatesDAO emailTemplatesDAO() {return new EmailTemplatesDAOImpl();}

    @Bean
    public UserDAO userDAO() {return new UserDAOImpl();}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FileService fileService() {return new FileService();}

    @Bean
    public UserService userService() {return new UserService();}

    @Bean
    public MigrationMain migrationMain() { return new MigrationMain();}

    @Bean
    public StatusUpdaterMain statusUpdaterMain() { return new StatusUpdaterMain();}

    @Bean
    public MissingFilesProcessorMain missingFilesProcessorMain() { return new MissingFilesProcessorMain();}

    @Bean
    public EmailValidationProcessorMain emailValidationProcessorMain() { return new EmailValidationProcessorMain();}

    @Bean
    public MigrationController migrationController() throws IOException { return new MigrationController();}

    @Bean
    public MigrationDao migrationDao(DataSource dataSource) { return new MigrationDao(dataSource);}

    @Bean
    public AssessmentService assessmentService() { return new AssessmentServiceImpl();}

    @Bean
    public EvaluationDAO evaluationDAO() { return new EvaluationDAOImpl();}
}