package com.toptal.migration;

import java.io.IOException;
import javax.sql.DataSource;

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
import com.cloudaxis.agsc.portal.mail.MailConfig;
import com.cloudaxis.agsc.portal.service.AssessmentService;
import com.cloudaxis.agsc.portal.service.AssessmentServiceImpl;
import com.cloudaxis.agsc.portal.service.EmailService;
import com.cloudaxis.agsc.portal.service.FileService;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;
import com.cloudaxis.agsc.portal.service.UserService;
import com.toptal.migration.dao.MigrationDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan({ "com.cloudaxis.agsc.portal.dao.*",
                 "com.cloudaxis.agsc.portal.service.FileService",
                 "com.toptal.migration.*" })
@Import({ MailConfig.class})
@PropertySources({
		@PropertySource("file:${portal.conf.folder}/conf/database.properties"),
		@PropertySource("file:${portal.conf.folder}/conf/application.properties") })
@Profile("migration")
public class FileValidationAppConfig
{

    private static final Logger logger = Logger.getLogger(FileValidationAppConfig.class);

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

	@Bean
	public SelectedCaregiverDAO selectedCaregiverDAO() {
		return new SelectedCaregiverDAOImpl();
	}

    @Bean
    public FileService fileService() {return new FileService();}

    @Bean
    public FileValidatorMain migrationMain() { return new FileValidatorMain();}

    @Bean
    public FileValidationController migrationController() throws IOException { return new FileValidationController();}
}