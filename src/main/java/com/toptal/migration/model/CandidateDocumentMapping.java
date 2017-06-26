package com.toptal.migration.model;

/**
 * <code>CandidateDocumentMapping</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CandidateDocumentMapping
{
    private String prospectId;
    private String oldFileName;
    private String newFileName;
    private String directory;


    public CandidateDocumentMapping(String[] fields)
    {

        this.prospectId = fields[0];
        this.oldFileName = fields[1];
        this.newFileName = fields[3];
        this.directory = fields[2];
    }

    public String getProspectId()
    {
        return prospectId;
    }

    public String getOldFileName()
    {
        return oldFileName;
    }

    public String getNewFileName()
    {
        return newFileName;
    }

    public String getDirectory()
    {
        return directory;
    }

    @Override
    public String toString()
    {
        return "CandidateDocumentMapping{" +
               "prospectId='" + prospectId + '\'' +
               ", oldFileName='" + oldFileName + '\'' +
               ", newFileName='" + newFileName + '\'' +
               ", directory='" + directory + '\'' +
               '}';
    }
}
