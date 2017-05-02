package com.toptal.migration;

import com.cloudaxis.agsc.portal.dao.EmailHistoryDAO;
import com.cloudaxis.agsc.portal.dao.EmailTemplatesDAO;
import com.cloudaxis.agsc.portal.dao.UserProfileDAO;
import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.cloudaxis.agsc.portal.model.Profile;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.service.AssessmentService;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <code>BaseServicesImpl</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
@Service
public class BaseServicesImpl implements BaseServices {

    private final SelectedCaregiverService caregiverService;
    private final AssessmentService assessmentService;
    private final EmailTemplatesDAO emailTemplatesDao;
    private final UserProfileDAO userProfileDAO;

    public BaseServicesImpl(SelectedCaregiverService caregiverService,
                            AssessmentService assessmentService,
                            EmailTemplatesDAO emailTemplatesDao,
                            UserProfileDAO userProfileDAO){
        this.caregiverService = caregiverService;
        this.assessmentService = assessmentService;
        this.emailTemplatesDao = emailTemplatesDao;
        this.userProfileDAO = userProfileDAO;
    }

    @Override
    public int createProfile(Profile candidateProfile) {
        final int candidateId = userProfileDAO.addProfile(candidateProfile);
        caregiverService.saveBio(String.valueOf(candidateId));
        return candidateId;
    }

    @Override
    public void createEmailRecord(EmailHistory emailHistory) {
        emailTemplatesDao.saveEmail(emailHistory);
    }

    @Override
    public void createNewComment(String comment, User user, String candidateId) {
        caregiverService.postNewComment(comment, user, candidateId);

    }

    @Override
    public void createEvaluationAnswerList(String applicantId,
                                           String evaluatedId,
                                           String templateId,
                                           String[] evaluationQuestionIds,
                                           String[] skillRatings,
                                           String[] comments,
                                           String summary) {
        assessmentService.saveEvaluationAnswerList(applicantId,
                                                   evaluatedId,
                                                   templateId,
                                                   evaluationQuestionIds,
                                                   skillRatings,
                                                   comments,
                                                   summary);

    }

    @Override
    public void uploadFile(MultipartFile file, Caregiver candidate, String type)
            throws IOException {
        caregiverService.uploadFile(file, candidate, type);
    }

    @Override
    public Caregiver getCaregiver(String id) {
        return caregiverService.getCaregiver(id);
    }

    @Override
    public void updateCandidateStatus(Caregiver candidate, User user, String status) {
        caregiverService.editStatus(candidate, user, status);

    }
}
