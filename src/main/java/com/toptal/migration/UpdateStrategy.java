package com.toptal.migration;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.toptal.migration.dao.MigrationDao;
import java.io.IOException;
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
    public void migrateCandidate() throws IOException, EmptyResultDataAccessException {
        updateCandidate();

        migrateAttachments();

        migrateEvaluations();

        migrateComments();

        migrateEmails();
    }

    private void updateCandidate() {
        BaseServices baseServices = migrationContext.getBaseServices();

        CandidateStorage candidateStorage = migrationContext.getCandidateStorage();
        Caregiver current = candidateStorage.loadCandidate(migrationContext.getProspectId());

        Integer caregiverId = migrationDao.findCandidateByEmail(current.getEmail());
        logger.info("updating candidate, found candidate ID=" + caregiverId);

        candidate = baseServices.getCaregiver(String.valueOf(caregiverId));
        candidate.setDateApplied(current.getDateApplied());
        logger.info("changing date applied to " + current.getDateApplied());

        migrationDao.updateAppliedDate(caregiverId, current.getDateApplied());
    }
}
