package com.toptal.migration.model;

/**
 * <code>CandidateNote</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CandidateNote {
    private String prospectId;
    private String comment;

    public CandidateNote(String[] fields) {
        prospectId = fields[0];
        comment = fields[3];
    }

    public String getComment() {
        return comment;
    }

    public String getProspectId() {
        return prospectId;
    }
}
