package com.toptal.migration;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.toptal.migration.model.CandidateDocument;
import java.io.File;
import java.io.PrintWriter;
import org.apache.log4j.Logger;

/**
 * <code>MissingProspectFileProcessStrategy</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class MissingProspectFileProcessStrategy{

    private static final Logger logger = Logger.getLogger(MissingProspectFileProcessStrategy.class);
    private final CandidateMigrationContext migrationContext;

    public MissingProspectFileProcessStrategy(CandidateMigrationContext migrationContext) {
        this.migrationContext = migrationContext;
    }

    public void processCandidate(PrintWriter printWriter) {
        final String prospectId = migrationContext.getProspectId();

        boolean foundDocuments = validateDocuments(migrationContext.getCandidateStorage()
                                                           .loadCandidateDocuments(prospectId));

        foundDocuments &= validateDocuments(migrationContext.getCandidateStorage()
                                                    .loadCandidateFiles(migrationContext.getProspectId()));
        foundDocuments &= validateResume(migrationContext.getCandidateStorage()
                                                 .loadCandidateResume(migrationContext.getProspectId()));

        if(!foundDocuments){
            printWriter.println(prospectId);
        }
    }

    private boolean validateDocuments(CandidateDocument[] candidateDocuments) {
        if(candidateDocuments != null){
            for(CandidateDocument document : candidateDocuments) {
                String fileName = document.getFileName();
                File file = migrationContext.getAttachmentResolver().resolveProspectFile(
                        migrationContext.getProspectId(), fileName);
                if(!file.exists()) {
                    logger.info("File "+file+" doesn't exist!");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateResume(CandidateDocument candidateDocument) {
        if(candidateDocument != null){
                File file = migrationContext.getAttachmentResolver().resolveProspectResume(candidateDocument.getProspectId(),
                        candidateDocument.getFileName());
                if(!file.exists()) {
                    logger.info("File "+file+" doesn't exist!");
                    return false;
                }

        }
        return true;
    }
}
