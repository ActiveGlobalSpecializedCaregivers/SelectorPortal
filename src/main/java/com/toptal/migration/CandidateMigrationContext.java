package com.toptal.migration;

import com.cloudaxis.agsc.portal.dao.EmailHistoryDAO;
import com.cloudaxis.agsc.portal.dao.UserProfileDAO;
import com.cloudaxis.agsc.portal.model.User;
import com.cloudaxis.agsc.portal.service.AssessmentService;
import com.cloudaxis.agsc.portal.service.SelectedCaregiverService;

/**
 * <code>CandidateMigrationContext</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CandidateMigrationContext {
    private static final int PROSPECT_ID_FIELD_INDEX = 0;
    private static final int DECISION_FIELD_INDEX = 4;
    private static final int SELECTOR_NAME_FIELD_INDEX = 5;
    private static final int STATUS_FIELD_INDEX = 19;

    private final String[] fields;
    private final AttachmentResolver attachmentResolver;
    private final CandidateStorage candidateStorage;
    private User user;
    private final BaseServices baseServices;

    public CandidateMigrationContext(String[] fields,
                                     User user,
                                     BaseServices baseServices,
                                     AttachmentResolver attachmentResolver,
                                     CandidateStorage candidateStorage) {
        this.fields = fields;
        this.user = user;
        this.baseServices = baseServices;
        this.attachmentResolver = attachmentResolver;
        this.candidateStorage = candidateStorage;
    }

    public AttachmentResolver getAttachmentResolver() {
        return attachmentResolver;
    }

    public CandidateStorage getCandidateStorage() {
        return candidateStorage;
    }

    public String getDecision() {
        return fields[DECISION_FIELD_INDEX];
    }

    public String getProspectId(){
        return fields[PROSPECT_ID_FIELD_INDEX];
    }

    public String getSelectorName(){
        return fields[SELECTOR_NAME_FIELD_INDEX];
    }

    public String getCandidateStatus(){
        return fields[STATUS_FIELD_INDEX];
    }

    public User getUser() {
        return user;
    }

    public BaseServices getBaseServices() {
        return baseServices;
    }
}
