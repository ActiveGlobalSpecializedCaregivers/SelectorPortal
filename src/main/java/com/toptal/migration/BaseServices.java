package com.toptal.migration;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.cloudaxis.agsc.portal.model.Profile;
import com.cloudaxis.agsc.portal.model.User;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * <code>BaseServices</code> facade to base application functionality for migration.
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public interface BaseServices {
    int createProfile(Profile candidateProfile);
    void createEmailRecord(EmailHistory emailHistory);
    void createNewComment(String comment, User user, String candidateId);
    void createEvaluationAnswerList(String applicantId,
                                    String evaluatedId,
                                    String templateId,
                                    String[] evaluationQuestionIds,
                                    String[] skillRatings,
                                    String[] comments,
                                    String summary);

    void uploadFile(MultipartFile file, Caregiver candidate, String type) throws IOException;
    Caregiver getCaregiver(String id);

    void updateCandidateStatus(Caregiver candidate, User user, String status);
}
