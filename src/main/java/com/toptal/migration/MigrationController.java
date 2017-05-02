package com.toptal.migration;

import com.cloudaxis.agsc.portal.dao.EmailTemplatesDAO;
import com.cloudaxis.agsc.portal.dao.UserDAO;
import com.cloudaxis.agsc.portal.dao.UserProfileDAO;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.service.AssessmentService;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;
import com.toptal.migration.dao.MigrationDao;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * <code>MigrationController</code> controls data migration process. Reads from data file
 * data and, depending on data, either creates or updates candidate data.
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class MigrationController {
    private static final Logger logger = Logger.getLogger(MigrationController.class);
    private final Scanner scanner;
    private final CandidateStorage candidateStorage;
    private AttachmentResolver attachmentResolver;

    @Autowired
    private SelectedCaregiverService selectedCaregiverService;
    @Autowired
    private EmailTemplatesDAO emailTemplatesDAO;
    @Autowired
    private MigrationDao migrationDao;
    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private UserProfileDAO userProfileDAO;
    @Autowired
    private UserDAO userDAO;

    private int totalUpdated;
    private int totalProcessed;
    private int totalSkipped;
    private int totalError;

    public MigrationController() throws IOException {
        String fileName = System.getProperty("config.main.file");
        logger.info("Loading data from file:" + fileName);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName), 1024*1024);
        scanner = new Scanner(bis);

        candidateStorage = new CsvCandidateStorage(new PropertiesDataFileProcessorFactory(System.getProperty(
                "config.file")));
        attachmentResolver = new DefaultAttachmentResolver();
    }


    public void processMissingProspectFile(String fileName) throws IOException {
        try(PrintWriter printWriter = new PrintWriter(new FileWriter(fileName))) {
            scanner.nextLine();// skip header row
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    processForMissing(line, printWriter);
                    totalProcessed++;
                }
                catch (Throwable e) {
                    logger.error("Error processing line:" + line);
                    logger.error("Error = " + e);
                    totalError++;
                }
            }
        }

        logger.info("Missing file processing is complete.");
        logger.info("Total processed records:"+totalProcessed);
        logger.info("Total skipped:"+totalSkipped);
        logger.info("Total error:"+totalError);
    }

    public void migrateData() {
        scanner.nextLine();// skip header row
        while(scanner.hasNextLine()){
            final String line = scanner.nextLine();
            if(line.isEmpty()){
                continue;
            }

            try {
                process(line);
                totalProcessed++;
            }
            catch (Throwable e) {
                logger.error("Error processing line:" + line);
                logger.error("Error = " + e);
                totalError++;
            }
        }
        logger.info("Data migration is complete.");
        logger.info("Total processed records:"+totalProcessed);
        logger.info("Total created:"+ totalProcessed);
        logger.info("Total updated:"+totalUpdated);
        logger.info("Total skipped:"+totalSkipped);
        logger.info("Total error:"+totalError);
    }

    private void process(String line) throws IOException, EmptyResultDataAccessException {
        String[] fields = line.split(";");
        User user = setupMigrationUser();
        BaseServices baseServices = new BaseServicesImpl(selectedCaregiverService, assessmentService,
                                                         emailTemplatesDAO, userProfileDAO);
        CandidateMigrationContext migrationContext =
                new CandidateMigrationContext(fields,
                                              user,baseServices,
                                              attachmentResolver,
                                              candidateStorage);
        logger.info("processing prospect: " + migrationContext.getProspectId() +
                    " decision: " + migrationContext.getDecision());
        String decision = migrationContext.getDecision();
        if("create".equals(decision)){
            CreateStrategy strategy = new CreateStrategy(migrationContext, migrationDao);
            strategy.migrateCandidate();
            totalProcessed++;
        }
        else if("name but not file on selector".equals(decision)){
            UpdateStrategy strategy = new UpdateStrategy(migrationContext, migrationDao);
            strategy.migrateCandidate();
            totalUpdated++;
        }
        else{
            logger.info("Skipping candidate...");
            totalSkipped++;
        }
    }

    private void processForMissing(String line, PrintWriter printWriter) throws IOException, EmptyResultDataAccessException {
        String[] fields = line.split(";");
        User user = setupMigrationUser();
        BaseServices baseServices = new BaseServicesImpl(selectedCaregiverService, assessmentService,
                                                         emailTemplatesDAO, userProfileDAO);
        CandidateMigrationContext migrationContext =
                new CandidateMigrationContext(fields,
                                              user,baseServices,
                                              attachmentResolver,
                                              candidateStorage);
        logger.info("processing prospect: " + migrationContext.getProspectId() +
                    " decision: " + migrationContext.getDecision());
        String decision = migrationContext.getDecision();
        if("create".equals(decision) || "name but not file on selector".equals(decision)){
            MissingProspectFileProcessStrategy strategy = new MissingProspectFileProcessStrategy(migrationContext);
            strategy.processCandidate(printWriter);
        }
        else{
            logger.info("Skipping candidate...");
            totalSkipped++;
        }
    }

    private User setupMigrationUser() {
        User user = userDAO.getUserByFirstNameAndLastName("Toptal","Migrator");
        if(user == null){
            user = new User();
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCreateDate(new Date());
            user.setCredentialsNonExpired(true);
            user.setEmail("migrator@toptal.com");
            user.setEnabled(true);
            user.setFirstName("Toptal");
            user.setLastName("Migrator");
            user.setLocation("Toptal");
            user.setUsername("toptal.migrator");
            user.setPassword("");
            user.setRole("DATA_MIGRATOR");

            userDAO.createUser(user);

            user = userDAO.getUserByFirstNameAndLastName("Toptal","Migrator");
        }
        return user;
    }

    public AssessmentService getAssessmentService() {
        return assessmentService;
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    public SelectedCaregiverService getSelectedCaregiverService() {
        return selectedCaregiverService;
    }

    public void setSelectedCaregiverService(SelectedCaregiverService selectedCaregiverService) {
        this.selectedCaregiverService = selectedCaregiverService;
    }

    public EmailTemplatesDAO getEmailTemplatesDAO() {
        return emailTemplatesDAO;
    }

    public void setEmailTemplatesDAO(EmailTemplatesDAO emailTemplatesDAO) {
        this.emailTemplatesDAO = emailTemplatesDAO;
    }

    public MigrationDao getMigrationDao() {
        return migrationDao;
    }

    public void setMigrationDao(MigrationDao migrationDao) {
        this.migrationDao = migrationDao;
    }

    public UserProfileDAO getUserProfileDAO() {
        return userProfileDAO;
    }

    public void setUserProfileDAO(UserProfileDAO userProfileDAO) {
        this.userProfileDAO = userProfileDAO;
    }
}
