package com.toptal.migration.model;

/**
 * <code>CandidateDocument</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CandidateDocument {
    private String prospectId;
    private String fileName;

    public CandidateDocument(String[] fields) {
        prospectId = fields[0];
        fileName = fields[1];
    }

    public String getFileName() {
        return fileName;
    }

    public String getProspectId() {
        return prospectId;
    }
}
