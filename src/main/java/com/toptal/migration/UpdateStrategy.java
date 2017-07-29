package com.toptal.migration;

import com.cloudaxis.agsc.portal.helpers.StringUtil;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.toptal.migration.dao.MigrationDao;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.toptal.migration.model.CandidateQuestionnaire;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * <code>UpdateStrategy</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class UpdateStrategy extends AbstractStrategy{
    private static final Logger logger = Logger.getLogger(UpdateStrategy.class);




    public UpdateStrategy(CandidateMigrationContext migrationContext, MigrationDao migrationDao) {
        super(migrationContext, migrationDao);
    }

    @Override
    public void migrateCandidate() throws IOException, EmptyResultDataAccessException, ParseException
    {
        updateCandidate();

        migrateAttachments();

        migrateEvaluations();

        migrateComments();

        migrateEmails();
    }

    private void updateCandidate() throws ParseException
    {
        BaseServices baseServices = migrationContext.getBaseServices();

        CandidateStorage candidateStorage = migrationContext.getCandidateStorage();
        Caregiver current = candidateStorage.loadCandidate(migrationContext.getProspectId());

        Date dob = findDob();
        Integer caregiverId;
        if(dob != null)
        {
            caregiverId = migrationDao.findCandidate(migrationContext.getSelectorName(), dob);
        }
        else
        {
            caregiverId = migrationDao.findCandidate(migrationContext.getSelectorName());
        }
        logger.info("updating candidate, found candidate ID=" + caregiverId);

        candidate = baseServices.getCaregiver(String.valueOf(caregiverId));
        Date dateApplied = getDateApplied(current);
        candidate.setDateApplied(dateApplied);
        logger.info("changing date applied to " + dateApplied);

        migrationDao.updateAppliedDate(caregiverId, dateApplied);
    }

}
