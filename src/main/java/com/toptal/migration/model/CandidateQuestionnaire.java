package com.toptal.migration.model;

/**
 * <code>CandidateQuestionnaire</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CandidateQuestionnaire {
    private String prospectId;
    private String prospectEmail;
    private String jobTitle;
    private String questionnaireName;
    private String question;
    private String answer;

    public CandidateQuestionnaire(String[] fields) {
        prospectId = fields[0];
        prospectEmail = fields[3];
        jobTitle = fields[4];
        questionnaireName = fields[5];
        question = StringUtil.stripQuotes(fields[6]);
        answer = fields[7];
    }

    public String getAnswer() {
        return answer;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getProspectEmail() {
        return prospectEmail;
    }

    public String getProspectId() {
        return prospectId;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestionnaireName() {
        return questionnaireName;
    }
}
