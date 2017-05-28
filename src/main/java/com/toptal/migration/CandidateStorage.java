package com.toptal.migration;

import java.util.Map;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.cloudaxis.agsc.portal.model.EmailHistory;
import com.toptal.migration.model.Candidate;
import com.toptal.migration.model.CandidateDocument;
import com.toptal.migration.model.CandidateDocumentMapping;
import com.toptal.migration.model.CandidateEmail;
import com.toptal.migration.model.CandidateEvaluation;
import com.toptal.migration.model.CandidateFeedback;
import com.toptal.migration.model.CandidateNote;
import com.toptal.migration.model.CandidateQuestionnaire;

/**
 * <code>CandidateStorage</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public interface CandidateStorage {

    Caregiver loadCandidate(String prospectId);
    CandidateDocument[] loadCandidateDocuments(String prospectId);
    CandidateEvaluation[] loadCandidateEvaluations(String prospectId);
    CandidateFeedback[] loadCandidateFeedback(String prospectId);
    CandidateDocument[] loadCandidateFiles(String prospectId);
    CandidateNote[] loadCandidateNotes(String prospectId);
    CandidateQuestionnaire[] loadCandidateQuestionnaires(String prospectId);
    CandidateEmail[] loadCandidateEmails(String prospectId);
    CandidateDocument loadCandidateResume(String prospectId);
    Map<String, CandidateDocumentMapping> loadCandidateDocumentMappings(String prospectId);
}
