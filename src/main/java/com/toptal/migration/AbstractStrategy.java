package com.toptal.migration;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.toptal.migration.dao.MigrationDao;
import com.toptal.migration.model.CandidateDocument;
import com.toptal.migration.model.CandidateEmail;
import com.toptal.migration.model.CandidateEvaluation;
import com.toptal.migration.model.CandidateFeedback;
import com.toptal.migration.model.CandidateNote;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * <code>AbstractStrategy</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public abstract class AbstractStrategy implements CandidateMigrationStrategy{
    private static final Logger logger = Logger.getLogger(AbstractStrategy.class);
    protected final CandidateMigrationContext migrationContext;
    protected final MigrationDao migrationDao;
    protected Caregiver candidate;
    protected String prospectId;
    public static final String[] EVALUATION_QUESTION_IDS = new String[]{"0", "1", "2", "3", "4"};

    protected AbstractStrategy(CandidateMigrationContext migrationContext, MigrationDao migrationDao) {
        this.migrationContext = migrationContext;
        this.migrationDao = migrationDao;
        this.prospectId = migrationContext.getProspectId();
    }

    protected void migrateEmails() {
        logger.debug("Skipping email migration");
    }

    protected void migrateComments() {
        CandidateFeedback[] feedback = migrationContext.getCandidateStorage().loadCandidateFeedback(prospectId);

        BaseServices service = migrationContext.getBaseServices();

        if (feedback != null) {
            logger.info("Creating comments for candidate based on feedback records.");
            for(CandidateFeedback candidateFeedback : feedback) {
                service.createNewComment(candidateFeedback.getFeedbackText(),
                                         migrationContext.getUser(),
                                         candidate.getUserId());
            }
        }
        else{
            logger.info("No feedback records for candidate found");
        }

        logger.debug("Skipping notes migration");
    }

    protected void migrateEvaluations() {
        final BaseServices baseServices = migrationContext.getBaseServices();

        String summary = "";
        String applicantId = candidate.getUserId();
        String evaluatedId = String.valueOf(migrationContext.getUser().getUserId());
        final String templateId = "1";
        String[] skillRatings = {"0", "0", "0", "0", "0"};
        String[] comments = {"","","","",""};

        final CandidateEvaluation[] candidateEvaluations = migrationContext.getCandidateStorage()
                .loadCandidateEvaluations(migrationContext.getProspectId());

        if(candidateEvaluations == null){
            logger.info("No candidate evaluations found for candidate.");
            return;
        }

        for(CandidateEvaluation evaluation : candidateEvaluations){
            final String note = evaluation.getEvaluationNote();
            if(!note.isEmpty() && !summary.contains(note)){
                   summary += (note + '\n');
            }
            String skill = evaluation.getSkillName();
            int skillIndex = -2;
            if(skill.startsWith("\"Understands")){
                skillIndex = 0;
            } else if(skill.startsWith("\"Motivation")){
                skillIndex = 1;
            } else if(skill.startsWith("\"Commitment")){
                skillIndex = 4;
            } else if(skill.startsWith("\"Nursing")){
                skillIndex = 3;
            } else if(skill.startsWith("\"Can")){
                skillIndex = -1;
            } else if(skill.startsWith("\"Interpersonal")){
                skillIndex = 2;
            }
            if(skillIndex >= 0) {
                skillRatings[skillIndex] = evaluation.getSkillRating();
                comments[skillIndex] = evaluation.getRatingComment();
            }
            else if(skillIndex == -1){
                // English - add to summary
                summary += ("Can understand  & communicate in English,"
                            + evaluation.getSkillWeight()
                            +", " +evaluation.getSkillRating()
                            +", "+evaluation.getRatingComment());
            }
            else{
                logger.info("Skill not found :" + skill);
            }
        }

        baseServices.createEvaluationAnswerList(applicantId, evaluatedId,
                                                templateId,
                                                EVALUATION_QUESTION_IDS, skillRatings,
                                                comments, summary);
    }

    protected void migrateAttachments() throws IOException {
        BaseServices baseServices = migrationContext.getBaseServices();
        CandidateDocument[] candidateDocuments = migrationContext.getCandidateStorage()
                .loadCandidateDocuments(prospectId);
        uploadDocuments(baseServices, candidateDocuments);

        candidateDocuments = migrationContext.getCandidateStorage().loadCandidateFiles(migrationContext.getProspectId());

        uploadDocuments(baseServices, candidateDocuments);

        CandidateDocument resumeDocument = migrationContext.getCandidateStorage()
                .loadCandidateResume(migrationContext.getProspectId());

        if(resumeDocument != null){
            logger.info("Found candidate resume.");
            File file = migrationContext.getAttachmentResolver().resolveProspectResume(resumeDocument.getProspectId(),
                    resumeDocument.getFileName());
            if(file.exists()) {
                MultipartFile multipartFile = new MigrationMultipartFile(file,
                                                                         resumeDocument.getFileName());
                baseServices.uploadFile(multipartFile, candidate, "resume");

                candidate.setResume(resumeDocument.getFileName());
                migrationDao.updateCandidateResume(candidate.getUserId(),
                                                    resumeDocument.getFileName());
            }
            else{
                logger.info("Resume file "+file+" doesn't exist!");
            }
        }
        else{
            logger.info("Candidate resume is not found.");
        }
    }

    private void uploadDocuments(BaseServices baseServices, CandidateDocument[] candidateDocuments)
            throws IOException {
        if (candidateDocuments != null) {
            logger.info("Uploading candidate documents.");
            for(CandidateDocument document : candidateDocuments) {
                String fileName = document.getFileName();
                File file = migrationContext.getAttachmentResolver().resolveProspectFile(
                        migrationContext.getProspectId(), fileName);
                if(file.exists()) {
                    MultipartFile multipartFile = new MigrationMultipartFile(file, fileName);
                    baseServices.uploadFile(multipartFile, candidate, "other_file");
                }
                else{
                    logger.info("File "+file+" doesn't exist!");
                }
            }
        }
        else{
            logger.info("No candidate documents found.");
        }
    }

    protected void setCandidate(Caregiver candidate) {
        this.candidate = candidate;
    }

    private class MigrationMultipartFile implements MultipartFile {
        private final String fileName;
        private final File file;

        public MigrationMultipartFile(File file, String fileName) {
            this.file = file;
            this.fileName = fileName;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getOriginalFilename() {
            return fileName;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return file.length();
        }

        @Override
        public byte[] getBytes() throws IOException {
            byte[] bytes = new byte[(int) file.length()];// assuming < 2Gb
            try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file),
                                                                  (int) file.length())){
                int count = bis.read(bytes);
                if(count != file.length()){
                    throw new IOException("Failed to read file fully, only "+count+" instead of "+file.length());
                }
            }
            return bytes;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public void transferTo(File targetFile) throws IOException, IllegalStateException {
            if(this.file.length() == 0){
                return;// corrupted file
            }

            byte[] bytes = new byte[(int) this.file.length()];// assuming < 2Gb
            try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(this.file),
                                                                  (int) this.file.length());
                OutputStream out = new FileOutputStream(targetFile)){
                int count = bis.read(bytes);
                if(count != this.file.length()){
                    throw new IOException("Failed to read file fully, only "+count+" instead of "+this.file.length());
                }
                out.write(bytes);
            }
        }
    }
}
