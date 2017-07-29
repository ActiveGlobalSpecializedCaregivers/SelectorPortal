package com.toptal.migration;

import java.io.PrintWriter;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.toptal.migration.dao.MigrationDao;
import org.apache.log4j.Logger;

/**
 * <code>EmailValidationStrategy</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class EmailValidationStrategy
{

    private static final Logger logger = Logger.getLogger(EmailValidationStrategy.class);
    private final CandidateMigrationContext migrationContext;
    private MigrationDao migrationDao;

    public EmailValidationStrategy(CandidateMigrationContext migrationContext,
                                   MigrationDao migrationDao) {
        this.migrationContext = migrationContext;
        this.migrationDao = migrationDao;
    }

    public void processCandidate(PrintWriter printWriter) {

        CandidateStorage candidateStorage = migrationContext.getCandidateStorage();
        Caregiver current = candidateStorage.loadCandidate(migrationContext.getProspectId());

        try
        {
            Integer caregiverId = migrationDao.findCandidateByEmail(current.getEmail());
            Caregiver candidate = migrationContext.getBaseServices().getCaregiver(String.valueOf(caregiverId));
            printWriter.println(migrationContext.getProspectId()+";"+current.getEmail()+";"+current.getFullName()+
                                ";"+candidate.getFullName()+";match="+(current.getFullName().equals(candidate.getFullName())));
        }
        catch (Throwable e)
        {
            printWriter.println("No candidate found for prospect "+migrationContext.getProspectId());
        }
    }
}
