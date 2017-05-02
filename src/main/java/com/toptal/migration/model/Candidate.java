package com.toptal.migration.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <code>Candidate</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class Candidate {
    private static final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
    private final String[] fields;

    public Candidate(String[] fields) {
        this.fields = fields;
    }

    public String getProspectId() {
        return fields[0];
    }

    public String getFirstName(){
        return fields[1];
    }

    public String getLastName(){
        return fields[2];
    }

    public String getEmail(){
        return fields[3];
    }

    public String getPhone(){
        return fields[8];
    }

    public Date getDateApplied(){
        return DateUtils.parse(fields[21], dateParser);
    }
}
