package com.toptal.migration.model;

/**
 * <code>CandidateFeedback</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CandidateFeedback {
    private String prospectId;
    private String prospectEmail;
    private String feedbackText;

    public CandidateFeedback(String[] fields) {
        prospectId = fields[0];
        prospectEmail = fields[3];
        feedbackText = fields[4];
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public String getProspectEmail() {
        return prospectEmail;
    }

    public String getProspectId() {
        return prospectId;
    }
}
